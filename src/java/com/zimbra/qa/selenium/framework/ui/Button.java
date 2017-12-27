/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.ui;

/**
 * The <code>Button</code> class defines constants that represent general
 * buttons in the client apps.
 * <p>
 * <p>
 * Action constant names start with "B_" for buttons or "O_" for optional
 * context menu button, and take the general format <code>B_PAGE_TEXT</code>,
 * where "Page" is the application name such as MAIL, ADDRESSBOOK, and "Text" is
 * the displayed English text on the button. For non-page specific Buttons, the
 * "Page" is not specified.
 * <p>
 * The action constants can be used in page methods, for example:
 *
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
	public static final Button B_NEW_FOLDER = new Button("B_NEW_FOLDER");
	public static final Button B_NEW_TAG = new Button("B_NEW_TAG");
	public static final Button B_GEAR_BOX = new Button("B_GEAR_BOX");
	public static final Button B_NEW_IN_NEW_WINDOW = new Button("B_NEW_IN_NEW_WINDOW");
	public static final Button B_DETACH_COMPOSE = new Button("B_DETACH_COMPOSE");
	public static final Button B_DELETE = new Button("B_DELETE");
	public static final Button B_MOVE = new Button("B_MOVE");
	public static final Button B_PRINT = new Button("B_PRINT");
	public static final Button B_TAG = new Button("B_TAG");
	public static final Button B_SAVE = new Button("B_SAVE");
	public static final Button B_RENAME = new Button("B_RENAME");
	public static final Button B_SHARE = new Button("B_SHARE");
	public static final Button B_CLOSE = new Button("B_CLOSE");
	public static final Button B_ACTIONS = new Button("B_ACTIONS");
	public static final Button B_REDIRECT = new Button("B_REDIRECT");
	public static final Button B_MUTE = new Button("B_MUTE");
	public static final Button B_HELP = new Button("B_HELP");
	public static final Button B_USER_NAME = new Button("B_USER_NAME");
	public static final Button B_Attachment = new Button("B_Attachment");
	public static final Button B_DUPLICATE = new Button("B_DUPLICATE");
	public static final Button B_OPENTAB = new Button("B_OPENTAB");
	public static final Button B_ATTACH = new Button("B_ATTACH");
	public static final Button O_ATTACH_DROPDOWN = new Button("O_ATTACH_DROPDOWN");
	public static final Button B_MY_COMPUTER = new Button("B_MY_COMPUTER");
	public static final Button B_ATTACH_INLINE = new Button("B_ATTACH_INLINE");
	public static final Button B_BEGIN_SETUP = new Button("B_BEGIN_SETUP");
	public static final Button B_FINISH = new Button("B_FINISH");
	public static final Button B_REVOKE = new Button("B_REVOKE");
	public static final Button B_RESET = new Button("B_RESET_Domain");
	public static final Button B_RESET_DOMAIN = new Button("B_RESET_DOMAIN");
	public static final Button B_ADD_ACCOUNT = new Button("B_ADD_ACCOUNT");
	public static final Button B_EDIT_ACCOUNT = new Button("B_EDIT_ACCOUNT");
	public static final Button B_DELETE_ACCOUNT = new Button("B_DELETE_ACCOUNT");
	public static final Button B_EDIT_DOMAIN = new Button("B_EDIT_DOMAIN");
	public static final Button B_ADD_COS = new Button("B_ADD_COS");
	public static final Button B_EDIT_COS = new Button("B_EDIT_COS");
	public static final Button B_DELETE_COS = new Button("B_DELETE_COS");
	public static final Button B_ADD_NEW_BUDDY = new Button("B_ADD_NEW_BUDDY");

	public static final Button O_NEW = new Button("O_NEW");
	public static final Button O_NEW_ADMIN = new Button("O_NEW_ADMIN");
	public static final Button O_NEW_MESSAGE = new Button("O_NEW_MESSAGE");
	public static final Button O_NEW_CONTACT = new Button("O_NEW_CONTACT");
	public static final Button O_NEW_CONTACTGROUP = new Button("O_NEW_CONTACTGROUP");
	public static final Button O_NEW_DISTRIBUTION_LIST = new Button("O_NEW_DISTRIBUTION_LIST");
	public static final Button O_NEW_APPOINTMENT = new Button("O_NEW_APPOINTMENT");
	public static final Button O_NEW_TASK = new Button("O_NEW_TASK");
	public static final Button O_NEW_DOCUMENT = new Button("O_NEW_DOCUMENT");
	public static final Button O_NEW_FOLDER = new Button("O_NEW_FOLDER");
	public static final Button O_NEW_TAG = new Button("O_NEW_TAG");
	public static final Button O_REMOVE_TAG = new Button("O_REMOVE_TAG");
	public static final Button O_NEW_CONTACTS_FOLDER = new Button("O_NEW_CONTACTS_FOLDER");
	public static final Button O_NEW_CALENDAR = new Button("O_NEW_CALENDAR");
	public static final Button O_NEW_TASK_FOLDER = new Button("O_NEW_TASK_FOLDER");
	public static final Button O_NEW_BRIEFCASE = new Button("O_NEW_BRIEFCASE");
	public static final Button O_TAG_NEWTAG = new Button("O_TAG_NEWTAG");
	public static final Button O_TAG_REMOVETAG = new Button("O_TAG_REMOVETAG");
	public static final Button O_PRINT_TASKFOLDER = new Button("O_PRINT_TASKFOLDER");
	public static final Button O_DUPLICATE_COS = new Button("O_DUPLICATE_COS");
	public static final Button O_VIEW_ACCOUNTS = new Button("O_VIEW_ACCOUNTS");
	public static final Button O_CONFIGURE_GRANTS = new Button("O_CONFIGURE_GRANTS");

	// Accept decline options
	public static final Button B_CALENDAR = new Button("B_CALENDAR");
	public static final Button O_ACCEPT_NOTIFY_ORGANIZER = new Button("O_ACCEPT_NOTIFY_ORGANIZER");
	public static final Button O_ACCEPT_EDIT_REPLY = new Button("O_ACCEPT_EDIT_REPLY");
	public static final Button O_ACCEPT_DONT_NOTIFY_ORGANIZER = new Button("O_ACCEPT_DONTNOTIFY_ORGANIZER");
	public static final Button O_TENTATIVE_NOTIFY_ORGANIZER = new Button("O_TENTATIVE_NOTIFY_ORGANIZER");
	public static final Button O_TENTATIVE_EDIT_REPLY = new Button("O_TENTATIVE_EDIT_REPLY");
	public static final Button O_TENTATIVE_DONT_NOTIFY_ORGANIZER = new Button("O_TENTATIVE_DONTNOTIFY_ORGANIZER");
	public static final Button O_DECLINE_NOTIFY_ORGANIZER = new Button("O_DECLINE_NOTIFY_ORGANIZER");
	public static final Button O_DECLINE_EDIT_REPLY = new Button("O_DECLINE_EDIT_REPLY");
	public static final Button O_DECLINE_DONT_NOTIFY_ORGANIZER = new Button("O_DECLINE_DONTNOTIFY_ORGANIZER");

	// General dialog buttons
	public static final Button B_YES = new Button("B_YES");
	public static final Button B_NO = new Button("B_NO");
	public static final Button B_CANCEL = new Button("B_CANCEL");
	public static final Button B_OK = new Button("B_OK");
	public static final Button B_ADD = new Button("B_ADD");
	public static final Button B_ADD_NEW = new Button("B_ADD_NEW");
	public static final Button B_BACK = new Button("B_BACK");
	public static final Button B_NEXT = new Button("B_NEXT");
	public static final Button O_EDIT_LINK = new Button("O_EDIT_LINK");
	public static final Button O_REVOKE_LINK = new Button("O_REVOKE_LINK");
	public static final Button O_RESEND_LINK = new Button("O_RESEND_LINK");
	public static final Button B_MORE_DETAILS = new Button("B_MORE_DETAILS");
	public static final Button B_SENDCANCELLATION = new Button("B_SENDCANCELLATION");
	public static final Button B_EDITMESSAGE = new Button("B_EDITMESSAGE");
	public static final Button B_CANCEL_CONFIRMDELETE = new Button("B_CANCEL_CONFIRMDELETE");

	// Signature warning dialog buttons
	public static final Button B_Signature_OK = new Button("B_Signature_Ok");

	// Main Page buttons and pulldown options
	public static final Button B_ACCOUNT = new Button("B_ACCOUNT");

	public static final Button O_ADMIN_CONSOLE = new Button("O_ADMIN_CONSOLE");
	public static final Button O_PRODUCT_HELP = new Button("O_PRODUCT_HELP");
	public static final Button O_HELP_CENTRAL_ONLINE = new Button("O_HELP_CENTRAL_ONLINE");
	public static final Button O_NEW_FEATURES = new Button("O_NEW_FEATURES");
	public static final Button O_ABOUT = new Button("O_ABOUT");
	public static final Button O_OFFLINE_SETTINGS = new Button("O_OFFLINE_SETTINGS");
	public static final Button O_CHANGE_PASSWORD = new Button("O_CHANGE_PASSWORD");
	public static final Button O_SHORTCUT = new Button("O_SHORTCUT");

	// MailPage buttons and pulldown options
	public static final Button B_GETMAIL = new Button("B_GETMAIL");
	public static final Button B_FOLDER_TREE = new Button("B_FOLDER_TREE");
	public static final Button B_LOAD_IMAGES = new Button("B_LOAD_IMAGES");
	public static final Button B_LOADFEED = new Button("B_LOADFEED");
	public static final Button B_REPLY = new Button("B_REPLY");
	public static final Button B_REPLYALL = new Button("B_REPLYALL");
	public static final Button B_FORWARD = new Button("B_FORWARD");
	public static final Button B_RESPORTSPAM = new Button("B_RESPORTSPAM");
	public static final Button B_RESPORTNOTSPAM = new Button("B_RESPORTNOTSPAM");
	public static final Button B_NEWWINDOW = new Button("B_NEWWINDOW");
	public static final Button B_LISTVIEW = new Button("B_LISTVIEW");
	public static final Button B_ARCHIVE = new Button("B_ARCHIVE");
	public static final Button B_READMORE = new Button("B_READMORE");
	public static final Button B_SELECT_ALL = new Button("B_SELECT_ALL");
	public static final Button B_SHIFT_SELECT_ALL = new Button("B_SHIFT_SELECT_ALL");

	public static final Button O_LISTVIEW_BYCONVERSATION = new Button("O_LISTVIEW_BYCONVERSATION");
	public static final Button O_LISTVIEW_BYMESSAGE = new Button("O_LISTVIEW_BYMESSAGE");
	public static final Button O_LISTVIEW_READINGPANEBOTTOM = new Button("O_LISTVIEW_READINGPANEBOTTOM");
	public static final Button O_LISTVIEW_READINGPANERIGHT = new Button("O_LISTVIEW_READINGPANERIGHT");
	public static final Button O_LISTVIEW_READINGPANEOFF = new Button("O_LISTVIEW_READINGPANEOFF");

	// MailPage View Reading Pane (options)
	public static final Button B_MAIL_VIEW_READING_PANE_BOTTOM = new Button("B_MAIL_VIEW_READING_PANE_BOTTOM");
	public static final Button B_MAIL_VIEW_READING_PANE_RIGHT = new Button("B_MAIL_VIEW_READING_PANE_RIGHT");
	public static final Button B_MAIL_VIEW_READING_PANE_OFF = new Button("B_MAIL_VIEW_READING_PANE_OFF");
	public static final Button B_MIME_ATTACHEMENT = new Button("B_MIME_ATTACHEMENT");
	public static final Button B_ATTACH_PREVIEW = new Button("B_ATTACH_PREVIEW");
	public static final Button B_EML_ATTACHEMENT = new Button("B_EML_ATTACHEMENT");

	public static final Button B_MAIL_VIEW_BY_CONVERSATION = new Button("B_MAIL_VIEW_BY_CONVERSATION");
	public static final Button B_MAIL_VIEW_BY_MESSAGE = new Button("B_MAIL_VIEW_BY_MESSAGE");

	// MailPage list buttons (sort by options)
	public static final Button B_MAIL_LIST_SORTBY_FLAGGED = new Button("B_MAIL_LIST_SORTBY_FLAGGED");
	public static final Button B_MAIL_LIST_SORTBY_FROM = new Button("B_MAIL_LIST_SORTBY_FROM");
	public static final Button B_MAIL_LIST_SORTBY_ATTACHMENT = new Button("B_MAIL_LIST_SORTBY_ATTACHMENT");
	public static final Button B_MAIL_LIST_SORTBY_SUBJECT = new Button("B_MAIL_LIST_SORTBY_SUBJECT");;
	public static final Button B_MAIL_LIST_SORTBY_SIZE = new Button("B_MAIL_LIST_SORTBY_SIZE");
	public static final Button B_MAIL_LIST_SORTBY_RECEIVED = new Button("B_MAIL_LIST_SORTBY_RECEIVED");

	// MailPage list buttons (group by options)
	public static final Button B_MAIL_LIST_GROUPBY_FROM = new Button("B_MAIL_LIST_GROUPBY_FROM");;
	public static final Button B_MAIL_LIST_GROUPBY_DATE = new Button("B_MAIL_LIST_GROUPBY_DATE");;
	public static final Button B_MAIL_LIST_GROUPBY_SIZE = new Button("B_MAIL_LIST_GROUPBY_SIZE");;

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
	public static final Button O_SHOW_CONVERSATION = new Button("O_SHOW_CONVERSATION");
	public static final Button O_NEW_FILTER = new Button("O_NEW_FILTER");
	public static final Button O_CREATE_APPOINTMENT = new Button("O_CREATE_APPOINTMENT");
	public static final Button O_CREATE_TASK = new Button("O_CREATE_TASK");
	public static final Button O_CLEAR_SEARCH_HIGHLIGHTS = new Button("O_CLEAR_SEARCH_HIGHLIGHTS");
	// Attach Context menu
	public static final Button O_MAILATTACH = new Button("O_MAILATTACH");
	public static final Button O_CONTACTATTACH = new Button("O_CONTACTATTACH");
	public static final Button O_BRIEFCASEATTACH = new Button("O_BRIEFCASEATTACH");

	// Compose mail buttons and pulldown options
	public static final Button B_SEND = new Button("B_SEND");
	public static final Button B_SAVE_DRAFT = new Button("B_SAVE_DRAFT");
	public static final Button B_ADD_ATTACHMENT = new Button("B_ADD_ATTACHMENT");
	public static final Button B_SPELL_CHECK = new Button("B_SPELL_CHECK");
	public static final Button B_SIGNATURE = new Button("B_SIGNATURE");
	public static final Button B_OPTIONS = new Button("B_OPTIONS");
	public static final Button B_PRIORITY = new Button("B_PRIORITY");
	public static final Button B_TO = new Button("B_TO");
	public static final Button B_OPTIONAL = new Button("B_OPTIONAL");
	public static final Button B_CC = new Button("B_CC");
	public static final Button B_BCC = new Button("B_BCC");
	public static final Button B_SHOWBCC = new Button("B_SHOWBCC");
	public static final Button B_SHOWCC = new Button("B_SHOWCC");
	public static final Button B_REMOVE = new Button("B_REMOVE");
	public static final Button B_REMOVE_ALL = new Button("B_REMOVE_ALL");
	public static final Button B_SHOW_NAMES_FROM = new Button("B_SHOW_NAMES_FROM");
	public static final Button B_RFC822_ATTACHMENT_LINK = new Button("B_RFC822_ATTACHMENT_LINK");

	public static final Button O_SEND_SEND = new Button("O_SEND_SEND");
	public static final Button O_SEND_SEND_LATER = new Button("O_SEND_SEND_LATER");
	public static final Button O_SIGNATURE_DO_NOT_ADD_SIGNATURE = new Button("O_SIGNATURE_DO_NOT_ADD_SIGNATURE");
	public static final Button O_ADD_SIGNATURE = new Button("O_ADD_SIGNATURE");
	public static final Button O_OPTION_FORMAT_AS_HTML = new Button("O_OPTION_FORMAT_AS_HTML");
	public static final Button O_OPTION_FORMAT_AS_TEXT = new Button("O_OPTION_FORMAT_AS_TEXT");
	public static final Button O_OPTION_REQUEST_READ_RECEIPT = new Button("O_OPTION_REQUEST_READ_RECEIPT");
	public static final Button O_DONT_INCLUDE_ORIGINAL_MESSAGE = new Button("O_DONT_INCLUDE_ORIGINAL_MESSAGE");
	public static final Button O_INCLUDE_ORIGINAL_MESSAGE = new Button("O_INCLUDE_ORIGINAL_MESSAGE");
	public static final Button O_INCLUDE_LAST_MESSAGE_ONLY = new Button("O_INCLUDE_LAST_MESSAGE_ONLY");
	public static final Button O_INCLUDE_ORIGINAL_AS_ATTACHMENT = new Button("O_INCLUDE_ORIGINAL_AS_ATTACHMENT");
	public static final Button O_USE_PRIFIX = new Button("O_USE_PRIFIX");
	public static final Button O_INCLUDE_HEADERS = new Button("O_INCLUDE_HEADERS");
	public static final Button O_PRIORITY_HIGH = new Button("O_PRIORITY_HIGH");
	public static final Button O_PRIORITY_NORMAL = new Button("O_PRIORITY_NORMAL");
	public static final Button O_PRIORITY_LOW = new Button("O_PRIORITY_LOW");
	public static final Button O_CONTACTS = new Button("O_CONTACTS");
	public static final Button O_PERSONAL_AND_SHARED_CONTACTS = new Button("O_PERSONAL_AND_SHARED_CONTACTS");
	public static final Button O_GLOBAL_ADDRESS_LIST = new Button("O_GLOBAL_ADDRESS_LIST");
	public static final Button O_ADD_FWD_SIGNATURE = new Button("O_ADD_FWD_SIGNATURE");
	public static final Button O_ADD_Reply_SIGNATURE = new Button("O_ADD_Reply_SIGNATURE");
	public static final Button O_ADD_ReplyAll_SIGNATURE = new Button("O_ADD_ReplyAll_SIGNATURE");
	public static final Button B_SECURE_EMAIL = new Button("B_SECURE_EMAIL");
	public static final Button O_DONT_SIGN = new Button("O_DONT_SIGN");
	public static final Button O_SIGN = new Button("O_SIGN");
	public static final Button O_SIGN_AND_ENCRYPT = new Button("O_SIGN_AND_ENCRYPT");

	// Show Conversation page buttons and pulldown options
	public static final Button B_CLOSE_CONVERSATION = new Button("B_CLOSE_CONVERSATION");

	// Dumpster dialog
	public static final Button B_RECOVER_DELETED_ITEMS = new Button("B_RECOVER_DELETED_ITEMS");
	public static final Button B_RECOVER_TO = new Button("B_RECOVER_TO");

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

	// Octopus buttons
	public static final Button B_TAB_MY_FILES = new Button("B_TAB_MY_FILES");
	public static final Button B_MY_FILES = new Button("B_MY_FILES");
	public static final Button B_MY_FILES_LIST_ITEM = new Button("B_MY_FILES_LIST_ITEM");
	public static final Button O_FOLDER_SHARE = new Button("O_FOLDER_SHARE");
	public static final Button O_FILE_SHARE = new Button("O_FILE_SHARE");
	public static final Button B_STOP_SHARING = new Button("B_STOP_SHARING");
	public static final Button B_LEAVE_THIS_SHARED_FOLDER = new Button("O_LEAVE_THIS_SHARED_FOLDER");
	public static final Button B_SHOW_MESSAGE = new Button("B_SHOW_MESSAGE");
	public static final Button B_EXPAND = new Button("B_EXPAND");
	public static final Button B_COLLAPSE = new Button("B_COLLAPSE");
	public static final Button B_HISTORY = new Button("B_HISTORY");
	public static final Button B_COMMENTS = new Button("B_COMMENTS");
	public static final Button O_SIGN_OUT = new Button("O_SIGN_OUT");
	public static final Button O_SETTINGS = new Button("O_SETTINGS");

	public static final Button B_TAB_SHARING = new Button("B_TAB_SHARING");
	public static final Button B_IGNORE = new Button("B_IGNORE");
	public static final Button B_ADD_TO_MY_FILES = new Button("B_ADD_TO_MY_FILES");

	public static final Button B_TAB_FAVORITES = new Button("B_TAB_FAVORITES");
	public static final Button O_FAVORITE = new Button("O_FAVORITE");
	public static final Button O_NOT_FAVORITE = new Button("O_NOT_FAVORITE");
	public static final Button B_WATCH = new Button("B_WATCH");
	public static final Button B_UNWATCH = new Button("B_UNWATCH");

	public static final Button B_TAB_HISTORY = new Button("B_TAB_HISTORY");
	public static final Button O_ALL_TYPES = new Button("O_ALL_TYPES");
	public static final Button O_FAVORITES = new Button("O_FAVORITES");
	public static final Button O_COMMENT = new Button("O_COMMENT");
	public static final Button O_SHARING = new Button("O_SHARING");
	public static final Button O_NEW_VERSION = new Button("O_NEW_VERSION");

	public static final Button B_TAB_TRASH = new Button("B_TAB_TRASH");

	public static final Button B_TAB_SEARCH = new Button("B_TAB_SEARCH");

	public static final Button B_SETTINGS = new Button("B_SETTINGS");

	public static final Button B_DONE = new Button("B_DONE");

	public static final Button B_UNLINK_AND_WIPE = new Button("B_UNLINK_AND_WIPE");

	// Briefcase buttons
	public static final Button B_UPLOAD_FILE = new Button("B_UPLOAD_FILE");
	public static final Button B_EDIT_FILE = new Button("B_EDIT_FILE");
	public static final Button B_OPEN_IN_SEPARATE_WINDOW = new Button("B_OPEN_IN_SEPARATE_WINDOW");
	public static final Button B_LAUNCH_IN_SEPARATE_WINDOW = new Button("B_LAUNCH_IN_SEPARATE_WINDOW");
	public static final Button O_SEND_AS_ATTACHMENT = new Button("O_SEND_AS_ATTACHMENT");
	public static final Button O_RESTORE_AS_CURRENT_VERSION = new Button("O_RESTORE_AS_CURRENT_VERSION");
	public static final Button O_SEND_LINK = new Button("O_SEND_LINK");
	public static final Button O_EDIT = new Button("O_EDIT");
	public static final Button O_OPEN = new Button("O_OPEN");
	public static final Button O_DELETE = new Button("O_DELETE");
	public static final Button O_TAG_FILE = new Button("O_TAG_FILE");
	public static final Button O_RENAME = new Button("O_RENAME");
	public static final Button O_CHECK_IN_FILE = new Button("O_CHECK_IN_FILE");
	public static final Button O_DISCARD_CHECK_OUT = new Button("O_DISCARD_CHECK_OUT");
	public static final Button B_TREE_EDIT_PROPERTIES = new Button("B_TREE_EDIT_PROPERTIES");
	public static final Button B_BROWSE = new Button("B_BROWSE");

	public static final Button O_EDIT_DISTRIBUTION_LIST = new Button("O_EDIT_DISTRIBUTION_LIST");
	public static final Button O_NEW_EMAIL = new Button("O_NEW_EMAIL");
	public static final Button O_SHARE_CONTACTS_FOLDER = new Button("O_SHARE_CONTACTS_FOLDER");
	public static final Button O_RENAME_FOLDER = new Button("O_RENAME_FOLDER");
	public static final Button O_EDIT_PROPERTIES = new Button("O_EDIT_PROPERTIES");
	public static final Button O_EXPAND_ALL = new Button("O_EXPAND_ALL");

	// Addressbook button
	public static final Button B_EDIT = new Button("zb__CNS__EDIT");
	public static final Button B_FILEAS = new Button("td$=_FILE_AS_select_container");

	public static final Button B_CONTACTGROUP = new Button("zmi__Contacts__CONTACTGROUP_MENU");

	// Addressbook - Contact Group form
	public static final Button B_CONTACTGROUP_ADD_ADDRESS = new Button("B_CONTACTGROUP_ADD_ADDRESS");
	public static final Button B_CONTACTGROUP_ADD_SEARCH_RESULT = new Button("B_CONTACTGROUP_ADD_SEARCH_RESULT");
	public static final Button B_CONTACTGROUP_ADD_ALL_SEARCH_RESULT = new Button(
			"B_CONTACTGROUP_ADD_ALL_SEARCH_RESULT");
	public static final Button B_CONTACTGROUP_SEARCH_TYPE = new Button("O_CONTACTGROUP_SEARCH_TYPE");
	public static final Button O_CONTACTGROUP_SEARCH_CONTACTS = new Button("O_CONTACTGROUP_SEARCH_CONTACTS");
	public static final Button O_CONTACTGROUP_SEARCH_GAL = new Button("O_CONTACTGROUP_SEARCH_GAL");
	public static final Button O_CONTACTGROUP_SEARCH_PERSONAL_AND_SHARED = new Button(
			"O_CONTACTGROUP_SEARCH_PERSONAL_AND_SHARED");
	public static final Button B_CHOOSE_ADDRESSBOOK = new Button("B_CHOOSE_ADDRESSBOOK");

	public static final Button B_ADD_ALL = new Button("B_ADD_ALL");
	public static final Button B_DISTRIBUTIONLIST_PROPERTIES = new Button("B_DISTRIBUTIONLIST_PROPERTIES");
	public static final Button B_DISTRIBUTIONLIST_ADD_ADDRESS = new Button("B_DISTRIBUTIONLIST_ADD_ADDRESS");
	public static final Button B_DISTRIBUTIONLIST_ADD_SEARCH_RESULT = new Button(
			"B_DISTRIBUTIONLIST_ADD_SEARCH_RESULT");
	public static final Button B_DISTRIBUTIONLIST_ADD_ALL_SEARCH_RESULT = new Button(
			"B_DISTRIBUTIONLIST_ADD_ALL_SEARCH_RESULT");
	public static final Button B_DISTRIBUTIONLIST_SEARCH_TYPE = new Button("O_DISTRIBUTIONLIST_SEARCH_TYPE");
	public static final Button O_DISTRIBUTIONLIST_SEARCH_CONTACTS = new Button("O_DISTRIBUTIONLIST_SEARCH_CONTACTS");
	public static final Button O_DISTRIBUTIONLIST_SEARCH_GAL = new Button("O_DISTRIBUTIONLIST_SEARCH_GAL");
	public static final Button O_DISTRIBUTIONLIST_SEARCH_PERSONAL_AND_SHARED = new Button(
			"O_DISTRIBUTIONLIST_SEARCH_PERSONAL_AND_SHARED");

	public static final Button O_SEARCH_MAIL_SENT_TO_CONTACT = new Button("O_SEARCH_MAIL_SENT_TO_CONTACT");
	public static final Button O_SEARCH_MAIL_RECEIVED_FROM_CONTACT = new Button("O_SEARCH_MAIL_RECEIVED_FROM_CONTACT");

	// Expand pulldown
	public static final Button O_PREFIX = new Button("O_PREFIX");
	public static final Button O_FIRST = new Button("O_FIRST");
	public static final Button O_MIDDLE = new Button("O_MIDDLE");
	public static final Button O_MAIDEN = new Button("O_MAIDEN");
	public static final Button O_LAST = new Button("O_LAST");
	public static final Button O_SUFFIX = new Button("O_SUFFIX");
	public static final Button O_NICKNAME = new Button("O_NICKNAME");
	public static final Button O_JOB_TITLE = new Button("O_JOB_TITLE");
	public static final Button O_DEPARTMENT = new Button("O_DEPARTMENT");
	public static final Button O_COMPANY = new Button("O_COMPANY");

	// File As pulldown
	public static final Button O_FILEAS_FIRSTLAST = new Button("O_FILEAS_FIRSTLAST");
	public static final Button O_FILEAS_LASTFIRST = new Button("O_FILEAS_LASTFIRST");
	public static final Button O_FILEAS_COMPANY = new Button("O_FILEAS_COMPANY");
	public static final Button O_FILEAS_FIRSTLASTCOMPANY = new Button("O_FILEAS_FIRSTLASTCOMPANY");
	public static final Button O_FILEAS_LASTFIRSTCOMPANY = new Button("O_FILEAS_LASTFIRSTCOMPANY");
	public static final Button O_FILEAS_COMPANYFIRSTLAST = new Button("O_FILEAS_COMPANYFIRSTLAST");
	public static final Button O_FILEAS_COMPANYLASTFIRST = new Button("O_FILEAS_COMPANYLASTFIRST");

	// Addressbook alphabet bar buttons
	public static final Button B_AB_ALL = new Button("0"); // _idx="0"
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

	// Task buttons
	public static final Button B_TASK_FILTERBY = new Button("B_TASK_FILTERBY");
	public static final Button B_TASK_MARKCOMPLETED = new Button("B_TASK_MARKCOMPLETED");
	public static final Button O_TASK_TODOLIST = new Button("O_TASK_TODOLIST");

	// Tree buttons
	public static final Button B_TREE_FOLDERS_OPTIONS = new Button("B_TREE_FOLDERS_PROPERTIES");
	public static final Button B_TREE_SEARCHES_OPTIONS = new Button("B_TREE_FOLDERS_PROPERTIES");
	public static final Button B_TREE_TAGS_OPTIONS = new Button("B_TREE_TAGS_PROPERTIES");

	public static final Button B_TREE_NEWFOLDER = new Button("B_TREE_NEWFOLDER");
	public static final Button B_TREE_NEWADDRESSBOOK = new Button("B_TREE_NEWADDRESSBOOK");
	public static final Button B_TREE_NEWCALENDAR = new Button("B_TREE_NEWCALENDAR");
	public static final Button B_TREE_NEW_EXTERNAL_CALENDAR = new Button("B_TREE_NEW_EXTERNAL_CALENDAR");
	public static final Button B_TREE_NEWTASKLIST = new Button("B_TREE_NEWTASKLIST");
	public static final Button B_TREE_NEWBRIEFCASE = new Button("B_TREE_NEWBRIEFCASE");
	public static final Button B_TREE_BRIEFCASE_EXPANDCOLLAPSE = new Button("B_TREE_BRIEFCASE_EXPANDCOLLAPSE");
	public static final Button B_TREE_NEWTAG = new Button("B_TREE_NEWTAG");
	public static final Button B_TREE_RENAMETAG = new Button("B_TREE_RENAMETAG");
	public static final Button B_TREE_DELETE = new Button("B_TREE_DELETE");
	public static final Button B_TREE_EDIT = new Button("B_TREE_EDIT");
	public static final Button B_TREE_FIND_SHARES = new Button("B_TREE_FIND_SHARES");
	// Tree buttons (Mail folders)
	public static final Button B_TREE_FOLDER_MARKASREAD = new Button("B_TREE_FOLDER_MARKASREAD");
	public static final Button B_TREE_FOLDER_EXPANDALL = new Button("B_TREE_FOLDER_EXPANDALL");
	public static final Button B_TREE_FOLDER_EMPTY = new Button("B_TREE_FOLDER_EMPTY");
	public static final Button B_TREE_FOLDER_GET_EXTERNAL = new Button("B_TREE_FOLDER_GET_EXTERNAL");
	public static final Button B_TREE_SHOW_REMAINING_FOLDERS = new Button("B_TREE_SHOW_REMAINING_FOLDERS");

	// Mail 'Display' buttons
	public static final Button B_ACCEPT = new Button("B_ACCEPT");
	public static final Button B_ACCEPT_PROPOSE_NEW_TIME = new Button("B_ACCEPT_PROPOSE_NEW_TIME");
	public static final Button B_ACCEPT_DROPDOWN = new Button("B_ACCEPT_DROPDOWN");
	public static final Button B_DECLINE = new Button("B_DECLINE");
	public static final Button B_DECLINE_PROPOSE_NEW_TIME = new Button("B_DECLINE_PROPOSE_NEW_TIME");
	public static final Button B_DECLINE_DROPDOWN = new Button("B_DECLINE_DROPDOWN");
	public static final Button B_TENTATIVE = new Button("B_TENTATIVE");
	public static final Button B_TENTATIVE_DROPDOWN = new Button("B_TENTATIVE_DROPDOWN");
	public static final Button B_ACCEPT_SHARE = new Button("B_ACCEPT_SHARE");;
	public static final Button B_DECLINE_SHARE = new Button("B_DECLINE_SHARE");;

	public static final Button B_PROPOSE_NEW_TIME = new Button("B_PROPOSE_NEW_TIME");
	public static final Button B_VIEW_ENTIRE_MESSAGE = new Button("B_VIEW_ENTIRE_MESSAGE");
	public static final Button B_HIGHLIGHT_OBJECTS = new Button("B_HIGHLIGHT_OBJECTS");
	public static final Button B_ADD_TO_CALENDAR = new Button("B_ADD_TO_CALENDAR");
	public static final Button B_BRIEFCASE = new Button("B_BRIEFCASE");

	public static final Button B_QUICK_REPLY_REPLY = new Button("B_QUICK_REPLY_REPLY");
	public static final Button B_QUICK_REPLY_REPLY_ALL = new Button("B_QUICK_REPLY_REPLY_ALL");
	public static final Button B_QUICK_REPLY_FORWARD = new Button("B_QUICK_REPLY_FORWARD");
	public static final Button B_QUICK_REPLY_MORE_ACTIONS = new Button("B_QUICK_REPLY_MORE_ACTIONS");
	public static final Button B_QUICK_REPLY_SEND = new Button("B_QUICK_REPLY_SEND");
	public static final Button B_QUICK_REPLY_CANCEL = new Button("B_QUICK_REPLY_CANCEL");
	public static final Button B_QUICK_REPLY_MORE = new Button("B_QUICK_REPLY_MORE");

	// Full pane view buttons
	public static final Button B_DELETE_FULL_VIEW_PANE = new Button("B_DELETE_FULL_VIEW_PANE");

	// Calendar
	public static final Button B_SHOW = new Button("B_SHOW");
	public static final Button B_SAVEANDCLOSE = new Button("B_SAVEANDCLOSE");
	public static final Button B_REFRESH = new Button("B_REFRESH");
	public static final Button B_SUGGESTATIME = new Button("B_SUGGESTATIME");
	public static final Button B_SUGGESTALOCATION = new Button("B_SUGGESTALOCATION");
	public static final Button B_SHOW_TIMES_ANYWAY = new Button("B_SHOW_TIMES_ANYWAY");
	public static final Button O_SUGGESTION_PREFERENCES = new Button("O_SUGGESTION_PREFERENCES");
	public static final Button O_FORMAT_AS_HTML = new Button("O_FORMAT_AS_HTML");
	public static final Button O_FORMAT_AS_HTML_MULTI_WINDOW = new Button("O_FORMAT_AS_HTML_MULTI_WINDOW");
	public static final Button O_FORMAT_AS_PLAIN_TEXT_MULTI_WINDOW = new Button("O_FORMAT_AS_PLAIN_TEXT_MULTI_WINDOW");
	public static final Button O_FORMAT_AS_PLAIN_TEXT = new Button("O_FORMAT_AS_PLAIN_TEXT");
	public static final Button B_10AM = new Button("B_10AM");
	public static final Button B_FIRST_TIME_SUGGESTION = new Button("B_FIRST_TIME_SUGGESTION");
	public static final Button B_SUGGESTEDLOCATION = new Button("B_SUGGESTEDLOCATION");
	public static final Button B_ONLY_INCLUDE_MY_WORKING_HOURS = new Button("B_ONLY_INCLUDE_MY_WORKING_HOURS");
	public static final Button B_ONLY_INCLUDE_OTHER_ATTENDEES_WORKING_HOURS = new Button(
			"B_ONLY_INCLUDE_OTHER_ATTENDEES_WORKING_HOURS");
	public static final Button F_NAME_EDIT_FIELD = new Button("F_NAME_EDIT_FIELD");
	public static final Button B_TAGAPPOINTMENTMENU = new Button("B_TAGAPPOINTMENTMENU");
	public static final Button B_LOCATIONMENU = new Button("B_LOCATIONMENU");
	public static final Button B_NEXT_PAGE = new Button("B_NEXT_PAGE");
	public static final Button B_PREVIOUS_PAGE = new Button("B_PREVIOUS_PAGE");
	public static final Button B_MONTH = new Button("B_MONTH");
	public static final Button B_TODAY = new Button("B_TODAY");
	public static final Button B_REPEAT_DROPDOWN_DISABLED = new Button("B_REPEAT_DROPDOWN_DISABLED");
	public static final Button B_REPEAT_DESCRIPTION_DISABLED = new Button("B_REPEAT_DESCRIPTION_DISABLED");

	public static final Button B_VIEW = new Button("B_VIEW");
	public static final Button B_DAY_VIEW = new Button("B_DAY_VIEW");
	public static final Button B_WORKWEEK_VIEW = new Button("B_WORKWEEK_VIEW");
	public static final Button B_WEEK_VIEW = new Button("B_WEEK_VIEW");
	public static final Button B_MONTH_VIEW = new Button("B_MONTH_VIEW");
	public static final Button B_LIST_VIEW = new Button("B_LIST_VIEW");
	public static final Button B_SCHEDULE_VIEW = new Button("B_SCHEDULE_VIEW");
	public static final Button B_FREEBUSY_VIEW = new Button("B_FREEBUSY_VIEW");

	public static final Button O_LISTVIEW_TAG = new Button("O_LISTVIEW_TAG");
	public static final Button O_LISTVIEW_NEWTAG = new Button("O_LISTVIEW_NEWTAG");
	public static final Button O_LISTVIEW_REMOVETAG = new Button("O_LISTVIEW_REMOVETAG");

	public static final Button B_DELETE_DISABLED = new Button("B_DELETE_DISABLED");
	public static final Button O_DELETE_DISABLED = new Button("O_DELETE_DISABLED");
	public static final Button O_REINVITE_ATTENDEES_DISABLED = new Button("O_REINVITE_ATTENDEES_DISABLED");
	public static final Button O_FORWARD_DISABLED = new Button("O_FORWARD_DISABLED");
	public static final Button O_MOVE_DISABLED = new Button("O_MOVE_DISABLED");
	public static final Button O_CREATE_A_COPY_DISABLED = new Button("O_CREATE_A_COPY_DISABLED");
	public static final Button O_SHOW_ORIGINAL_DISABLED = new Button("O_SHOW_ORIGINAL_DISABLED");
	public static final Button O_TAG_APPOINTMENT_DISABLED = new Button("O_TAG_APPOINTMENT_DISABLED");
	public static final Button O_REPLY_DISABLED = new Button("O_REPLY_DISABLED");
	public static final Button B_TAG_APPOINTMENT_DISABLED_READONLY_APPT = new Button(
			"B_TAG_APPOINTMENT_DISABLED_READONLY_APPT");
	public static final Button B_SAVE_DISABLED_READONLY_APPT = new Button("B_SAVE_DISABLED_READONLY_APPT");
	public static final Button B_ACCEPTED_DISABLED_READONLY_APPT = new Button("B_ACCEPTED_DISABLED_READONLY_APPT");
	public static final Button O_EDIT_DISABLED_READONLY_APPT = new Button("O_EDIT_DISABLED_READONLY_APPT");
	public static final Button O_FORWARD_DISABLED_READONLY_APPT = new Button("O_FORWARD_DISABLED_READONLY_APPT");
	public static final Button O_PROPOSE_NEW_TIME_DISABLED_READONLY_APPT = new Button(
			"O_PROPOSE_NEW_TIME_DISABLED_READONLY_APPT");
	public static final Button O_DELETE_DISABLED_READONLY_APPT = new Button("O_DELETE_DISABLED_READONLY_APPT");
	public static final Button O_SHARE_CALENDAR_DISABLED = new Button("O_SHARE_CALENDAR");

	public static final Button O_VIEW_DAY_MENU = new Button("O_VIEW_DAY_MENU");
	public static final Button O_VIEW_WORK_WEEK_MENU = new Button("O_VIEW_WORK_WEEK_MENU");
	public static final Button O_VIEW_WEEK_MENU = new Button("O_VIEW_WEEK_MENU");
	public static final Button O_VIEW_MONTH_MENU = new Button("O_VIEW_MONTH_MENU");
	public static final Button O_VIEW_LIST_MENU = new Button("O_VIEW_LIST_MENU");
	public static final Button O_VIEW_SCHEDULE_MENU = new Button("O_VIEW_SCHEDULE_MENU");
	public static final Button O_OPEN_MENU = new Button("O_OPEN_MENU");
	public static final Button O_PRINT_MENU = new Button("O_PRINT_MENU");
	public static final Button O_ACCEPT_MENU = new Button("O_ACCEPT_MENU");
	public static final Button O_TENTATIVE_MENU = new Button("O_TENTATIVE_MENU");
	public static final Button O_DECLINE_MENU = new Button("O_DECLINE_MENU");
	public static final Button O_EDIT_REPLY_MENU = new Button("O_EDIT_REPLY_MENU");
	public static final Button O_EDIT_REPLY_ACCEPT_SUB_MENU = new Button("O_EDIT_REPLY_ACCEPT_SUB_MENU");
	public static final Button O_EDIT_REPLY_TENTATIVE_SUB_MENU = new Button("O_EDIT_REPLY_TENTATIVE_SUB_MENU");
	public static final Button O_EDIT_REPLY_DECLINE_SUB_MENU = new Button("O_EDIT_REPLY_DECLINE_SUB_MENU");
	public static final Button O_PROPOSE_NEW_TIME_MENU = new Button("O_PROPOSE_NEW_TIME_MENU");
	public static final Button O_CREATE_A_COPY_MENU = new Button("O_CREATE_A_COPY_MENU");
	public static final Button O_REPLY_MENU = new Button("O_REPLY_MENU");
	public static final Button O_REPLY_TO_ALL_MENU = new Button("O_REPLY_TO_ALL_MENU");
	public static final Button O_REINVITE = new Button("O_REINVITE");
	public static final Button O_FORWARD_MENU = new Button("O_FORWARD_MENU");
	public static final Button O_DELETE_MENU = new Button("O_DELETE_MENU");
	public static final Button O_CANCEL_MENU = new Button("O_CANCEL_MENU");
	public static final Button O_MOVE_MENU = new Button("O_MOVE_MENU");
	public static final Button O_TAG_APPOINTMENT_MENU = new Button("O_TAG_APPOINTMENT_MENU");
	public static final Button O_TAG_APPOINTMENT_NEW_TAG_SUB_MENU = new Button("O_TAG_APPOINTMENT_NEW_TAG_SUB_MENU");
	public static final Button O_TAG_APPOINTMENT_REMOVE_TAG_SUB_MENU = new Button(
			"O_TAG_APPOINTMENT_REMOVE_TAG_SUB_MENU");
	public static final Button O_SHOW_ORIGINAL_MENU = new Button("O_SHOW_ORIGINAL_MENU");
	public static final Button O_QUICK_COMMANDS_MENU = new Button("O_QUICK_COMMANDS_MENU");
	public static final Button O_INSTANCE_MENU = new Button("O_INSTANCE_MENU");
	public static final Button O_SERIES_MENU = new Button("O_SERIES_MENU");
	public static final Button O_OPEN_INSTANCE_MENU = new Button("O_OPEN_INSTANCE_MENU");
	public static final Button O_FORWARD_INSTANCE_MENU = new Button("O_FORWARD_INSTANCE_MENU");
	public static final Button O_DELETE_INSTANCE_MENU = new Button("O_DELETE_INSTANCE_MENU");
	public static final Button O_OPEN_SERIES_MENU = new Button("O_OPEN_SERIES_MENU");
	public static final Button O_FORWARD_SERIES_MENU = new Button("O_FORWARD_SERIES_MENU");
	public static final Button O_NEW_APPOINTMENT_MENU = new Button("O_NEW_APPOINTMENT_MENU");
	public static final Button O_NEW_ALL_DAY_APPOINTMENT_MENU = new Button("O_NEW_ALL_DAY_APPOINTMENT_MENU");
	public static final Button O_GO_TO_TODAY_MENU = new Button("O_GO_TO_TODAY_MENU");
	public static final Button O_VIEW_MENU = new Button("O_VIEW_MENU");
	public static final Button O_VIEW_DAY_SUB_MENU = new Button("O_VIEW_DAY_SUB_MENU");
	public static final Button O_VIEW_WORK_WEEK_SUB_MENU = new Button("O_VIEW_WORK_WEEK_SUB_MENU");
	public static final Button O_VIEW_WEEK_SUB_MENU = new Button("O_VIEW_WEEK_SUB_MENU");
	public static final Button O_VIEW_MONTH_SUB_MENU = new Button("O_VIEW_MONTH_SUB_MENU");
	public static final Button O_VIEW_LIST_SUB_MENU = new Button("O_VIEW_LIST_SUB_MENU");
	public static final Button O_VIEW_SCHEDULE_SUB_MENU = new Button("O_VIEW_SCHEDULE_SUB_MENU");
	public static final Button O_NEEDS_ACTION_MENU = new Button("O_NEEDS_ACTION_MENU");
	public static final Button O_ACCEPTED_MENU = new Button("O_ACCEPTED_MENU");
	public static final Button O_DECLINED_MENU = new Button("O_DECLINED_MENU");

	public static final Button B_OPEN_THIS_INSTANCE = new Button("B_OPEN_THIS_INSTANCE");
	public static final Button B_OPEN_THE_SERIES = new Button("B_OPEN_THE_SERIES");
	public static final Button B_DELETE_THIS_INSTANCE = new Button("B_DELETE_THIS_INSTANCE");
	public static final Button B_DELETE_THE_SERIES = new Button("B_DELETE_THE_SERIES");

	public static final Button O_EVERY_DAY_MENU = new Button("O_EVERY_DAY_MENU");
	public static final Button O_EVERY_WEEK_MENU = new Button("O_EVERY_WEEK_MENU");
	public static final Button O_EVERY_MONTH_MENU = new Button("O_EVERY_MONTH_MENU");
	public static final Button O_EVERY_YEAR_MENU = new Button("O_EVERY_YEAR_MENU");
	public static final Button O_CUSTOM_MENU = new Button("O_CUSTOM_MENU");

	public static final Button B_EVERY_DAY_RADIO_BUTTON = new Button("B_EVERY_DAY_RADIO_BUTTON");
	public static final Button B_EVERY_WEEKDAY_RADIO_BUTTON = new Button("B_EVERY_WEEKDAY_RADIO_BUTTON");
	public static final Button B_EVERY_X_DAYS_RADIO_BUTTON = new Button("B_EVERY_X_DAYS_RADIO_BUTTON");
	public static final Button E_EVERY_X_DAYS_EDIT_FIELD = new Button("E_EVERY_X_DAYS_EDIT_FIELD");

	public static final Button B_EVERY_X_RADIO_BUTTON = new Button("B_EVERY_X_RADIO_BUTTON");
	public static final Button O_EVERY_X_DROP_DOWN = new Button("O_EVERY_X_DROP_DOWN");
	public static final Button B_EVERY_X_WEEKS_ON_RADIO_BUTTON = new Button("B_EVERY_X_WEEKS_ON_RADIO_BUTTON");
	public static final Button E_EVERY_X_WEEKS_ON_EDIT_FIELD = new Button("E_EVERY_X_WEEKS_ON_EDIT_FIELD");
	public static final Button B_SUNDAY_CHECK_BOX = new Button("B_SUNDAY_CHECK_BOX");
	public static final Button B_MONDAY_CHECK_BOX = new Button("B_MONDAY_CHECK_BOX");
	public static final Button B_TUESDAY_CHECK_BOX = new Button("B_TUESDAY_CHECK_BOX");
	public static final Button B_WEDNESDAY_CHECK_BOX = new Button("B_WEDNESDAY_CHECK_BOX");
	public static final Button B_THURSDAY_CHECK_BOX = new Button("B_THURSDAY_CHECK_BOX");
	public static final Button B_FRIDAY_CHECK_BOX = new Button("B_FRIDAY_CHECK_BOX");
	public static final Button B_SATURDAY_CHECK_BOX = new Button("B_SATURDAY_CHECK_BOX");

	public static final Button B_DAY_X_OF_EVERY_Y_MONTHS_RADIO_BUTTON = new Button(
			"B_DAY_X_OF_EVERY_Y_MONTHS_RADIO_BUTTON");
	public static final Button E_DAY_X_OF_EVERY_Y_MONTHS_EDIT_FIELD = new Button(
			"E_DAY_X_OF_EVERY_Y_MONTHS_EDIT_FIELD");
	public static final Button B_THE_X_Y_OF_EVERY_Z_MONTHS_RADIO_BUTTON = new Button(
			"B_THE_X_Y_OF_EVERY_Z_MONTHS_RADIO_BUTTON");
	public static final Button E_THE_X_Y_OF_EVERY_Z_MONTHS_EDIT_FIELD = new Button(
			"E_THE_X_Y_OF_EVERY_Z_MONTHS_EDIT_FIELD");

	public static final Button B_EVERY_YEAR_ON_X_Y_RADIO_BUTTON = new Button("B_EVERY_YEAR_ON_X_Y_RADIO_BUTTON");
	public static final Button E_EVERY_YEAR_ON_X_Y_EDIT_FIELD = new Button("E_EVERY_YEAR_ON_X_Y_EDIT_FIELD");
	public static final Button B_THE_X_Y_OF_EVERY_Z_RADIO_BUTTON = new Button("B_THE_X_Y_OF_EVERY_Z_RADIO_BUTTON");
	public static final Button E_THE_X_Y_OF_EVERY_Z_EDIT_FIELD = new Button("E_THE_X_Y_OF_EVERY_Z_EDIT_FIELD");

	public static final Button B_NO_END_DATE_RADIO_BUTTON = new Button("B_NO_END_DATE_RADIO_BUTTON");
	public static final Button B_END_AFTER_X_OCCURRENCES_RADIO_BUTTON = new Button(
			"B_END_AFTER_X_OCCURRENCES_RADIO_BUTTON");
	public static final Button B_END_AFTER_X_OCCURRENCES_EDIT_FIELD = new Button(
			"B_END_AFTER_X_OCCURRENCES_EDIT_FIELD");
	public static final Button B_END_BY_DATE_RADIO_BUTTON = new Button("B_END_BY_DATE_RADIO_BUTTON");
	public static final Button E_END_BY_DATE_EDIT_FIELD = new Button("E_END_BY_DATE_EDIT_FIELD");

	public static final Button B_ICS_LINK_IN_BODY = new Button("B_ICS_LINK_IN_BODY");
	public static final Button B_CREATE_NEW_CALENDAR = new Button("B_CREATE_NEW_CALENDAR");

	// Calendar dialogs
	public static final Button B_SEND_CANCELLATION = new Button("B_SEND_CANCELLATION");
	public static final Button B_EDIT_CANCELLATION = new Button("B_EDIT_CANCELLATION");
	public static final Button B_NOTIFY_ORGANIZER = new Button("B_NOTIFY_ORGANIZER");
	public static final Button B_DONT_NOTIFY_ORGANIZER = new Button("B_DONT_NOTIFY_ORGANIZER");
	public static final Button B_DELETE_ALL_OCCURRENCES = new Button("B_DELETE_ALL_OCCURRENCES");
	public static final Button B_DELETE_THIS_AND_FUTURE_OCCURRENCES = new Button(
			"B_DELETE_THIS_AND_FUTURE_OCCURRENCES");

	public static final Button B_SEND_UPDATES_ONLY_TO_ADDED_OR_REMOVED_ATTENDEES = new Button(
			"B_SEND_UPDATES_ONLY_TO_ADDED_OR_REMOVED_ATTENDEES");
	public static final Button B_SEND_UPDATES_TO_ALL_ATTENDEES = new Button("B_SEND_UPDATES_TO_ALL_ATTENDEES");
	public static final Button B_CHOOSE_CONTACT_FROM_PICKER = new Button("B_CHOOSE_CONTACT_FROM_PICKER");

	// Calendar tree
	public static final Button B_RELOAD = new Button("B_RELOAD");

	// Preferences
	public static final Button B_CHANGE_PASSWORD = new Button("B_CHANGE_PASSWORD");
	public static final Button B_NEW_IN_FILTER = new Button("B_NEW_IN_FILTER");
	public static final Button B_EDIT_IN_FILTER = new Button("B_EDIT_IN_FILTER");
	public static final Button B_DELETE_IN_FILTER = new Button("B_DELETE_IN_FILTER");
	public static final Button B_RUN_IN_FILTER = new Button("B_RUN_IN_FILTER");
	public static final Button B_NEW_OUT_FILTER = new Button("B_NEW_OUT_FILTER");
	public static final Button B_EDIT_OUT_FILTER = new Button("B_EDIT_OUT_FILTER");
	public static final Button B_DELETE_OUT_FILTER = new Button("B_DELETE_OUT_FILTER");
	public static final Button B_RUN_OUT_FILTER = new Button("B_RUN_OUT_FILTER");

	public static final Button B_ADD_DELEGATE = new Button("B_ADD_DELEGATE");
	public static final Button B_EDIT_PERMISSIONS = new Button("B_EDIT_PERMISSIONS");
	public static final Button B_REMOVE_PERMISSIONS = new Button("B_REMOVE_PERMISSIONS");

	public static final Button B_ACTIVITY_STREAM_SETTINGS = new Button("B_ACTIVITY_STREAM_SETTINGS");
	public static final Button B_ACTIVITY_STREAM_ENABLE = new Button("B_ACTIVITY_STREAM_ENABLE");
	public static final Button B_NEW_QUICK_COMMAND = new Button("B_NEW_QUICK_COMMAND");
	public static final Button B_EDIT_QUICK_COMMAND = new Button("B_EDIT_QUICK_COMMAND");
	public static final Button B_DELETE_QUICK_COMMAND = new Button("B_DELETE_QUICK_COMMAND");
	public static final Button O_SHARE_FOLDER_TYPE = new Button("O_SHARE_FOLDER_TYPE");
	public static final Button R_SEND_AUTOREPLY_MESSAGE = new Button("R_SEND_AUTOREPLY_MESSAGE");
	public static final Button C_SEND_AUTOREPLY_FOR_TIME_PERIOD = new Button("C_SEND_AUTOREPLY_FOR_TIME_PERIOD");
	public static final Button C_OUT_OF_OFFICE_ALLDAY = new Button("C_OUT_OF_OFFICE_ALLDAY");
	public static final Button C_OUT_OF_OFFICE_CALENDAR_APPT = new Button("C_OUT_OF_OFFICE_CALENDAR_APPT ");
	public static final Button B_BROWSE_FILE = new Button("B_BROWSE_FILE");
	public static final Button B_IMPORT = new Button("B_IMPORT");
	public static final Button B_IMPORT_OK = new Button("B_IMPORT_OK");
	public static final Button B_ADD_APPLICATION_CODE = new Button("B_ADD_APPLICATION_CODE");
	public static final Button B_BROWSE_TO_CERTIFICATE = new Button("B_BROWSE_TO_CERTIFICATE");
	public static final Button B_SUBMIT = new Button("B_SUBMIT");

	// Calendar preferences
	public static final Button R_CUSTOM_WORK_HOURS = new Button("R_CUSTOM_WORK_HOURS");
	public static final Button B_CUSTOMIZE = new Button("B_CUSTOMIZE");
	public static final Button C_SUNDAY_WORK_HOUR = new Button("C_SUNDAY_WORK_HOUR");
	public static final Button C_MONDAY_WORK_HOUR = new Button("C_MONDAY_WORK_HOUR");
	public static final Button C_TUESDAY_WORK_HOUR = new Button("C_TUESDAY_WORK_HOUR");
	public static final Button C_WEDNESDAY_WORK_HOUR = new Button("C_WEDNESDAY_WORK_HOUR");
	public static final Button C_THURSDAY_WORK_HOUR = new Button("C_THURSDAY_WORK_HOUR");
	public static final Button C_FRIDAY_WORK_HOUR = new Button("C_FRIDAY_WORK_HOUR");
	public static final Button C_SATURDAY_WORK_HOUR = new Button("C_SATURDAY_WORK_HOUR");
	public static final Button C_MONDAY_WORK_WEEK = new Button("C_MONDAY_WORK_WEEK");
	public static final Button O_START_WEEK_ON = new Button("O_START_WEEK_ON");
	public static final Button O_START_WEEK_ON_SUNDAY = new Button("O_START_WEEK_ON_SUNDAY");
	public static final Button O_START_WEEK_ON_MONDAY = new Button("O_START_WEEK_ON_MONDAY");
	public static final Button O_START_WEEK_ON_TUESDAY = new Button("O_START_WEEK_ON_TUESDAY");
	public static final Button O_START_WEEK_ON_WEDNESDAY = new Button("O_START_WEEK_ON_WEDNESDAY");
	public static final Button O_DEFAULT_APPOINTMENT_DURATION = new Button("O_DEFAULT_APPOINTMENT_DURATION");
	public static final Button O_TIMEZONE = new Button("O_TIMEZONE");
	public static final Button O_APPOINTMENT_DURATION_30 = new Button("O_APPOINTMENT_DURATION_30");
	public static final Button O_APPOINTMENT_DURATION_60 = new Button("O_APPOINTMENT_DURATION_60");
	public static final Button O_APPOINTMENT_DURATION_90 = new Button("O_APPOINTMENT_DURATION_90");
	public static final Button O_APPOINTMENT_DURATION_120 = new Button("O_APPOINTMENT_DURATION_120");
	public static final Button C_SHOW_CALENDAR_WEEK_NUMBERS = new Button("C_SHOW_CALENDAR_WEEK_NUMBERS");

	// Notification preferences
	public static final Button B_SEND_VERIFICATION_CODE = new Button("B_SEND_VERIFICATION_CODE");
	public static final Button B_VALIDATE_CODE = new Button("B_VALIDATE_CODE");

	// Calendar poups

	public static final Button B_SAVE_SEND_UPDATES = new Button("B_SAVE_SEND_UPDATES");
	public static final Button B_DONTSAVE_KEEP_OPEN = new Button("B_DONTSAVE_KEEP_OPEN");
	public static final Button B_DISCARD_CLOSE = new Button("B_DISCARD_CLOSE");
	public static final Button B_NEXT_WEEK = new Button("B_NEXT_WEEK ");
	public static final Button B_NEXT_MONTH = new Button("B_NEXT_MONTH");
	public static final Button O_TAG_APPT = new Button("O_TAG_APPT");

	public static final Button B_SEARCH_LOCATION = new Button("B_SEARCH_LOCATION");
	public static final Button B_SELECT_LOCATION = new Button("B_SELECT_LOCATION");
	public static final Button B_LOCATION = new Button("B_LOCATION");
	// Calendar
	public static final Button B_CREATE_COPY = new Button("B_CREATE_COPY");

	//// Admin Console
	// Accounts buttons
	public static final Button O_CONFIGURE_GAL = new Button("O_CONFIGURE_GAL");
	public static final Button O_ACCOUNTS_ACCOUNT = new Button("O_ACCOUNTS_ACCOUNT");
	public static final Button O_ADD_DOMAIN_ALIAS = new Button("O_ADD_DOMAIN_ALIAS");
	public static final Button B_VIEW_MAIL = new Button("B_VIEW_MAIL");
	public static final Button O_VIEW_MAIL = new Button("O_VIEW_MAIL");
	public static final Button O_MOVE_ALIAS = new Button("O_MOVE_ALIAS");
	public static final Button B_INVALIDATE_SESSIONS = new Button("B_INVALIDATE_SESSIONS");
	public static final Button O_INVALIDATE_SESSIONS = new Button("O_INVALIDATE_SESSIONS");
	public static final Button O_MOVE_MAILBOX = new Button("O_MOVE_MAILBOX");
	public static final Button B_MUST_CHANGE_PASSWORD = new Button("B_MUST_CHANGE_PASSWORD");
	public static final Button O_SEARCH_MAIL = new Button("O_SEARCH_MAIL");
	public static final Button B_CONFIGURE_GRANTS = new Button("B_CONFIGURE_GRANTS");
	public static final Button B_RESTORE = new Button("B_RESTORE");
	public static final Button B_ACCOUNTS_LIMIT_PER_DOMAIN = new Button("B_ACCOUNTS_LIMIT_PER_DOMAIN");
	public static final Button B_ACCOUNTS_LIMIT_PER_COS = new Button("B_ACCOUNTS_LIMIT_PER_COS");
	public static final Button B_DESCRIPTION = new Button("B_DESCRIPTION");
	public static final Button B_NOTES = new Button("B_NOTES");
	public static final Button O_VIEW_RESULTS = new Button("O_VIEW_RESULTS");
	public static final Button B_MAIL = new Button("B_MAIL");
	public static final Button B_SHOW_SEARCH_STRINGS = new Button("B_SHOW_SEARCH_STRINGS");
	public static final Button B_SHOW_IMAP_SEARCH_FOLDERS = new Button("B_SHOW_IMAP_SEARCH_FOLDERS");
	public static final Button B_ENABLE_MOBILE_SYNC = new Button("B_ENABLE_MOBILE_SYNC");
	public static final Button B_ENABLE_MOBILE_POLICY = new Button("B_ENABLE_MOBILE_POLICY");
	public static final Button B_FEATURES = new Button("B_FEATURES");
	public static final Button B_PREFERENCES = new Button("B_PREFERENCES");
	public static final Button B_ALIASES = new Button("B_ALIASES");
	public static final Button B_MOBILE_ACCESS = new Button("B_MOBILE_ACCESS");
	public static final Button B_ENABLE_ARCHIVING = new Button("B_ENABLE_ARCHIVING");
	public static final Button B_ARCHIVING = new Button("B_ARCHIVING");
	public static final Button B_ADD_ACL_AT_DOMAIN = new Button("B_ADD_ACL_AT_DOMAIN");
	public static final Button B_SEARCH_MAIL = new Button("B_SEARCH_MAIL");
	public static final Button B_TOGGLE_STATUS = new Button("B_TOGGLE_STATUS");
	public static final Button B_ADD_GLOBAL_ACL = new Button("B_ADD_GLOBAL_ACL");
	public static final Button B_DEPLOY_ZIMLET = new Button("B_DEPLOY_ZIMLET");
	public static final Button B_UPLOAD_ZIMLET = new Button("B_UPLOAD_ZIMLET");
	public static final Button B_UNDEPLOY_ZIMLET = new Button("B_UNDEPLOY_ZIMLET");
	public static final Button B_BACKUP = new Button("B_BACKUP");
	public static final Button B_ZIMLETS = new Button("B_ZIMLETS");
	public static final Button B_THEMES = new Button("B_THEMES");
	public static final Button B_PROPERTIES = new Button("B_PROPERTIES");
	public static final Button B_DYNAMIC_GROUP = new Button("B_DYNAMIC_GROUP");
	public static final Button B_HIDE_IN_GAL = new Button("B_HIDE_IN_GAL");
	public static final Button B_ADMIN_GROUP = new Button("B_ADMIN_GROUP");
	public static final Button B_SET_REPLY_TO = new Button("B_SET_REPLY_TO");
	public static final Button B_OWNER = new Button("B_OWNER");
	public static final Button B_ENABLE_MOBILE_SYNC_ZEXTRAS = new Button("B_ENABLE_MOBILE_SYNC_ZEXTRAS");
	public static final Button B_ENABLE_MOBILE_DEVICE_MANAGEMENT = new Button("B_ENABLE_MOBILE_DEVICE_MANAGEMENT");
	public static final Button B_ALLOW_NON_PROVISIONABLE_DEVICES = new Button("B_ALLOW_NON_PROVISIONABLE_DEVICES");
	public static final Button B_ALLOW_PARTIAL_POLICY_ENFORCEMENT = new Button("B_ALLOW_PARTIAL_POLICY_ENFORCEMENT");

	// Distribution List buttons
	public static final Button O_DISTRIBUTIUONLISTS_DISTRIBUTIONLIST = new Button(
			"O_DISTRIBUTIUONLISTS_DISTRIBUTIONLIST");
	public static final Button O_VIEW_RIGHTS = new Button("O_VIEW_RIGHTS");

	// Aliases buttons
	public static final Button O_ALIASES_ALIAS = new Button("O_ALIASES_ALIAS");

	// Resources button
	public static final Button O_RESOURCES_RESOURCE = new Button("O_RESOURCES_RESOURCE");
	public static final Button B_CANCEL_INSTANCE_LINK = new Button("B_CANCEL_INSTANCE_LINK");
	public static final Button B_ADD_ACL = new Button("B_ADD_ACL");
	public static final Button B_EDIT_ACL = new Button("B_EDIT_ACL");
	public static final Button B_DELETE_ACL = new Button("B_DELETE_ACL");

	// Certificate buttons
	public static final Button B_VIEW_CERTIFICATE = new Button("B_VIEW_CERTIFICATE");
	public static final Button B_UPLOAD_CERTIFICATE = new Button("B_UPLOAD_CERTIFICATE");
	public static final Button B_UPLOAD_ROOT_CERTIFICATE = new Button("B_UPLOAD_ROOT_CERTIFICATE");

	// License buttons
	public static final Button B_UPDATE_LICENSE = new Button("B_UPDATE_LICENSE");
	public static final Button B_UPLOAD_LICENSE = new Button("B_UPLOAD_LICENSE");
	public static final Button B_ACTIVATE_LICENSE = new Button("B_ACTIVATE_LICENSE");

	// Global settings
	public static final Button O_DAYS = new Button("O_DAYS");
	public static final Button O_WEEKS = new Button("O_WEEKS");
	public static final Button O_MONTHS = new Button("O_MONTHS");
	public static final Button O_YEARS = new Button("O_YEARS");

	// Bubble buttons
	public static Button B_NEW_MAIL = new Button("B_NEW_MAIL");
	public static Button B_GO_TO_URL = new Button("B_GO_TO_URL");
	public static Button B_FIND_EMAILS = new Button("B_FIND_EMAILS");
	public static Button B_RECEIVED_FROM_RECIPIENT = new Button("B_RECEIVED_FROM_RECIPIENT");
	public static Button B_SENT_TO_RECIPIENT = new Button("B_SENT_TO_RECIPIENT");
	public static Button B_ADD_TO_FILTER = new Button("B_ADD_TO_FILTER");
	public static Button B_ADD_TO_CONTACTS = new Button("B_ADD_TO_CONTACTS");
	public static Button B_MOVE_TO_TO = new Button("B_MOVE_TO_TO");
	public static Button B_MOVE_TO_CC = new Button("B_MOVE_TO_CC");
	public static Button B_MOVE_TO_BCC = new Button("B_MOVE_TO_BCC");

	public static Button B_SEARCH_EQUIPMENT = new Button("B_SEARCH_EQUIPMENT");
	public static Button B_SELECT_EQUIPMENT = new Button("B_SELECT_EQUIPMENT");
	public static Button B_EQUIPMENT = new Button("B_EQUIPMENT");
	public static Button B_SHOW_EQUIPMENT = new Button("B_SHOW_EQUIPMENT");
	public static Button B_SHOW_OPTIONAL = new Button("B_SHOW_OPTIONAL");
	public static Button B_SELECT_FIRST_CONTACT = new Button("B_SELECT_FIRST_CONTACT");
	public static Button B_SAVE_WITH_CONFLICT = new Button("B_SAVE_WITH_CONFLICT");
	public static Button B_CANCEL_CONFLICT = new Button("B_CANCEL_CONFLICT");
	public static Button B_SEND_WITH_CONFLICT = new Button("B_SEND_WITH_CONFLICT");

	public static Button B_SAVE_MODIFICATION = new Button("B_SAVE_MODIFICATION");
	public static Button B_CANCEL_MODIFICATION = new Button("B_CANCEL_MODIFICATION");

	// Added for Touch Client Contacts
	public static Button B_PHONE_TYPE = new Button("B_PHONE_TYPE");
	public static Button B_ADDRESS_TYPE = new Button("B_ADDRESS_TYPE");
	public static Button B_URL_TYPE = new Button("B_URL_TYPE");
	public static Button O_MOBILE = new Button("O_MOBILE");
	public static Button O_HOME = new Button("O_HOME");
	public static Button O_WORK = new Button("O_WORK");
	public static Button O_OTHER = new Button("O_OTHER");
	public static Button B_SUBFOLDER_ICON = new Button("B_SUBFOLDER_ICON");

	// Added for Touch client Mail
	public static Button B_MAIL_ACTION = new Button("B_MAIL_ACTION");
	public static Button B_MARK_CONVERSATION_UNREAD = new Button("B_MARK_CONVERSATION_UNREAD");
	public static Button B_MARK_CONVERSATION_READ = new Button("B_MARK_CONVERSATION_READ");
	public static Button B_FLAG_CONVERSATION = new Button("B_FLAG_CONVERSATION");
	public static Button B_UNFLAG_CONVERSATION = new Button("B_UNFLAG_CONVERSATION");
	public static Button B_MOVE_CONVERSATION = new Button("B_MOVE_CONVERSATION");
	public static Button B_MOVE_MESSAGE = new Button("B_MOVE_MESSAGE");
	public static Button B_TAG_MESSAGE = new Button("B_TAG_MESSAGE");
	public static Button B_TAG_CONVERSATION = new Button("B_TAG_CONVERSATION");
	public static Button B_SWITCH_TO_MESSAGE_VIEW = new Button("B_SWITCH_TO_MESSAGE_VIEW");
	public static Button B_REPLY_MAIL = new Button("B_REPLY_MENU");
	public static Button B_REPLY_TO_ALL = new Button("B_REPLY_TO_ALL");
	public static Button B_FORWARD_MAIL = new Button("B_FORWARD_MAIL");
	public static Button B_SWITCH_TO_CONVERSATION_VIEW = new Button("B_SWITCH_TO_CONVERSATION_VIEW");
	public static Button B_TAG_MAIL = new Button("B_TAG_MAIL");
	public static Button B_UNTAG_MAIL = new Button("B_UNTAG_MAIL");
	public static Button B_CANCEL_TAG_MAIL = new Button("B_CANCEL_TAG_MAIL");
	public static Button B_CANCEL_MOVE_MAIL = new Button("B_CANCEL_MOVE_MAIL");
	public static Button B_REMOVE_TAG_MAIL = new Button("B_REMOVE_TAG_MAIL");
	public static Button B_SELECT_TAG = new Button("B_SELECT_TAG");
	public static Button B_SPAM_MESSAGE = new Button("B_SPAM_MESSAGE");
	public static Button B_NOT_SPAM_MESSAGE = new Button("B_NOT_SPAM_MESSAGE");
	public static Button B_CONVERSATION_ACTION_DROPDOWN = new Button("B_CONVERSATION_ACTION_DROPDOWN");
	public static Button B_FLAG_MESSAGE = new Button("B_FLAG_MESSAGE");
	public static Button B_UNFLAG_MESSAGE = new Button("B_UNFLAG_MESSAGE");

	public static Button B_SEARCH_TYPE = new Button("B_SEARCH_TYPE");
	public static Button O_ACCOUNTS = new Button("O_ACCOUNTS");
	public static Button O_DISTRIBUTION_LISTS = new Button("O_DISTRIBUTION_LISTS");
	public static Button O_ALIASES = new Button("O_ALIASES");
	public static Button O_RESOURCES = new Button("O_RESOURCES");
	public static Button O_DOMAINS = new Button("O_DOMAINS");
	public static Button O_CLASS_OF_SERVICE = new Button("O_CLASS_OF_SERVICE");
	public static Button O_ALL_OBJECT_TYPES = new Button("O_ALL_OBJECT_TYPES");
	public static Button B_ADVANCED = new Button("B_ADVANCED");

	// Added for Admin Console Home Page Links
	public static Button B_INSTALL_CERTIFICATE = new Button("B_INSTALL_CERTIFICATE");
	public static Button B_HOME_DOMAIN = new Button("B_HOME_DOMAIN");
	public static Button B_CONFIGURE_GAL = new Button("B_CONFIGURE_GAL");
	public static Button B_CONFIGURE_AUTHENTICATION = new Button("B_CONFIGURE_AUTHENTICATION");
	public static Button B_HOME_ACCOUNT = new Button("B_HOME_ACCOUNT");
	public static Button B_HOME_MIGRATION = new Button("B_HOME_MIGRATION");

	// Added for Admin Console Two Factor Authentication
	public static Button B_ENABLE_TWO_FACTOR_AUTH = new Button("B_ENABLE_TWO_FACTOR_AUTH");
	public static Button B_REQUIRED_TWO_FACTOR_AUTH = new Button("B_REQUIRED_TWO_FACTOR_AUTH");
	public static Button B_TWO_FACTOR_AUTH_NUM_SCRATCH_CODES = new Button("B_TWO_FACTOR_AUTH_NUM_SCRATCH_CODES");
	public static Button B_ENABLE_APPLICATION_PASSCODES = new Button("B_ENABLE_APPLICATION_PASSCODES");

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