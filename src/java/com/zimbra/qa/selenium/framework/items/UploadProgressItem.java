/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.items;

public class UploadProgressItem extends AItem{

	public String uploadProgressBarLocator=null;
	public String uploadFileName=null;
	public String uploadFileSize=null;
	public String uploadProgresSize=null;
	public String uploadStop=null;
	public String uploadBarValue=null;


	public UploadProgressItem()
	{

	}

	public void setLocator(String locator)
	{
		uploadProgressBarLocator=locator;
	}
	public String getLocator()
	{
		return uploadProgressBarLocator;
	}
	public void setFileName(String filename)
	{
		this.uploadFileName=filename;
	}
	public String getFileName()
	{
		return this.uploadFileName;
	}
	public void setUploadFileSize(String fileSize)
	{
		int index = fileSize.indexOf("of");
		uploadFileSize=fileSize.substring(index+2, fileSize.length()).trim();
		uploadProgresSize=fileSize.substring(0, index).trim();
	}
	public String getUploadFileSize()
	{
		return this.uploadFileSize;
	}
	public void setUploadBarValue(String value)
	{
		uploadBarValue=value;
	}
	public String getUploadBarValue()
	{
		return this.uploadBarValue;
	}
	public String getUploadProgressSize()
	{
		return this.uploadProgresSize;
	}

	@Override
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(UploadProgressItem.class.getSimpleName()).append('\n');
		sb.append("GUI Data:\n");
		sb.append("UploadFileName: ").append(getFileName()).append('\n');
		sb.append("UploadFileSize: ").append(getUploadFileSize()).append('\n');
		sb.append("UploadProgressBarValue: ").append(getUploadFileSize()).append('\n');
		return (sb.toString());
	}
}
