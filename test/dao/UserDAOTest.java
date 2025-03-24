package dao;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserDAOTest {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chrome.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void testAddUser() {
        driver.get("http://localhost:9999/BookShop/home");

        // Thực hiện các hành động tự động hóa như tìm kiếm phần tử, điền form, v.v.
        driver.findElement(By.id("addButton")).click();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Đóng trình duyệt sau khi kiểm tra xong
        }
    }
}
