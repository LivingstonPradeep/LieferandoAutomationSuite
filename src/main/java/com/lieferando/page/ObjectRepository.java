package com.lieferando.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.lieferando.commonUtils.CommonUtils;
import com.lieferando.exceptions.ElementNotAvailableToClickException;

public class ObjectRepository {
	
	protected static Logger log=Logger.getLogger(ObjectRepository.class);
	
	public static final String xpathHomeLogo = "/html/body/header/div[1]/div[1]/a/img";
	
	
	public void clickHomeLogo(RemoteWebDriver driver) throws ElementNotAvailableToClickException{
		log.info("Clicking On Home Logo");
		
		CommonUtils.clickElementbByXpath(driver, xpathHomeLogo);
	}
	
	


}
