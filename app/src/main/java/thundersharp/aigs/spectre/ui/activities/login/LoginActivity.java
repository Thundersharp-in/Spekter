package thundersharp.aigs.spectre.ui.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.Progressbars;
import thundersharp.aigs.spectre.core.helpers.LoginProvider;
import thundersharp.aigs.spectre.core.interfaces.LoginInterface;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email,edit_password;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        edit_email = findViewById(R.id.editText_email);
        edit_password = findViewById(R.id.editText_password);

        ((AppCompatButton)findViewById(R.id.signIN)).setOnClickListener(t->{
            if (edit_email.getText().toString().isEmpty()){
                edit_email.setError("Email id is required!");
                edit_email.requestFocus();
            }else if (!edit_email.getText().toString().trim().endsWith("@acharya.ac.in")){
                edit_email.setError("Sorry only students of acharya can enroll currently :(");
                edit_email.requestFocus();

            }else if(edit_password.getText().toString().isEmpty()){
                edit_password.setError("Password cannot be empty!");
                edit_password.requestFocus();

            }else {
                alertDialog.show();
                LoginProvider
                        .getInstance()
                        .setCredentials(LoginProvider.Builder.getInstance().setEmail_id(edit_email.getText().toString()).setPassword(edit_password.getText().toString()))
                        .attachLoginObserver(new LoginInterface() {

                            @Override
                            public void onLoginSuccess(String provider, Task<AuthResult> authResultTask, boolean verified) {

                                alertDialog.dismiss();
                            }

                            @Override
                            public void onLoginFailure(Exception exception) {
                                Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
            }
        });

    }
}