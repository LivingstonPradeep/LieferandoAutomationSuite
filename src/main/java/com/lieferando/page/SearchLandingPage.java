package com.lieferando.page;


import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.lieferando.commonUtils.CommonUtils;


public class SearchLandingPage extends ObjectRepository{
	
	
	public static final String xpathAddressBar = "//*[@id='dropdown-location']/span/span[1]";
	
	/*
	 * This Method verifies if the given input string is found in the address bar of the Search Landing page
	 */
	
	public boolean verifyIfGivenAddressInAddressBar(RemoteWebDriver driver, String address){
		
		//CommonUtils.waitForElementToBeVisible(driver, xpathAddressBar);
		
		String textInAddressBar = driver.findElement(By.xpath(xpathAddressBar)).getText(); // getting the text from the address bar
		
		if(textInAddressBar != null && !textInAddressBar.isEmpty()){
			
			log.info("Address Found in the address bar is :"+textInAddressBar);
			
			return StringUtils.containsIgnoreCase(textInAddressBar, address); // compare the input with string from address bar
			
		}
		else{
			log.error("There was no address in the address bar");
			
			return false;
		}
		
		
	}
	
			
		
}
