package com.lieferando.commonUtils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lieferando.exceptions.ElementNotAvailableToClickException;

public class CommonUtils {

	protected static Logger log=Logger.getLogger(CommonUtils.class);

	public static void clickElementbByXpath(RemoteWebDriver driver, String xpath) throws ElementNotAvailableToClickException {

		waitForElementToBeClickable(driver, xpath);
		
		if(driver.findElements(By.xpath(xpath)).size() > 0){

			log.info("Element "+xpath+ " found. Clicking on element");

			driver.findElement(By.xpath(xpath)).click();

		}

		else{

			log.error("Element "+xpath+ " NOT found.");

			throw new ElementNotAvailableToClickException("Element "+xpath+ " NOT found.");
		}


	}

	public static void loginAndMaximise(RemoteWebDriver driver, String url) throws InterruptedException {

		log.info("Maximising browser window");
		driver.manage().window().maximize();

		Thread.sleep(2000);
		
		
		log.info("Hitting the URL "+url);
		driver.get(url);
		
		Thread.sleep(2000);

	}
	
	public static void waitForElementToBeClickable(RemoteWebDriver driver, String xpath){
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}
	
	
	public static void waitForElementToBeVisible(RemoteWebDriver driver, String xpath){
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath))));
	}
	
	
	public static void waitForElementToBeContainText(RemoteWebDriver driver, String xpath, String text){
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(xpath)), text));
	}
	
	public static void scrollTillElementView(RemoteWebDriver driver, String xpath){

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		WebElement element = driver.findElement(By.xpath(xpath));

		jse.executeScript("arguments[0].scrollIntoView(true);", element);

		jse.executeScript("window.scrollBy(0,-100)", "");

	}
	
	public static void clickUsingJS(RemoteWebDriver driver, String xpath){

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		WebElement element = driver.findElement(By.xpath(xpath));

		jse.executeScript("arguments[0].scrollIntoView(true);", element);

		jse.executeScript("arguments[0].click();", element);

	}

}
