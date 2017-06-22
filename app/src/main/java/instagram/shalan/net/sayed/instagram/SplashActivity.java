package instagram.shalan.net.sayed.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Handler;

public class SplashActivity extends AppCompatActivity {
    boolean active = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread thread= new Thread(){
            @Override
            public void run() {
                super.run();
                int wait=1;
                while (active&&wait<3000)
                {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
                    }
                    if(active)
                        wait+=100;
                }
                Intent i = new Intent(SplashActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        };
        thread.start();
    }
}
