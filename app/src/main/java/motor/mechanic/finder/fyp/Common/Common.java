package motor.mechanic.finder.fyp.Common;

import motor.mechanic.finder.fyp.Retrofit.IGoogleAPI;
import motor.mechanic.finder.fyp.Retrofit.RetrofitClient;

public class Common {
    public static final String baseURL = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleAPI(){

        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }
}
