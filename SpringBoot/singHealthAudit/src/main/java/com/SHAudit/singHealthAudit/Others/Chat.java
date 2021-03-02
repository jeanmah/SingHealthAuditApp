package com.SHAudit.singHealthAudit.Others;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Chat")
public class Chat {
    @Id
    private int chat_id;
    private int tenant_id;
    private int auditor_id;
    private String messages;
}
