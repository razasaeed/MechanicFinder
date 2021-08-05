package motor.mechanic.finder.fyp.DataModels;

public class WorkshopRequestDataModel {

    private String key, user_id, username, user_location, address, user_phone, request_time, mechanic_email, request_status,
            request_to, main_service_name, main_service_price, main_service_pic, sub_service_name, sub_service_price, emp_name,
            emp_pic, emp_cnic, emp_contact, emp_location, emp_address, workshop_name, workshop_address, workshop_pic, workshop_email,
            workshop_phone, completion_status;

    public WorkshopRequestDataModel() {
    }

    public WorkshopRequestDataModel(String key, String user_id, String username, String user_location, String address,
                                    String user_phone, String request_time, String mechanic_email, String request_status,
                                    String request_to, String main_service_name, String main_service_price, String main_service_pic,
                                    String sub_service_name, String sub_service_price, String emp_name, String emp_pic,
                                    String emp_cnic, String emp_contact, String emp_location, String emp_address,
                                    String workshop_name, String workshop_address, String workshop_pic, String workshop_email,
                                    String workshop_phone, String completion_status) {
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
        this.main_service_name = main_service_name;
        this.main_service_price = main_service_price;
        this.main_service_pic = main_service_pic;
        this.sub_service_name = sub_service_name;
        this.sub_service_price = sub_service_price;
        this.emp_name = emp_name;
        this.emp_pic = emp_pic;
        this.emp_cnic = emp_cnic;
        this.emp_contact = emp_contact;
        this.emp_location = emp_location;
        this.emp_address = emp_address;
        this.workshop_name = workshop_name;
        this.workshop_address = workshop_address;
        this.workshop_pic = workshop_pic;
        this.workshop_email = workshop_email;
        this.workshop_phone = workshop_phone;
        this.completion_status = completion_status;
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

    public String getMain_service_name() {
        return main_service_name;
    }

    public void setMain_service_name(String main_service_name) {
        this.main_service_name = main_service_name;
    }

    public String getMain_service_price() {
        return main_service_price;
    }

    public void setMain_service_price(String main_service_price) {
        this.main_service_price = main_service_price;
    }

    public String getMain_service_pic() {
        return main_service_pic;
    }

    public void setMain_service_pic(String main_service_pic) {
        this.main_service_pic = main_service_pic;
    }

    public String getSub_service_name() {
        return sub_service_name;
    }

    public void setSub_service_name(String sub_service_name) {
        this.sub_service_name = sub_service_name;
    }

    public String getSub_service_price() {
        return sub_service_price;
    }

    public void setSub_service_price(String sub_service_price) {
        this.sub_service_price = sub_service_price;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_pic() {
        return emp_pic;
    }

    public void setEmp_pic(String emp_pic) {
        this.emp_pic = emp_pic;
    }

    public String getEmp_cnic() {
        return emp_cnic;
    }

    public void setEmp_cnic(String emp_cnic) {
        this.emp_cnic = emp_cnic;
    }

    public String getEmp_contact() {
        return emp_contact;
    }

    public void setEmp_contact(String emp_contact) {
        this.emp_contact = emp_contact;
    }

    public String getEmp_location() {
        return emp_location;
    }

    public void setEmp_location(String emp_location) {
        this.emp_location = emp_location;
    }

    public String getEmp_address() {
        return emp_address;
    }

    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    public String getWorkshop_name() {
        return workshop_name;
    }

    public void setWorkshop_name(String workshop_name) {
        this.workshop_name = workshop_name;
    }

    public String getWorkshop_address() {
        return workshop_address;
    }

    public void setWorkshop_address(String workshop_address) {
        this.workshop_address = workshop_address;
    }

    public String getWorkshop_pic() {
        return workshop_pic;
    }

    public void setWorkshop_pic(String workshop_pic) {
        this.workshop_pic = workshop_pic;
    }

    public String getWorkshop_email() {
        return workshop_email;
    }

    public void setWorkshop_email(String workshop_email) {
        this.workshop_email = workshop_email;
    }

    public String getWorkshop_phone() {
        return workshop_phone;
    }

    public void setWorkshop_phone(String workshop_phone) {
        this.workshop_phone = workshop_phone;
    }

    public String getCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(String completion_status) {
        this.completion_status = completion_status;
    }
}
