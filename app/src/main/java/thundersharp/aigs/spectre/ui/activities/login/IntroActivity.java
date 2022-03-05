package thundersharp.aigs.spectre.ui.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.ui.activities.login.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ((AppCompatButton) findViewById(R.id.getStarted)).setOnClickListener(k->{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}