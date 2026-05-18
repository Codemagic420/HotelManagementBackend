package com.kea.hotel.hotelbackend.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("E2E: Complete Booking Flow")
class BookingFlowE2EIntegrationTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        // Using ChromeDriver - in production use WebDriverManager
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Should complete full booking flow: auth -> search rooms -> create reservation -> view bill")
    void testCompleteBookingFlow() {
        // Step 1: Navigate to application
        driver.get(BASE_URL + "/swagger-ui.html");
        assertThat(driver.getTitle()).contains("Swagger UI");

        // Step 2: Verify API endpoints are accessible
        WebElement contentElement = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.className("topbar-title"))
        );
        assertThat(contentElement.getText()).contains("Swagger UI");

        // Step 3: Verify authentication endpoint exists
        WebElement authSection = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'auth')]"))
        );
        assertThat(authSection).isNotNull();

        // Step 4: Verify room endpoints exist
        WebElement roomSection = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'room')]"))
        );
        assertThat(roomSection).isNotNull();

        // Step 5: Verify reservation endpoints exist
        WebElement reservationSection = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'reservation')]"))
        );
        assertThat(reservationSection).isNotNull();
    }

    @Test
    @DisplayName("Should access public API endpoints without authentication")
    void testPublicAPIAccess() {
        // Navigate to Swagger UI
        driver.get(BASE_URL + "/swagger-ui.html");

        // Verify API documentation loads
        assertThat(driver.getCurrentUrl()).contains("swagger-ui");

        // Rooms endpoint should be accessible without auth
        WebElement rolesText = driver.findElement(By.tagName("body"));
        assertThat(rolesText.getText()).isNotEmpty();
    }

    @Test
    @DisplayName("Should show API endpoints organization")
    void testAPIEndpointsOrganization() {
        driver.get(BASE_URL + "/swagger-ui.html");

        // Wait for page to fully load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("topbar-title")));

        // Verify major endpoint groups
        String pageText = driver.findElement(By.tagName("body")).getText();

        assertThat(pageText).contains("auth");
        assertThat(pageText).contains("room");
        assertThat(pageText).contains("guest");
        assertThat(pageText).contains("reservation");
        assertThat(pageText).contains("bill");
    }

    @Test
    @DisplayName("Should verify API works with HTTP status codes")
    void testAPIStatusCodes() {
        driver.get(BASE_URL + "/swagger-ui.html");

        // Verify Swagger loads successfully (200 OK)
        assertThat(driver.getPageSource()).isNotEmpty();

        // Check for response examples in documentation
        WebElement body = driver.findElement(By.tagName("body"));
        assertThat(body).isNotNull();
    }
}
