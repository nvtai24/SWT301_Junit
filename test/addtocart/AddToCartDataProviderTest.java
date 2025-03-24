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

    @BeforeMethod
    public void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @DataProvider(name = "testData")
    public Object[][] getTestData() throws Exception {
        List<Object[]> testData = new ArrayList<>();
        String csvFile = "test/resources/cart_test.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Bỏ qua header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 6) {
                    throw new IllegalArgumentException("Invalid CSV line: " + line);
                }
                testData.add(new Object[]{
                    data[0], // testCaseName
                    data[1].isEmpty() ? null : data[1], // bookId
                    Integer.parseInt(data[2]), // quantity
                    data[3], // expectedUrlContains
                    data[4].isEmpty() ? null : data[4], // expectedCartQuantity
                    Boolean.parseBoolean(data[5]) // shouldLogin
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + csvFile, e);
        }
        if (testData.isEmpty()) {
            throw new RuntimeException("No data found in CSV file: " + csvFile);
        }
        return testData.toArray(new Object[0][]);
    }

    @Test(dataProvider = "testData")
    public void testAddToCart(String testCaseName, String bookId, int quantity,
            String expectedUrlContains, String expectedCartQuantity,
            boolean shouldLogin) throws InterruptedException {
        if (shouldLogin) {
            login();
        }

        if (bookId != null) {
            driver.get(url + "book-detail?bookId=" + bookId);
            WebElement quantityInput = driver.findElement(By.name("quantity"));
            quantityInput.clear();
            quantityInput.sendKeys(String.valueOf(quantity));
            driver.findElement(By.name("addToCartBtn")).click();
        } else {
            driver.get(url + "home");
            List<WebElement> books = driver.findElements(By.className("book-item"));
            WebElement book1 = books.get(0);
            String nameBook1 = book1.findElement(By.name("bookTitleHidden")).getAttribute("value");
            WebElement addToCartButton = book1.findElement(By.className("addToCartBtn"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", addToCartButton);

            if (expectedCartQuantity != null) {
                Thread.sleep(2000);
                WebElement cartProductName = driver.findElement(By.xpath("//table[contains(@class, 'tg-cart-table')]//td[@class='tg-cart-name']//a"));
                String productNameInCart = cartProductName.getText();
                Assert.assertEquals(nameBook1, productNameInCart, "Book name are not the same");
            }
        }

        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(expectedUrlContains),
                "URL does not contain expected string. Actual: " + currentUrl);

        if (expectedCartQuantity != null) {
            String actualCartQuantity = driver.findElement(By.id("cartQuantity")).getAttribute("value");
            Assert.assertEquals(actualCartQuantity, expectedCartQuantity,
                    "Cart quantity does not match expected value");
        }
    }

    private void login() throws InterruptedException {
        driver.get(url + "login.jsp");
        Thread.sleep(2000);
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginSubmit = driver.findElement(By.id("loginSubmit"));
        username.sendKeys("admin");
        password.sendKeys("admin123");
        loginSubmit.click();
        Thread.sleep(2000);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.findElement(By.id("tg-minicart")).click();
                Thread.sleep(1000);
                driver.findElement(By.id("clearCart")).click();
                Thread.sleep(1000);
                driver.findElement(By.id("logout")).click();
                driver.quit();
            } catch (Exception ex) {
                Logger.getLogger(AddToCartDataProviderTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
