package motor.mechanic.finder.fyp.DataModels;

public class MechanicRequestDataModel {

    private String key, user_id, username, user_location, address, user_phone, request_time, mechanic_email, request_status, request_to;

    public MechanicRequestDataModel() {
    }

    public MechanicRequestDataModel(String key, String user_id, String username, String user_location, String address, String user_phone, String request_time, String mechanic_email, String request_status, String request_to) {
        this.key = key;
        this.user_id = user_id;
        this.username = username;
        this.user_location = user_location;
        this.address = address;
        this.user_phone = user_phone;
        this.request_time = request_time;
        this.mechanic_email = mechanic_email;
        this.request_status = request_status;
        this.request_to = request_to;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getMechanic_email() {
        return mechanic_email;
    }

    public void setMechanic_email(String mechanic_email) {
        this.mechanic_email = mechanic_email;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public String getRequest_to() {
        return request_to;
    }

    public void setRequest_to(String request_to) {
        this.request_to = request_to;
    }
}
