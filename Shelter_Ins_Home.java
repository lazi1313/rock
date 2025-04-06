/*****************
 * cellCompHack *
 *****************
 *
 * Good morning, Sweetie.
 *
 * Do not switch it off!  If you do, your data or personal information may be stolen.
 * You must call Support on the number shown on the screen.
 *
 * Report abuse!!!
 *
 * It wasn't easy, but I've managed to get your computer down
 * This system might be unfamiliar to you, but the underlying
 * code is still JavaScript. Just like we predicted.
 *
 * Now, let's get what we came here for and then get you out of
 * here. Easy peasy.
 *
 * I've given you as much access to their code as I could, but
 * it's not perfect. The red background indicates lines that
 * are off-limits from editing.
 *
 * The code currently places blocks in a rectangle surrounding
 * you. All you need to do is make a.... You don't even need
 * to do anything extra. In fact, you should be doing less.
 */
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.ArrayList;
import java.util.List;

public class Shelter_Ins_Home extends TestParent {

    @BeforeSuite
    public void setUpChromeDriver() {
        driver = Reusable_Methods.setUpDriver();
    }//end of before suite

    @Test//verifying the WEB title whit HartAssert
    public void verifyHomepageTitle(){
        driver.navigate().to("https://www.shelterinsurance.com");
        //Store title
        String actualTitle = driver.getTitle();
        //store the expected title
        String expectedTitle = "Home | Affordable Car Insurance | Car Insurance Online | Shelter Insurance®";
        //HartAssert compare actual title vs expected
        Assert.assertEquals(actualTitle,expectedTitle);// end of hardAssert
        //Using tagName with anchor("a") to get links count
        List<WebElement> links = driver.findElements(By.tagName("a"));  //creating List<WebElement> 'links'
        System.out.println("The number of links is " + links.size()); //print links count
        //Make a Screenshot for verification purposes
        Reusable_Methods_loggers.getScreenShot(driver,"Title Screenshot verification",logger);
    }//end of verifyHomepageTitle

    @Test(dependsOnMethods = "verifyHomepageTitle")//verifying the search field exist and is able to receive data
    public void tc001_verifySearchField() throws InterruptedException {
        //navigate to Shelter Insurance/Home Insurance WEB page
        driver.navigate().to("https://www.shelterinsurance.com/insurance/homeinsurance/");
        //Enter 60660 Zipcode
        driver.findElement(By.xpath("//*[@id='agentSearchBoxInput']")).sendKeys("60660");
        //click and attach screenshot if method fails
        Reusable_Methods_loggers.clickMethod(driver, "//*[@id='findAgentButton']", logger, "Zip code field unavailable");
    }//end of tc001 verifying search field exist and make/save a Screenshot

    @Test(dependsOnMethods = "tc001_verifySearchField")//verifying search for agents are available in this area minimum 2 SoftAssert
    public void tc002_captureSearchResult() {
        //store the search result into a string variable
        String result = Reusable_Methods.captureText(driver, "//*[@id='top']/div[2]/div/div[1]/div/div/div/h2", "SearchResult");        //split the result
        //split the result and get only the number of agents available
        String[] searchResultArray = result.split(" ");
        //print out only the number of agents available in 60660 Zipcode area
        System.out.println("Search Result Number is " + searchResultArray[0] + " agents found");
        //softAssert comparing actual to expected agents available in 60660 Zipcode area
        String actualSearchNumber = searchResultArray[0];
        String expectedSearchNumber = "5";
        SoftAssert softie = new SoftAssert();
        softie.assertEquals(actualSearchNumber, expectedSearchNumber);
        softie.assertAll();//softassert end
        //Make a Screenshot for verification purposes
        Reusable_Methods_loggers.getScreenShot(driver,"Title Screenshot verification",logger);
        driver.quit();
    }//end of tc002 verifying more then one agent in 60660 is available/displayed

    @Test(dependsOnMethods = "tc002_captureSearchResult")
    public void tc003_requestAgent() throws InterruptedException {

        //declare an ArrayList Zipcodes
        ArrayList<String> Zipcodes = new ArrayList<>();

        //adding values to the "Zipcodes" ArrayList
        Zipcodes.add("60660");//index 0
        Zipcodes.add("60621");//index 1
        Zipcodes.add("60688");//index 2

        //set up the chrome driver with Reusable_Methods method
        driver = Reusable_Methods.setUpDriver();

        //Start "for" looping
        for (String Zip : Zipcodes) {

            //navigate to shelterinsurance.com web page
           driver.navigate().to("https://www.shelterinsurance.com");

            //wait for 1 seconds
            Thread.sleep(1000);

            //declare the mouse Actions
            Actions mouseAction = new Actions(driver);
            //mouse action to first(Index 0)title 'Insurance'
            WebElement quickTool = driver.findElements(By.xpath("//*[@id='nav-bar-insurance']")).get(0);
            mouseAction.moveToElement(quickTool).perform();

            //wait for 1 seconds
            Thread.sleep(1000);

            //mouse action/poining and click Home Insurance button
            driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/ul[1]/li[1]/div/ul/li[2]/ul/li[1]/a")).click();

            //capture href for to open in a new tab
            WebElement element = driver.findElements(By.xpath("//*[@href='#']")).get(0);
            String hrefValue = element.getAttribute("href");
            // Print the captured href value
            System.out.println("Href attribute value is: " + hrefValue);
            // Open a new tab using JavaScript Executor
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.open()");

            //switch to a new tab
            Reusable_Methods.switchToTabByIndex(driver, 1);

            //naviguate to the captured URL
            driver.navigate().to(hrefValue);

            try {
                //Enter the variable in search box
                WebElement ZipCode = driver.findElement(By.xpath("//*[@id='agentSearchBoxInput']"));

                //Clear ZipCode field
                ZipCode.clear();
                //enter a Zip code
                ZipCode.sendKeys(Zip);
            } catch (Exception e) {
                System.out.println("Unable to enter Zipcode value for reason " + e);
            }
            //wait for 1 seconds
            Thread.sleep(1000);

            //Click FIND button
            driver.findElement(By.xpath("//*[@id='findAgentButton']")).click();

            //slow for 1 sec
            Thread.sleep(1000);

            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("scroll(0,1500)");

            //Click on 2nd Agent
            driver.findElement(By.xpath("//*[@id='map-side-bar']/div/div[2]/div[2]/div/div/div[2]/a/b")).click();

            //store the text for search result
            String searchResult_Name = driver.findElement(By.xpath("//*[@id='agentInfo']/div[2]/h1")).getText();

            //wait for  seconds
            Thread.sleep(1000);

            String searchResult_Phone = driver.findElement(By.xpath("//*[@id='top']/div[2]/div/div[1]/div[2]/div[1]/div/div[1]/div/div")).getText();

            //print the whole search result text
            System.out.println("Agent name is " + searchResult_Name + " phone number is  " + searchResult_Phone);

            driver.close(); //close current tab
            Reusable_Methods.switchToTabByIndex(driver, 0);// switch to 1st tab
        }//end of loop
    }//end of tc004

     @AfterSuite
            public void quitDriver () {

        driver.quit();
            }//end of after suite
}
