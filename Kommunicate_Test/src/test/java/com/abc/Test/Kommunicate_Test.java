package com.abc.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Kommunicate_Test 
{
	ExtentHtmlReporter htmlRep;
	ExtentReports extntRep;
	ExtentTest exTest;
	MultiPartEmail em=new MultiPartEmail();
	EmailAttachment at=new EmailAttachment();
	int cnt;
	String chatTag,faqTag;
	WebDriver dr;
	
	@BeforeClass
	public void report()
	{
		htmlRep=new ExtentHtmlReporter("./reports/extent_report.html");
		htmlRep.config().setEncoding("utf-8");
		htmlRep.config().setDocumentTitle("Report");
		htmlRep.config().setTheme(Theme.STANDARD);
		
		extntRep=new ExtentReports();
		extntRep.setSystemInfo("Browser", "Chrome");
		extntRep.attachReporter(htmlRep);
	}
	
	@BeforeMethod
	public void openBrowser()
	{
		dr=new ChromeDriver();
		dr.manage().window().maximize();
		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	@Test
	public void startTest() throws Exception 
	{	
		dr.get("https://test.kommunicate.io/");
		
		if(dr.getCurrentUrl().equalsIgnoreCase("https://test.kommunicate.io/"))
			{
				exTest=extntRep.createTest("Test Successful");
				exTest.log(Status.PASS, "Browser opened Successfully");
			}
			else
			{
				exTest=extntRep.createTest("Test Not Successful");
				exTest.log(Status.FAIL, "Browser not opened Successfully");
			}
			extntRep.flush();
			
		String handle=dr.getWindowHandle();
		dr.switchTo().window(handle);
		dr.findElement(By.xpath("//a[text()='Accept']")).click();
		dr.switchTo().frame("kommunicate-widget-iframe");
		WebElement chat=dr.findElement(By.xpath("//div[@id='launcher-svg-container']"));
		chatTag=chat.getTagName();
		chat.click();
		WebElement faq=dr.findElement(By.xpath("//button[@id='km-faq']"));
		faqTag=faq.getTagName();
		faq.click();
		Thread.sleep(3000);
		List<WebElement> faqcnt=dr.findElements(By.xpath("//ul[@id='km-faq-list-container']//li[@class='km-faq-list']"));
		cnt=faqcnt.size();
		System.out.println(cnt);
	}
	@AfterMethod
	public void closeBrowser(ITestResult res) throws Exception  
	{
		if(chatTag==null)
		{
			exTest=extntRep.createTest("Test Not Successful");
			exTest.log(Status.FAIL, "Clicking on chat icon step failed");
		}
		else
		{
			exTest=extntRep.createTest("Test Successful");
			exTest.log(Status.PASS, "Successfully clicked on chat icon");
		}
		extntRep.flush();
		if(faqTag== null)
		{
			exTest=extntRep.createTest("Test Not Successful");
			exTest.log(Status.FAIL, "Clicking on FAQ step failed");
		}
		else
		{
			exTest=extntRep.createTest("Test Successful");
			exTest.log(Status.PASS, "Successfully clicked on FAQ");
		}
		extntRep.flush();
		
		if(ITestResult.FAILURE==res.getStatus())
		{
			TakesScreenshot ts=(TakesScreenshot)dr;
			File src= ts.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("./Screenshot/FailedTest.png"));	
		}

		dr.quit();
	}

	@AfterClass
	public void sendMessage() throws Exception  
	{
		String file= "C:\\Users\\Swathi Bhat\\git\\Kommunicate_Test\\Kommunicate_Test\\Screenshot\\FailedTest.png"; 
		String rfile="C:\\Users\\Swathi Bhat\\git\\Kommunicate_Test\\Kommunicate_Test\\reports\\extent_report.html";
		
		MailSender.mail(em);
		if(cnt<20)
		{ 	
			em.setMsg("Count -- "+cnt);
		} 	
		try
		{  
			MailSender.attachment(at,file);
			em.attach(at);
		}
		catch(Exception e)
		{
			System.out.println("");
		}
		try
		{
			MailSender.attachment(at, rfile);
			em.attach(at);
		}
		catch(Exception e)
		{
			System.out.println("");
		}
		em.addTo("username@gmail.com");
		em.send();
		
		File del=new File(rfile);
		del.delete();
		
		File del1=new File(file);
		del1.delete();
	}
}
