package thundersharp.aigs.spectre.startup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.ui.activities.HomeActivity;
import thundersharp.aigs.spectre.ui.activities.auth.EmailVerificationActivity;
import thundersharp.aigs.spectre.ui.activities.auth.IntroActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    else startActivity(new Intent(SplashScreen.this, EmailVerificationActivity.class));

                }else startActivity(new Intent(SplashScreen.this, IntroActivity.class));

                finish();
            }
        },2000);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}