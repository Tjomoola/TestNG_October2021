package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class CrmTest {
	
	String browser;
	String url;
	WebDriver driver;
	
	
	
	
//	ELEMENT LIST
	By userName_Locator = By.xpath("//input[@id='username']");
	By password_Locator = By.xpath("//*[@id=\"password\"]");
	By signIn_Button_Locator = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By dashboard_Header_Locator = By.xpath("//h2[contains(text(), 'Dashboard')]");
	By Customer_Menu_Locator = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By Add_Customer_Menu_Locator = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By Add_Contact_Header_Locator = By.xpath("//*[@id=\"page-wrapper\"]/div[3]/div[1]/div/div/div/div[1]/h5");
	By Full_Name_Locator = By.xpath("//*[@id=\"account\"]");
	By Company_DropDown_Locator = By.xpath("//select[@id=\"cid\"]");
	By Email_Locator = By.xpath("//*[@id=\"email\"]");
	By Country_Locator = By.xpath("//*[@id=\"select2-country-container\"]");
	
	
	@BeforeTest
	public void readConfig() {
//		FileReader//Scanner//InputStream//BufferReader
		
		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser Used = " + browser);
			url = prop.getProperty("url");
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@BeforeMethod
	public void init() {
		
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\okikiade\\NewOctoberSelenium\\session5b_TestNG\\driver\\chromedriver.exe");
			driver = new ChromeDriver();
			
		}else if(browser.equalsIgnoreCase("firefox")){
			
			System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
			driver = new FirefoxDriver();
		}
				
		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

	}
	
	@Test(priority = 1)
	public void loginTest() {
		driver.findElement(userName_Locator).sendKeys("demo@techfios.com");
		driver.findElement(password_Locator).sendKeys("abc123");
		driver.findElement(signIn_Button_Locator).click();
		
		String dashboardHeaderText = driver.findElement(dashboard_Header_Locator).getText();
		Assert.assertEquals(dashboardHeaderText, "Dashboard", "Wrong Page!!");
		
	}
	@Test(priority = 2)
	public void addCustomerTest() {
		loginTest();
		driver.findElement(Customer_Menu_Locator).click();
		driver.findElement(Add_Customer_Menu_Locator).click();
		
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Add_Contact_Header_Locator));
		
		Assert.assertEquals(driver.findElement(Add_Contact_Header_Locator).getText(), "Add Contact", "Wrong page!!");
		
		
		driver.findElement(Full_Name_Locator).sendKeys("Selenium October" + generateRandom(9999));
		
		Select sel = new Select(driver.findElement(Company_DropDown_Locator));
		sel.selectByVisibleText("Techfios");
		
		driver.findElement(Email_Locator).sendKeys(generateRandom(999) + "demotechfios.com");
		
		WebDriverWait wait1 = new WebDriverWait(driver,5);
		wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Email_Locator));
		
		
		selectFromDropdown(Country_Locator, "Afghanistan");
			
	}
		
	public void selectFromDropdown(By locator, String visibleText) {
		Select sel = new Select(driver.findElement(locator));
		sel.deselectByVisibleText(visibleText);
		
	}
	
	public int generateRandom(int boundaryNum) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(boundaryNum);
		return generatedNum;
		
	}


	
	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
