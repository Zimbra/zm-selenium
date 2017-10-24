; Set focus to file name field in File Explorer dialog

SetFocusToFileNameField()

Func SetFocusToFileNameField()
   Local $windowHandle;
   Local $windowTitle;
   Local $windowField = "Edit1";

   If WinExists("File Upload") Then
	  $windowTitle = "File Upload";
   Else
	  $windowTitle = "Open";
   EndIf

   $windowHandle = WinWait($windowTitle, "");
   If Not WinActive($windowHandle) Then WinActivate($windowHandle);
   ControlSetText($windowHandle, "", "Edit1", "");
   ControlFocus($windowTitle, "", $windowField);
EndFunc