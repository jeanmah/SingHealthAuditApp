package com.c2g4.SingHealthWebApp.Chat;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;
import java.sql.Time;

@Table("ChatEntries")
public class ChatEntriesModel {
    @Id
    private int chatEntry_id;
    private Date date;
    private Time time;
    private int sender_id;
    private String subject;
    private String messageBody;
    private JsonNode attachments;

    public ChatEntriesModel(int chatEntry_id, Date date, Time time, int sender_id, String subject, String messageBody, JsonNode attachments) {
        this.chatEntry_id = chatEntry_id;
        this.date = date;
        this.time = time;
        this.sender_id = sender_id;
        this.subject = subject;
        this.messageBody = messageBody;
        this.attachments = attachments;
    }

    public int getChatEntry_id() {
        return chatEntry_id;
    }

    public void setChatEntry_id(int chatEntry_id) {
        this.chatEntry_id = chatEntry_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public JsonNode getAttachments() {
        return attachments;
    }

    public void setAttachments(JsonNode attachments) {
        this.attachments = attachments;
    }
}
