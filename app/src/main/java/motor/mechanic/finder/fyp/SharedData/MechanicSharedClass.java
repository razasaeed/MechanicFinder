package motor.mechanic.finder.fyp.SharedData;

import android.net.Uri;

import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;

import java.util.ArrayList;
import java.util.List;

public class MechanicSharedClass {

    public static String key, city, email, phone_no, password, fullname, cnic, contact, work_exp, workshop_fullname, workshop_address,
            workshop_email, workshop_phone, num_of_workers, workshop_password = null;
    public static Uri mechanicImg = null;

    public static int selected_mechanic_position = 0;
    public static List<MechanicDataModel> mechanicsData = new ArrayList<>();
    public static List<MechanicDataModel> mechanicsDataTwo = new ArrayList<>();
    public static String mechanicDetailCallCheck = null;

    public static String my_request_title, my_request_detail, product_name, sub_product_name, product_price, sub_product_price, product_pic;

}
