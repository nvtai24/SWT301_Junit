package addtocart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddToCartDataProviderTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String url = "http://localhost:8084/BookShop/";

    private List<TestResult> testResults = new ArrayList<>();

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @DataProvider(name = "testData")
    public Object[][] getTestData() throws Exception {
        List<Object[]> testData = new ArrayList<>();
        String csvFile = "test/resources/cart_test.csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                testData.add(new Object[]{data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5])});
            }
        }

        return testData.toArray(new Object[0][]);
    }

    @Test(dataProvider = "testData")
    public void testAddToCart(String testCaseName, String bookId, String quantity, String expectedUrlContains,
            String expectedCartQuantity, boolean shouldLogin) {
        long startTime = System.currentTimeMillis();

        try {
            if (shouldLogin) {
                driver.get(url + "login.jsp");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
                
                WebElement username = driver.findElement(By.id("username"));
                WebElement password = driver.findElement(By.id("password"));
                WebElement loginSubmit = driver.findElement(By.id("loginSubmit"));

                username.sendKeys("admin");
                password.sendKeys("admin123");
                loginSubmit.click();
                wait.until(ExpectedConditions.urlContains("home"));
            }

            if (testCaseName.equals("testUpdateQuantity")) {
                driver.get(url + "book-detail?bookId=" + bookId);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("quantity")));
                WebElement quantityInputInitial = driver.findElement(By.name("quantity"));
                quantityInputInitial.clear();
                quantityInputInitial.sendKeys("1");
                driver.findElement(By.name("addToCartBtn")).click();
                wait.until(ExpectedConditions.urlContains("cart"));
            }

            if (bookId.isEmpty()) {
                driver.get(url + "home");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("book-item")));
                List<WebElement> books = driver.findElements(By.className("book-item"));
                WebElement book1 = books.get(0);
                WebElement addToCartButton = book1.findElement(By.className("addToCartBtn"));
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", addToCartButton);
            } else {
                driver.get(url + "book-detail?bookId=" + bookId);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("quantity")));
                WebElement quantityInput = driver.findElement(By.name("quantity"));
                quantityInput.clear();
                quantityInput.sendKeys(quantity);
                driver.findElement(By.name("addToCartBtn")).click();
            }

            wait.until(ExpectedConditions.urlContains(expectedUrlContains));
            String currentUrl = driver.getCurrentUrl();

            Assert.assertTrue(currentUrl.contains(expectedUrlContains),
                    testCaseName + ": Expected URL to contain '" + expectedUrlContains + "', but got: " + currentUrl);

            if (!expectedCartQuantity.isEmpty() && currentUrl.contains("cart")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cartQuantity")));
                String cartQuantity = driver.findElement(By.id("cartQuantity")).getAttribute("value");
                Assert.assertEquals(cartQuantity, expectedCartQuantity,
                        testCaseName + ": Quantity in cart does not match expected value");
            }

            long executionTime = System.currentTimeMillis() - startTime;
            testResults.add(new TestResult(testCaseName, "PASSED", "Test passed successfully", executionTime));

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            testResults.add(new TestResult(testCaseName, "FAILED", e.getMessage(), executionTime));
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                if (driver.getCurrentUrl().contains("cart")) {
                    wait.until(ExpectedConditions.elementToBeClickable(By.id("tg-minicart")));
                    driver.findElement(By.id("tg-minicart")).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.id("clearCart")));
                    driver.findElement(By.id("clearCart")).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("clearCart")));
                }

                ReportExporter.exportToCSV(testResults);
                ReportExporter.exportToHTML(testResults);

                driver.quit();
            } catch (Exception ex) {
                Logger.getLogger(AddToCartDataProviderTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}