/*
* Copyright 2004 ThoughtWorks, Inc
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/

function TestLoop(commandFactory) {
    this.commandFactory = commandFactory;
}

TestLoop.prototype = {

    start : function() {
        selenium.reset();
        LOG.debug("currentTest.start()");
        this.continueTest();
    },

    continueTest : function() {
        /**
         * Select the next command and continue the test.
         */
        LOG.debug("currentTest.continueTest() - acquire the next command");
        if (! this.aborted) {
            this.currentCommand = this.nextCommand();
        }
        if (! this.requiresCallBack) {
            this.continueTestAtCurrentCommand();
        } // otherwise, just finish and let the callback invoke continueTestAtCurrentCommand()
    },

    continueTestAtCurrentCommand : function() {
        LOG.debug("currentTest.continueTestAtCurrentCommand()");
        if (this.currentCommand) {
            // TODO: rename commandStarted to commandSelected, OR roll it into nextCommand
            this.commandStarted(this.currentCommand);
            this._resumeAfterDelay();
        } else {
            this._testComplete();
        }
    },

    _resumeAfterDelay : function() {
        /**
         * Pause, then execute the current command.
         */

        // Get the command delay. If a pauseInterval is set, use it once
        // and reset it.  Otherwise, use the defined command-interval.
        var delay = this.pauseInterval || this.getCommandInterval();
        this.pauseInterval = undefined;

        if (this.currentCommand.isBreakpoint || delay < 0) {
            // Pause: enable the "next/continue" button
            this.pause();
        } else {
            window.setTimeout(fnBind(this.resume, this), delay);
        }
    },

    resume: function() {
        /**
         * Select the next command and continue the test.
         */
        LOG.debug("currentTest.resume() - actually execute");
        try {

			this._startTime = new Date().getTime();
			LOG.debug("***selenium-execution loop: this._startTime " + this._startTime);

            selenium.browserbot.runScheduledPollers();
            this._executeCurrentCommand();
            this.continueTestWhenConditionIsTrue();
        } catch (e) {
            if (!this._handleCommandError(e)) {
                this.testComplete();
            } else {
                this.continueTest();
            }
        }
    },

    _testComplete : function() {
        selenium.ensureNoUnhandledPopups();
        this.testComplete();
    },

    _executeCurrentCommand : function() {
        /**
         * Execute the current command.
         *
         * @return a function which will be used to determine when
         * execution can continue, or null if we can continue immediately
         */
        var command = this.currentCommand;
        LOG.info("Executing: |" + command.command + " | " + command.target + " | " + command.value + " |");

        var handler = this.commandFactory.getCommandHandler(command.command);
        if (handler == null) {
            throw new SeleniumError("Unknown command: '" + command.command + "'");
        }

        command.target = selenium.preprocessParameter(command.target);
        command.value = selenium.preprocessParameter(command.value);
        LOG.debug("Command found, going to execute " + command.command);
        this.result = handler.execute(selenium, command);
        

        this.waitForCondition = this.result.terminationCondition;

    },

    _handleCommandError : function(e) {
        if (!e.isSeleniumError) {
            LOG.exception(e);
            var msg = "Selenium failure. Please report to the Selenium Users forum at http://forums.openqa.org, with error details from the log window.";
            msg += "  The error message is: " + extractExceptionMessage(e);
            return this.commandError(msg);
        } else {
            LOG.error(e.message);
            return this.commandError(e.message);
        }
    },

    continueTestWhenConditionIsTrue: function () {
        /**
         * Busy wait for waitForCondition() to become true, and then carry
         * on with test.  Fail the current test if there's a timeout or an
         * exception.
         */
        //LOG.debug("currentTest.continueTestWhenConditionIsTrue()");
        selenium.browserbot.runScheduledPollers();
		 LOG.debug("continueTestWhenConditionIsTrue is called ");
        try {
            if (this.waitForCondition == null) {
				//LOG.setPerfLogLevelCount(LOG.getPerfLogLevelCount() + 1);
				LOG.debug("0");
                LOG.debug("null condition; let's continueTest()");
                LOG.debug("Command complete");
				this._endTime =  new Date().getTime();
				LOG.debug("***selenium-execution loop: this._endTime " + this._endTime);
				if(LOG.perfStartLogging) {
					var t = (this._endTime - this._startTime)/1000;
					LOG.perfinfo("TIME TAKEN: " +  ( t + "seconds"));
					//LOG.perfinfo("INNERHTML SIZE: "+ selenium.browserbot.getCurrentWindow().document.getElementsByTagName("HTML")[0].innerHTML.length);
					ZM_DB_OBJ.setTimeTakenAndCommit(t);
					LOG.perfStartLogging = false;
				}
                this.commandComplete(this.result);
                this.continueTest();
            } else if (this.waitForCondition()) {
				//LOG.setPerfLogLevelCount(LOG.getPerfLogLevelCount() + 1);
				LOG.debug("1");
                LOG.debug("condition satisfied; let's continueTest()");
                this.waitForCondition = null;
                LOG.debug("Command complete");
				this._endTime =  new Date().getTime();
				if(LOG.perfStartLogging) {
					var t = (this._endTime - this._startTime)/1000;
					LOG.perfinfo("TIME TAKEN: " +  ( t + "seconds"));
					//LOG.perfinfo("INNERHTML SIZE: "+ selenium.browserbot.getCurrentWindow().document.getElementsByTagName("HTML")[0].innerHTML.length);					
					ZM_DB_OBJ.setTimeTakenAndCommit(t);
					LOG.perfStartLogging = false;
				}
                this.commandComplete(this.result);
                this.continueTest();
            } else {
				LOG.debug("2");
                LOG.debug("waitForCondition was false; keep waiting!");
                window.setTimeout(fnBind(this.continueTestWhenConditionIsTrue, this), 250);
            }
        } catch (e) {
			LOG.debug("3");
			this.result = {};
			var expnMsg = extractExceptionMessage(e);
			if(expnMsg.indexOf("Timed")>=0 && selenium.browserbot.dontFailOnTimeout) {           
				this.result.failed = false;
			} else {
				this.result.failed = true;
				this.result.failureMessage = expnMsg;
			}
            this.commandComplete(this.result);
            this.continueTest();
        }
    },

    pause : function() {},
    nextCommand : function() {},
    commandStarted : function() {},
    commandComplete : function() {},
    commandError : function() {},
    testComplete : function() {},

    getCommandInterval : function() {
        return 0;
    }

}
