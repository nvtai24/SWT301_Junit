package dao;

import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.io.File;
import java.time.Duration;

public class AuthorUITest {
    
    WebDriver driver;
    private final String BASE_URL = "http://localhost:9999/BookShop/home"; // Base URL - home page
    private final String CHROME_DRIVER_PATH = "C:\\Users\\DELL\\SWT301_Junit\\chromedriver-win64\\chromedriver.exe";
    
    @BeforeClass
    public void setUp() {
        // Set up ChromeDriver path
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        // Initialize WebDriver
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    @BeforeMethod
    public void loginAsAdmin() {
        // Navigate to home page first
        driver.get(BASE_URL);
        
        // Find and click the login link in the header
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Login/Register')]"));
        loginLink.click();
        
        // Verify we are on the login page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Welcome Back')]")));
        
        // Login as admin
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector(".tg-btn.tg-active"));
        
        usernameField.clear();
        usernameField.sendKeys("admin");
        passwordField.clear();
        passwordField.sendKeys("admin123");
        loginButton.click();
        
        // Verify successful login - wait for home page elements
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("tg-navigationarea")));
        
        // Verify that we're logged in by checking for the username in the header
        WebElement userLoggedIn = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//a[@id='tg-currenty']/span[contains(text(), 'admin')]")));
        Assert.assertTrue(userLoggedIn.isDisplayed(), "Login verification failed");
        
        // Navigate to Manage Author page from the menu
        WebElement manageAuthorLink = driver.findElement(By.linkText("Manager Author"));
        manageAuthorLink.click();
        
        // Verify we are on the Manage Author page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Manager Author']")));
    }
    
    @DataProvider(name = "authorData")
    public Object[][] getAuthorData() {
        return new Object[][] {
            {"John Smith", "Sample description for John Smith", "author1.jpg"},
            {"Emily Johnson", "Award-winning novelist known for fiction", "author2.jpg"},
            {"Michael Brown", "Science fiction writer with multiple bestsellers", "author3.jpg"},
            {"Sarah Wilson", "Historical fiction author specializing in 19th century", null} // Test with no image
        };
    }
    
    @Test(dataProvider = "authorData")
    public void testAddAuthor(String authorName, String description, String imageName) {
        // Click "Add New Author" button
        WebElement addButton = driver.findElement(By.id("showFormBtn"));
        addButton.click();
        
        // Wait for form to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addAuthorForm")));
        
        // Fill in author details
        WebElement nameField = driver.findElement(By.id("authorName"));
        WebElement descriptionField = driver.findElement(By.id("authorDescription"));
        
        nameField.clear();
        nameField.sendKeys(authorName);
        
        descriptionField.clear();
        descriptionField.sendKeys(description);
        
        // Upload image if provided
        if (imageName != null) {
            WebElement fileInput = driver.findElement(By.id("authorImageFile"));
            // Adjusted path for Ant project structure
            String imagePath = System.getProperty("user.dir") + "/web/images/author/" + imageName;
            File imageFile = new File(imagePath);
            
            if (imageFile.exists()) {
                fileInput.sendKeys(imageFile.getAbsolutePath());
                
                // Verify image preview is displayed
                WebElement imagePreview = driver.findElement(By.id("imagePreview"));
                try {
                    wait.until(ExpectedConditions.attributeContains(imagePreview, "style", "display: block"));
                } catch (Exception e) {
                    System.out.println("Image preview may not be visible, but continuing test");
                }
            } else {
                System.out.println("Image file not found: " + imagePath);
            }
        }
        
        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector(".btn-submit"));
        submitButton.click();
        
        // Wait for page to reload after submission
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order-list")));
        
        // Verify author was added by checking the table
        boolean authorFound = isAuthorInTable(authorName);
        Assert.assertTrue(authorFound, "Author '" + authorName + "' was not found in the table after adding");
    }
    
    private boolean isAuthorInTable(String authorName) {
        try {
            // Find all name cells in the table
            java.util.List<WebElement> nameCells = driver.findElements(By.xpath("//table[@id='order-list']/tbody/tr/td[2]"));
            
            // Check if any cell contains the author name
            for (WebElement cell : nameCells) {
                if (cell.getText().equals(authorName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}