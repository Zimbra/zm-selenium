SetFocusToFileNameField()
Func SetFocusToFileNameField()

    ; Get the window title of the most recent active window.
	Local $activeWindowTitle = WinGetTitle("[ACTIVE]")

	; Focus to file name field
	ControlFocus($sText,"","Edit1")
	
EndFunc