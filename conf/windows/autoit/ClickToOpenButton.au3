; Click to Open button in File Explorer dialog

#include <AutoItConstants.au3>
#include <MsgBoxConstants.au3>

ClickToOpenButton()

Func ClickToOpenButton()
   Local $windowHandle;
   Local $windowTitle;
   Local $openButton = "Button1";
   Local $openButtonHandle;
   Local $openButtonPosition;

   If WinExists("File Upload") Then
	  $windowTitle = "File Upload";
   Else
	  $windowTitle = "Open";
   EndIf

   WinActivate($windowTitle);
   ControlClick($windowTitle, "", $openButton);

   ; $openButtonHandle = ControlGetHandle($windowTitle, "", $openButton);
   ; $openButtonPosition = WinGetPos($openButtonHandle);
   ; MouseClick($MOUSE_CLICK_PRIMARY, $openButtonPosition[0]+20, $openButtonPosition[1]+10, 2);
EndFunc