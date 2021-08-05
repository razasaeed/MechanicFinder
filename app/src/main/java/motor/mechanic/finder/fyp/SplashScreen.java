package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.img);
       // imageView.animate().scaleX(0.5f).scaleY(0.5f).setDuration(2000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                if(isLogin){
                Intent intent = new Intent(SplashScreen.this,ChoiceActivity.class);
                startActivity(intent);
//                    finish();
//                }else{
//                    Intent intent = new Intent(SplashScreen.this, Registration.class);
//                    startActivity(intent);
//                    finish();
//                }

            }
        }, 2000);
    }
}
