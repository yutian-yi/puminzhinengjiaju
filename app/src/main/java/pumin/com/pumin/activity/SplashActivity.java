package pumin.com.pumin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import pumin.com.pumin.R;
import pumin.com.pumin.untils.SpUtils;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 800);
    }
    class splashhandler implements Runnable {

//        private Intent intent;

        public void run() {
//            if (SpUtils.getString(SplashActivity.this,"Token")!=null) {
//                intent = new Intent(SplashActivity.this, FacilityActivity.class);
//                startActivity(intent);
//            }
            startActivity(new Intent(getApplication(), MainActivity.class));
            SplashActivity.this.finish();
        }

    }
}
