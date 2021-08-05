package motor.mechanic.finder.fyp;

public class Mech_WorkshopInfo_DataModel {

    String  etAddress, etEmail;

    public Mech_WorkshopInfo_DataModel() {
    }

    public Mech_WorkshopInfo_DataModel(String etAddress,String etEmail) {
        this.etAddress = etAddress;
        this.etEmail = etEmail;

    }

    public String getEtAddress() {
        return etAddress;
    }

    public void setEtAddress(String etAddress) {
        this.etAddress = etAddress;
    }

    public String getEtEmail() {
        return etEmail;
    }

    public void setEtEmail(String etEmail) {
        this.etEmail = etEmail;
    }

}
