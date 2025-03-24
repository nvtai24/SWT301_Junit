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

/**
 *
 * @author Nvtai
 */
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
        String csvFile = "test/resources/cart_test.csv"; // Đường dẫn tới file CSV
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Bỏ qua header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                testData.add(new Object[]{data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5])});
            }
        }

        return testData.toArray(new Object[0][]);
    }

    @Test(dataProvider = "testData")
    public void testAddToCart(String testCaseName, String bookId, String quantity, String expectedUrlContains, 
                              String expectedCartQuantity, boolean shouldLogin) throws InterruptedException {
        // Đăng nhập nếu shouldLogin là true
        if (shouldLogin) {
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

        // Xử lý trường hợp đặc biệt cho testUpdateQuantity
        if (testCaseName.equals("testUpdateQuantity")) {
            // Thêm 1 sản phẩm vào giỏ hàng trước
            driver.get(url + "book-detail?bookId=" + bookId);
            WebElement quantityInputInitial = driver.findElement(By.name("quantity"));
            quantityInputInitial.clear();
            quantityInputInitial.sendKeys("1");
            driver.findElement(By.name("addToCartBtn")).click();
            Thread.sleep(2000);
        }

        // Điều hướng đến trang phù hợp và thực hiện hành động
        if (bookId.isEmpty()) {
            driver.get(url + "home");
            Thread.sleep(2000);
            List<WebElement> books = driver.findElements(By.className("book-item"));
            WebElement book1 = books.get(0);
            WebElement addToCartButton = book1.findElement(By.className("addToCartBtn"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", addToCartButton);
        } else {
            driver.get(url + "book-detail?bookId=" + bookId);
            WebElement quantityInput = driver.findElement(By.name("quantity"));
            quantityInput.clear();
            quantityInput.sendKeys(quantity);
            driver.findElement(By.name("addToCartBtn")).click();
        }

        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();

        // Kiểm tra URL
        Assert.assertTrue(currentUrl.contains(expectedUrlContains), 
            testCaseName + ": Expected URL to contain '" + expectedUrlContains + "', but got: " + currentUrl);

        // Kiểm tra số lượng trong giỏ hàng nếu có giá trị expectedCartQuantity
        if (!expectedCartQuantity.isEmpty() && currentUrl.contains("cart")) {
            String cartQuantity = driver.findElement(By.id("cartQuantity")).getAttribute("value");
            Assert.assertEquals(cartQuantity, expectedCartQuantity, 
                testCaseName + ": Quantity in cart does not match expected value");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                if (driver.getCurrentUrl().contains("cart")) {
                    driver.findElement(By.id("tg-minicart")).click();
                    Thread.sleep(1000);
                    driver.findElement(By.id("clearCart")).click();
                    Thread.sleep(1000);
                }
                driver.quit();
            } catch (InterruptedException ex) {
                Logger.getLogger(AddToCartDataProviderTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}