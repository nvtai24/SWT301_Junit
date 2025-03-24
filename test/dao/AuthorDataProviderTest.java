package dao;

import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;

import java.io.*;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorDataProviderTest {
    
    WebDriver driver;
    private final String BASE_URL = "http://localhost:8084/BookShop/login"; // Home page URL
    private final String CHROME_DRIVER_PATH = "chromedriver-win64/chromedriver.exe";
    private final String CSV_FILE_PATH = "test/resources/authors.csv";
    private final String SCREENSHOT_DIR = "test-output/screenshots/";
    private boolean loggedIn = false;
    
    @BeforeClass
    public void setUp() {
        // Create screenshot directory
        new File(SCREENSHOT_DIR).mkdirs();
        
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        // Initialize WebDriver
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        try {
            driver.get(BASE_URL);
            takeScreenshot("login_page");
            slowDown(1000); // Tạm dừng 2 giây để quan sát
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Login to Your Account')]")));
            
            WebElement usernameField = driver.findElement(By.name("username"));
            highlightElement(usernameField);
            usernameField.clear();
            slowSendKeys(usernameField, "admin");
            
            WebElement passwordField = driver.findElement(By.name("password"));
            highlightElement(passwordField);
            passwordField.clear();
            slowSendKeys(passwordField, "admin123");
            
            takeScreenshot("credentials_entered");
            slowDown(1000); // Tạm dừng trước khi nhấn
            
            WebElement loginButton = driver.findElement(By.cssSelector(".tg-btn.tg-active"));
            highlightElement(loginButton);
            loginButton.click();
            
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("tg-navigationarea")));
            takeScreenshot("after_login");
            slowDown(1000); 
            WebElement manageAuthorLink = driver.findElement(By.linkText("Manager Author"));
            highlightElement(manageAuthorLink);
            slowDown(1000); 
            manageAuthorLink.click();
            
         
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Manager Author']")));
            takeScreenshot("manage_author_page");
            slowDown(1000); 
            
            loggedIn = true;
        } catch (Exception e) {
            takeScreenshot("login_failure");
            e.printStackTrace();
            throw e;
        }
    }
    
    @BeforeMethod
    public void checkLoggedIn() {
        if (!loggedIn) {
            System.out.println("ERROR: Not logged in - cannot run test");
            throw new RuntimeException("Not logged in to application");
        }
        
        try {
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("manager-author")) {
                WebElement manageAuthorLink = driver.findElement(By.linkText("Manager Author"));
                highlightElement(manageAuthorLink);
                slowDown(1000);
                manageAuthorLink.click();
                
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Manager Author']")));
                slowDown(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            takeScreenshot("navigation_failure");
            throw e;
        }
    }
    
    @DataProvider(name = "authorDataFromCSV")
    public Object[][] getAuthorDataFromCSV() {
        List<Object[]> testData = new ArrayList<>();
        File csvFile = new File(CSV_FILE_PATH);
        
        if (!csvFile.exists()) {
            try {
                csvFile.getParentFile().mkdirs();
                try (PrintWriter writer = new PrintWriter(csvFile)) {
                    writer.println("authorName,description,imageName,expectedResult");
                    writer.println("Mark Twain,American writer and humorist,author1.jpg,true");
                    writer.println("J.K. Rowling,British author best known for Harry Potter series,author2.jpg,true");
                    writer.println("Stephen King,Horror and supernatural fiction writer,author3.jpg,true");
                    writer.println("George Orwell,English novelist and essayist known for dystopian fiction,,true");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;
            
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                String[] data = line.split(",");
                
                // Handling CSV format: authorName,description,imageName,expectedResult
                String authorName = data[0].trim();
                String description = data[1].trim();
                String imageName = data.length > 2 && !data[2].trim().isEmpty() ? data[2].trim() : null;
                boolean expectedResult = data.length > 3 && data[3].trim().equalsIgnoreCase("true");
                
                testData.add(new Object[]{authorName, description, imageName, expectedResult});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return testData.toArray(new Object[0][]);
    }
    
    @Test(dataProvider = "authorDataFromCSV")
    public void testAddAuthor(String authorName, String description, String imageName, boolean expectedResult) {
        try {
            System.out.println("\n----------- Test add author: " + authorName + " -----------\n");
            
            WebElement addButton = driver.findElement(By.id("showFormBtn"));
            highlightElement(addButton);
            slowDown(1500); 
            addButton.click();
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addAuthorForm")));
            slowDown(1000); 
            
            WebElement nameField = driver.findElement(By.id("authorName"));
            highlightElement(nameField);
            nameField.clear();
            slowSendKeys(nameField, authorName);
            slowDown(1000); 
            
            WebElement descriptionField = driver.findElement(By.id("authorDescription"));
            highlightElement(descriptionField);
            descriptionField.clear();
            slowSendKeys(descriptionField, description);
            slowDown(1000); 
            
            if (imageName != null) {
                WebElement fileInput = driver.findElement(By.id("authorImageFile"));
                highlightElement(fileInput);
                
                // Using the actual project structure path to images
                String imagePath = System.getProperty("user.dir") + "/web/images/author/" + imageName;
                File imageFile = new File(imagePath);
                
                if (imageFile.exists()) {
                    fileInput.sendKeys(imageFile.getAbsolutePath());
                    slowDown(1000); // Tạm dừng sau khi chọn file
                    
                    // Verify image preview is displayed
                    try {
                        WebElement imagePreview = driver.findElement(By.id("imagePreview"));
                        wait.until(ExpectedConditions.attributeContains(imagePreview, "style", "display: block"));
                        highlightElement(imagePreview);
                    } catch (Exception e) {
                        System.out.println("Image preview not displayed but continuing test");
                    }
                } else {
                    System.out.println("Warning: Image file not found: " + imagePath);
                }
            }
            
            // Take screenshot before submitting
            takeScreenshot("before_submit_" + authorName.replaceAll("\\s+", "_"));
            slowDown(1000); // Tạm dừng trước khi gửi form
            
            // Submit the form
            WebElement submitButton = driver.findElement(By.cssSelector(".btn-submit"));
            highlightElement(submitButton);
            slowDown(1500); // Tạm dừng sau khi highlight nút gửi
            submitButton.click();
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order-list")));
            slowDown(3000); // Tạm dừng sau khi gửi form để xem kết quả
            
            takeScreenshot("after_submit_" + authorName.replaceAll("\\s+", "_"));
            
            boolean authorFound = isAuthorInTable(authorName);
            
            if (expectedResult) {
                Assert.assertTrue(authorFound, "Author '" + authorName + "' was not found in the table after adding");
                System.out.println("SUCCESS: Author '" + authorName + "' was successfully added to the table");
            } else {
                Assert.assertFalse(authorFound, "Author '" + authorName + "' was found in the table when it should have failed");
                System.out.println("SUCCESS: Author '" + authorName + "' was correctly not added to the table as expected");
            }
            
            slowDown(3000); 
            
        } catch (Exception e) {
            takeScreenshot("error_" + authorName.replaceAll("\\s+", "_"));
            e.printStackTrace();
            throw e;
        }
    }
    
    private boolean isAuthorInTable(String authorName) {
        try {
            // Find all name cells in the table
            java.util.List<WebElement> nameCells = driver.findElements(By.xpath("//table[@id='order-list']/tbody/tr/td[2]"));
            
            // Check if any cell contains the author name
            for (WebElement cell : nameCells) {
                if (cell.getText().equals(authorName)) {
                    highlightElement(cell); // Highlight the matching cell
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
   
    private void slowDown(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
 
    private void slowSendKeys(WebElement element, String text) {
        for (char c : text.toCharArray()) {
            element.sendKeys(Character.toString(c));
            slowDown(150); // Tạm dừng giữa các ký tự
        }
    }
    
    
    private void highlightElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String originalStyle = element.getAttribute("style");
            
            js.executeScript(
                "arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", 
                element
            );
            
            slowDown(500);
            
            js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);", 
                element, 
                originalStyle
            );
        } catch (Exception e) {
            System.out.println("Could not highlight element: " + e.getMessage());
        }
    }
    
    public void takeScreenshot(String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = SCREENSHOT_DIR + screenshotName + "_" + timestamp + ".png";
            File destination = new File(filename);
            
            try (FileInputStream fileInputStream = new FileInputStream(source);
                 FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
            }
            
            System.out.println("Screenshot saved to: " + filename);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }
    
    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            String methodName = result.getMethod().getMethodName();
            takeScreenshot("failure_" + methodName);
        }
    }
    
    @AfterClass
    public void tearDown() {
        slowDown(5000);
        
        if (driver != null) {
            driver.quit();
        }
    }
}