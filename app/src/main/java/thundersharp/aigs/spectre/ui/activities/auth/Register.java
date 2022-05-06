package thundersharp.aigs.spectre.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.helpers.LoginProvider;
import thundersharp.aigs.spectre.core.interfaces.RegisterPresenter;
import thundersharp.aigs.spectre.core.utils.Progressbars;

public class Register extends AppCompatActivity {

    private RadioGroup radioGroup;
    private boolean acharya = true;
    private EditText name,email,phone,password;
    boolean passwordVis = false;

    private ImageView password_toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AlertDialog alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        radioGroup = findViewById(R.id.radioGrp);
        name = findViewById(R.id.editText_name);
        email = findViewById(R.id.editText_email);
        phone = findViewById(R.id.editText_auid);
        password = findViewById(R.id.editText_password);

        password_toggle = findViewById(R.id.password_toggle);

        radioGroup.check(R.id.acharya);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.acharya){
                acharya = true;
                email.setHint("Acharya Email");

            }else if (i == R.id.non_acharya){
                acharya = false;
                email.setHint("Your Email");
            }

        });

        ((ImageView)findViewById(R.id.password_toggle)).setOnClickListener(g->{
            if (passwordVis){
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_toggle.setImageDrawable(getDrawable(R.drawable.ic_outline_visibility_off_24));
                passwordVis = false;
            }else {
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password_toggle.setImageDrawable(getDrawable(R.drawable.ic_outline_remove_red_eye_24));
                passwordVis = true;
            }
        });

        ((ImageView) findViewById(R.id.signIN))
                .setOnClickListener(n->{
                    if (acharya){
                        if (!email.getText().toString().trim().endsWith("@acharya.ac.in")) {
                            email.setError("Sorry only students of acharya can enroll currently from here you can create your account as 'Non-acharyan' :)");
                            email.requestFocus();
                        }else if (name.getText().toString().isEmpty()){
                            name.setError("Name cannot be empty!");
                            name.requestFocus();
                        }else if (email.getText().toString().isEmpty()){
                            email.setError("Email id cannot be empty!");
                            email.requestFocus();
                        }else if (password.getText().toString().isEmpty()){
                            password.setError("Password cannot be empty!");
                            password.requestFocus();
                        }else if (phone.getText().toString().isEmpty() || phone.getText().toString().length() != 10){
                            phone.setError("Invalid phone number! ");
                            phone.requestFocus();
                        }else{
                            alertDialog.show();
                            LoginProvider
                                    .getInstance()
                                    .registerUser(LoginProvider
                                            .RegistrationDataBuilder
                                            .getInstance()
                                            .setEmail(email.getText().toString())
                                            .setName(name.getText().toString())
                                            .setPhone(phone.getText().toString())
                                            .setAcharyan(acharya)
                                            .setPassWord(password.getText().toString()))
                                    .attachRegistrationObserver(new RegisterPresenter() {
                                        @Override
                                        public void onRegisterSuccess(Task<AuthResult> authResultTask, boolean verificationLinkSent) {
                                            if (verificationLinkSent)
                                                Toast.makeText(Register.this, "Verification link sent", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this,EmailVerificationActivity.class));
                                            finish();
                                            alertDialog.dismiss();
                                        }

                                        @Override
                                        public void onRegistrationFailure(Exception e) {
                                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    });
                        }

                    }else {

                        if (email.getText().toString().trim().endsWith("@acharya.ac.in")) {
                            email.setError("Hey acharyan please select 'Acharyan' radio button below.");
                            email.requestFocus();
                        }else if (name.getText().toString().isEmpty()){
                            name.setError("Name cannot be empty!");
                            name.requestFocus();
                        }else if (email.getText().toString().isEmpty()){
                            email.setError("Email id cannot be empty!");
                            email.requestFocus();
                        }else if (password.getText().toString().isEmpty()){
                            password.setError("Password cannot be empty!");
                            password.requestFocus();
                        }else if (phone.getText().toString().isEmpty() || phone.getText().toString().length() != 10) {
                            phone.setError("Invalid phone number!");
                            phone.requestFocus();
                        }else {
                            alertDialog.show();
                            LoginProvider
                                    .getInstance()
                                    .registerUser(LoginProvider
                                            .RegistrationDataBuilder
                                            .getInstance()
                                            .setEmail(email.getText().toString())
                                            .setName(name.getText().toString())
                                            .setPhone(phone.getText().toString())
                                            .setAcharyan(acharya)
                                            .setPassWord(password.getText().toString()))
                                    .attachRegistrationObserver(new RegisterPresenter() {
                                        @Override
                                        public void onRegisterSuccess(Task<AuthResult> authResultTask, boolean verificationLinkSent) {
                                            if (verificationLinkSent)
                                                Toast.makeText(Register.this, "Verification link sent", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this,EmailVerificationActivity.class));
                                            finish();
                                            alertDialog.dismiss();
                                        }

                                        @Override
                                        public void onRegistrationFailure(Exception e) {
                                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    });

                        }
                    }
        });

        ((AppCompatButton)findViewById(R.id.sign_in_btn))
                .setOnClickListener(n->{
                    startActivity(new Intent(this,LoginActivity.class));
                    finish();
                });
    }

    @Override
    public void onBackPressed() {
        Progressbars.getInstance().displayExitDialog(this);
    }
}