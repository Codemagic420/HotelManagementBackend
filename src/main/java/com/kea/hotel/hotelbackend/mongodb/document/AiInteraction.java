package com.kea.hotel.hotelbackend.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ai_interactions")
public class AiInteraction {

    @Id
    private String id;

    @Indexed
    private String botType;

    private String question;
    private String answer;
    private List<String> sources;
    private LocalDateTime timestamp;

    public AiInteraction() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBotType() { return botType; }
    public void setBotType(String botType) { this.botType = botType; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
