package thundersharp.aigs.spectre.ui.activities.exhibition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.pdfviwer.PdfLoader;
import thundersharp.aigs.pdfviwer.PdfModel;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ParticipantsListAdapter;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.ProjectListner;
import thundersharp.aigs.spectre.core.models.Participants;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.progress.BrowseProgress;
import thundersharp.aigs.spectre.core.progress.BrowseProgressStall;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;


public class ScannerProjectInfo extends AppCompatActivity {

    private TextView category;
    private ProjectBasicInfo projectBasicInfo;
    private AlertDialog alertDialog;

    private SeekBar seekBar;
    private EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_project_info);
        projectBasicInfo = (ProjectBasicInfo) getIntent().getSerializableExtra("projects_basic_info");
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);

        if (projectBasicInfo == null) {
            finish();
            Toast.makeText(this, "Cannot go to the requested page !", Toast.LENGTH_SHORT).show();
        }

        category = findViewById(R.id.by);
        ((TextView) findViewById(R.id.tittle)).setText(projectBasicInfo.NAME);
        ((TextView) findViewById(R.id.short_desc)).setText(projectBasicInfo.SHORT_DESCRIPTION);
        ((TextView) findViewById(R.id.visitTime)).setText("This stall was last visited on "+TimeUtils.getTimeInStringFromTimeStamp(System.currentTimeMillis()+""));

        BrowseProgressStall.getInstance(this).selectStorageInstanceByName(CONSTANTS.STALLS_VISIT_PROGRESS).setPageBrowsed(projectBasicInfo);


        if (projectBasicInfo.TYPE.equalsIgnoreCase("0")) {
            category.setText("Category : IOT");
        } else if (projectBasicInfo.TYPE.equalsIgnoreCase("1")) {
            category.setText("Category : Artificial Intelligence");
        } else if (projectBasicInfo.TYPE.equalsIgnoreCase("2")) {
            category.setText("Category : Cyber Security");
        } else {
            category.setText("Category : Cyber Security");
        }
        loadCover();

        findViewById(R.id.share).setOnClickListener(i->{
            new AlertDialog.Builder(this)
                    .setMessage("Project id :"+projectBasicInfo.ID)
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialogInterface, i12) -> dialogInterface.dismiss()).show();
        });
        findViewById(R.id.team_members).setOnClickListener(n->ShowOtherBottomSheet());
        findViewById(R.id.go_two).setOnClickListener(n->ShowOtherBottomSheet());

        findViewById(R.id.fab).setOnClickListener(n->{
            generateShareLink(projectBasicInfo.ID,projectBasicInfo);
        });
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(r->finish());

        findViewById(R.id.details).setOnClickListener(n->openPdfDetails());
        findViewById(R.id.go_one).setOnClickListener(n->openPdfDetails());

        findViewById(R.id.go_three).setOnClickListener(n->openMakeDetails());

        findViewById(R.id.go_five).setOnClickListener(n->startActivity(new Intent(this,ProjectGallery.class).putExtra("data",projectBasicInfo)));

        findViewById(R.id.go_four).setOnClickListener(t -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_donate);
            seekBar = bottomSheetDialog.findViewById(R.id.seekbar);
            amount = bottomSheetDialog.findViewById(R.id.amount);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    amount.setText("₹" + i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequenceM, int i, int i1, int i2) {
                    String charSequence = charSequenceM.toString();
                    if (charSequence.contains("₹")) {
                        charSequence = charSequence.replace("₹", "");
                    }

                    if (!charSequence.toString().trim().isEmpty()) {
                        try {
                            int num = Integer.parseInt(charSequence.toString());
                            seekBar.setProgress(num);
                            //Toast.makeText(ScannerProjectInfo.this,charSequence.toString(),Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(ScannerProjectInfo.this, "Numbers only !", Toast.LENGTH_SHORT).show();
                            amount.setText(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            bottomSheetDialog.findViewById(R.id.register).setOnClickListener(k->{
                new AlertDialog.Builder(ScannerProjectInfo.this)
                        .setMessage("Online donation option is currently disabled by the admin. If you still wish to donate then you can donate to tis project on the Reception.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setCancelable(false).show();
            });

            bottomSheetDialog.show();
        });

    }

    private void loadCover(){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECT_DETAILS)
                .child(projectBasicInfo.ID)
                .child(CONSTANTS.COVER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<String> data = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                data.add(dataSnapshot.getValue(String.class));
                            }
                            Glide.with(ScannerProjectInfo.this).load(data.get(0)).into((ImageView)findViewById(R.id.topImage) );
                        }else {
                            Toast.makeText(ScannerProjectInfo.this,"Error loading content",Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ScannerProjectInfo.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

    }

    private void openMakeDetails() {
        alertDialog.show();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECT_DETAILS)
                .child(projectBasicInfo.ID)
                .child(CONSTANTS.HOW_TO_MAKE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<String> data = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                data.add(dataSnapshot.getValue(String.class));
                            }
                            PdfLoader
                                    .getInstance(ScannerProjectInfo.this)
                                    .setSecureMode(true)
                                    .setPdfData(new PdfModel("",data.get(0),"Make it yourself","")).loadPdf();
                        }else {
                            Toast.makeText(ScannerProjectInfo.this,"Error loading content",Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ScannerProjectInfo.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

    }

    private void openPdfDetails() {
        alertDialog.show();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECT_DETAILS)
                .child(projectBasicInfo.ID)
                .child(CONSTANTS.PROJECT_DETAILED_DESCRIPTION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<String> data = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                data.add(dataSnapshot.getValue(String.class));
                            }
                            PdfLoader
                                    .getInstance(ScannerProjectInfo.this)
                                    .setSecureMode(true)
                                    .setPdfData(new PdfModel("",data.get(0),"Project in depth","")).loadPdf();
                        }else {
                            Toast.makeText(ScannerProjectInfo.this,"Error loading content",Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ScannerProjectInfo.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
    }

    private void ShowOtherBottomSheet(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.item_participants);

        DatabaseHelpers
                .getInstance()
                .setProjectId(Long.parseLong(projectBasicInfo.ID))
                .setFetchParticipantsListeners(new ProjectListner.fetchParticipants() {
                    @Override
                    public void onParticipantsFetchSuccess(List<Participants> participantsList) {
                        ((RecyclerView)bottomSheetDialog.findViewById(R.id.recycler)).setAdapter(new ParticipantsListAdapter(participantsList));
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(ScannerProjectInfo.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        bottomSheetDialog.show();
    }

    protected synchronized void generateShareLink(String projectId, ProjectBasicInfo projectsInfo) {
        String url = "https://spekteraigs.page.link/projects/?projectId="+projectId+"&type%1";
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix("https://spekteraigs.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey,\n\nLook at this awesome project which is hosted in the Spekter Tech Fest "+
                                    "\n\nProject name : "+projectsInfo.NAME+
                                    "\nDescription : "+projectsInfo.SHORT_DESCRIPTION+
                                    "\nCategory : "+projectsInfo.TYPE+
                                    "\n\nTo view more about this project download the Spekter app or visit the event and scan the stall QR codes."+
                                    "\nProject link : "+shortLink+
                                    "\n\nSpekter App link : https://play.google.com/store/apps/details?id=thundersharp.aigs.spectre");
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        } else {
                            Toast.makeText(ScannerProjectInfo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // Error
                            // ...
                        }
                    }
                });

    }


}