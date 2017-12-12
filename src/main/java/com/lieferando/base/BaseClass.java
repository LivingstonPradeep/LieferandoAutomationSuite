package com.lieferando.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.lieferando.commonUtils.TestPropertyLoader;
import com.lieferando.exceptions.TestDataException;

import jxl.read.biff.BiffException;


/*
 * BaseClass - Parent class which contains the Preprocessing and post processing methods
 * Also contains the common variables
 * 
 */
public class BaseClass implements BaseConstants{

	public RemoteWebDriver driver = null;
	public String currentOs = System.getProperty(osName);
	public Properties filePathProperties = new Properties();
	public Map<String, Map<String, Map<String,String>>> testDataSuite;

	protected static Logger log=Logger.getLogger(BaseClass.class);

	@BeforeSuite
	public void initialSetup() throws BiffException, IOException{

		//Any Preprocessing before starting the Suite
		String log4jConfPath = "./Log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		

		//Populate the Test data
		testDataSuite = getTestDataSuite();
		
		//Load the file path properties
		loadFilePathProperties();

	}

	/* 
	 * This method is used to initialize the web driver
	 * before running every test
	 */

	@BeforeMethod(alwaysRun = true)
	public void beforeTest() throws IOException, BiffException {

		

		
	}

	
	/* 
	 * This method is used to close the webdriver
	 * and other teardown tasks
	 */ 
	@AfterMethod(alwaysRun = true)
	public void afterTestMethod(ITestResult result) throws InterruptedException {
		//Closing the web driver
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.close();
		Thread.sleep(3000);
		driver.quit();
	}
	
	@AfterTest(alwaysRun = true)
	public void afterSuiteMethod() throws InterruptedException {
		driver.quit();
		Thread.sleep(3000);
	}

//	/**
//	 * should be called for every test before execution. it initialises the
//	 * properties files and the defines the constants User name, Password and
//	 * the browser
//	 * 
//	 * @throws IOException
//	 */
//	private void initialize(String browser, String version, String platform)
//			throws IOException {
//
//		//Define the Browser
//		loadFilePathProperties();
//
//		driver = getDriverForBrowser(browser,version,platform);
//
//	}
//
//
	/**
	 * Loads the filepaths from the property file
	 * @return
	 */
	private Properties loadFilePathProperties() {

		try {
			filePathProperties.load(new FileInputStream(TestPropertyLoader.FILE_PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return filePathProperties;

	}

	/**
	 * This method will fetch the Remote WebDriver based on type of browser specified
	 * @param browser
	 * @param version
	 * @param platform
	 * @return
	 */

	public synchronized RemoteWebDriver getDriverForBrowser(String browser, String version, String platform) {

		//Set the platform (windows/linux) in the desiredCapabilities object


		if(browser.equalsIgnoreCase(internetExplorer) || StringUtils.containsIgnoreCase(browser, explorer)){

			// IE is available only in Windows

			if(!StringUtils.containsIgnoreCase(currentOs, windows)){
				log.info("IE available only in Windows OS. Creating Chrome Driver instead");

				driver = createChromeDriver(currentOs);

			}
			else{
				driver = createIEDriver();
			}


		}
		else if(browser.equalsIgnoreCase(chrome)){

			driver = createChromeDriver(currentOs);
		}


		return driver;
	}



	private RemoteWebDriver createChromeDriver(String os) {

		log.info("Creating Chrome Driver");
		os = os.toLowerCase();

		String chromeBaseDir = "";

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setBrowserName("chrome");

		if(StringUtils.containsIgnoreCase(os, windows)){

			capabilities.setPlatform(Platform.WINDOWS);

			chromeBaseDir = System.getProperty("user.dir")+File.separator+filePathProperties.getProperty("Drivers_Folder")+File.separator+filePathProperties.getProperty("Chrome_Driver_win");
		}
		else if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 ){
			chromeBaseDir = System.getProperty("user.dir")+File.separator+filePathProperties.getProperty("Drivers_Folder")+File.separator+filePathProperties.getProperty("Chrome_Driver_unix");
		}


		System.setProperty(chromeDriver,chromeBaseDir);


		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", false);
		
		options.addArguments("--start-maximized");
		options.addArguments("--enable-strict-powerful-feature-restrictions");
		options.addArguments("--disable-geolocation");
		options.addArguments("--disable-search-geolocation-disclosure");
		

//		
		

		log.info("Setting Chrome Capabilities and initiating Chrome Driver");

		//		return new ChromeDriver(capabilities);

		return new ChromeDriver(options);

	}



	private RemoteWebDriver createIEDriver(){

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

		System.setProperty(ieDriver, System.getProperty("user.dir")+File.separator+filePathProperties.getProperty("Drivers_Folder")+File.separator+filePathProperties.getProperty("IE_Driver"));

		desiredCapabilities.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
		desiredCapabilities.setCapability("ignoreZoomSetting", true);
		desiredCapabilities.setCapability("ignoreProtectedModeSettings", true);
		try {
			return new RemoteWebDriver(desiredCapabilities);
		} catch (WebDriverException e) {
			e.printStackTrace();
			log.error("Could not create IE driver. Continuing test case with Chrome Driver");
			return createChromeDriver(TestPropertyLoader.PLATFORM);
		}

	}

	/*
	 * getTestData method returns the Map of the test data
	 */

	private Map<String,String> getTestData(String testData) throws BiffException, IOException{


		Map<String,String> testDataMap = new HashMap<String, String>();
		
		Pattern jsonPattern = Pattern.compile("(?:\"(.*?)\"):(?:\"(.*?)\")");
		Matcher jsonMatcher = jsonPattern.matcher(testData);
		
		while(jsonMatcher.find()) {

			String jsonKey = jsonMatcher.group(1);
			String jsonValue = jsonMatcher.group(2);
			
			testDataMap.put(jsonKey, jsonValue);

		}
		
		return testDataMap;


	}

	/*
	 * getTestDataSuite method will populate the test data from the json
	 */

	private Map<String,Map<String,Map<String,String>>> getTestDataSuite() throws BiffException, IOException{

		Map<String,Map<String,Map<String,String>>> testDataSuiteMap = new HashMap<String,Map<String,Map<String,String>>>();

		JSONParser parser = new JSONParser();

		Object obj;

		try {

			obj = parser.parse(new FileReader(TestPropertyLoader.TEST_DATA_FILE));
			JSONObject jsonObject = (JSONObject) obj;

			JSONArray testDataSuiteArray= (JSONArray) jsonObject.get("testDataSuite");

			for(int envCount=0; envCount < testDataSuiteArray.size(); envCount++) {
				
				JSONObject eachEnv = (JSONObject)testDataSuiteArray.get(envCount);
				String env = (String) eachEnv.get("env"); 
				JSONArray testDataArray = (JSONArray) eachEnv.get("testData");
				
				Iterator testDataIter = testDataArray.iterator();
				
				Map<String, Map<String, String>> testDataMap = new HashMap<String, Map<String, String>>();
				
				while (testDataIter.hasNext()) {
					
					JSONObject testDataString = (JSONObject)testDataIter.next();
					
					Map<String, String> testData = getTestData(testDataString.toString());
					
					testDataMap.put(testData.get("testId"),testData);
	            }
				
				testDataSuiteMap.put(env,testDataMap);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return testDataSuiteMap;

	}
	
	
	protected Map<String, String> getTestDataForMethod(String testId) throws TestDataException{
		
		Map<String, Map<String, String>> testDataForEnv = testDataSuite.get(TestPropertyLoader.ENV);
		
		if(testDataForEnv == null || testDataForEnv.isEmpty()){
			log.error("The test data for the Env :"+TestPropertyLoader.ENV +" was not found");
			throw new TestDataException("The test data for the Env :"+TestPropertyLoader.ENV + " was not found");
		}
		
		Map<String, String> testData = testDataForEnv.get(testId);
		
		if(testData == null || testData.isEmpty()){
			log.error("The test data for the Env :"+TestPropertyLoader.ENV + " and test ID :"+testId+ " was not found");
			throw new TestDataException("The test data for the Env :"+TestPropertyLoader.ENV + " and test ID :"+testId+ " was not found");
		}
		
		return testData;
		
		
	}

}
