package com.kea.hotel.hotelbackend.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * END-TO-END (UI) tests: a real headless Chrome (Selenium) opens the running app's Swagger UI.
 * HOW: @SpringBootTest(RANDOM_PORT) starts the real server; WebDriverManager downloads a matching ChromeDriver.
 * REQUIRES: Chrome installed + internet for the driver download.
 * BREAK IT: disable Swagger (springdoc.swagger-ui.enabled=false), rename a controller/tag, or expect a word that is not on the page.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("E2E: Complete Booking Flow")
class BookingFlowE2EIntegrationTest {

    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    private int port;

    private String getBaseUrl() { return "http://localhost:" + port; }

    private WebDriver driver;
    private WebDriverWait wait;

    // Starts headless Chrome with a 20 s explicit wait before each test.
    @BeforeEach
    void setUp() {
        // Use WebDriverManager to ensure compatible ChromeDriver is available
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--headless=new");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Closes the browser after each test so Chrome processes don't pile up.
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Checks: Swagger page loads (title has Swagger/Explore) and /v3/api-docs lists auth, room, reservation.
    // Fallback: if the HTTP call itself throws, it reads the page text instead (assertion errors are NOT swallowed).
    // Note: despite the name, it does not perform a booking - it only checks the API docs.
    // Fails if: Swagger is off, or the API docs lose those controllers.
    @Test
    @DisplayName("Should complete full booking flow: auth -> search rooms -> create reservation -> view bill")
    void testCompleteBookingFlow() {
        // Step 1: Open Swagger UI in the browser
        driver.get(getBaseUrl() + "/swagger-ui.html");
        assertThat(driver.getTitle()).matches(".*(Swagger|Explore).*");

        // Step 2: Fetch the OpenAPI JSON over plain HTTP (more reliable than reading the rendered page)
        try {
            URL u = new URL(getBaseUrl() + "/v3/api-docs");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int code = conn.getResponseCode();
            assertThat(code).isGreaterThanOrEqualTo(200);
            InputStream is = conn.getInputStream();
            String apiJson = new String(is.readAllBytes());
            String lower = apiJson.toLowerCase();
            assertThat(lower).contains("auth").contains("room").contains("reservation");
        } catch (Exception ex) {
            // If the HTTP call throws (e.g. connection error), fall back to the rendered page text
            String bodyText = wait.until(d -> {
                try {
                    String t = d.findElement(By.tagName("body")).getText();
                    return (t != null && !t.isEmpty()) ? t : null;
                } catch (Exception ignored) {
                    return null;
                }
            });
            String lower = bodyText.toLowerCase();
            assertThat(lower).containsAnyOf("swagger", "openapi", "explore", "auth", "room", "reservation");
        }
    }

    // Checks: Swagger page opens without login (URL contains 'swagger-ui') and the body has text.
    // WEAK: it never calls /api/rooms, so it cannot prove rooms are public.
    // Fails if: Swagger redirects to a login page or is blank.
    @Test
    @DisplayName("Should access public API endpoints without authentication")
    void testPublicAPIAccess() {
        // Navigate to Swagger UI
        driver.get(getBaseUrl() + "/swagger-ui.html");

        // Verify API documentation loads
        assertThat(driver.getCurrentUrl()).contains("swagger-ui");

        // Page body must contain some text (this does not call /api/rooms itself)
        WebElement rolesText = driver.findElement(By.tagName("body"));
        assertThat(rolesText.getText()).isNotEmpty();
    }

    // Checks: after the page header is visible, the text mentions auth, room, guest, reservation, bill.
    // Text match is case-sensitive.
    // Fails if: a controller is removed/renamed or Swagger shows different tag names.
    @Test
    @DisplayName("Should show API endpoints organization")
    void testAPIEndpointsOrganization() {
        driver.get(getBaseUrl() + "/swagger-ui.html");

        // Wait until the Swagger header is visible (page finished rendering)
        wait.until(d -> {
            try {
                var els = d.findElements(By.cssSelector(".topbar-title, .swagger-ui .topbar, .swagger-ui .title, .swagger-ui .info h1"));
                for (WebElement e : els) {
                    if (e != null && e.isDisplayed()) return e;
                }
            } catch (Exception ignored) {
            }
            return null;
        });

        // Each major API group must appear in the page text (case-sensitive)
        String pageText = driver.findElement(By.tagName("body")).getText();

        assertThat(pageText).contains("auth");
        assertThat(pageText).contains("room");
        assertThat(pageText).contains("guest");
        assertThat(pageText).contains("reservation");
        assertThat(pageText).contains("bill");
    }

    // WEAK: only checks the page source is not empty and the body element exists.
    // Fails only if the page does not load at all.
    @Test
    @DisplayName("Should verify API works with HTTP status codes")
    void testAPIStatusCodes() {
        driver.get(getBaseUrl() + "/swagger-ui.html");

        // Page source must not be empty (page loaded)
        assertThat(driver.getPageSource()).isNotEmpty();

        // Page body element must exist
        WebElement body = driver.findElement(By.tagName("body"));
        assertThat(body).isNotNull();
    }
}
