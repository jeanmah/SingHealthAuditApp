package com.SHAudit.singHealthAudit.Others;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

@Table("Notifications")
public class Notifications {
    @Id
    private int notification_id;
    private int creator_id;
    private String message;
    private Date create_date;
    private Date receipt_date;
    private String to_role_ids;
}
