package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.mongodb.document.AiInteraction;
import com.kea.hotel.hotelbackend.service.AiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/guest/ask")
    public ResponseEntity<Map<String, Object>> guestAsk(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(aiService.askGuest(body.get("question")));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "answer", "Sorry, I'm not available right now. Please contact our reception team directly."));
        }
    }

    @PostMapping("/staff/ask")
    public ResponseEntity<Map<String, Object>> staffAsk(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(aiService.askStaff(body.get("question")));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("answer", "AI service unavailable.", "sources", List.of()));
        }
    }

    @GetMapping("/interactions")
    public Page<AiInteraction> getInteractions(
            @RequestParam(required = false) String botType,
            @PageableDefault(size = 20) Pageable pageable) {
        return botType != null
                ? aiService.getInteractionsByBotType(botType, pageable)
                : aiService.getInteractions(pageable);
    }
}
