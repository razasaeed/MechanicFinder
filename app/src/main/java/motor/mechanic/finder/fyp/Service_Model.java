package motor.mechanic.finder.fyp;

import com.google.firebase.database.Exclude;

public class Service_Model {
    private String etSerID, etSerName, etSerPrice, etSerDesc, key, etSerVehType;
    int position;

    public Service_Model(){

    }

    public Service_Model(String etSerID, String etSerName, String etSerPrice, String etSerDesc, String etSerVehType){
        this.etSerID = etSerID;
        this.etSerName = etSerName;
        this.etSerPrice = etSerPrice;
        this.etSerDesc = etSerDesc;
        this.etSerVehType = etSerVehType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

// //   public Service_Model (int position){
//        this.position = position;
//    }

    public String getEtSerID() {
        return etSerID;
    }

    public String getEtSerName() {
        return etSerName;
    }

    public String getEtSerPrice() {
        return etSerPrice;
    }

    public String getEtSerDesc() {
        return etSerDesc;
    }

    public void setEtSerID(String etSerID) {
        this.etSerID = etSerID;
    }

    public void setEtSerName(String etSerName) {
        this.etSerName = etSerName;
    }

    public void setEtSerPrice(String etSerPrice) {
        this.etSerPrice = etSerPrice;
    }

    public void setEtSerDesc(String etSerDesc) {
        this.etSerDesc = etSerDesc;
    }

    public String getEtSerVehType() {
        return etSerVehType;
    }

    public void setEtSerVehType(String etSerVehType) {
        this.etSerVehType = etSerVehType;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
