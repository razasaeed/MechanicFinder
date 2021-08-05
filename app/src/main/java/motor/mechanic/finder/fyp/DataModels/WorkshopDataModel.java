package motor.mechanic.finder.fyp.DataModels;

public class WorkshopDataModel {

    private String id, workshop_fullname, workshop_address, workshop_email, workshop_password, workshop_phone, num_of_workers,
            workshop_img, online_status, location, address;

    public WorkshopDataModel() {
    }

    public WorkshopDataModel(String id, String workshop_fullname, String workshop_address, String workshop_email,
                             String workshop_password, String workshop_phone, String num_of_workers,
                             String workshop_img, String online_status, String location, String address) {
        this.id = id;
        this.workshop_fullname = workshop_fullname;
        this.workshop_address = workshop_address;
        this.workshop_email = workshop_email;
        this.workshop_password = workshop_password;
        this.workshop_phone = workshop_phone;
        this.num_of_workers = num_of_workers;
        this.workshop_img = workshop_img;
        this.online_status = online_status;
        this.location = location;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkshop_fullname() {
        return workshop_fullname;
    }

    public void setWorkshop_fullname(String workshop_fullname) {
        this.workshop_fullname = workshop_fullname;
    }

    public String getWorkshop_address() {
        return workshop_address;
    }

    public void setWorkshop_address(String workshop_address) {
        this.workshop_address = workshop_address;
    }

    public String getWorkshop_email() {
        return workshop_email;
    }

    public void setWorkshop_email(String workshop_email) {
        this.workshop_email = workshop_email;
    }

    public String getWorkshop_password() {
        return workshop_password;
    }

    public void setWorkshop_password(String workshop_password) {
        this.workshop_password = workshop_password;
    }

    public String getWorkshop_phone() {
        return workshop_phone;
    }

    public void setWorkshop_phone(String workshop_phone) {
        this.workshop_phone = workshop_phone;
    }

    public String getNum_of_workers() {
        return num_of_workers;
    }

    public void setNum_of_workers(String num_of_workers) {
        this.num_of_workers = num_of_workers;
    }

    public String getWorkshop_img() {
        return workshop_img;
    }

    public void setWorkshop_img(String workshop_img) {
        this.workshop_img = workshop_img;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}