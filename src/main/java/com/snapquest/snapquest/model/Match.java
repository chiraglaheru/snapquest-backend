package com.snapquest.snapquest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.snapquest.snapquest.model.Quest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "matches")
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long player1Id;
    private Long player2Id;

    public Boolean getPlayer1Submitted() {
        return player1Submitted;
    }

    public void setPlayer1Submitted(Boolean player1Submitted) {
        this.player1Submitted = player1Submitted;
    }

    public Boolean getPlayer2Submitted() {
        return player2Submitted;
    }

    public void setPlayer2Submitted(Boolean player2Submitted) {
        this.player2Submitted = player2Submitted;
    }
    private Double latitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private Double longitude;
    private Boolean player1Submitted = false;
    private Boolean player2Submitted = false;

    @Column(name = "quest_id")
    private Long questId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "quest_id",
            insertable = false,
            updatable = false
    )
    private Quest quest;

    public Quest getQuest(){
        return  quest;
    }

    private String status;

    private Long winnerId;

    private LocalDateTime createdAt =
            LocalDateTime.now();

    public Match() {}

    public Long getId() {
        return id;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public Long getQuestId() {
        return questId;
    }

    public void setQuestId(Long questId) {
        this.questId = questId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}