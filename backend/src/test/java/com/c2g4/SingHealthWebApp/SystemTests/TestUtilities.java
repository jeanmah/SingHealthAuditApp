package com.c2g4.SingHealthWebApp.SystemTests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtilities {
	
	public boolean comfortSleep = false;
	public WebDriver driver = null;
	public int timeOut = 2000;
	public int timeSleep = 100;
	public String siteRootURL = "http://localhost:3000";
	
	public static final int TESTMODE = 0;
	public static final int DEBUGMODE = 1;
	
	public TestUtilities(WebDriver driver, int mode) {
		this.driver = driver;
		if(mode == DEBUGMODE) {
			comfortSleep = true;
		}
	}
	
	//UTILITIES
	public WebElement tryFindLinkElementByLink(String link, int duration) {
		for(int i = 0; i < duration; i = i + 500) {
			List<WebElement> webElements = driver.findElements(By.tagName("a"));
			for(WebElement webElement:webElements) {
				if(webElement.getAttribute("href").matches(siteRootURL + link)) {
					return webElement;
				}
			}
		}
		return null;
	}
	
	public WebElement tryFindLinkElementByLink(String link) {
		return tryFindLinkElementByLink(link, timeOut);
	}
	
	public boolean checkElementExists(By by, int duration) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, duration);
			wait.until(ExpectedConditions.elementToBeClickable(by));
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean checkElementExists(By by) {
		return checkElementExists(by, timeOut/1000);
	}

	
	public void comfortSleep(int duration){
		if(comfortSleep) {
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void comfortSleep(){
		comfortSleep(timeSleep);
	}
}
