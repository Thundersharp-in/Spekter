package thundersharp.aigs.spectre.ui.activities.exhibition;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ProjectsHolderAdapter;
import thundersharp.aigs.spectre.core.exceptions.ArgumentsMissingException;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.helpers.ProfileDataSync;
import thundersharp.aigs.spectre.core.interfaces.BasicDataInterface;
import thundersharp.aigs.spectre.core.interfaces.ExhibitionInterface;
import thundersharp.aigs.spectre.core.interfaces.OnProgressChanged;
import thundersharp.aigs.spectre.core.models.ProfileData;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.models.ProjectShortDescription;
import thundersharp.aigs.spectre.core.models.SliderModel;
import thundersharp.aigs.spectre.core.models.TicketsData;
import thundersharp.aigs.spectre.core.progress.BrowseProgress;
import thundersharp.aigs.spectre.core.starters.Tickets;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.core.utils.TimeUtils;
import thundersharp.aigs.spectre.ui.activities.passes.BookPasses;

public class ExhibitionHome extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private SliderLayout slider;
    private AppCompatButton book;
    private NestedScrollView mainContents;
    private RelativeLayout preAnimation;
    private RecyclerView recyclerProjects;
    private List<ProjectShortDescription> projectShortDescriptions;

    private ProjectsHolderAdapter projectsHolderAdapter;
    private BrowseProgress browseProgress;

    private WaveLoadingView waveLoadingView;
    private android.app.AlertDialog alertDialog;

    private ProfileDataSync profileDataSync;
    private ProfileData profileData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_home);

        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        book = findViewById(R.id.book);
        mainContents = findViewById(R.id.mainScroll);
        preAnimation = findViewById(R.id.mainContainer);
        slider = findViewById(R.id.slider);
        waveLoadingView = findViewById(R.id.projectsReviwed);
        waveLoadingView.setCenterTitle(new DecimalFormat("#.##").format(0.00)+" %");

        findViewById(R.id.rrr).setOnClickListener(n -> finish());
        profileData = ProfileDataSync.getInstance(this).initializeLocalStorage().pullDataBack();

        recyclerProjects = findViewById(R.id.recyclerProjects);

        setPreAnimation(true);
        setupCrousel();

        //TODO CHECK FOR EXISTING BOOKING
        findViewById(R.id.notification).setOnClickListener(k -> {
            alertDialog.show();
            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.PASSES)
                    .child(FirebaseAuth.getInstance().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){

                                DatabaseHelpers
                                        .getInstance()
                                        .setBasicDataInterface(new BasicDataInterface() {
                                            @Override
                                            public void dataFetchSuccess(DataSnapshot snapshotBasic) {
                                                try {
                                                    Tickets
                                                            .getInstance(ExhibitionHome.this)
                                                            .setTicketsData(new TicketsData(
                                                                    TimeUtils.getTimeFromTimeStamp(snapshotBasic.child("EVENT_DATE").getValue(String.class)),
                                                                    snapshot.child("SLOT").getValue(String.class),
                                                                    "TECH EXHIBITION",
                                                                    "Spekter is the technical fest of AIGS with over thousands of visitors it provides students with a plethora of opportunities for students to experience the beauty and amalgamations of science and technology.",
                                                                    snapshot.child("NAME").getValue(String.class),
                                                                    snapshot.child("EMAIL").getValue(String.class),
                                                                    snapshot.child("PHONE").getValue(String.class),
                                                                    snapshotBasic.child("VENUE").getValue(String.class),
                                                                    null,
                                                                    snapshot.child("ID").getValue(String.class)))
                                                            .showTickets();
                                                } catch (ArgumentsMissingException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFetchError(Exception e) {
                                                Toast.makeText(ExhibitionHome.this, "Server error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }else {
                                Toast.makeText(ExhibitionHome.this, "No passes issued kindly issue a pass first.", Toast.LENGTH_LONG).show();
                            }

                            alertDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ExhibitionHome.this, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPreAnimation(false);
            }
        }, 4000);

        DatabaseHelpers
                .getInstance()
                .setExhibitionListener(new ExhibitionInterface() {
                    @Override
                    public void onProjectsFetchSuccess(List<ProjectBasicInfo> projectBasicInfoList) {
                        browseProgress.setProjectsCount(projectBasicInfoList.size()).reSyncStorageWithDatabase(projectBasicInfoList);
                        projectsHolderAdapter = new ProjectsHolderAdapter(projectBasicInfoList);
                        recyclerProjects.setLayoutManager(new GridLayoutManager(ExhibitionHome.this, 2));
                        recyclerProjects.setAdapter(projectsHolderAdapter);
                    }

                    @Override
                    public void onProjectsFetchFailure(Exception e) {
                        Toast.makeText(ExhibitionHome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        browseProgress = BrowseProgress
                .getInstance(this)
                .selectStorageInstanceByName(CONSTANTS.EXHIBITION_VISIT_PROGRESS)
                .setOnProgressListener(new OnProgressChanged() {
                    @Override
                    public void onProgressUpdated(double finalPercent, ProjectBasicInfo projectBasicInfo) {
                        Toast.makeText(ExhibitionHome.this, ""+finalPercent, Toast.LENGTH_SHORT).show();
                        waveLoadingView.setProgressValue((int) finalPercent);
                        waveLoadingView.setCenterTitle(new DecimalFormat("#.##").format(finalPercent)+" %");
                    }

                    @Override
                    public void onItemDeletedInReSync(String projectId) {
                        Toast.makeText(ExhibitionHome.this, "Deleted while re sync. ID:"+projectId , Toast.LENGTH_SHORT).show();
                    }
                });


        ((EditText) findViewById(R.id.searchbar))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (projectsHolderAdapter != null) {
                            projectsHolderAdapter.getFilter().filter(charSequence);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

        findViewById(R.id.book).setOnClickListener(b -> startActivity(new Intent(this, BookPasses.class)));

        findViewById(R.id.stallsVisited).setOnClickListener(p -> {
            new AlertDialog.Builder(this)
                    .setTitle("Stalls visited")
                    .setMessage("This progress indicator shows how many projects stalls you have visited physically, this data is calculated by the stalls qr code scans.")
                    .setPositiveButton("OK", ((dialogInterface, i) -> dialogInterface.dismiss()))
                    .show();
        });

        findViewById(R.id.projectsReviwed).setOnClickListener(p -> {
            new AlertDialog.Builder(this)
                    .setTitle("Projects reviewed")
                    .setMessage("This progress indicator shows how many projects you have gone through in the app, this data is calculated by your interaction with the projects in the app.")
                    .setPositiveButton("OK", ((dialogInterface, i) -> dialogInterface.dismiss()))
                    .show();
        });

    }

    private synchronized void setPreAnimation(boolean animation) {
        if (animation) {
            book.setVisibility(View.GONE);
            mainContents.setVisibility(View.GONE);
            preAnimation.setVisibility(View.VISIBLE);
        } else {
            book.setVisibility(View.VISIBLE);
            mainContents.setVisibility(View.VISIBLE);
            preAnimation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    private void setupCrousel() {
        ArrayList<String> listUrl = new ArrayList<>();
        Bundle bundle = new Bundle();

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.SLIDER)
                .child(CONSTANTS.DATABASE_EXHIBITION_SLIDER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                DefaultSliderView sliderView = new DefaultSliderView(ExhibitionHome.this);
                                sliderView
                                        .image(dataSnapshot.child("URL").getValue(String.class))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(ExhibitionHome.this);

                                /* add your extra information */
                                SliderModel model = dataSnapshot.getValue(SliderModel.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.clear();
                                bundle1.putSerializable("data", model);
                                sliderView.bundle(bundle1);

                                slider.addSlider(sliderView);
                            }

                        } else {
                            listUrl.add("https://images.unsplash.com/photo-1582513166998-56ed1ea02d13?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1932&q=80");
                            listUrl.add("https://images.unsplash.com/photo-1507413245164-6160d8298b31?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80");

                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();

                            for (int i = 0; i < listUrl.size(); i++) {
                                DefaultSliderView sliderView = new DefaultSliderView(ExhibitionHome.this);
                                // if you want show image only / without description text use DefaultSliderView instead

                                // initialize SliderLayout
                                sliderView
                                        .image(listUrl.get(i))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(ExhibitionHome.this);

                                //add your extra information
                                sliderView.bundle(bundle);

                                slider.addSlider(sliderView);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        /* slider.addOnPageChangeListener(Content_home.this); */
        slider.stopCyclingWhenTouch(false);
    }

}