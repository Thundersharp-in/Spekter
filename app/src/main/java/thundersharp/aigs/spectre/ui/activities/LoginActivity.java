package thundersharp.aigs.spectre.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.helpers.LoginProvider;
import thundersharp.aigs.spectre.core.interfaces.LoginInterface;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


/*
        LoginProvider
                .getInstance()
                .setCredentials(LoginProvider.Builder.getInstance().setEmail_id("").setPassword(""))
                .attachLoginObserver(new LoginInterface() {

                    @Override
                    public void onLoginSuccess(String provider, Task<AuthResult> authResultTask, boolean verified) {

                    }

                    @Override
                    public void onLoginFailure(Exception exception) {

                    }
                });
*/
    }
}