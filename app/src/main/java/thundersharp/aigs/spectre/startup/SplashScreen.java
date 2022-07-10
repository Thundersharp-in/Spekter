package thundersharp.aigs.spectre.startup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import thundersharp.aigs.spectre.BuildConfig;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.ui.activities.home.HomeActivity;
import thundersharp.aigs.spectre.ui.activities.auth.EmailVerificationActivity;
import thundersharp.aigs.spectre.ui.activities.auth.IntroActivity;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseDatabase
                .getInstance()
                .getReference("STARTUP")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            if (snapshot.child("BOOTUP").getValue(Boolean.class) != null && snapshot.child("APP VER").getValue(Integer.class) != null){
                                if (!snapshot.child("BOOTUP").getValue(Boolean.class)){
                                    showRestriction();
                                }else {
                                    int versionCode = BuildConfig.VERSION_CODE;
                                    if (versionCode < snapshot.child("APP VER").getValue(Integer.class)){
                                        showOldVerApk();
                                    }else {
                                        if (FirebaseAuth.getInstance().getCurrentUser() != null){
                                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                                                startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                                            else startActivity(new Intent(SplashScreen.this, EmailVerificationActivity.class));

                                        }else startActivity(new Intent(SplashScreen.this, IntroActivity.class));

                                        finish();
                                    }
                                }
                            }else showContactUs();
                        }else showContactUs();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showContactUs();
                    }
                });

    }

    private void showOldVerApk() {
        new AlertDialog.Builder(this)
                .setMessage("This version of apk is too outdated, please update the app to a newer version to continue.")
                .setCancelable(false)
                .setNegativeButton("CLOSE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .setPositiveButton("UPDATE", (dialogInterface, i) -> {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException ante) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }).show();
    }

    private void showRestriction() {
        new AlertDialog.Builder(this)
                .setMessage("We are currently updating the platform we will be back soon, thanks for your understanding.")
                .setCancelable(false)
                .setNegativeButton("CLOSE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                })
               .show();

    }

    private void showContactUs() {
        new AlertDialog.Builder(this)
                .setMessage("Some internal error has occurred please contact admin to resolve the issue.")
                .setCancelable(false)
                .setNegativeButton("CLOSE", (dialogInterface, i) -> {
                    finish();
                    dialogInterface.dismiss();
                })
                .setPositiveButton("CONTACT", (dialogInterface, i) -> {

                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "app_support@spekteraigs.in"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Not able to start the app.");

                    emailIntent.setType("message/rfc822");
                    try {
                        startActivity(Intent.createChooser(emailIntent,
                                "Send email using..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this,
                                "No email clients installed.",
                                Toast.LENGTH_SHORT).show();
                    }

                }).show();

    }


}