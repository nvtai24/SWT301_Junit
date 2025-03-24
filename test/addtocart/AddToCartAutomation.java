/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package addtocart;

import java.time.Duration;
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
import org.testng.annotations.Test;

/**
 *
 * @author Nvtai
 */
public class AddToCartAutomation {

    private WebDriver driver;
    private WebDriverWait wait;

    private String url = "http://localhost:8084/BookShop/";

    @BeforeMethod
    public void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "chromedriver-win64/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

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

//    @Test
//    public void testAddToCartWithoutLogin() throws InterruptedException {
//        driver.get(url + "home");
//
//        Thread.sleep(2000);
//
//        // Find all buttons with name="addBtn"
//        List<WebElement> buttons = driver.findElements(By.id("addBtn"));
//        Assert.assertTrue(!buttons.isEmpty(), "No 'Add to cart' buttons found");
//
//        // Get the first button and try to click it with JavaScript
//        WebElement button1 = buttons.get(0);
//
//        // Try JavaScript click which often works better for problematic elements
//        JavascriptExecutor executor = (JavascriptExecutor) driver;
//        executor.executeScript("arguments[0].click();", button1);
//
//        // Wait for redirect
//        Thread.sleep(2000);
//
//        // Verify current URL contains "login"
//        String currentUrl = driver.getCurrentUrl();
//        Assert.assertTrue(currentUrl.contains("login"), "Not redirected to login page. Current URL: " + currentUrl);
//
//        driver.quit();
//    }
    @Test
    public void testAddToCartWithLogin() throws InterruptedException {
        List<WebElement> books = driver.findElements(By.className("book-item"));

        WebElement book1 = books.get(0);

        String nameBook1 = book1.findElement(By.name("bookTitleHidden")).getAttribute("value");

        WebElement addToCartButton = book1.findElement(By.className("addToCartBtn"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", addToCartButton);

        Thread.sleep(2000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart"), "Can not add to cart");

        WebElement cartProductName = driver.findElement(By.xpath("//table[contains(@class, 'tg-cart-table')]//td[@class='tg-cart-name']//a"));
        String productNameInCart = cartProductName.getText();

        Assert.assertEquals(nameBook1, productNameInCart, "Book name are not the same");
    }

    @Test
    public void testAddToCartWithAvailableQuantity() throws InterruptedException {
        driver.get(url + "book-detail?bookId=4");

        String quantityAvailable = driver.findElement(By.name("hiddenQuantity")).getAttribute("value");

        WebElement quantityInput = driver.findElement(By.name("quantity"));

        quantityInput.clear();
        quantityInput.sendKeys(quantityAvailable);

        driver.findElement(By.name("addToCartBtn")).click();
        Thread.sleep(2000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart"), "Not redirected to cart page. Current URL: " + currentUrl);
    }

    @Test
    public void testAddToCartMoreThanAvailableQuantity() throws InterruptedException {
        driver.get(url + "book-detail?bookId=4");

        String quantityAvailable = driver.findElement(By.name("hiddenQuantity")).getAttribute("value");
        int q = Integer.parseInt(quantityAvailable);
        q = q + 1;

        WebElement quantityInput = driver.findElement(By.name("quantity"));

        quantityInput.clear();
        quantityInput.sendKeys(q + "");

        String oldUrl = driver.getCurrentUrl();

        driver.findElement(By.name("addToCartBtn")).click();
        Thread.sleep(2000);

        String newUrl = driver.getCurrentUrl();

        Assert.assertEquals(oldUrl, newUrl, "Be directed to cart page");
    }

    @Test
    public void testAddToCartUnavailableQuantity() throws InterruptedException {
        driver.get(url + "book-detail?bookId=4");

        String quantityAvailable = driver.findElement(By.name("hiddenQuantity")).getAttribute("value");
        int q = Integer.parseInt(quantityAvailable);
        q = q + 1;

        WebElement quantityInput = driver.findElement(By.name("quantity"));

        quantityInput.clear();
        quantityInput.sendKeys("-1");

        String oldUrl = driver.getCurrentUrl();

        driver.findElement(By.name("addToCartBtn")).click();
        Thread.sleep(2000);

        String newUrl = driver.getCurrentUrl();

        Assert.assertEquals(oldUrl, newUrl, "Be directed to cart page");
    }

    @Test
    public void testAddToCartChangeQuantity() throws InterruptedException {
        driver.get(url + "book-detail?bookId=4");

        String quantityAvailable = driver.findElement(By.name("hiddenQuantity")).getAttribute("value");
        int quantity = Integer.parseInt(quantityAvailable);

        int count = 1;

        while (count <= quantity + 1) {
            driver.findElement(By.id("plusBtn")).click();
            count++;
        }

        while (count >= -1) {
            driver.findElement(By.id("minusBtn")).click();
            count--;
        }

        WebElement quantityInput = driver.findElement(By.name("quantity"));

        quantityInput.clear();
        quantityInput.sendKeys(quantityAvailable);

        driver.findElement(By.name("addToCartBtn")).click();
        Thread.sleep(2000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart"), "Not redirected to cart page. Current URL: " + currentUrl);
    }

    @Test
    public void testUpdateQuantity() throws InterruptedException {
        driver.get(url + "book-detail?bookId=4");
        driver.findElement(By.name("addToCartBtn")).click();
        Thread.sleep(1000);

        driver.get(url + "book-detail?bookId=4");
        WebElement quantityInput = driver.findElement(By.name("quantity"));
        quantityInput.clear();
        quantityInput.sendKeys("2");
        driver.findElement(By.name("addToCartBtn")).click();

        Thread.sleep(1000);

        String actual = driver.findElement(By.id("cartQuantity")).getAttribute("value");

        Assert.assertEquals(actual, "3", "Quantity in cart should be 3");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.findElement(By.id("tg-minicart")).click();
                Thread.sleep(1000);
                driver.findElement(By.id("clearCart")).click();
                Thread.sleep(1000);
                driver.quit();
            } catch (InterruptedException ex) {
                Logger.getLogger(AddToCartAutomation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
