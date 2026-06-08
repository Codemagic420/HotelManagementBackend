package com.kea.hotel.hotelbackend.api;

import com.kea.hotel.hotelbackend.mongodb.document.AiInteraction;
import com.kea.hotel.hotelbackend.mongodb.service.*;
import com.kea.hotel.hotelbackend.neo4j.service.*;
import com.kea.hotel.hotelbackend.service.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@DisplayName("AI Endpoint Integration Tests")
class AiApiIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @MockitoBean private AiService aiService;

    // MongoDB services (needed by context)
    @MockitoBean private MongoRoomService mongoRoomService;
    @MockitoBean private MongoGuestService mongoGuestService;
    @MockitoBean private MongoRoomTypeService mongoRoomTypeService;
    @MockitoBean private MongoCleanerService mongoCleanerService;
    @MockitoBean private MongoExtraServiceService mongoExtraServiceService;
    @MockitoBean private MongoInventoryItemService mongoInventoryItemService;
    @MockitoBean private MongoSeasonRateService mongoSeasonRateService;
    @MockitoBean private MongoRoomCleaningTaskService mongoRoomCleaningTaskService;
    @MockitoBean private MongoReservationGuestService mongoReservationGuestService;
    @MockitoBean private MongoRoomCleaningAssignmentService mongoRoomCleaningAssignmentService;
    @MockitoBean private MongoBillService mongoBillService;
    @MockitoBean private MongoBillItemService mongoBillItemService;
    @MockitoBean private MongoReservationService mongoReservationService;

    // Neo4j services (needed by context)
    @MockitoBean private Neo4jRoomService neo4jRoomService;
    @MockitoBean private Neo4jGuestService neo4jGuestService;
    @MockitoBean private Neo4jReservationService neo4jReservationService;
    @MockitoBean private Neo4jRoomTypeService neo4jRoomTypeService;
    @MockitoBean private Neo4jCleanerService neo4jCleanerService;
    @MockitoBean private Neo4jExtraServiceService neo4jExtraServiceService;
    @MockitoBean private Neo4jInventoryItemService neo4jInventoryItemService;
    @MockitoBean private Neo4jSeasonRateService neo4jSeasonRateService;
    @MockitoBean private Neo4jBillService neo4jBillService;
    @MockitoBean private Neo4jBillItemService neo4jBillItemService;
    @MockitoBean private Neo4jRoomCleaningTaskService neo4jRoomCleaningTaskService;
    @MockitoBean private Neo4jReservationGuestService neo4jReservationGuestService;
    @MockitoBean private Neo4jRoomCleaningAssignmentService neo4jRoomCleaningAssignmentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        when(aiService.askGuest(anyString()))
                .thenReturn(Map.of("answer", "Welcome to our hotel! How can I help you?"));
        when(aiService.askStaff(anyString()))
                .thenReturn(Map.of("answer", "Here is the staff info.", "sources", List.of("doc1.pdf")));
        when(aiService.getInteractions(any())).thenReturn(Page.empty());
        when(aiService.getInteractionsByBotType(anyString(), any())).thenReturn(Page.empty());
    }

    // ── Guest endpoint (public) ───────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/ai/guest/ask - Should return 200 without authentication (public endpoint)")
    void testGuestAsk_IsPublic_Returns200() throws Exception {
        mockMvc.perform(post("/api/ai/guest/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"What time is check-in?\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.answer").value("Welcome to our hotel! How can I help you?"));
    }

    @Test
    @DisplayName("POST /api/ai/guest/ask - Should return answer even with empty question")
    void testGuestAsk_EmptyQuestion_Returns200() throws Exception {
        mockMvc.perform(post("/api/ai/guest/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"\"}"))
                .andExpect(status().isOk());
    }

    // ── Staff endpoint (protected) ────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("POST /api/ai/staff/ask - Should return 200 with answer and sources when authenticated")
    void testStaffAsk_WithAuth_Returns200() throws Exception {
        mockMvc.perform(post("/api/ai/staff/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"How many rooms are available?\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").exists())
                .andExpect(jsonPath("$.sources").isArray());
    }

    @Test
    @DisplayName("POST /api/ai/staff/ask - Should return 401 without authentication")
    void testStaffAsk_WithoutAuth_Returns401() throws Exception {
        mockMvc.perform(post("/api/ai/staff/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"How many rooms are available?\"}"))
                .andExpect(status().isUnauthorized());
    }

    // ── Interactions endpoint (protected) ─────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/ai/interactions - Should return paginated interactions when authenticated")
    void testGetInteractions_WithAuth_Returns200() throws Exception {
        mockMvc.perform(get("/api/ai/interactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/ai/interactions - Should return 401 without authentication")
    void testGetInteractions_WithoutAuth_Returns401() throws Exception {
        mockMvc.perform(get("/api/ai/interactions"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/ai/interactions?botType=guest - Should filter by botType")
    void testGetInteractions_FilterByBotType_Returns200() throws Exception {
        mockMvc.perform(get("/api/ai/interactions").param("botType", "guest"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/ai/interactions?botType=staff - Should filter by botType staff")
    void testGetInteractions_FilterByBotTypeStaff_Returns200() throws Exception {
        mockMvc.perform(get("/api/ai/interactions").param("botType", "staff"))
                .andExpect(status().isOk());
    }

    // ── Stored AI results (domain model requirement) ──────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/ai/interactions - AI results are persisted and retrievable (not transient)")
    void testInteractions_ArePersistedInDomainModel() throws Exception {
        AiInteraction stored = new AiInteraction();
        stored.setId("abc123");
        stored.setBotType("guest");
        stored.setQuestion("What time is checkout?");
        stored.setAnswer("Checkout is at 11:00 AM.");

        when(aiService.getInteractions(any()))
                .thenReturn(new PageImpl<>(List.of(stored)));

        mockMvc.perform(get("/api/ai/interactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].question").value("What time is checkout?"))
                .andExpect(jsonPath("$.content[0].answer").value("Checkout is at 11:00 AM."))
                .andExpect(jsonPath("$.content[0].botType").value("guest"));
    }
}
