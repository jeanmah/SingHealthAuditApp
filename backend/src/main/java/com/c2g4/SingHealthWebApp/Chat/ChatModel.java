package com.c2g4.SingHealthWebApp.Chat;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Chat")
public class ChatModel {
    @Id
    private int chat_id;
    private int tenant_id;
    private int auditor_id;
    private JsonNode messages;

    public ChatModel(int chat_id, int tenant_id, int auditor_id, JsonNode messages) {
        this.chat_id = chat_id;
        this.tenant_id = tenant_id;
        this.auditor_id = auditor_id;
        this.messages = messages;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(int tenant_id) {
        this.tenant_id = tenant_id;
    }

    public int getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(int auditor_id) {
        this.auditor_id = auditor_id;
    }

    public JsonNode getMessages() {
        return messages;
    }

    public void setMessages(JsonNode messages) {
        this.messages = messages;
    }
}
