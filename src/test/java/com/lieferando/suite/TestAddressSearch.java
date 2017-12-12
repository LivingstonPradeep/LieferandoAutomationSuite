package com.lieferando.suite;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DriverCommand;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lieferando.base.BaseClass;
import com.lieferando.commonUtils.CommonUtils;
import com.lieferando.commonUtils.TestPropertyLoader;
import com.lieferando.exceptions.SearchLandingPageException;
import com.lieferando.exceptions.TestDataException;
import com.lieferando.page.HomePage;
import com.lieferando.page.SearchLandingPage;

public class TestAddressSearch extends BaseClass{

	@Parameters({ "testId" })
	@Test				
	public void testSearchAddress(String testId) throws TestDataException, InterruptedException {	

		HomePage homePage =  new HomePage();

		SearchLandingPage searchLandingPage = new SearchLandingPage();

		Map<String, String> testData = getTestDataForMethod(testId);

		String address = testData.get("searchTerm");

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);


		//Enter Search term
		String firstSuggestion = homePage.enterSearchTermAndGetFirstSuggestion(driver, address);

		assertNotNull(firstSuggestion,"Asserting if the search term "+address+" returns any suggestion");

		Thread.sleep(2000);

		log.info("Asserting if the input address :"+firstSuggestion+" is present in the address bar of Search Landing Page");

		assertTrue(searchLandingPage.verifyIfGivenAddressInAddressBar(driver, firstSuggestion), "Asserting if the input address :"+firstSuggestion+" is present in the address bar of Search Landing Page");


	}	

	@Parameters({ "testId" })
	@Test				
	public void testInvalidSearchString(String testId) throws TestDataException, InterruptedException {	

		HomePage homePage =  new HomePage();

		SearchLandingPage searchLandingPage = new SearchLandingPage();

		Map<String, String> testData = getTestDataForMethod(testId);

		String address = testData.get("searchTerm");

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);


		//Enter Search term
		String firstSuggestion = homePage.enterSearchTermAndGetFirstSuggestion(driver, address);

		assertNull(firstSuggestion,"Asserting if the search term "+address+" returns any suggestion");

	}	

	@Parameters({ "testId" })
	@Test				
	public void testSearchPincode(String testId) throws TestDataException, InterruptedException {	

		testSearchPincodeMethod(testId);
	}	
	
	@Parameters({ "testId" })
	@Test				
	public void testSearchIncorrectPincode(String testId) throws TestDataException, InterruptedException {	

		testSearchPincodeMethod(testId);
	}	

	@Parameters({ "testId" })
	@Test				
	public void selectAnySuggestion(String testId) throws TestDataException, InterruptedException {	

		selectAnySuggestionMethod(testId);
	}	
	
	@Parameters({ "testId" })
	@Test				
	public void verifyIncompleteAddress(String testId) throws TestDataException, InterruptedException {	

		selectAnySuggestionMethod(testId);
	}	

	@Parameters({ "testId" })
	@Test				
	public void verifyGUI(String testId) throws TestDataException, InterruptedException {	

		HomePage homePage =  new HomePage();

		Map<String, String> testData = getTestDataForMethod(testId);

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);

		//Check Search Bar

		assertTrue(homePage.verifySearchBarDisplayed(driver));

		assertTrue(homePage.verifyShowButton(driver));

	}	

	@Parameters({ "testId" })
	@Test				
	public void clickShowButton(String testId) throws TestDataException, InterruptedException {	

		HomePage homePage =  new HomePage();

		Map<String, String> testData = getTestDataForMethod(testId);

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);
		
		homePage.clickShowButton(driver);

		homePage.verifyDeliveryAreaError(driver);
	}


	public void testSearchPincodeMethod(String testId) throws TestDataException, InterruptedException{
		HomePage homePage =  new HomePage();

		SearchLandingPage searchLandingPage = new SearchLandingPage();

		Map<String, String> testData = getTestDataForMethod(testId);

		String pincode = testData.get("searchTerm");

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);


		//Enter Search pincode
		homePage.searchGivenAddress(driver, pincode);

		Thread.sleep(2000);

		String currentURL = driver.getCurrentUrl();

		if(StringUtils.containsIgnoreCase(testData.get("description"), "incorrect")){
			assertFalse(currentURL.contains(pincode));
		}

		else{

			assertTrue(searchLandingPage.verifyIfGivenAddressInAddressBar(driver, testData.get("address")), "Asserting if the input pincode :"+pincode+" is landing on the correct address :"+testData.get("address"));

		}
	}
	
	public void selectAnySuggestionMethod(String testId) throws TestDataException, InterruptedException{
		HomePage homePage =  new HomePage();

		SearchLandingPage searchLandingPage = new SearchLandingPage();

		Map<String, String> testData = getTestDataForMethod(testId);

		String address = testData.get("searchTerm");

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);


		//Enter Search address
		homePage.enterGivenAddressInAddressBar(driver, address);

		Thread.sleep(2000);


		if(StringUtils.containsIgnoreCase( testData.get("description"), "incomplete")){

			String suggestionText = homePage.selectSuggestion(driver,testData.get("suggestionIndex"));

			assertTrue(homePage.isIncompleteAddressNotificationPresent(driver));
		}

		else{
			String suggestionText = homePage.selectSuggestion(driver,testData.get("suggestionIndex"));

			assertNotNull(suggestionText, "Asserting if suggestion index :"+testData.get("suggestionIndex")+" is present for search term "+address);

			assertTrue(searchLandingPage.verifyIfGivenAddressInAddressBar(driver, suggestionText), "Asserting if the input address :"+suggestionText+" is present in the address bar of Search Landing Page");

		}

	}
	
	@Parameters({ "testId" })
	@Test				
	public void testSearchAddressUsingShow(String testId) throws TestDataException, InterruptedException {	

		HomePage homePage =  new HomePage();

		SearchLandingPage searchLandingPage = new SearchLandingPage();

		Map<String, String> testData = getTestDataForMethod(testId);

		String address = testData.get("searchTerm");

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);


		//Enter Search term
		String firstSuggestion = homePage.searchUsingShowAndGetFirstSuggestion(driver, address);

		assertNotNull(firstSuggestion,"Asserting if the search term "+address+" returns any suggestion");

		Thread.sleep(2000);

		log.info("Asserting if the input address :"+firstSuggestion+" is present in the address bar of Search Landing Page");

		assertTrue(searchLandingPage.verifyIfGivenAddressInAddressBar(driver, firstSuggestion), "Asserting if the input address :"+firstSuggestion+" is present in the address bar of Search Landing Page");


	}	

	@Parameters({ "testId" })
	@Test				
	public void testInvalidSearchStringUsingShow(String testId) throws TestDataException, InterruptedException {	

		HomePage homePage =  new HomePage();

		SearchLandingPage searchLandingPage = new SearchLandingPage();

		Map<String, String> testData = getTestDataForMethod(testId);

		String address = testData.get("searchTerm");

		log.info("*******************Test Case ID :"+ testData.get("testId")+"*******************");

		log.info("Test Case Description :"+ testData.get("description"));

		//Create Webdriver

		driver = getDriverForBrowser(testData.get("browser"), TestPropertyLoader.VERSION,  TestPropertyLoader.PLATFORM);

		//Hit the URL and Maximise
		CommonUtils.loginAndMaximise(driver, TestPropertyLoader.APP_URL);


		//Enter Search term
		String firstSuggestion = homePage.searchUsingShowAndGetFirstSuggestion(driver, address);

		assertNull(firstSuggestion,"Asserting if the search term "+address+" returns any suggestion");

	}	


}
