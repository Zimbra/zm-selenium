/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.framework.items;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

/**
 * @author Matt Rhoades
 *
 */
public class SavedSearchFolderItem extends AItem implements IItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);

	private String name = null;
	private String query = null;
	private String types = null;
	private String parentId = "0";

	/**
	 * Create a new SavedSearchFolderItem object
	 */
	public SavedSearchFolderItem() {
		setName(ConfigProperties.getUniqueString());
	}

	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {

		// TODO: handle all folder properties, not just name and parent

		// TODO: Maybe use JaxbUtil to create it?

		account.soapSend("<CreateSearchFolderRequest xmlns='urn:zimbraMail'>" + "<search name='" + getName()
				+ "' query='" + getQuery() + "' types='" + getTypes() + "' sortBy='dateDesc' l='1'/>"
				+ "</CreateSearchFolderRequest>");

		Element[] response = account.soapSelectNodes("//mail:CreateSearchFolderResponse");
		if (response.length != 1) {
			throw new HarnessException("Unable to create folder " + account.soapLastResponse());
		}

	}

	/**
	 * Import a SavedSearchFolderItem specified in a <search/> element from
	 * GetSearchFolderRequest <br>
	 * 
	 * @param response
	 * @return
	 * @throws HarnessException
	 */
	public static SavedSearchFolderItem importFromSOAP(Element search) throws HarnessException {
		if (search == null)
			throw new HarnessException("Element cannot be null");

		logger.debug("importFromSOAP(" + search.prettyPrint() + ")");

		// TODO: can the ZimbraSOAP methods be used to convert this response to item?

		// Example response:
		// <GetSearchFolderResponse xmlns="urn:zimbraMail">
		// <search id="..." name="..." query="..." [types="..."] [sortBy="..."]
		// l="{folder}"/>+
		// </GetSearchFolderResponse>

		SavedSearchFolderItem item = null;

		try {

			item = new SavedSearchFolderItem();
			item.setId(search.getAttribute("id"));
			item.setParentId(search.getAttribute("l"));
			item.setName(search.getAttribute("name"));
			item.setQuery(search.getAttribute("query"));
			// item.setTypes(search.getAttribute("types"));

			return (item);

		} catch (NumberFormatException e) {
			throw new HarnessException("Unable to create FolderItem", e);
		} catch (ServiceException e) {
			throw new HarnessException("Unable to create FolderItem", e);
		} finally {
			if (item != null)
				logger.info(item.prettyPrint());
		}
	}

	/**
	 * Import a folder by name
	 * 
	 * @param account
	 * @param folder
	 * @return
	 * @throws HarnessException
	 */
	public static SavedSearchFolderItem importFromSOAP(ZimbraAccount account, String name) throws HarnessException {
		logger.debug("importFromSOAP(" + account.EmailAddress + ", " + name + ")");

		// Get all the folders
		account.soapSend("<GetSearchFolderRequest xmlns='urn:zimbraMail'/>");
		Element search = account.soapSelectNode("//mail:search[@name='" + name + "']", 1);

		return (importFromSOAP(search));
	}

	@Override
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(SavedSearchFolderItem.class.getSimpleName()).append('\n');
		sb.append("ID: ").append(getId()).append('\n');
		sb.append("Name: ").append(getName()).append('\n');
		sb.append("Query: ").append(getQuery()).append('\n');
		sb.append("Types: ").append(getTypes()).append('\n');
		sb.append("Parent ID: ").append(getParentId()).append('\n');
		return (sb.toString());
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getTypes() {
		return types;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}
}