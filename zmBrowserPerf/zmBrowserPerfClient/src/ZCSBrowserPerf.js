var perfDB = function() {
	this.currentUIMsg = "";
	this.currentTimeTaken = "";
	this._flashObjCount = 0;
	this.vec = new Array();
	this.uniqueBrowsers = new Array();
	this.uniqueVersions = new Array();
	this.url = "/zmBrowserPerfServer/signup";
	if (window.XMLHttpRequest) {
		this.req = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		this.req = new ActiveXObject("Microsoft.XMLHTTP");
	}

}
perfDB.prototype.getPerfDataFromDB =
function () {
	var me = this;
	this.req.open("Get", this.url, true);
	this.req.onreadystatechange = function() {
		if (me.req.readyState == 4) {
			if (me.req.status == 200) {
				me.parseDB();
			}
		}
	};
	this.req.send(null);
}


perfDB.prototype.loadChartData_bar =
function (version) {
	var str = "", ver, val;
	this._BrowsersForBarChart = new Array();
	for (var i = 0; i < this.headers.length; i++) {
		var hdr = this.headers[i];
		if (hdr == "browser" || hdr == "version")
			continue;

		str = str + hdr + ";";
		val = new Array();
		for (var j = 0; j < this.vec.length; j++) {
			var tmp = this.vec[j];
			if (tmp._version == version) {
				val.push(eval("tmp._avg." + hdr));
				this.addToArrayIfUnique(this._BrowsersForBarChart, tmp._browser);
			}
		}
		str = str + val.join(";") + "\n";
	}
	return str;

}

perfDB.prototype.loadChartData_barStacked =
function (version) {
	var str = "", ver, val;
	this._BrowsersForBarChart = new Array();
	for (var i = 0; i < this.uniqueBrowsers.length; i++) {
		var br = this.uniqueBrowsers[i];
		var currVec = this._getVecUsingVersionAndBrowser(version, br);
		str = str + br + ";";
		val = new Array();
		//for(var j =0; j < this.headers.length; j++) {
		for (var j = 0; j < this.headers.length; j++) {
			var hdr = this.headers[j];
			if (hdr == "browser" || hdr == "version")
				continue;

			val.push(eval("currVec._avg." + hdr));
		}
		str = str + val.join(";") + "\n";
	}
	return str;

}

perfDB.prototype._getVecUsingVersionAndBrowser =
function (version, browser) {
	for (var j = 0; j < this.vec.length; j++) {
		var tmp = this.vec[j];
		if (tmp._version == version && tmp._browser == browser) {
			return tmp;
		}
	}
}

perfDB.prototype.loadChartData_line =
function (column) {
	var str = "", ver, val;
	for (var i = 0; i < this.uniqueVersions.length; i++) {
		ver = this.uniqueVersions[i];
		str = str + ver + ";";
		val = "";
		for (var j = 0; j < this.uniqueBrowsers.length; j ++) {

			var br = this.uniqueBrowsers[j];
			val = val + this._getBrowserVerVal(br, ver, column);//get login time

			if (j != this.uniqueBrowsers.length - 1)//until the last value, add a ;
				val = val + ";";
		}
		str = str + val + "\n";
	}
	return str;
}

perfDB.prototype._getBrowserVerVal =
function(br, ver, column) {
	for (var i = 0; i < this.vec.length; i++) {
		var tmp = this.vec[i];
		if (tmp._browser == br && tmp._version == ver) {
			if (column == "Total_Rendering_Time")//if totalTime is asked, then
				return  tmp.Total_Rendering_Time;
			else
				return eval("tmp._avg." + column);
		}
	}
	return "";
}

perfDB.prototype.loadChartSettings_bar =
function (column) {
	return "<settings><type>column</type><data_type>csv</data_type><csv_separator>;</csv_separator><skip_rows>0</skip_rows><font>Garamond</font><text_size>15</text_size><text_color>#000000</text_color><decimals_separator>.</decimals_separator><thousands_separator></thousands_separator><digits_after_decimal></digits_after_decimal><redraw>true</redraw><reload_data_interval></reload_data_interval><preloader_on_reload></preloader_on_reload><add_time_stamp>false</add_time_stamp><precision>2</precision><depth>20</depth><angle>30</angle><column><type>clustered</type><width>50</width><spacing>5</spacing><alpha>100</alpha><border_color>#000000</border_color><border_alpha></border_alpha><data_labels><![CDATA[]]></data_labels><data_labels_text_color></data_labels_text_color><data_labels_text_size></data_labels_text_size><data_labels_position></data_labels_position><balloon_text><![CDATA[{title},{series},{value}]]></balloon_text><link_target></link_target><gradient></gradient></column><line><connect></connect><width></width><alpha></alpha><fill_alpha></fill_alpha><bullet></bullet><bullet_size></bullet_size><data_labels><![CDATA[{value}]]></data_labels><data_labels_text_color></data_labels_text_color><data_labels_text_size></data_labels_text_size><balloon_text><![CDATA[{value}]]></balloon_text><link_target></link_target></line><background><color>#FFFFFF</color><alpha>100</alpha><border_color>#FFFFFF</border_color><border_alpha>15</border_alpha><file></file></background><plot_area><color>#000000</color><alpha>10</alpha><border_color></border_color><border_alpha></border_alpha><margins><left>80</left><top>80</top><right>80</right><bottom>80</bottom></margins></plot_area><grid><category><color>#000000</color><alpha>20</alpha><dashed>true</dashed><dash_length>5</dash_length></category><value><color>#000000</color><alpha>20</alpha><dashed>true</dashed><dash_length>5</dash_length><approx_count>10</approx_count></value></grid><values><category><enabled>true</enabled><frequency>1</frequency><rotate>15</rotate><color></color><text_size></text_size></category><value><enabled>true</enabled><reverse></reverse><min>0</min><max></max><strict_min_max></strict_min_max><frequency>1</frequency><rotate></rotate><skip_first>true</skip_first><skip_last>false</skip_last><color></color><text_size></text_size><integers_only></integers_only></value></values><axes><category><color>#000000</color><alpha>20</alpha><width>1</width><tick_length>7</tick_length></category><value><color>#000000</color><alpha>20</alpha><width>1</width><tick_length>7</tick_length><logarithmic></logarithmic></value></axes><balloon><enabled>true</enabled><color></color><alpha>100</alpha><text_color>#FFFFFF</text_color><text_size></text_size></balloon><legend><enabled>true</enabled><x>130</x><y>110</y><width></width><max_columns></max_columns><color>#000000</color><alpha></alpha><border_color>#000000</border_color><border_alpha>0</border_alpha><text_color></text_color><text_size></text_size><spacing>5</spacing><margins>0</margins><key><size>16</size><border_color></border_color></key></legend><export_as_image><file></file><target></target><x></x><y></y><color></color><alpha></alpha><text_color></text_color><text_size></text_size></export_as_image><error_messages><enabled></enabled><x></x><y></y><color></color><alpha></alpha><text_color></text_color><text_size></text_size></error_messages><strings><no_data></no_data><export_as_image></export_as_image><collecting_data></collecting_data></strings><labels><label><x>0</x><y>10</y><rotate></rotate><width></width><align>center</align><text_color></text_color><text_size>18</text_size><text><![CDATA[<b>" + column + "</b>]]></text></label></labels><graphs>" + this._loadSeriesSettings_bar() + "</graphs></settings>";
}

perfDB.prototype.loadChartSettings_barStacked =
function (column) {
	var graphs = this._loadSeriesSettings_barStacked();
	return "<settings><type>bar</type><data_type>csv</data_type><csv_separator>;</csv_separator><skip_rows></skip_rows><font>Tahoma</font><text_size></text_size><text_color></text_color><decimals_separator>.</decimals_separator><thousands_separator></thousands_separator><digits_after_decimal></digits_after_decimal><redraw></redraw><reload_data_interval></reload_data_interval><preloader_on_reload></preloader_on_reload><add_time_stamp>false</add_time_stamp><precision></precision><depth>10</depth><angle>30</angle><column><type>stacked</type><width>25</width><spacing>0</spacing><alpha>100</alpha><border_color>#FFFFFF</border_color><border_alpha></border_alpha><data_labels><![CDATA[<b>{value}</b>]]></data_labels><data_labels_text_color></data_labels_text_color><data_labels_text_size></data_labels_text_size><data_labels_position></data_labels_position><balloon_text><![CDATA[{title},{series},{value}]]></balloon_text><link_target></link_target><gradient></gradient></column><line><connect></connect><width></width><alpha></alpha><fill_alpha></fill_alpha><bullet></bullet><bullet_size></bullet_size><data_labels><![CDATA[{value}]]></data_labels><data_labels_text_color></data_labels_text_color><data_labels_text_size></data_labels_text_size><balloon_text><![CDATA[{value}]]></balloon_text><link_target></link_target></line><background><color>#FFFFFF</color><alpha></alpha><border_color>#000000</border_color><border_alpha>15</border_alpha><file></file></background><plot_area><color>#FFFFFF</color><alpha>0</alpha><border_color></border_color><border_alpha></border_alpha><margins><left>50</left><top>60</top><right>50</right><bottom>120</bottom></margins></plot_area><grid><category><color>#000000</color><alpha>5</alpha><dashed>false</dashed><dash_length>5</dash_length></category><value><color>#000000</color><alpha>5</alpha><dashed>false</dashed><dash_length>5</dash_length><approx_count>10</approx_count></value></grid><values><category><enabled>true</enabled><frequency>1</frequency><rotate></rotate><color></color><text_size></text_size></category><value><enabled>true</enabled><reverse></reverse><min>0</min><max></max><strict_min_max></strict_min_max><frequency>1</frequency><rotate></rotate><skip_first>true</skip_first><skip_last>false</skip_last><color></color><text_size></text_size><unit></unit><unit_position>right</unit_position><integers_only></integers_only></value></values><axes><category><color>#000000</color><alpha>100</alpha><width>1</width><tick_length>7</tick_length></category><value><color>#000000</color><alpha>100</alpha><width>1</width><tick_length>7</tick_length><logarithmic></logarithmic></value></axes><balloon><enabled>true</enabled><color></color><alpha>100</alpha><text_color></text_color><text_size>11</text_size></balloon><legend><enabled>true</enabled><x></x><y></y><width></width><max_columns></max_columns><color>#FFFFFF</color><alpha>0</alpha><border_color>#000000</border_color><border_alpha>0</border_alpha><text_color></text_color><text_size></text_size><spacing>5</spacing><margins>0</margins><key><size>16</size><border_color></border_color></key></legend><labels><label><x>0</x><y>25</y><rotate></rotate><width></width><align>center</align><text_color></text_color><text_size></text_size><text><![CDATA[<b>" + column + "</b>]]></text></label></labels><graphs>" + graphs + "</graphs></settings>";
}


perfDB.prototype._loadSeriesSettings_barStacked =
function () {
	var ser = "";
	for (var i = 0; i < this.headers.length; i++) {
		var hdr = this.headers[i];
		if (hdr == "browser" || hdr == "version")
			continue;

		ser = ser + "<graph><type>column</type><title>" + hdr + "</title><data_labels><![CDATA[]]></data_labels>" +
		      "<balloon_text><![CDATA[{title}={value} {series}]]></balloon_text><visible_in_legend></visible_in_legend></graph>";
	}

	return ser;
}
perfDB.prototype._loadSeriesSettings_bar =
function () {
	var ser = "";
	for (var i = 0; i < this._BrowsersForBarChart.length; i++) {
		ser = ser + "<graph><type>column</type><title>" + this._BrowsersForBarChart[i] + "</title><data_labels><![CDATA[]]></data_labels>" +
		      "<balloon_text><![CDATA[{series}={title} ({value})]]></balloon_text><visible_in_legend></visible_in_legend></graph>";
	}

	return ser;
}
perfDB.prototype.loadChartSettings_line =
function (column) {
	return "<settings><data_type>csv</data_type><decimals_separator>.</decimals_separator><labels><label><y>12</y><width>520</width><align>center</align><text><![CDATA[<b>" + column + "</b>]]></text></label></labels><legend><x></x><y>32</y><width></width><max_columns></max_columns><color></color><alpha></alpha><border_color></border_color><border_alpha></border_alpha><text_color></text_color><text_color_hover></text_color_hover><text_size></text_size><spacing></spacing><margins></margins><graph_on_off></graph_on_off><key><size></size><border_color></border_color><key_mark_color></key_mark_color></key><values><enabled></enabled><width></width><align></align><text><![CDATA[]]></text></values></legend><graphs>" + this._loadSeriesSettings_line() + "</graphs></settings>";
}
perfDB.prototype._loadSeriesSettings_line =
function () {
	var ser = "";
	for (var i = 0; i < this.uniqueBrowsers.length; i++) {
		ser = ser + "<graph><axis>left</axis><title>" +
		      this.uniqueBrowsers[i] + "</title><fill_alpha>30</fill_alpha>" +
		      "<bullet>round_outlined</bullet><bullet_size>10</bullet_size>" +
		      "<balloon_text><![CDATA[{value} ({title})]]></balloon_text></graph>";
	}

	return ser;
}

perfDB.prototype.showAllLineCharts =
function () {

	//cleanup any stale data
	document.getElementById("flashGlobalDiv").innerHTML = "";


	//create a table shell to show all the line charts
	this.createTableForFlashObjects();


	for (i = 0; i < this.headers.length; i++) {
		var hdr = this.headers[i];
		if (hdr != "browser" && hdr != "version") {
			this.showLineChartPerColumn(hdr, this._getFlashDiv(hdr));
		}
	}
}

perfDB.prototype.createTableForFlashObjects =
function () {
	var td = "", tr = "";
	var cnt = 0;
	for (i = 0; i < this.headers.length; i++) {
		var hdr = this.headers[i];
		if (hdr != "browser" && hdr != "version") {
			var id = "flashTD_" + hdr;
			td = td + "<td id=" + id + "><td>";
			cnt++;

			if ((cnt == 5) || i == (this.headers.length - 1)) {
				tr = tr + "<tr>" + td + "</tr>";
				td = "";
				cnt = 0;
			}

		}
	}
	document.getElementById("flashGlobalDiv").innerHTML = "<table>" + tr + "</table>";
}

perfDB.prototype.showBarChart =
function (version) {
	document.getElementById("barchartGlobalDiv").innerHTML = "";

	var so = new SWFObject("amcolumn/amcolumn/amcolumn.swf", "amcolumn", "2000", "500", "8", "#FFFFFF");
	so.addVariable("path", "amcolumn/amcolumn/");
	var cData = this.loadChartData_bar(version);
	var cSettings = this.loadChartSettings_bar(version);
	so.addVariable("chart_data", cData);
	so.addVariable("chart_settings", cSettings);
	so.write("barchartGlobalDiv");

	this.showBarChartStacked(version);
}

perfDB.prototype.showBarChartStacked =
function (version) {
	var so = new SWFObject("amcolumn/amcolumn/amcolumn.swf", "amcolumn", "2000", "500", "8", "#FFFFFF");
	so.addVariable("path", "amcolumn/amcolumn/");
	var cData = this.loadChartData_barStacked(version);
	var cSettings = this.loadChartSettings_barStacked(version);
	so.addVariable("chart_data", cData);
	so.addVariable("chart_settings", cSettings);
	so.write("barStackedchartGlobalDiv");
}


perfDB.prototype.createVersionsMenuForBarChart =
function () {
	document.getElementById("bc_SelectMenuDIV").innerHTML = "";//cleanup stale data
	var tmp = "";
	var str = "<select id=\"perf_barchartMenu\"><option value=\"Any\" selected>- Please Select a Zimbra Version -</option>";
	for (var i = 0; i < this.uniqueVersions.length; i++) {
		tmp = tmp + "<option value=\"versionsForBCMenuItem_" + i + "\">" + this.uniqueVersions[i] + "</option>";
	}
	document.getElementById("bc_SelectMenuDIV").innerHTML = str + tmp + "</select>";

	document.getElementById("perf_barchartMenu").onchange = perf_createClosure(this, this._getVersionForBarchart);

}
perfDB.prototype._getVersionForBarchart =
function () {
	var menu = document.getElementById("perf_barchartMenu");
	var version = menu.options[menu.selectedIndex].text;
	this.showBarChart(version);
}

perfDB.prototype.showLineChartPerColumn =
function (column, divId) {
	// <![CDATA[
	var so = new SWFObject("amline/amline/amline.swf", "amline", "520", "400", "8", "#FFFFFF");
	so.addVariable("path", "amline/amline/");
		//so.addVariable("settings_file", escape("amline/amline/amline_settings.xml"));  // you can set two or more different settings files here (separated by commas)
	//so.addVariable("data_file", escape("amline/amline/amline_data.txt"));
	var cData = this.loadChartData_line(column);
	var cSettings = this.loadChartSettings_line(column);
	so.addVariable("chart_data", cData);
	so.addVariable("chart_settings", cSettings);
//	so.addVariable("chart_data", "");                                       // you can pass chart data as a string directly from this file
	//	so.addVariable("chart_settings", "");                                   // you can pass chart settings as a string directly from this file
	//	so.addVariable("additional_chart_settings", "");                        // you append some chart settings to the loaded ones
	//  so.addVariable("loading_settings", "LOADING SETTINGS");                 // you can set custom "loading settings" text here
	//  so.addVariable("loading_data", "LOADING DATA");                         // you can set custom "loading data" text here
	so.addVariable("preloader_color", "#999999");
	so.write(divId);
		// ]]>
}
perfDB.prototype._getFlashDiv =
function (column) {
	var div = document.createElement("DIV");
	var id = "flashSubDiv_" + (this._flashObjCount++);
	div.setAttribute('id', id);
		//div.style.width = "400px";
	//div.style.height = "400px";
	document.getElementById("flashTD_" + column).appendChild(div);

	return id;
}


perfDB.prototype.constructExt2Table_slowest =
function () {
	var data = this._getDataForExt2_slowest();
	var hdrs = this._getHeadersForExt2_slowest();
	var hdrProps = this._getHeaderPropertiesForExt2_slowest();
	createGrid(data, hdrs, hdrProps, "JSExt2_AverageTableDIV_Slowest", "Top 5 Slowest Actions:", 500);
}

function sortByPerfVal(a, b) {
	var x = eval(a.split("==")[1]);
	var y = eval(b.split("==")[1]);
	return ((x < y) ? 1 : ((x > y) ? -1 : 0));
}
perfDB.prototype._getDataForExt2_slowest =
function () {
	//get values array
	var valArry = new Array();
	for (var i = 0; i < this.vec.length; i++) {
		var avgObj = this.vec[i]._avg;
		var tmpArry = new Array();
		var fiveElementsArry = new Array();
		for (var j = 0; j < this.headers.length; j++) {
			var hdr = this.headers[j];
			if (hdr != "browser" && hdr != "version") {
				tmpArry.push(hdr + "==" + eval("avgObj." + hdr));
			}
		}
		//sort
		var sortedArry = tmpArry.sort(sortByPerfVal);
		//push browser, version then 5 elements to a fiveElementsArry arry.
		fiveElementsArry.push(avgObj.browser);
		fiveElementsArry.push(avgObj.version);
		for (var k = 0; k < 5; k++) {
			fiveElementsArry.push(sortedArry[k]);
		}
		//finally push fiveElementsArry array to an arrayOfArray
		valArry.push(fiveElementsArry);
	}
	return valArry;
}


perfDB.prototype._getHeadersForExt2_slowest =
function () {
	this._slowTableHdrs = ["Browser","Version","Slowest", "2nd Slowest", "3rd Slowest", "4rth Slowest", "5th Slowest"];
	var tmpArry = new Array();
	for (var j = 0; j < this._slowTableHdrs.length; j++) {
		var hdr = this._slowTableHdrs[j];
		if (hdr == "browser" || hdr == "version")
			tmpArry.push("{name: '" + hdr + "'}");
		else
			tmpArry.push("{name: '" + hdr + "'}");
	}
	return eval("([" + tmpArry.join(",") + "])");

}

perfDB.prototype._getHeaderPropertiesForExt2_slowest =
function () {
	//get headers properties info
	var tmpArry = new Array();
	for (var j = 0; j < this._slowTableHdrs.length; j++) {
		var hdr = this._slowTableHdrs[j];
		if (hdr == "browser")
			tmpArry.push("{header: '" + hdr + "',   width: 70, sortable: true, dataIndex: '" + hdr + "'}");
		else
			tmpArry.push("{header: '" + hdr + "',   width: 170, sortable: true, dataIndex: '" + hdr + "'}");
	}
	return eval("([" + tmpArry.join(",") + "])");

}

//=====================================================

perfDB.prototype.constructExt2Table =
function () {
	var data = this._getDataForExt2();
	var hdrs = this._getHeadersForExt2();
	var hdrProps = this._getHeaderPropertiesForExt2();
	createGrid(data, hdrs, hdrProps, "JSExt2_AverageTableDIV", "Average Time Taken", 500);

}

perfDB.prototype._getDataForExt2 =
function () {
	//get values array
	var valArry = new Array();
	for (var i = 0; i < this.vec.length; i++) {
		var avgObj = this.vec[i]._avg;
		var tmpArry = new Array();
		for (var j = 0; j < this.headers.length; j++) {
			tmpArry.push(eval("avgObj." + (this.headers[j])));
		}
		valArry.push(tmpArry);
	}
	return valArry;
}
perfDB.prototype._getHeadersForExt2 =
function () {
	//get headers info
	var tmpArry = new Array();
	for (var j = 0; j < this.headers.length; j++) {
		var hdr = this.headers[j];
		if (hdr == "browser" || hdr == "version")
			tmpArry.push("{name: '" + hdr + "'}");
		else
			tmpArry.push("{name: '" + hdr + "', type: 'float'}");
	}
	return eval("([" + tmpArry.join(",") + "])");

}

perfDB.prototype._getHeaderPropertiesForExt2 =
function () {
	//get headers properties info
	var tmpArry = new Array();
	for (var j = 0; j < this.headers.length; j++) {
		var hdr = this.headers[j];

		if (hdr == "version")
			tmpArry.push("{header: '" + hdr + "',   width: 150, sortable: true, dataIndex: '" + this.headers[j] + "'}");
		else if (hdr == "login")
			tmpArry.push("{header: '" + hdr + "',   width: 100, renderer:  loginRule, sortable: true, dataIndex: '" + this.headers[j] + "'}");
		else
			tmpArry.push("{header: '" + hdr + "',   width: 100, sortable: true, dataIndex: '" + this.headers[j] + "'}");
	}
	return eval("([" + tmpArry.join(",") + "])");

}
function loginRule(val) {
	if (val <= 5) {
		return '<span style="color:green;">' + val + '</span>';
	} else {
		return '<span style="color:red;">' + val + '</span>';
	}
	return val;
}
//=====================================================


perfDB.prototype.constructAllValuesTable =
function() {
	var rows = "", h = "";
	for (var n = 0; n < this.headers.length; n++) {
		h = h + "<th>" + this.headers[n] + "</th>";
	}
	h = "<tr>" + h + "</tr>";
	for (var i = 0; i < this._rows.length; i++) {
		var valRow = this._rows[i];
		var td = "";
		for (var j = 0; j < this.headers.length; j++) {
			td = td + "<td>" + eval("valRow." + this.headers[j]) + "</td>";
		}
		rows = rows + "<tr>" + td + "</tr>";

	}
	var perfTable = document.getElementById('perfTableId');
	perfTable.innerHTML = "<table class=\"sortable\" id=\"idid\" cellpadding=\"0\" cellspacing=\"0\">" + h + rows + "</table>";

}

//absolete
perfDB.prototype.constructAveragePerfTable =
function () {


	//construct table..
	var rows = "", h = "";
	for (var n = 0; n < this.headers.length; n++) {
		h = h + "<th>" + this.headers[n] + "</th>";
	}
	h = "<tr>" + h + "</tr>";
	for (var a = 0; a < this.vec.length; a++) {
		var avgRow = this.vec[a]._avg;
		var td = "";
		for (var b = 0; b < this.headers.length; b++) {
			td = td + "<td>" + eval("avgRow." + this.headers[b]) + "</td>";
		}
		rows = rows + "<tr>" + td + "</tr>";
	}

	//rows = rows + "<tr>" + td + "</tr>";
	var perfAvgTable = document.getElementById('perfAverageTableId');
	perfAvgTable.innerHTML = "<table class=\"sortable\" id=\"someid\" cellpadding=\"0\" cellspacing=\"0\">" + h + rows + "</table>";

}
/*  function retrieveCityState(){
		var zip = document.getElementById("zipcode");
		var url = "/ch05-suggest/zipcodes?zip=" + escape(zip.value);
		name.value="?"+name.value;
		if (window.XMLHttpRequest){
			 req = new XMLHttpRequest();
		 }
		 else if (window.ActiveXObject){
			 req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		req.open("Get",url,true);
		req.onreadystatechange = callbackCityState;
		req.send(null);
	} */
perfDB.prototype._calculateAvgAndTotalTime =
function () {
	this._calculateAndStorePerfAverage();  //calculate average
	this._calculateTotalTime(); //calculate total time of all average values
}

perfDB.prototype._calculateTotalTime =
function () {
	for (var i = 0; i < this.vec.length; i++) {
		var total = 0;
		var avgObj = this.vec[i]._avg;
		for (var j = 0; j < this.headers.length; j++) {
			var hdr = this.headers[j];
			if (hdr == "browser" || hdr == "version")
				  continue;

			total = total + eval(eval("avgObj." + hdr));
		}
		this.vec[i].Total_Rendering_Time = total;
	}
}


//should be called after parseDB
perfDB.prototype._calculateAndStorePerfAverage =
function () {
	this._keyCount = 0;
	for (var i = 0; i < this.vec.length; i++) {
		var set = this.vec[i];
		var tmpArry = new Array();
		for (var j = 0; j < this.headers.length; j++) {
			var hdr = this.headers[j];
			if (hdr != "browser" && hdr != "version") {
				var val = 0;
				for (var k = 0; k < set.length; k++) {

					val = val + eval(eval("set[k]" + "." + hdr));
				}
				try {//take avg and ,limit decimal to 2 digits
					tmpArry.push(hdr + ":" + "\"" + (parseInt((val / set.length) * 100)) / 100 + "\"");
				} catch(e) {
					tmpArry.push(hdr + ":" + "expn");
					//this.vec[i]["average"].eval(hdr) = "expn";
				}
			} else {
				tmpArry.push(hdr + ":" + "\"" + eval("set[0]" + "." + hdr) + "\"");
				//this.vec[i]["average"].hdr = hdr;
			}
		}
		this.vec[i]._avg = eval("( {" + tmpArry.join(",") + "})");
	}


}


perfDB.prototype.parseDB =
function() {
	this._keyCount = 0;
	var jsonData = this.req.responseText;
	var myJSONObject = eval('(' + jsonData + ')');
	this._rows = myJSONObject.rows;

	this.headers = new Array();
	this.headers.push("browser");
	this.headers.push("version");
	this.headers.push("login");
	this.headers.push("open_mail");
	this.headers.push("open_2ndmail");
	this.headers.push("dblclick_mail");
	this.headers.push("dblclick_2ndmail");
	this.headers.push("mailcompose_1");
	this.headers.push("mailcompose_2");
	this.headers.push("mailcompose_3");
	this.headers.push("compose_new_window_1");
	this.headers.push("compose_new_window_2");
	this.headers.push("goto_calendar");
	this.headers.push("goto_contacts");
	this.headers.push("goto_tasks");
	this.headers.push("goto_docs");
	this.headers.push("goto_pref");
	this.headers.push("goto_mail");
	this.headers.push("goto_calendar_2");
	this.headers.push("goto_contacts_2");
	this.headers.push("goto_tasks_2");
	this.headers.push("goto_docs_2");
	this.headers.push("goto_pref_2");
	this.headers.push("goto_mail_2");
	this.headers.push("cal_schedule_tab");
	this.headers.push("cal_attendees_tab");
	this.headers.push("cal_locations_tab");
	this.headers.push("cal_resources_tab");
	this.headers.push("cal_schedule_tab_2");
	this.headers.push("cal_attendees_tab_2");
	this.headers.push("cal_locations_tab_2");
	this.headers.push("cal_resources_tab_2");
	this.headers.push("pref_mail_tab");
	this.headers.push("pref_compose_tab");
	this.headers.push("pref_signature_tab");
	this.headers.push("pref_addressbook_tab");
	this.headers.push("pref_accounts_tab");
	this.headers.push("pref_calendar_tab");
	this.headers.push("pref_shortcuts_tab");
//	this.headers.push("contacts_card_view");
	//	this.headers.push("contacts_list_view");
	//	this.headers.push("contacts_card_view_2");
	//	this.headers.push("contacts_list_view_2");
	this.headers.push("click_sent");
	this.headers.push("click_trash");
	this.headers.push("click_inbox");
	this.headers.push("click_drafts");
	this.headers.push("click_junk");
	this.headers.push("click_sent_2");
	this.headers.push("click_trash_2");
	this.headers.push("click_inbox_2");
	this.headers.push("click_drafts_2");
	this.headers.push("click_junk_2");
//	this.headers.push("mail_msg_view");
	//	this.headers.push("mail_conv_view");
	//	this.headers.push("mail_msg_view_2");
	//	this.headers.push("mail_conv_view_2");

	for (var i = 0; i < this._rows.length; i++) {
		var valRow = this._rows[i];
		this.storeInBaseVector(valRow);
	}
	//store perf average values
	this._calculateAvgAndTotalTime();
	//show average values in overview...
	document.getElementById("showLoadingOnOverviewDIV").innerHTML = "";//remove loading...
	this.constructExt2Table();
	this.constructExt2Table_slowest();
	this.showLineChartPerColumn("Total_Rendering_Time", "showTotalTimeLineChartDIV");
	this.showLineChartPerColumn("login", "showLoginLineChartDIV");
}


perfDB.prototype.storeInBaseVector =
function(valRow) {
	var br = valRow.browser;
	var ver = valRow.version;

	if (br == "?" || br == undefined || br == "1" || br == "null"
			|| ver == "?" || ver == undefined || ver == "1" || br == "null")
		return;

	var key = br + "_" + ver;


	var keyIndx = this.DBKeyExists(key);
	if (keyIndx == -1) {
		this.vec[this._keyCount] = new Array();
		this.vec[this._keyCount]._key = key;
		keyIndx = this._keyCount;//reset and reinitialize keyIndx for newly created obj
		//set browser and version values that will be used later(for charting and average etc)
		this.vec[this._keyCount]._browser = br;
		this.vec[this._keyCount]._version = ver;
		this.addToArrayIfUnique(this.uniqueBrowsers, br);
		this.addToArrayIfUnique(this.uniqueVersions, ver);
		this._keyCount++;
	}

	this.vec[keyIndx].push(valRow);


}
perfDB.prototype.addToArrayIfUnique =
function(arry, val) {
	var flg = true;
	for (var i = 0; i < arry.length; i++) {
		if (arry[i] == val) {
			flg = false;
			break;
		}
	}
	if (flg)
		arry.push(val);

}
perfDB.prototype.DBKeyExists =
function(key) {
	for (var i = 0; i < this.vec.length; i++) {
		if (this.vec[i]._key == key) {
			return i;
		}
	}
	return -1;
}

var PERF_DB_OBJ;

function perf_createClosure(object, method) {
	var shim = function() {
		method.apply(object, arguments);
	}
	return shim;
}

function initDBObj() {
	if (!PERF_DB_OBJ)
		PERF_DB_OBJ = new perfDB();

	// PERF_DB_OBJ.getPerfDataFromDB();
}

function initialize() {
	if (!PERF_DB_OBJ)
		PERF_DB_OBJ = new perfDB();
	var newdiv = document.createElement('div');
	newdiv.setAttribute('id', "mainDIV");
	document.body.appendChild(newdiv);
	//newdiv.align = "center";
	//newdiv.style.width = window.document.body.clientWidth-10;
	//newdiv.style.overflow = "auto";

	//	var myButton = document.createElement("BUTTON");
	//	myButton.setAttribute('id', "getPerfButton");
	//	myButton.appendChild(document.createTextNode("GET PERF DATA"));
	//	document.getElementById("mainDIV").appendChild(myButton);
	//	myButton.onclick = perf_createClosure(PERF_DB_OBJ, PERF_DB_OBJ.getPerfDataFromDB);

	PERF_DB_OBJ.getPerfDataFromDB();

//	var myButton = document.createElement("BUTTON");
	//	myButton.setAttribute('id', "getPerfAvgButton");
	//	myButton.appendChild(document.createTextNode("GET PERF AVERAGE DATA"));
	//	document.getElementById("mainDIV").appendChild(myButton);
	//	myButton.onclick = perf_createClosure(PERF_DB_OBJ, PERF_DB_OBJ.constructAveragePerfTable);

	//	myButton = document.createElement("BUTTON");
	//	myButton.setAttribute('id', "showBarChartButton");
	//	myButton.appendChild(document.createTextNode("Show BAR Charts"));
	//	document.getElementById("mainDIV").appendChild(myButton);
	//	myButton.onclick = perf_createClosure(PERF_DB_OBJ, PERF_DB_OBJ.showBarChart);

	//	myButton = document.createElement("BUTTON");
	//	myButton.setAttribute('id', "createGrid");
	//	myButton.appendChild(document.createTextNode("Create Grid"));
	//	document.getElementById("mainDIV").appendChild(myButton);
	//	myButton.onclick = perf_createClosure(PERF_DB_OBJ, PERF_DB_OBJ.constructExt2Table);

	//		myButton = document.createElement("BUTTON");
	//	myButton.setAttribute('id', "showLineChartButton");
	//	myButton.appendChild(document.createTextNode("Show Line Charts"));
	//	document.getElementById("mainDIV").appendChild(myButton);
	//	myButton.onclick = perf_createClosure(PERF_DB_OBJ, PERF_DB_OBJ.showAllLineCharts);


	//		myButton = document.createElement("BUTTON");
	//	myButton.setAttribute('id', "createTabs");
	//	myButton.appendChild(document.createTextNode("Create Tabs"));
	//	document.getElementById("mainDIV").appendChild(myButton);
	//	myButton.onclick = perf_createClosure(PERF_DB_OBJ, createTabs);

	//create tabs div and attach the rest to it.
	_createAndAppendDivs("mainDIV", "globalTabs_shell", false);
	_createAndAppendDivs("mainDIV", "perf_overview", true);
	_createAndAppendDivs("globalTabs_shell", "perf_columnChart", true);
	_createAndAppendDivs("globalTabs_shell", "perf_trends", true);

		//overview tab..
	_createAndAppendDivs("perf_overview", "perfAverageTableId", false);
	_createAndAppendDivs("perf_overview", "showLoadingOnOverviewDIV", false);//attach loading div
	//create table to hold two line graphs...
	_createAndAppendDivs("perf_overview", "overview_showImportantLineGraphs", false);
	document.getElementById("overview_showImportantLineGraphs").innerHTML = "<table><tr><td id ='showTotalTimeLineChartDIV'></TD>"
	+"<td id ='showLoginLineChartDIV'></TD></TR></table>";

	_createAndAppendDivs("perf_overview", "JSExt2_AverageTableDIV", false);//avg chart extjs2 version
	_createAndAppendDivs("perf_overview", "JSExt2_AverageTableDIV_Slowest", false);//avg chart extjs2 version
	_createAndAppendDivs("perf_overview", "perfAverageTableId", false);//avg chart normal-version
	_createAndAppendDivs("perf_overview", "perfTableId", false);//all DB values

	//bar chart tab..
	_createAndAppendDivs("perf_columnChart", "bc_SelectMenuDIV", false);//bar chart versions menu
	_createAndAppendDivs("perf_columnChart", "barchartGlobalDiv", false);//bar chart for a particular version
	_createAndAppendDivs("perf_columnChart", "barStackedchartGlobalDiv", false);//bar chart for a particular version
	//trends tab..
	_createAndAppendDivs("perf_trends", "flashGlobalDiv", false);//line chart for all actions

	//create global tabs...
	var tmpClosure1 = perf_createClosure(PERF_DB_OBJ, PERF_DB_OBJ.createVersionsMenuForBarChart);
	var tmpClosure2 = perf_createClosure(PERF_DB_OBJ, PERF_DB_OBJ.showAllLineCharts);
	var val = [{contentEl:'perf_overview', title: 'Overview: Average Time Taken'},
		{contentEl:'perf_columnChart', title: 'Bar Chart: Compares Different Browsers, Same versions', listeners: {activate: tmpClosure1}},
		{contentEl:'perf_trends', title: 'Trends: Line Charts for individual actions traking trend', listeners: {activate: tmpClosure2}}];
	createGlobalTabs(val);
	document.getElementById("showLoadingOnOverviewDIV").innerHTML = "Loading...";

	/*		var	div = document.createElement("DIV");
	   div.setAttribute('id', "flashGlobalDiv");//flash charts div
	   document.getElementById("globalTabs").appendChild(div);

   //	 div = document.createElement("DIV");
   //	div.setAttribute('id', "tabs1");
	 //  document.getElementById("mainDIV").appendChild(div);

		div = document.createElement("DIV");
	   div.setAttribute('id', "perfTableId");//all values
	   document.getElementById("globalTabs").appendChild(div);

	   div = document.createElement("DIV");
	   div.setAttribute('id', "perfAverageTableId");//average divs
	   document.getElementById("globalTabs").appendChild(div);
	   */
}

function _createAndAppendDivs(parentId, nodeId, setSize) {
	var div = document.createElement("DIV");
	div.setAttribute('id', nodeId);
	if (setSize) {
		div.className = "x-hide-display";
		//	div.style.width = window.document.body.clientWidth-10;
		div.style.height = "1000px";
		div.style.overflow = "auto";
	}
	document.getElementById(parentId).appendChild(div);
}

window.onload = initialize;

