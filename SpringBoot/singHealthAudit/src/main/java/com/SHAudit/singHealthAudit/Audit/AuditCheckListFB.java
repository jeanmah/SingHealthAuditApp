package com.SHAudit.singHealthAudit.Audit;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("FBCheckList")
public class AuditCheckListFB {
    @Id
    private int fb_qn_id;
    private String category;
    private double weight;
    private String requirement;
    private String sub_requirement;

    public int getFb_qn_id() {
        return fb_qn_id;
    }

    public void setFb_qn_id(int fb_qn_id) {
        this.fb_qn_id = fb_qn_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getSub_requirement() {
        return sub_requirement;
    }

    public void setSub_requirement(String sub_requirement) {
        this.sub_requirement = sub_requirement;
    }
}
