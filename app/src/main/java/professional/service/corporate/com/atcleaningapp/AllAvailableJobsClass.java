package professional.service.corporate.com.atcleaningapp;

import java.io.Serializable;

class AllAvailableJobsClass implements Serializable {
    int id;
    String project_name;
    String post_date;
    String title;
    String description;
    public int assignee_id;
    public int project_id;
    public String latitude;
    public String min;
    public String user_name;
    public String contractor_status;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContractor_status() {
        return contractor_status;
    }

    public void setContractor_status(String contractor_status) {
        this.contractor_status = contractor_status;
    }

    public String getAssigned_date() {
        return assigned_date;
    }

    public void setAssigned_date(String assigned_date) {
        this.assigned_date = assigned_date;
    }

    public String assigned_date;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String dist;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String longitude;
    public String url;

    public String getBid_count() {
        return bid_count;
    }

    public void setBid_count(String bid_count) {
        this.bid_count = bid_count;
    }

    public String bid_count;

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String completion;

    public int getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getHirer_id() {
        return hirer_id;
    }

    public void setHirer_id(int hirer_id) {
        this.hirer_id = hirer_id;
    }

    public int hirer_id;


    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String bid;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String availability;

    public String getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(String completion_date) {
        this.completion_date = completion_date;
    }

    public String completion_date;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String state;

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

    public int slNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSoil_level() {
        return soil_level;
    }

    public void setSoil_level(String soil_level) {
        this.soil_level = soil_level;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getSize_of_rooms() {
        return size_of_rooms;
    }

    public void setSize_of_rooms(String size_of_rooms) {
        this.size_of_rooms = size_of_rooms;
    }

    String soil_level;
    String bathrooms;
    String size_of_rooms;

}
