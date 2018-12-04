package professional.service.corporate.com.atcleaningapp;

public class LoginDBClass {
    private String id;

    public String getExpiry() {
        return expiry;
    }

    private String expiry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void  setExpiry(String expiry)
    {
        this.expiry = expiry;
    }
}
