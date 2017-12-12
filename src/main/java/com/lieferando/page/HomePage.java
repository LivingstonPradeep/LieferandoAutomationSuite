package com.lieferando.page;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.regexp.recompile;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.lieferando.base.BaseClass;
import com.lieferando.commonUtils.CommonUtils;
import com.lieferando.exceptions.ElementNotAvailableToClickException;
import com.lieferando.exceptions.TestDataException;

public class HomePage extends ObjectRepository{


	public static final String xpathSearchBar = "//*[@id='imysearchstring']";

	public static final String xpathSubmitDeliveryArea = "//*[@id='submit_deliveryarea']";
	
	public static final String xpathSubmitDeliveryAreaError = "//*[@id='ideliveryareaerror' and contains(@style,'block')]";

	public static final String xpathLanguageDropDown = "//*[@id='locale']/div[1]";

	public static final String xpathCountryDropDown = "//*[@id='country']/div";

	public static final String xpathCountryChoose = "//*[@id='country']/div/ul/li/a";

	public static final String xpathUserMenu= "/html/body/header/div[1]/div[2]/button";

	public static final String xpathLogin= "//*[@id='userpanel-wrapper']/ul/li[1]/a";

	public static final String xpathRegister= "//*[@id='userpanel-wrapper']/ul/li[2]/a";

	public static final String xpathFavorites= "//*[@id='userpanel-wrapper']/ul/li[3]/a";

	public static final String xpathHelp= "//*[@id='userpanel-wrapper']/ul/li[4]/a";

	public static final String xpathLanguageDropDownGerman= "//*[@id='locale']//a[@href='/de/']";

	public static final String xpathLanguageDropDownEnglish= "//*[@id='locale']//a[@href='/en/']";

	public static final String xpathAutoCompleteText= "(//*[@id='reference']/span)";

	public static final String xpathGotoTop= "//*[@id='toTop']";

	public static final String xpathEnterCompleteAddressNoti= "//*[@id='ideliveryareaform']/div/div[3]/div/div[2]/span";


	/*
	 * searchGivenAddress - this method enters the search term in the search box
	 * 
	 */
	public void searchGivenAddress(RemoteWebDriver driver, String searchTerm){

		enterGivenAddressInAddressBar(driver, searchTerm);

		log.info("Hitting Enter");

		WebElement searchBar = driver.findElement(By.xpath(xpathSearchBar));

		searchBar.sendKeys(Keys.ENTER);


	}

	/*
	 * enterGivenAddressInAddressBar - this method enters the search term in the search box
	 * 
	 */
	public void enterGivenAddressInAddressBar(RemoteWebDriver driver, String searchTerm){

		log.info("Typing address into search box");

		CommonUtils.waitForElementToBeVisible(driver, xpathSearchBar);

		WebElement searchBar = driver.findElement(By.xpath(xpathSearchBar));

		searchBar.click();

		searchBar.clear();

		searchBar.sendKeys(searchTerm);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	/*
	 * enterSearchTermInSearchBox - this method enters the search term in the search box and returns the first autocomplete hit
	 * 
	 */
	public String enterSearchTermAndGetFirstSuggestion(RemoteWebDriver driver, String searchTerm){

		log.info("Typing address into search box");

		CommonUtils.waitForElementToBeVisible(driver, xpathSearchBar);

		WebElement searchBar = driver.findElement(By.xpath(xpathSearchBar));

		searchBar.click();

		searchBar.clear();

		searchBar.sendKeys(searchTerm);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(driver.findElements(By.xpath(xpathAutoCompleteText+"[1]")).isEmpty()){

			log.error("No Suggestions found for entered address string :"+searchTerm);

			return null;

		}

		WebElement firstAutoCompleteElement = driver.findElement(By.xpath(xpathAutoCompleteText+"[1]"));

		if(firstAutoCompleteElement == null){

			log.error("No Suggestions found for entered address string :"+searchTerm);

			return null;

		}

		String firstAutoCompleteHit = firstAutoCompleteElement.getText();

		log.info("Hitting Enter");

		searchBar.sendKeys(Keys.ENTER);

		return firstAutoCompleteHit;


	}

	/*
	 * searchUsingShowAndGetFirstSuggestion - this method enters the search term in the search box and returns the first autocomplete hit
	 * 
	 */
	public String searchUsingShowAndGetFirstSuggestion(RemoteWebDriver driver, String searchTerm){

		log.info("Typing address into search box");

		CommonUtils.waitForElementToBeVisible(driver, xpathSearchBar);

		WebElement searchBar = driver.findElement(By.xpath(xpathSearchBar));

		searchBar.click();

		searchBar.clear();

		searchBar.sendKeys(searchTerm);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(driver.findElements(By.xpath(xpathAutoCompleteText+"[1]")).isEmpty()){

			log.error("No Suggestions found for entered address string :"+searchTerm);

			return null;

		}

		WebElement firstAutoCompleteElement = driver.findElement(By.xpath(xpathAutoCompleteText+"[1]"));

		if(firstAutoCompleteElement == null){

			log.error("No Suggestions found for entered address string :"+searchTerm);

			return null;

		}

		String firstAutoCompleteHit = firstAutoCompleteElement.getText();

		log.info("Hitting Show button");

		verifyShowButton(driver);
		
		driver.findElement(By.xpath(xpathSubmitDeliveryArea)).click();

		return firstAutoCompleteHit;


	}

	public String getSuggestion(RemoteWebDriver driver, String suggestionIndex) {

		WebElement autoCompleteElement = driver.findElement(By.xpath(xpathAutoCompleteText+"["+suggestionIndex+"]"));

		if(autoCompleteElement == null){

			log.error("No Suggestions found for entered address string ");

			return null;

		}

		String autoCompleteHitText = autoCompleteElement.getText();

		log.info("Select the suggestion");

		return autoCompleteHitText;

	}

	public String selectSuggestion(RemoteWebDriver driver, String suggestionIndex) {

		String suggestion = getSuggestion(driver, suggestionIndex);

		WebElement autoCompleteElement = driver.findElement(By.xpath(xpathAutoCompleteText+"["+suggestionIndex+"]"));

		if(autoCompleteElement == null){

			log.error("No Suggestions found for entered address string ");

			return null;

		}

		autoCompleteElement.click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return suggestion;		


	}

	public boolean isIncompleteAddressNotificationPresent(RemoteWebDriver driver) {
		return (driver.findElements(By.xpath(xpathEnterCompleteAddressNoti)).size() > 0);
	}

	public void selectCountry(RemoteWebDriver driver, String country) throws TestDataException {

		driver.findElement(By.xpath(xpathCountryDropDown)).click();

		List<WebElement> selectCountryList = driver.findElements(By.xpath(xpathCountryChoose+"[text()='"+country+"']"));

		if(!(selectCountryList.size() > 0)){

			log.error("The given country :"+country+" was not found in the dropdown");

			throw new TestDataException("The given country :"+country+" was not found in the list of countries");

		}

		driver.findElement(By.xpath(xpathCountryChoose+"[text()='"+country+"']")).click();

	}

	public boolean verifySearchBarDisplayed(RemoteWebDriver driver) {

		return (driver.findElements(By.xpath(xpathSearchBar)).size() > 0);

	}

	public boolean verifyShowButton(RemoteWebDriver driver) {

		return (driver.findElements(By.xpath(xpathSubmitDeliveryArea)).size() > 0);

	}

	public void clickShowButton(RemoteWebDriver driver) {
		
		log.info("Clicking on Show Button");
		
		driver.findElement(By.xpath(xpathSubmitDeliveryArea)).click();
		
	}

	public boolean verifyDeliveryAreaError(RemoteWebDriver driver) {
		
		log.info("Verifying Delivery Area Error");
			
		return (driver.findElements(By.xpath(xpathSubmitDeliveryAreaError)).size() > 0);
		
	}


}
