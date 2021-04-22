package com.c2g4.SingHealthWebApp.SystemTests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class RobustnessTest {

	private final static String seleniumWebDriverPath = "external jars//chromedriver_89.exe";
	private final static String siteRootURL = "http://localhost:3000";

	private static WebDriver driver;

	private static TestUtilities testUtil;

	@BeforeAll
	public static void runBeforeAllTests() {
		System.setProperty("webdriver.chrome.driver", seleniumWebDriverPath);
		driver = new ChromeDriver();
		testUtil = new TestUtilities(driver, TestUtilities.TESTMODE);
		testUtil.timeOut = 5000;
		testUtil.timeSleep = 500;
		testUtil.siteRootURL = siteRootURL;
	}

	@AfterAll
	public static void runAfterAllTests() {
		driver.close();
	}

	@BeforeEach
	public void runBeforeEachTest() {
		driver.get(siteRootURL);
		testUtil.checkElementExists(By.id("username"));
	}

	
	//<><><> Monkeys <><><>
	@Test
	public void MonkeyButtonTest() {
		testUtil.auditUserLogin();
		List<WebElement> elements = driver.findElements(By.tagName("button"));
		
		int safety = 1000;
		while(safety > 0) {
			WebElement element = testUtil.getRandomElement(elements);
			int numFailed = 0;
			while(numFailed < elements.size()) {
				try {
					element.click();
					elements = driver.findElements(By.tagName("button"));
					break;
				}catch(Exception e) {
					numFailed++;
				}
			}
			if(numFailed == elements.size()) {
				driver.navigate().back();
				if(testUtil.checkElementExists(By.id("username"))) {
					testUtil.auditUserLogin();
				}
				elements = driver.findElements(By.tagName("button"));
			}
			safety--;
		}

		
	}
	
	@Test
	public void MonkeyURLTest() {
		testUtil.auditUserLogin();
		int safety = 1000;
		List<URL> urls = new ArrayList<>();
		while(safety > 0) {
			urls = getLinks(urls);
			if(urls.isEmpty()) {
				driver.navigate().back();
			}else {
				goSomewhere(urls);
			}
			safety++;
		}
	}
	
	private void goSomewhere(List<URL> urls) {
		driver.navigate().to(testUtil.getRandomElement(urls));
	}
	private List<URL> getLinks(List<URL> urls) {
		List<WebElement> elements = driver.findElements(By.tagName("a"));
		urls = new ArrayList<>();
		Set<String> strurls = new HashSet<>();
		for(WebElement element:elements) {
			String strURL = element.getAttribute("href");
			if (strURL != null && !strurls.contains(strURL)) {
				strurls.add(strURL);
				try {
					urls.add(new URL(strURL));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return urls;
	}

}
