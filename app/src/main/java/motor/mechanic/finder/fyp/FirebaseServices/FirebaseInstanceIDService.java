package motor.mechanic.finder.fyp.FirebaseServices;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String REG_TOKEN="REG_TOKEN";
    public static String recent_token;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        recent_token = s;
        Log.d(REG_TOKEN, recent_token);
    }

    /*@Override
    public void onTokenRefresh() {
        recent_token= FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, recent_token);
//        registerToken(recent_token);
    }*/

    /*private void registerToken(String recent_token) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", recent_token)
                .build();

        Request request = new Request.Builder()
                .url(SharedClass.registerfb)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
