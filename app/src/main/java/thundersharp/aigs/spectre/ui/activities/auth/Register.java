package thundersharp.aigs.spectre.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.utils.Progressbars;

public class Register extends AppCompatActivity {

    private RadioGroup radioGroup;
    private boolean acharya = true;
    private EditText name,email,phone,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        radioGroup = findViewById(R.id.radioGrp);
        name = findViewById(R.id.editText_name);
        email = findViewById(R.id.editText_email);
        phone = findViewById(R.id.editText_auid);
        password = findViewById(R.id.editText_password);

        radioGroup.check(R.id.acharya);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.acharya){
                    acharya = true;
                    email.setHint("Acharya Email");

                }else if (i == R.id.non_acharya){
                    acharya = false;
                    email.setHint("Your Email");
                }

            }
        });

        ((TextView)findViewById(R.id.sign_in_btn))
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