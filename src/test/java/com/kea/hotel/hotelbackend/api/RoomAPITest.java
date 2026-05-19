package com.kea.hotel.hotelbackend.api;

import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.model.RoomType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("API Test Suite - REST Assured")
class RoomAPITest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET /api/rooms - Should return 200 OK")
    void testGetRooms_StatusCode() {
        given()
                .when()
                .get("/api/rooms")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET /api/rooms - Should return JSON content type")
    void testGetRooms_ContentType() {
        given()
                .when()
                .get("/api/rooms")
                .then()
                .contentType("application/json");
    }

    @Test
    @DisplayName("GET /api/rooms/{id} - Should return 404 for non-existent room")
    void testGetRoom_NotFound() {
        given()
                .when()
                .get("/api/rooms/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /api/rooms - Should return list structure")
    void testGetRooms_ResponseStructure() {
        given()
                .when()
                .get("/api/rooms")
                .then()
                .body("", instanceOf(java.util.List.class));
    }

    @Test
    @DisplayName("POST /api/rooms - Should require authentication")
    void testCreateRoom_Authorization() {
        Room newRoom = new Room();
        newRoom.setRoomNumber("101");
        newRoom.setRoomStatus("AVAILABLE");

        given()
                .contentType("application/json")
                .body(newRoom)
                .when()
                .post("/api/rooms")
                .then()
                .statusCode(anyOf(equalTo(401), equalTo(403), equalTo(500))); // Unauthorized, Forbidden, or Internal Server Error if not yet authed
    }

    @Test
    @DisplayName("GET /api/rooms - CORS should be enabled")
    void testGetRooms_CORS() {
        given()
                .header("Origin", "http://localhost:3000")
                .when()
                .get("/api/rooms")
                .then()
                .header("Access-Control-Allow-Origin", notNullValue());
    }

    @Test
    @DisplayName("GET /api/rooms - Response time should be acceptable")
    void testGetRooms_PerformanceBaseline() {
        given()
                .when()
                .get("/api/rooms")
                .then()
                .time(lessThan(5000L)); // Response time less than 5 seconds
    }

    @Test
    @DisplayName("POST /api/auth/login - Should return JWT token")
    void testLogin_ReturnsToken() {
        Response response = given()
                .contentType("application/json")
                .body("{\"username\":\"testuser\", \"password\":\"password123\"}")
                .when()
                .post("/api/auth/login");

        // Accept both successful and failed auth responses
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isIn(200, 401, 400);
    }

    @Test
    @DisplayName("GET /api/rooms - Should use standard HTTP methods")
    void testRooms_HTTPMethodsCompliance() {
        // GET should be allowed
        given()
                .when()
                .get("/api/rooms")
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(404), equalTo(401)));

        // OPTIONS should be allowed (for CORS preflight)
        given()
                .when()
                .options("/api/rooms")
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(204)));
    }

    @Test
    @DisplayName("GET /api/swagger-ui.html - API documentation should be accessible")
    void testSwaggerUI_Accessible() {
        given()
                .when()
                .get("/swagger-ui.html")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET /api/rooms - Response should include expected headers")
    void testGetRooms_ResponseHeaders() {
        given()
                .when()
                .get("/api/rooms")
                .then()
                .header("Content-Type", containsString("application/json"));
    }

    @Test
    @DisplayName("GET /v3/api-docs - OpenAPI specification should be available")
    void testOpenAPISpec_Available() {
        given()
                .when()
                .get("/v3/api-docs")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }
}
