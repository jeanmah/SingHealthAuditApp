package com.SHAudit.singHealthAudit.Admin;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("branches")
public class Branch {
    @Id
    private String branch_id;
    private String branch_addr;
    private int no_of_tenants;
    private int no_of_auditors;
}
