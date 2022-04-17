package thundersharp.aigs.spectre.ui.activities.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.ui.activities.home.HomeActivity;

public class EmailVerificationActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);

        refreshAdminData();

        ((AppCompatButton)findViewById(R.id.getStarted)).setOnClickListener(n->{
            alertDialog.show();
            refreshAdminData();
            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                Toast.makeText(this,"Email verified",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                alertDialog.dismiss();
            }else {
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .sendEmailVerification()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(EmailVerificationActivity.this, "Verification link resent. ", Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(EmailVerificationActivity.this, "Failed to send verification link. ", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        });
            }
        });

        ((ImageView)findViewById(R.id.logout)).setOnClickListener(n->{
            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage("Do you really want to logout !!")
                    .setPositiveButton("YES", (dialogInterface, i) -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(this,IntroActivity.class));
                        finish();
                    })
                    .setNegativeButton("NO",((dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })).show();
        });

        ((AppCompatButton)findViewById(R.id.refresh)).setOnClickListener(b->refreshAdminData());

    }

    private synchronized void refreshAdminData(){
        FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    Toast.makeText(this,"Email verified",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }else Toast.makeText(this, "Email not yet verified", Toast.LENGTH_SHORT).show();
            }
        });
    }
}