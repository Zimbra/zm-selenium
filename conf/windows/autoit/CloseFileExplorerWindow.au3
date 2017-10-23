CloseFileExplorerWindow()

Func CloseFileExplorerWindow()
   Local $windowTitle;

   If WinExists("File Upload") Then
	  $windowTitle = "File Upload";
   Else
	  $windowTitle = "Open";
   EndIf

   WinClose ($windowTitle);
EndFunc

