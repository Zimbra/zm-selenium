; Click to Open button in File Explorer dialog

#include <AutoItConstants.au3>
#include <MsgBoxConstants.au3>

ClickToOpenButton()

Func ClickToOpenButton()
   Local $windowHandle;
   Local $windowTitle;
   Local $openButton = "Button1";

   If WinExists("File Upload") Then
	  $windowTitle = "File Upload";
   Else
	  $windowTitle = "Open";
   EndIf

   $windowHandle = WinWait($windowTitle, "");
   If Not WinActive($windowHandle) Then WinActivate($windowHandle);
   ControlClick($windowHandle, "", $openButton);

   ; Local $openButtonHandle;
   ; Local $openButtonPosition;
   ; $openButtonHandle = ControlGetHandle($windowTitle, "", $openButton);
   ; $openButtonPosition = WinGetPos($openButtonHandle);
   ; MouseClick($MOUSE_CLICK_PRIMARY, $openButtonPosition[0]+20, $openButtonPosition[1]+10, 2);
EndFunc