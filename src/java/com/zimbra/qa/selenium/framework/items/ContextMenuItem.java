package com.zimbra.qa.selenium.framework.items;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;

public class ContextMenuItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);
	 
			
	//FIXME
	public static final ContextMenuItem C_SEPARATOR = new ContextMenuItem("css=div[id='DWT']","","","");
         
    
	public final String locator;
	public final String image;
	public final String text;
	public final String shortcut;
	
	public ContextMenuItem (String locator,String text,String image,String shortcut) {
		this.locator=locator;
		this.image=image;
		this.text=text;
		this.shortcut=shortcut;	
	}
	
	

		
}
