package com.zimbra.qa.selenium.framework.ui;




/**
 * The <code>Button</code> class defines constants that represent
 * general buttons in the client apps.
 * <p>
 * <p>
 * Action constant names start with "B_" for buttons or "O_" for
 * optional context menu button, and take the general format
 * <code>B_PAGE_TEXT</code>,
 * where "Page" is the application name such as MAIL, ADDRESSBOOK, and
 * "Text" is the displayed English text on the button.  For non-page
 * specific Buttons, the "Page" is not specified.
 * <p>
 * The action constants can be used in page methods, for example:
 * <pre>
 * {@code
 * // Click on the NEW button to compose a new mail
 * app.zPageMail.zToolbarPressButton(Button.B_TAG, Button.O_TAG_REMOVETAG);
 * }
 * </pre>
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public class Button {

	// General buttons and pulldown options
	public static final Button B_NEW = new Button("B_NEW");
	public static final Button B_DELETE = new Button("B_DELETE");
	public static final Button B_MOVE = new Button("B_MOVE");
	public static final Button B_PRINT = new Button("B_PRINT");
	public static final Button B_TAG = new Button("B_TAG");
	public static final Button B_SAVE = new Button("B_SAVE");
	public static final Button B_RENAME = new Button("B_RENAME");
	public static final Button B_SHARE = new Button("B_SHARE");
	public static final Button B_CLOSE = new Button("B_CLOSE");
	public static final Button B_ACTIONS = new Button("B_ACTIONS");
	
	public static final Button O_NEW_MESSAGE = new Button("O_NEW_MESSAGE");
	public static final Button O_NEW_CONTACT = new Button("O_NEW_CONTACT");
	public static final Button O_NEW_CONTACTGROUP = new Button("O_NEW_CONTACTGROUP");
	public static final Button O_NEW_APPOINTMENT = new Button("O_NEW_APPOINTMENT");
	public static final Button O_NEW_TASK = new Button("O_NEW_TASK");
	public static final Button O_NEW_DOCUMENT = new Button("O_NEW_DOCUMENT");
	public static final Button O_NEW_FOLDER = new Button("O_NEW_FOLDER");
	public static final Button O_NEW_TAG = new Button("O_NEW_TAG");
	public static final Button O_NEW_ADDRESSBOOK = new Button("O_NEW_ADDRESSBOOK");
	public static final Button O_NEW_CALENDAR = new Button("O_NEW_CALENDAR");
	public static final Button O_NEW_TASKFOLDER = new Button("O_NEW_TASKFOLDER");
	public static final Button O_NEW_BRIEFCASE = new Button("O_NEW_BRIEFCASE");
	public static final Button O_TAG_NEWTAG = new Button("O_TAG_NEWTAG");
	public static final Button O_TAG_REMOVETAG = new Button("O_TAG_REMOVETAG");


	// General dialog buttons
	public static final Button B_YES = new Button("B_YES");
	public static final Button B_NO = new Button("B_NO");
	public static final Button B_CANCEL = new Button("B_CANCEL");
	public static final Button B_OK = new Button("B_OK");
	public static final Button O_EDIT_LINK = new Button("O_EDIT_LINK");

	// MailPage buttons and pulldown options
	public static final Button B_GETMAIL = new Button("B_GETMAIL");
	public static final Button B_LOADFEED = new Button("B_LOADFEED");
	public static final Button B_REPLY = new Button("B_REPLY");
	public static final Button B_REPLYALL = new Button("B_REPLYALL");
	public static final Button B_FORWARD = new Button("B_FORWARD");
	public static final Button B_RESPORTSPAM = new Button("B_RESPORTSPAM");
	public static final Button B_RESPORTNOTSPAM = new Button("B_RESPORTNOTSPAM");
	public static final Button B_NEWWINDOW = new Button("B_NEWWINDOW");
	public static final Button B_LISTVIEW = new Button("B_LISTVIEW");

	public static final Button O_LISTVIEW_BYCONVERSATION = new Button("O_LISTVIEW_BYCONVERSATION");
	public static final Button O_LISTVIEW_BYMESSAGE = new Button("O_LISTVIEW_BYMESSAGE");
	public static final Button O_LISTVIEW_READINGPANEBOTTOM = new Button("O_LISTVIEW_READINGPANEBOTTOM");
	public static final Button O_LISTVIEW_READINGPANERIGHT = new Button("O_LISTVIEW_READINGPANERIGHT");
	public static final Button O_LISTVIEW_READINGPANEOFF = new Button("O_LISTVIEW_READINGPANEOFF");

	// MailPage context menu
	public static final Button O_MARK_AS_READ = new Button("O_MARK_AS_READ");
	public static final Button O_MARK_AS_UNREAD = new Button("O_MARK_AS_UNREAD");
	public static final Button O_REPLY = new Button("O_REPLY");
	public static final Button O_REPLY_TO_ALL = new Button("O_REPLY_TO_ALL");
	public static final Button O_FORWARD = new Button("O_FORWARD");
	public static final Button O_EDIT_AS_NEW = new Button("O_EDIT_AS_NEW");
	public static final Button O_TAG_MESSAGE = new Button("O_TAG_MESSAGE");
	// public static final Button O_DELETE = new Button("O_DELETE");
	public static final Button O_MOVE = new Button("O_MOVE");
	public static final Button O_PRINT = new Button("O_PRINT");
	public static final Button O_MARK_AS_SPAM = new Button("O_MARK_AS_SPAM");
	public static final Button O_SHOW_ORIGINAL = new Button("O_SHOW_ORIGINAL");
	public static final Button O_NEW_FILTER = new Button("O_NEW_FILTER");
	public static final Button O_CREATE_APPOINTMENT = new Button("O_CREATE_APPOINTMENT");
	public static final Button O_CREATE_TASK = new Button("O_CREATE_TASK");
	public static final Button O_CLEAR_SEARCH_HIGHLIGHTS = new Button("O_CLEAR_SEARCH_HIGHLIGHTS");

	// Compose mail buttons and pulldown options
	public static final Button B_SEND = new Button("B_SEND");
	public static final Button B_SAVE_DRAFT = new Button("B_SAVE_DRAFT");
	public static final Button B_ADD_ATTACHMENT = new Button("B_ADD_ATTACHMENT");
	public static final Button B_SPELL_CHECK = new Button("B_SPELL_CHECK");
	public static final Button B_SIGNATURE = new Button("B_SIGNATURE");
	public static final Button B_OPTIONS = new Button("B_OPTIONS");
	public static final Button B_PRIORITY = new Button("B_PRIORITY");
	public static final Button B_SHOWBCC = new Button("B_SHOWBCC");

	public static final Button O_SEND_SEND_LATER = new Button("O_SEND_SEND_LATER");
	public static final Button O_SIGNATURE_DO_NOT_ADD_SIGNATURE = new Button("O_SIGNATURE_DO_NOT_ADD_SIGNATURE");
	public static final Button O_ADD_SIGNATURE = new Button("O_ADD_SIGNATURE");
	public static final Button O_OPTION_FORMAT_AS_HTML = new Button("O_OPTION_FORMAT_AS_HTML");
	public static final Button O_OPTION_FORMAT_AS_TEXT = new Button("O_OPTION_FORMAT_AS_TEXT");
	public static final Button O_OPTION_REQUEST_READ_RECEIPT = new Button("O_OPTION_REQUEST_READ_RECEIPT");
	public static final Button O_PRIORITY_HIGH = new Button("O_PRIORITY_HIGH");
	public static final Button O_PRIORITY_NORMAL = new Button("O_PRIORITY_NORMAL");
	public static final Button O_PRIORITY_LOW = new Button("O_PRIORITY_LOW");

	// SearchPage buttons and pulldown options
	public static final Button B_SEARCHTYPE = new Button("B_SEARCHTYPE");
	public static final Button B_SEARCH = new Button("B_SEARCH");
	public static final Button B_SEARCHSAVE = new Button("B_SEARCHSAVE");
	public static final Button B_SEARCHADVANCED = new Button("B_SEARCHADVANCED");
	
	public static final Button O_SEARCHTYPE_ALL = new Button("O_SEARCHTYPE_ALL");
	public static final Button O_SEARCHTYPE_EMAIL = new Button("O_SEARCHTYPE_EMAIL");
	public static final Button O_SEARCHTYPE_CONTACTS = new Button("O_SEARCHTYPE_CONTACTS");
	public static final Button O_SEARCHTYPE_GAL = new Button("O_SEARCHTYPE_GAL");
	public static final Button O_SEARCHTYPE_APPOINTMENTS = new Button("O_SEARCHTYPE_APPOINTMENTS");
	public static final Button O_SEARCHTYPE_TASKS = new Button("O_SEARCHTYPE_TASKS");
	public static final Button O_SEARCHTYPE_FILES = new Button("O_SEARCHTYPE_FILES");
	public static final Button O_SEARCHTYPE_INCLUDESHARED = new Button("O_SEARCHTYPE_INCLUDESHARED");

	
	//Briefcase buttons
	public static final Button B_UPLOAD_FILE = new Button("B_UPLOAD_FILE");
	public static final Button B_EDIT_FILE = new Button("B_EDIT_FILE");
	public static final Button B_OPEN_IN_SEPARATE_WINDOW = new Button("B_OPEN_IN_SEPARATE_WINDOW");
	public static final Button B_LAUNCH_IN_SEPARATE_WINDOW = new Button("B_LAUNCH_IN_SEPARATE_WINDOW");
	public static final Button O_SEND_AS_ATTACHMENT = new Button("O_SEND_AS_ATTACHMENT");
	public static final Button O_SEND_LINK = new Button("O_SEND_LINK");
	public static final Button O_EDIT = new Button("O_EDIT");
	public static final Button O_OPEN = new Button("O_OPEN");
	public static final Button O_DELETE = new Button("O_DELETE");
	public static final Button O_TAG_FILE = new Button("O_TAG_FILE");
	
    //Addressbook button
	public static final Button B_EDIT = new Button("zb__CNS__EDIT");
	public static final Button B_CONTACTGROUP = new Button("zmi__Contacts__CONTACTGROUP_MENU");

	public static final Button O_SEARCH_MAIL_SENT_TO_CONTACT = new Button("O_SEARCH_MAIL_SENT_TO_CONTACT");
	public static final Button O_SEARCH_MAIL_RECEIVED_FROM_CONTACT = new Button("O_SEARCH_MAIL_RECEIVED_FROM_CONTACT");
	 
	//Addressbook alphabet bar buttons
	public static final Button B_AB_ALL = new Button("0"); //_idx="0"
	public static final Button B_AB_123 = new Button("1");
	public static final Button B_AB_A = new Button("2");
	public static final Button B_AB_B = new Button("3");
	public static final Button B_AB_C = new Button("4");
	public static final Button B_AB_D = new Button("5");
	public static final Button B_AB_E = new Button("6");
	public static final Button B_AB_F = new Button("7");
	public static final Button B_AB_G = new Button("8");
	public static final Button B_AB_H = new Button("9");
	public static final Button B_AB_I = new Button("10");
	public static final Button B_AB_J = new Button("11");
	public static final Button B_AB_K = new Button("12");
	public static final Button B_AB_L = new Button("13");
	public static final Button B_AB_M = new Button("14");
	public static final Button B_AB_N = new Button("15");
	public static final Button B_AB_O = new Button("16");
	public static final Button B_AB_P = new Button("17");
	public static final Button B_AB_Q = new Button("18");
	public static final Button B_AB_R = new Button("19");
	public static final Button B_AB_S = new Button("20");
	public static final Button B_AB_T = new Button("21");
	public static final Button B_AB_U = new Button("22");
	public static final Button B_AB_V = new Button("23");
	public static final Button B_AB_W = new Button("24");
	public static final Button B_AB_X = new Button("25");
	public static final Button B_AB_Y = new Button("26");
	public static final Button B_AB_Z = new Button("27");

	

	//TODO: add more
	
	// Task buttons
	public static final Button B_TASK_FILTERBY = new Button("B_TASK_FILTERBY");
	public static final Button B_TASK_MARKCOMPLETED = new Button("B_TASK_FILTERBY");

	
	// Tree buttons
	public static final Button B_TREE_NEWFOLDER = new Button("B_TREE_NEWFOLDER");
	public static final Button B_TREE_NEWADDRESSBOOK = new Button("B_TREE_NEWADDRESSBOOK");
	public static final Button B_TREE_NEWCALENDAR = new Button("B_TREE_NEWCALENDAR");
	public static final Button B_TREE_NEWTASKLIST = new Button("B_TREE_NEWTASKLIST");
	public static final Button B_TREE_NEWBRIEFCASE = new Button("B_TREE_NEWBRIEFCASE");
	public static final Button B_TREE_BRIEFCASE_EXPANDCOLLAPSE = new Button("B_TREE_BRIEFCASE_EXPANDCOLLAPSE");
	public static final Button B_TREE_NEWTAG = new Button("B_TREE_NEWTAG");
	public static final Button B_TREE_RENAMETAG = new Button("B_TREE_RENAMETAG");
	public static final Button B_TREE_DELETE = new Button("B_TREE_DELETE");
	public static final Button B_TREE_EDIT = new Button("B_TREE_EDIT");
	// Tree buttons (Mail folders)
	public static final Button B_TREE_FOLDER_MARKASREAD = new Button("B_TREE_FOLDER_MARKASREAD");
	public static final Button B_TREE_FOLDER_EXPANDALL = new Button("B_TREE_FOLDER_EXPANDALL");
	public static final Button B_TREE_FOLDER_EMPTY = new Button("B_TREE_FOLDER_EMPTY");
	
	// Mail 'Display' buttons
	public static final Button B_ACCEPT = new Button("B_ACCEPT");
	public static final Button B_DECLINE = new Button("B_DECLINE");
	public static final Button B_TENTATIVE = new Button("B_TENTATIVE");
	public static final Button B_PROPOSE_NEW_TIME = new Button("B_PROPOSE_NEW_TIME");
	public static final Button B_VIEW_ENTIRE_MESSAGE = new Button("B_VIEW_ENTIRE_MESSAGE");
	public static final Button B_HIGHLIGHT_OBJECTS = new Button("B_HIGHLIGHT_OBJECTS");
	
	// Calendar Buttons
	public static final Button B_REFRESH = new Button("B_REFRESH");
	
    // Calendar	Views
	public static final Button O_LISTVIEW_DAY = new Button("POPUP_DAY_VIEW");
	public static final Button O_LISTVIEW_WORKWEEK = new Button("POPUP_WORK_WEEK_VIEW");
	public static final Button O_LISTVIEW_WEEK = new Button("POPUP_WEEK_VIEW");
	public static final Button O_LISTVIEW_MONTH = new Button("POPUP_MONTH_VIEW");
	public static final Button O_LISTVIEW_LIST = new Button("POPUP_CAL_LIST_VIEW");
	public static final Button O_LISTVIEW_SCHEDULE = new Button("POPUP_SCHEDULE_VIEW");

	//// Admin Console
	
	// Accounts buttons
	public static final Button O_ACCOUNTS_ACCOUNT = new Button("O_ACCOUNTS_ACCOUNT");

	// Distribution List  buttons
	public static final Button O_DISTRIBUTIUONLISTS_DISTRIBUTIONLIST=new Button("O_DISTRIBUTIUONLISTS_DISTRIBUTIONLIST");
	
	// Aliases buttons
	public static final Button O_ALIASES_ALIAS = new Button("O_ALIASES_ALIAS");
	
	// Resources button
	public static final Button O_RESOURCES_RESOURCE = new Button("O_RESOURCES_RESOURCE");
	

	
	// Button properties
	private final String ID;

	protected Button(String id) {
		this.ID = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Button other = (Button) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ID;
	}


}
