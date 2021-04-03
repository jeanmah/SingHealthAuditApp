package com.c2g4.SingHealthWebApp.Chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;
import java.sql.Time;

@Table("ChatEntries")
@AccessType(AccessType.Type.PROPERTY)
public class ChatEntriesModel {
    @Id
    private int chatEntry_id;
    private Date date;
    private Time time;
    private int sender_id;
    private String subject;
    private String messageBody;
    @Transient
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

    @Transient
    public JsonNode getAttachments() {
        return attachments;
    }
    @Transient
    public void setAttachments(JsonNode attachments) {
        this.attachments = attachments;
    }

    @Column("attachments")
    public String get_attachments_for_MySql() {
        ObjectMapper objectmapper = new ObjectMapper();
        try {
            return objectmapper.writeValueAsString(attachments);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Column(value="attachments")
    public void set_attachments_for_MySql(String JsonString) {
        ObjectMapper objectmapper = new ObjectMapper();
        if(JsonString==null){
            this.attachments =objectmapper.createObjectNode();
            return;
        }
        try {
            this.attachments = objectmapper.readTree(JsonString);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
