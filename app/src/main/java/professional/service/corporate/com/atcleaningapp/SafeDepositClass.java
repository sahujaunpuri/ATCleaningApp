package professional.service.corporate.com.atcleaningapp;

import java.math.BigDecimal;

class SafeDepositClass {
    public int slNo;
    public int id;
    public int assignee_id;
    public String project_name;
    public String bid_amt;
    public String payee;
    public String description;
    public BigDecimal milestone_amt;
    public String sku;

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String payment_date;

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMilestone_amt() {
        return milestone_amt;
    }

    public void setMilestone_amt(BigDecimal milestone_amt) {
        this.milestone_amt = milestone_amt;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String status;

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getBid_amt() {
        return bid_amt;
    }

    public void setBid_amt(String bid_amt) {
        this.bid_amt = bid_amt;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String user_name;
    public String url;

    public String getSafe_amt() {
        return safe_amt;
    }

    public void setSafe_amt(String safe_amt) {
        this.safe_amt = safe_amt;
    }

    public String safe_amt;
}
