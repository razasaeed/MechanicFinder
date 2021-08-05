package motor.mechanic.finder.fyp;

import com.google.firebase.database.Exclude;

public class Worker_Model {

    private String worker_image, etWorkerName, etWorkerCNIC, etPhoneNo, etWExp;
    private String key;
    private int position;

    public Worker_Model(String worker_image, String etWorkerName, String etWorkerCNIC, String etPhoneNo,String etWExp){
            this.worker_image = worker_image;
            this.etWorkerName = etWorkerName;
            this.etWorkerCNIC= etWorkerCNIC;
            this.etPhoneNo = etPhoneNo;
            this.etWExp = etWExp;
    }

    public Worker_Model() {
       //empty constructor needed
    }

    public Worker_Model (int position){
        this.position = position;
    }

    public String getWorker_image() {
        return worker_image;
    }

    public String getEtWorkerName() {
        return etWorkerName;
    }

    public String getEtWorkerCNIC() {
        return etWorkerCNIC;
    }

    public String getEtPhoneNo() {
        return etPhoneNo;
    }

    public String getEtWExp() {
        return etWExp;
    }

    public void setWorker_image(String worker_image) {
        this.worker_image = worker_image;
    }

    public void setEtWorkerName(String etWorkerName) {
        this.etWorkerName = etWorkerName;
    }

    public void setEtWorkerCNIC(String etWorkerCNIC) {
        this.etWorkerCNIC = etWorkerCNIC;
    }

    public void setEtPhoneNo(String etPhoneNo) {
        this.etPhoneNo = etPhoneNo;
    }

    public void setEtWExp(String etWExp) {
        this.etWExp = etWExp;
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
