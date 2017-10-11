ClickToOpenButton()
Func ClickToOpenButton()

	; Get the window title of the most recent active window.
	Local $activeWindowTitle = WinGetTitle("[ACTIVE]")

	; Click Open button
	ControlClick($activeWindowTitle, "" , "Button1")
	
EndFunc
