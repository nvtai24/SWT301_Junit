package dao;

import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.*;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorDataProviderTest {
    
    WebDriver driver;
    private final String BASE_URL = "http://localhost:8084/BookShop/login";
    private final String CHROME_DRIVER_PATH = "chromedriver-win64/chromedriver.exe";
    private final String CSV_FILE_PATH = "test/resources/authors.csv";
    private final String SCREENSHOT_DIR = "test-output/screenshots/";
    private final String REPORT_DIR = "test-output/reports/";
    private boolean loggedIn = false;
    
    private Map<String, TestResult> testResults = new HashMap<>();
    
    private enum TestStatus {
        PASSED, FAILED, SKIPPED, ERROR
    }
    
    private class TestResult {
        String authorName;
        String description;
        String imageName;
        boolean expectedResult;
        TestStatus status;
        String message;
        String screenshotPath;
        long executionTime;
        
        public TestResult(String authorName, String description, String imageName, boolean expectedResult) {
            this.authorName = authorName;
            this.description = description;
            this.imageName = imageName;
            this.expectedResult = expectedResult;
        }
    }
    
    @BeforeClass
    public void setUp() {
        new File(SCREENSHOT_DIR).mkdirs();
        new File(REPORT_DIR).mkdirs();
        
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            driver.get(BASE_URL);
            takeScreenshot("login_page");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Login to Your Account')]")));
            
            WebElement usernameField = driver.findElement(By.name("username"));
            highlightElement(usernameField);
            usernameField.clear();
            usernameField.sendKeys("admin");
            
            WebElement passwordField = driver.findElement(By.name("password"));
            highlightElement(passwordField);
            passwordField.clear();
            passwordField.sendKeys("admin123");
            
            takeScreenshot("credentials_entered");
            
            WebElement loginButton = driver.findElement(By.cssSelector(".tg-btn.tg-active"));
            highlightElement(loginButton);
            loginButton.click();
            
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("tg-navigationarea")));
            takeScreenshot("after_login");
            
            WebElement manageAuthorLink = driver.findElement(By.linkText("Manager Author"));
            highlightElement(manageAuthorLink);
            wait.until(ExpectedConditions.elementToBeClickable(manageAuthorLink));
            manageAuthorLink.click();
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Manager Author']")));
            takeScreenshot("manage_author_page");
            
            loggedIn = true;
            Reporter.log("Đăng nhập thành công và chuyển đến trang Manager Author", true);
        } catch (Exception e) {
            takeScreenshot("login_failure");
            Reporter.log("Lỗi khi đăng nhập: " + e.getMessage(), true);
            throw e;
        }
    }
    
    @BeforeMethod
    public void checkLoggedIn() {
        if (!loggedIn) {
            Reporter.log("ERROR: Not logged in - cannot run test", true);
            throw new RuntimeException("Not logged in to application");
        }
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("manager-author")) {
                WebElement manageAuthorLink = driver.findElement(By.linkText("Manager Author"));
                highlightElement(manageAuthorLink);
                wait.until(ExpectedConditions.elementToBeClickable(manageAuthorLink));
                manageAuthorLink.click();
                
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Manager Author']")));
            }
        } catch (Exception e) {
            Reporter.log("Lỗi khi điều hướng đến trang Manager Author: " + e.getMessage(), true);
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
                    writer.println("authorName,description,imageName,expectedResult,errorAction");
                    writer.println("Haruki Murakami,Japanese novelist known for surrealist fiction,author1.jpg,true,");
                    writer.println("Gabriel García Márquez,Colombian novelist known for magical realism,author2.jpg,true,");
                    writer.println("Toni Morrison,American novelist and Nobel Prize winner,author3.jpg,true,");
                    writer.println("Chimamanda Ngozi Adichie,Nigerian author of contemporary fiction,author4.jpg,true,");
                    writer.println("Jane Austen,English novelist known for social commentary on 19th century life,author5.jpg,true,");
                    writer.println("Invalid Author Name,This author has an extremely long name that exceeds the database limits aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,author6.jpg,false,NAME_TOO_LONG");
                    writer.println("Special Characters,Author with special characters !@#$%^&*(),author7.jpg,true,");
                    writer.println("Empty Description,,author8.jpg,false,EMPTY_DESCRIPTION");
                    writer.println(",No name author but has description,author9.jpg,false,EMPTY_NAME");
                    writer.println("No Image Author,This author has no image,,true,");
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
                String authorName = data[0].trim();
                String description = data.length > 1 ? data[1].trim() : "";
                String imageName = data.length > 2 && !data[2].trim().isEmpty() ? data[2].trim() : null;
                boolean expectedResult = data.length > 3 && data[3].trim().equalsIgnoreCase("true");
                String errorAction = data.length > 4 ? data[4].trim() : "";
                
                testData.add(new Object[]{authorName, description, imageName, expectedResult, errorAction});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return testData.toArray(new Object[0][]);
    }
    
    @Test(dataProvider = "authorDataFromCSV")
    public void testAddAuthor(String authorName, String description, String imageName, boolean expectedResult, String errorAction) {
        long startTime = System.currentTimeMillis();
        TestResult result = new TestResult(authorName, description, imageName, expectedResult);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            Reporter.log("\n----------- Test add author: " + authorName + " -----------", true);
            
            if (!expectedResult && !errorAction.isEmpty()) {
                Reporter.log("Test case này dự kiến sẽ thất bại với hành động: " + errorAction, true);
            }
            
            WebElement addButton = driver.findElement(By.id("showFormBtn"));
            highlightElement(addButton);
            wait.until(ExpectedConditions.elementToBeClickable(addButton));
            addButton.click();
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addAuthorForm")));
            
            WebElement nameField = driver.findElement(By.id("authorName"));
            highlightElement(nameField);
            nameField.clear();
            if (!errorAction.equals("EMPTY_NAME")) {
                nameField.sendKeys(authorName);
            }
            
            WebElement descriptionField = driver.findElement(By.id("authorDescription"));
            highlightElement(descriptionField);
            descriptionField.clear();
            if (!errorAction.equals("EMPTY_DESCRIPTION")) {
                descriptionField.sendKeys(description);
            }
            
            if (imageName != null) {
                WebElement fileInput = driver.findElement(By.id("authorImageFile"));
                highlightElement(fileInput);
                
                String imagePath = System.getProperty("user.dir") + "/web/images/author/" + imageName;
                File imageFile = new File(imagePath);
                
                if (imageFile.exists()) {
                    fileInput.sendKeys(imageFile.getAbsolutePath());
                    try {
                        WebElement imagePreview = driver.findElement(By.id("imagePreview"));
                        wait.until(ExpectedConditions.attributeContains(imagePreview, "style", "display: block"));
                        highlightElement(imagePreview);
                    } catch (Exception e) {
                        Reporter.log("Image preview not displayed but continuing test", true);
                    }
                } else {
                    Reporter.log("Warning: Image file not found: " + imagePath, true);
                }
            }
            
            String screenshotPath = takeScreenshot("before_submit_" + authorName.replaceAll("\\s+", "_"));
            result.screenshotPath = screenshotPath;
            
            WebElement submitButton = driver.findElement(By.cssSelector(".btn-submit"));
            highlightElement(submitButton);
            wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            submitButton.click();
            
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order-list")));
                scrollToBottomOfTable();
                takeScreenshot("after_submit_scrolled_" + authorName.replaceAll("\\s+", "_"));
                
                boolean authorFound = isAuthorInTable(authorName);
                
                if (expectedResult) {
                    Assert.assertTrue(authorFound, "Author '" + authorName + "' was not found in the table after adding");
                    Reporter.log("SUCCESS: Author '" + authorName + "' was successfully added to the table", true);
                    result.status = TestStatus.PASSED;
                    result.message = "Author was successfully added as expected";
                } else {
                    Assert.assertFalse(authorFound, "Author '" + authorName + "' was found in the table when it should have failed");
                    Reporter.log("SUCCESS: Author '" + authorName + "' was correctly not added to the table as expected", true);
                    result.status = TestStatus.PASSED;
                    result.message = "Author was correctly not added as expected";
                }
            } catch (Exception e) {
                if (!expectedResult) {
                    Reporter.log("SUCCESS: Form submission failed as expected for error action: " + errorAction, true);
                    result.status = TestStatus.PASSED;
                    result.message = "Form submission failed as expected for action: " + errorAction;
                } else {
                    Reporter.log("FAILED: Expected successful submission but got error instead", true);
                    result.status = TestStatus.FAILED;
                    result.message = "Expected success but got error: " + e.getMessage();
                    String errorScreenshot = takeScreenshot("form_error_" + authorName.replaceAll("\\s+", "_"));
                    result.screenshotPath = errorScreenshot;
                    throw e;
                }
            }
            
        } catch (Exception e) {
            String errorScreenshot = takeScreenshot("error_" + authorName.replaceAll("\\s+", "_"));
            Reporter.log("ERROR: " + e.getMessage(), true);
            
            if (!expectedResult) {
                result.status = TestStatus.PASSED;
                result.message = "Got expected error for action: " + errorAction;
            } else {
                result.status = TestStatus.ERROR;
                result.message = "Unexpected error: " + e.getMessage();
                result.screenshotPath = errorScreenshot;
                throw e;
            }
        } finally {
            long endTime = System.currentTimeMillis();
            result.executionTime = endTime - startTime;
            testResults.put(authorName, result);
        }
    }
    
    private void scrollToBottomOfTable() {
        try {
            WebElement table = driver.findElement(By.id("order-list"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", table);
            
            List<WebElement> rows = table.findElements(By.tagName("tr"));
            if (!rows.isEmpty()) {
                WebElement lastRow = rows.get(rows.size() - 1);
                js.executeScript("arguments[0].scrollIntoView(false);", lastRow);
                highlightElement(lastRow);
                takeScreenshot("scrolled_to_bottom");
                Reporter.log("Scrolled to the bottom of the author table", true);
            }
        } catch (Exception e) {
            Reporter.log("Error scrolling to bottom of table: " + e.getMessage(), true);
        }
    }
    
    private boolean isAuthorInTable(String authorName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order-list")));
            WebElement table = driver.findElement(By.id("order-list"));
            List<WebElement> nameCells = driver.findElements(By.xpath("//table[@id='order-list']/tbody/tr/td[2]"));
            
            takeScreenshot("full_table_view_" + authorName.replaceAll("\\s+", "_"));
            Reporter.log("Tìm thấy " + nameCells.size() + " tác giả trong bảng", true);
            
            Reporter.log("Danh sách tác giả hiện có trong bảng:", true);
            for (int i = 0; i < nameCells.size(); i++) {
                Reporter.log((i+1) + ". " + nameCells.get(i).getText(), true);
            }
            
            for (WebElement cell : nameCells) {
                if (cell.getText().equals(authorName)) {
                    highlightElement(cell);
                    takeScreenshot("found_author_" + authorName.replaceAll("\\s+", "_"));
                    Reporter.log("MATCH FOUND: Tìm thấy tác giả '" + authorName + "' trong bảng", true);
                    return true;
                }
            }
            
            for (WebElement cell : nameCells) {
                if (cell.getText().contains(authorName) || authorName.contains(cell.getText())) {
                    highlightElement(cell);
                    Reporter.log("Found partial author name match: '" + cell.getText() + "' versus expected '" + authorName + "'", true);
                    takeScreenshot("found_author_partial_" + authorName.replaceAll("\\s+", "_"));
                    return true;
                }
            }
            
            Reporter.log("ERROR: KHÔNG tìm thấy tác giả '" + authorName + "' trong bảng sau khi kiểm tra " + nameCells.size() + " ô", true);
            takeScreenshot("author_not_found_" + authorName.replaceAll("\\s+", "_"));
            return false;
        } catch (Exception e) {
            Reporter.log("Error checking for author in table: " + e.getMessage(), true);
            return false;
        }
    }
    
    private void highlightElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String originalStyle = element.getAttribute("style");
            js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
            Thread.sleep(500); // Giữ lại một khoảng chờ nhỏ để highlight được hiển thị trong screenshot
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
        } catch (Exception e) {
            Reporter.log("Could not highlight element: " + e.getMessage(), true);
        }
    }
    
    public String takeScreenshot(String screenshotName) {
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
            
            Reporter.log("Screenshot saved to: " + filename, true);
            return filename;
        } catch (IOException e) {
            Reporter.log("Exception while taking screenshot: " + e.getMessage(), true);
            return null;
        }
    }
    
    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            Object[] params = result.getParameters();
            String authorName = params != null && params.length > 0 && params[0] != null ? 
                params[0].toString().replaceAll("\\s+", "_") : "unknown";
            
            String methodName = result.getMethod().getMethodName();
            String screenshotPath = takeScreenshot("failure_" + methodName + "_" + authorName);
            
            if (testResults.containsKey(authorName)) {
                TestResult testResult = testResults.get(authorName);
                testResult.status = TestStatus.FAILED;
                testResult.message = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test failed";
                testResult.screenshotPath = screenshotPath;
            }
            
            Reporter.log("Test failed for author: " + authorName + ". See screenshot: " + screenshotPath, true);
        }
    }
    
    @AfterClass
    public void tearDown() {
        for (Map.Entry<String, TestResult> entry : testResults.entrySet()) {
            TestResult result = entry.getValue();
            if (result.status == null) {
                result.status = TestStatus.SKIPPED;
                result.message = "Test might have been skipped or terminated abnormally";
            }
        }
        
        generateHTMLReport();
        generateCSVReport();
        
        int totalTests = testResults.size();
        int passedTests = 0, failedTests = 0, errorTests = 0, skippedTests = 0;
        
        for (TestResult result : testResults.values()) {
            switch (result.status) {
                case PASSED: passedTests++; break;
                case FAILED: failedTests++; break;
                case ERROR: errorTests++; break;
                case SKIPPED: skippedTests++; break;
            }
        }
        
        Reporter.log("\n=========== TEST SUMMARY ===========", true);
        Reporter.log("Total Tests: " + totalTests, true);
        Reporter.log("Passed: " + passedTests, true);
        Reporter.log("Failed: " + failedTests, true);
        Reporter.log("Errors: " + errorTests, true);
        Reporter.log("Skipped: " + skippedTests, true);
        Reporter.log("=====================================\n", true);
        
        if (driver != null) {
            driver.quit();
        }
    }
    
    private void generateHTMLReport() {
        try {
            String reportPath = REPORT_DIR + "test_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".html";
            try (PrintWriter writer = new PrintWriter(new File(reportPath))) {
                writer.println("<!DOCTYPE html>");
                writer.println("<html lang='en'><head>");
                writer.println("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                writer.println("<title>Author Test Results</title>");
                writer.println("<style>");
                writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
                writer.println("h1 { color: #333; } table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
                writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
                writer.println("th { background-color: #4CAF50; color: white; }");
                writer.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                writer.println(".pass { color: green; } .fail { color: red; } .error { color: orange; } .skipped { color: gray; }");
                writer.println(".screenshot { max-width: 100px; max-height: 100px; cursor: pointer; }");
                writer.println(".modal { display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.9); }");
                writer.println(".modal-content { margin: auto; display: block; width: 80%; max-width: 700px; }");
                writer.println(".close { position: absolute; top: 15px; right: 35px; color: #f1f1f1; font-size: 40px; font-weight: bold; cursor: pointer; }");
                writer.println("</style></head><body>");
                writer.println("<h1>Author Test Results</h1>");
                writer.println("<p>Test run completed at: " + new Date() + "</p>");
                
                int totalTests = testResults.size();
                int passedTests = 0, failedTests = 0, errorTests = 0, skippedTests = 0;
                for (TestResult result : testResults.values()) {
                    switch (result.status) {
                        case PASSED: passedTests++; break;
                        case FAILED: failedTests++; break;
                        case ERROR: errorTests++; break;
                        case SKIPPED: skippedTests++; break;
                    }
                }
                
                writer.println("<h2>Summary</h2>");
                writer.println("<p>Total Tests: " + totalTests + "</p>");
                writer.println("<p>Passed: <span class='pass'>" + passedTests + "</span></p>");
                writer.println("<p>Failed: <span class='fail'>" + failedTests + "</span></p>");
                writer.println("<p>Errors: <span class='error'>" + errorTests + "</span></p>");
                writer.println("<p>Skipped: <span class='skipped'>" + skippedTests + "</span></p>");
                
                writer.println("<h2>Test Details</h2><table>");
                writer.println("<tr><th>Author Name</th><th>Description</th><th>Image</th><th>Expected Result</th><th>Status</th><th>Message</th><th>Screenshot</th><th>Time (ms)</th></tr>");
                
                for (Map.Entry<String, TestResult> entry : testResults.entrySet()) {
                    TestResult result = entry.getValue();
                    String statusClass = switch (result.status) {
                        case PASSED -> "pass";
                        case FAILED -> "fail";
                        case ERROR -> "error";
                        case SKIPPED -> "skipped";
                    };
                    
                    writer.println("<tr>");
                    writer.println("<td>" + (result.authorName != null ? result.authorName : "") + "</td>");
                    writer.println("<td>" + (result.description != null ? result.description.substring(0, Math.min(50, result.description.length())) + (result.description.length() > 50 ? "..." : "") : "") + "</td>");
                    writer.println("<td>" + (result.imageName != null ? result.imageName : "None") + "</td>");
                    writer.println("<td>" + (result.expectedResult ? "Success" : "Failure") + "</td>");
                    writer.println("<td class='" + statusClass + "'>" + result.status + "</td>");
                    writer.println("<td>" + (result.message != null ? result.message : "") + "</td>");
                    writer.println("<td>" + (result.screenshotPath != null ? "<img src='" + result.screenshotPath.replace('\\', '/') + "' class='screenshot' onclick='showImage(\"" + result.screenshotPath.replace('\\', '/') + "\")'>" : "No screenshot") + "</td>");
                    writer.println("<td>" + result.executionTime + "</td>");
                    writer.println("</tr>");
                }
                
                writer.println("</table>");
                writer.println("<div id='imageModal' class='modal'><span class='close' onclick='closeModal()'>×</span><img class='modal-content' id='modalImage'></div>");
                writer.println("<script>");
                writer.println("function showImage(src) { var modal = document.getElementById('imageModal'); var modalImg = document.getElementById('modalImage'); modal.style.display = 'block'; modalImg.src = src; }");
                writer.println("function closeModal() { document.getElementById('imageModal').style.display = 'none'; }");
                writer.println("</script></body></html>");
            }
            Reporter.log("HTML Report generated: " + reportPath, true);
        } catch (IOException e) {
            Reporter.log("Error generating HTML report: " + e.getMessage(), true);
        }
    }
    
    private void generateCSVReport() {
        try {
            String reportPath = REPORT_DIR + "test_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
            try (PrintWriter writer = new PrintWriter(new File(reportPath))) {
                writer.println("Author Name,Description,Image,Expected Result,Status,Message,Screenshot,Time (ms)");
                for (Map.Entry<String, TestResult> entry : testResults.entrySet()) {
                    TestResult result = entry.getValue();
                    writer.println(csvEscape(result.authorName) + "," + 
                                 csvEscape(result.description) + "," + 
                                 csvEscape(result.imageName) + "," + 
                                 result.expectedResult + "," + 
                                 result.status + "," + 
                                 csvEscape(result.message) + "," + 
                                 csvEscape(result.screenshotPath) + "," + 
                                 result.executionTime);
                }
            }
            Reporter.log("CSV Report generated: " + reportPath, true);
        } catch (IOException e) {
            Reporter.log("Error generating CSV report: " + e.getMessage(), true);
        }
    }
    
    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}