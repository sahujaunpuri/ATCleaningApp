package professional.service.corporate.com.atcleaningapp;

import java.io.Serializable;

public class BidsByContractirClass implements Serializable {
    public String job_status;

    public String getBid_status() {
        return bid_status;
    }

    public void setBid_status(String bid_status) {
        this.bid_status = bid_status;
    }

    public String bid_status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String status;

    public String getDate_paid() {
        return date_paid;
    }

    public void setDate_paid(String date_paid) {
        this.date_paid = date_paid;
    }

    public String date_paid;

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_location() {
        return client_location;
    }

    public void setClient_location(String client_location) {
        this.client_location = client_location;
    }

    public String getWork_to_be_done() {
        return work_to_be_done;
    }

    public void setWork_to_be_done(String work_to_be_done) {
        this.work_to_be_done = work_to_be_done;
    }

    public String client_name;
    public String client_location;
    public String work_to_be_done;

    public String getEmployer_name() {
        return employer_name;
    }

    public void setEmployer_name(String employer_name) {
        this.employer_name = employer_name;
    }

    public String employer_name;

    public int getBid_project_id() {
        return bid_project_id;
    }

    public void setBid_project_id(int bid_project_id) {
        this.bid_project_id = bid_project_id;
    }

    public int bid_project_id;

    public int getBid_id() {
        return bid_id;
    }

    public void setBid_id(int bid_id) {
        this.bid_id = bid_id;
    }

    public int bid_id;

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public int slNo;

    public String getProject_status() {
        return project_status;
    }

    public void setProject_status(String project_status) {
        this.project_status = project_status;
    }

    public String project_status;

    public String getJob_status() {
        return job_status;
    }
    String details_inputted;
    String project_id;
    int bidder_id;
    String hirer_id;

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public int getBidder_id() {
        return bidder_id;
    }

    public void setBidder_id(int bidder_id) {
        this.bidder_id = bidder_id;
    }

    public String getHirer_id() {
        return hirer_id;
    }

    public void setHirer_id(String hirer_id) {
        this.hirer_id = hirer_id;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getDetails_inputted() {
        return details_inputted;
    }

    public void setDetails_inputted(String details_inputted) {
        this.details_inputted = details_inputted;
    }

    public String getProposal_inputted() {
        return proposal_inputted;
    }

    public void setProposal_inputted(String proposal_inputted) {
        this.proposal_inputted = proposal_inputted;
    }

    public String getExtra_Services_inputted() {
        return extra_Services_inputted;
    }

    public void setExtra_Services_inputted(String extra_Services_inputted) {
        this.extra_Services_inputted = extra_Services_inputted;
    }

    public String getQuestion_inputted() {
        return question_inputted;
    }

    public void setQuestion_inputted(String question_inputted) {
        this.question_inputted = question_inputted;
    }

    public String getBid_amt_inputted() {
        return bid_amt_inputted;
    }

    public void setBid_amt_inputted(String bid_amt_inputted) {
        this.bid_amt_inputted = bid_amt_inputted;
    }

    public String getBid_text_inputted() {
        return bid_text_inputted;
    }

    public void setBid_text_inputted(String bid_text_inputted) {
        this.bid_text_inputted = bid_text_inputted;
    }

    public String getMilestone_inputted() {
        return milestone_inputted;
    }

    public void setMilestone_inputted(String milestone_inputted) {
        this.milestone_inputted = milestone_inputted;
    }

    String proposal_inputted ;
    String extra_Services_inputted;
    String question_inputted;
    String bid_amt_inputted;
    String bid_text_inputted;
    String milestone_inputted;

}
