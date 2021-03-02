package com.SHAudit.singHealthAudit.Admin.Manager;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("managers")
public class Manager {
    @Id
    private int acc_id;
    private String branch_id;
}
