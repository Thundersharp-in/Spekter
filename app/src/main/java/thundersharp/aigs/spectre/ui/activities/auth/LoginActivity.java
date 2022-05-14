package thundersharp.aigs.spectre.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.helpers.ProfileDataSync;
import thundersharp.aigs.spectre.core.interfaces.ProfileSync;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.core.helpers.LoginProvider;
import thundersharp.aigs.spectre.core.interfaces.LoginInterface;
import thundersharp.aigs.spectre.ui.activities.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email, edit_password;
    private AlertDialog alertDialog;
    private ImageView password_toggle;

    private ProfileDataSync profileDataSync;
    private boolean passwordVis = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        edit_email = findViewById(R.id.editText_email);
        edit_password = findViewById(R.id.editText_password);
        password_toggle = findViewById(R.id.password_toggle);

        profileDataSync = ProfileDataSync.getInstance(this);

        ((AppCompatButton) findViewById(R.id.register)).setOnClickListener(p->{
            startActivity(new Intent(this,Register.class));
            finish();
        });

        ((ImageView)findViewById(R.id.password_toggle)).setOnClickListener(g->{
            if (passwordVis){
                edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_toggle.setImageDrawable(getDrawable(R.drawable.ic_outline_visibility_off_24));
                passwordVis = false;
            }else {
                edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password_toggle.setImageDrawable(getDrawable(R.drawable.ic_outline_remove_red_eye_24));
                passwordVis = true;
            }
        });


        ((ImageView) findViewById(R.id.signIN)).setOnClickListener(t -> {
            if (edit_email.getText().toString().isEmpty()) {
                edit_email.setError("Email id is required!");
                edit_email.requestFocus();
            }  else if (edit_password.getText().toString().isEmpty()) {
                edit_password.setError("Password cannot be empty!");
                edit_password.requestFocus();

            } else {
                alertDialog.show();
                LoginProvider
                        .getInstance()
                        .setCredentials(LoginProvider.Builder.getInstance().setEmail_id(edit_email.getText().toString().trim()).setPassword(edit_password.getText().toString()))
                        .attachLoginObserver(new LoginInterface() {

                            @Override
                            public void onLoginSuccess(String provider, Task<AuthResult> authResultTask, boolean verified) {
                                if (verified)
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                else{
                                    startActivity(new Intent(LoginActivity.this, EmailVerificationActivity.class));
                                    Toast.makeText(LoginActivity.this, "Email not verified, verification link resent !!", Toast.LENGTH_SHORT).show();
                                }


                                profileDataSync
                                        .initializeLocalStorage()
                                        .setUid(FirebaseAuth.getInstance().getUid())
                                        .setProfileSyncListener(new ProfileSync() {
                                            @Override
                                            public void onProfileDataSyncSuccess(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onProfileDataSyncFailure(Exception exception) {

                                            }
                                        });

                                finish();
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

    @Override
    public void onBackPressed() {
        Progressbars.getInstance().displayExitDialog(this);
    }
}