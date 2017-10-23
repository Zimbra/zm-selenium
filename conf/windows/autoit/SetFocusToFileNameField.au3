; Set focus to file name field in File Explorer dialog

SetFocusToFileNameField()

Func SetFocusToFileNameField()
   Local $windowTitle;
   Local $windowField = "Edit1";

   If WinExists("File Upload") Then
	  $windowTitle = "File Upload";
   Else
	  $windowTitle = "Open";
   EndIf

   ControlFocus($windowTitle, "", $windowField);
EndFunc