package thundersharp.aigs.spectre.ui.activities.lectures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ServiceTopicAdapter;
import thundersharp.aigs.spectre.core.annonations.ServiceType;
import thundersharp.aigs.spectre.core.exceptions.DataRenderError;
import thundersharp.aigs.spectre.core.models.ServiceTopicModel;
import thundersharp.aigs.spectre.core.models.SliderModel;
import thundersharp.aigs.spectre.core.serviceLoader.PaidService;
import thundersharp.aigs.spectre.core.serviceLoader.ServiceLoader;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class LecturesHome extends AppCompatActivity implements  BaseSliderView.OnSliderClickListener {

    private NestedScrollView mainContents;
    private RelativeLayout preAnimation;
    private SliderLayout slider;
    private LinearLayout service_l;
    private RecyclerView rv_LT, rv_LLT, rv_FC;
    private TextView see_all_c, seeAllLT, seeAllFC;
    private ShimmerFrameLayout shimmer_lt, shimmer_FC, shimmer_LLT;

    private SharedPreferences service_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures_home);

        service_close = getSharedPreferences("service_close", Context.MODE_PRIVATE);

        mainContents = findViewById(R.id.mainScroll);
        preAnimation = findViewById(R.id.mainContainer);
        slider = findViewById(R.id.slider);
        service_l = findViewById(R.id.service_l);

        findViewById(R.id.rrr).setOnClickListener(t->finish());
        findViewById(R.id.notification).setOnClickListener(t->new AlertDialog.Builder(this).setMessage("Only available for students of Acharya institutes at free of cost.").setPositiveButton("UNDERSTOOD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show());

        findViewById(R.id.purchased).setOnClickListener(o->startActivity(new Intent(this,SubsribedLectures.class)));

        rv_LT = findViewById(R.id.rv_LT);
        rv_LLT = findViewById(R.id.rv_LLT);
        rv_FC = findViewById(R.id.rv_FC);

        see_all_c = findViewById(R.id.see_all_c);
        seeAllLT = findViewById(R.id.seeAllLT);
        seeAllFC = findViewById(R.id.seeAllFC);

        shimmer_lt = findViewById(R.id.shimmer_lt);
        shimmer_FC = findViewById(R.id.shimmer_FC);
        shimmer_LLT = findViewById(R.id.shimmer_LLT);

        setPreAnimation(true);
        setupCrousel();

        findViewById(R.id.close).setOnClickListener(v -> {
            SharedPreferences.Editor editor = service_close.edit();
            editor.putBoolean("tab", true);
            editor.apply();
            service_l.setVisibility(View.GONE);
        });


        //new Handler().postDelayed(() -> ,4000);

        see_all_c.setOnClickListener(v -> startActivity(new Intent(this, ServiceMain.class).putExtra("pos", 0)));
        seeAllLT.setOnClickListener(v -> startActivity(new Intent(this, ServiceMain.class).putExtra("pos", 1)));
        seeAllFC.setOnClickListener(v -> startActivity(new Intent(this, ServiceMain.class).putExtra("pos", 2)));

        loadData();

    }

    private void loadData() {

        shimmer_lt.startShimmer();
        shimmer_lt.setVisibility(View.VISIBLE);
        shimmer_FC.startShimmer();
        shimmer_FC.setVisibility(View.VISIBLE);
        shimmer_LLT.startShimmer();
        shimmer_LLT.setVisibility(View.VISIBLE);

        ServiceTopicAdapter adapter = new ServiceTopicAdapter(new ArrayList<>(), this, ServiceType.TYPE_NORMAL_COURSE);
        rv_LT.setLayoutManager(new GridLayoutManager(this, 2));
        rv_LT.setAdapter(adapter);
        ServiceTopicAdapter adapter1 = new ServiceTopicAdapter(new ArrayList<>(), this, ServiceType.TYPE_LIVE_COURSE);
        rv_LLT.setLayoutManager(new GridLayoutManager(this, 2));
        rv_LLT.setAdapter(adapter1);
        ServiceTopicAdapter adapter2 = new ServiceTopicAdapter(new ArrayList<>(), this, ServiceType.TYPE_FREE_COURSE);
        rv_FC.setLayoutManager(new GridLayoutManager(this, 2));
        rv_FC.setAdapter(adapter2);

        PaidService
                .getInstance(this)
                .setDataCallbackCount(4)
                .addDataWatcher(new ServiceLoader.OnServiceLoaded() {
                    @Override
                    public void courseLoaded(List<ServiceTopicModel> topicModels) {

                        shimmer_lt.stopShimmer();
                        shimmer_lt.setVisibility(View.GONE);

                        ServiceTopicAdapter adapter = new ServiceTopicAdapter(topicModels, LecturesHome.this, ServiceType.TYPE_NORMAL_COURSE);
                        rv_LT.setLayoutManager(new GridLayoutManager(LecturesHome.this, 2));
                        rv_LT.setAdapter(adapter);
                        rv_LT.setHasFixedSize(true);
                        setPreAnimation(false);
                    }

                    @Override
                    public void liveCoursesLoaded(List<ServiceTopicModel> topicModels) {

                        shimmer_LLT.stopShimmer();
                        shimmer_LLT.setVisibility(View.GONE);

                        ServiceTopicAdapter adapter = new ServiceTopicAdapter(topicModels, LecturesHome.this, ServiceType.TYPE_LIVE_COURSE);
                        rv_LLT.setLayoutManager(new GridLayoutManager(LecturesHome.this, 2));
                        rv_LLT.setAdapter(adapter);
                        rv_LLT.setHasFixedSize(true);
                        setPreAnimation(false);
                    }

                    @Override
                    public void freeCourcesLoaded(List<ServiceTopicModel> topicModels) {

                        shimmer_FC.stopShimmer();
                        shimmer_FC.setVisibility(View.GONE);

                        ServiceTopicAdapter adapter = new ServiceTopicAdapter(topicModels, LecturesHome.this, ServiceType.TYPE_FREE_COURSE);
                        rv_FC.setLayoutManager(new GridLayoutManager(LecturesHome.this, 2));
                        rv_FC.setAdapter(adapter);
                        rv_FC.setHasFixedSize(true);
                        setPreAnimation(false);

                    }

                    @Override
                    public void dataRenderException(int type, DataRenderError dataRenderError) {
                        if (type == 0){

                            shimmer_lt.stopShimmer();
                            shimmer_lt.setVisibility(View.GONE);

                            ServiceTopicAdapter adapter = new ServiceTopicAdapter(new ArrayList<>(), LecturesHome.this, ServiceType.TYPE_NORMAL_COURSE);
                            rv_LT.setLayoutManager(new GridLayoutManager(LecturesHome.this, 2));
                            rv_LT.setAdapter(adapter);
                            rv_LT.setHasFixedSize(true);
                        }
                        if (type == 1){

                            shimmer_LLT.stopShimmer();
                            shimmer_LLT.setVisibility(View.GONE);

                            ServiceTopicAdapter adapter = new ServiceTopicAdapter(new ArrayList<>(), LecturesHome.this, ServiceType.TYPE_LIVE_COURSE);
                            rv_LLT.setLayoutManager(new GridLayoutManager(LecturesHome.this, 2));
                            rv_LLT.setAdapter(adapter);
                            rv_LLT.setHasFixedSize(true);
                        }
                        if (type == 2){

                            shimmer_FC.stopShimmer();
                            shimmer_FC.setVisibility(View.GONE);

                            ServiceTopicAdapter adapter = new ServiceTopicAdapter(new ArrayList<>(), LecturesHome.this, ServiceType.TYPE_FREE_COURSE);
                            rv_FC.setLayoutManager(new GridLayoutManager(LecturesHome.this, 2));
                            rv_FC.setAdapter(adapter);
                            rv_FC.setHasFixedSize(true);
                        }
                        Toast.makeText(LecturesHome.this, "" + dataRenderError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private synchronized void setPreAnimation(boolean animation){
        if (animation){
            mainContents.setVisibility(View.GONE);
            preAnimation.setVisibility(View.VISIBLE);
            service_l.setVisibility(View.GONE);
        }else {
            mainContents.setVisibility(View.VISIBLE);
            preAnimation.setVisibility(View.GONE);
            service_l.setVisibility(View.VISIBLE);
        }
    }

    private void setupCrousel(){
        ArrayList<String> listUrl = new ArrayList<>();
        Bundle bundle = new Bundle();

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.SLIDER)
                .child(CONSTANTS.DATABASE_COURSES_SLIDER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();
                            for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                                DefaultSliderView sliderView = new DefaultSliderView(LecturesHome.this);
                                sliderView
                                        .image(dataSnapshot.child("URL").getValue(String.class))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(LecturesHome.this);

                                //add your extra information
                                //SliderModel model = dataSnapshot.getValue(SliderModel.class);
                                //Bundle bundle1 = new Bundle();
                                //bundle1.clear();
                               // bundle1.putSerializable("data", model);
                                //bundle1.putInt("PAGE",dataSnapshot.child("PAGE").getValue(Integer.class));
                                sliderView.bundle(bundle);

                                slider.addSlider(sliderView);
                            }

                        }else {
                            listUrl.add("https://media.istockphoto.com/photos/online-education-concept-picture-id636332456?k=20&m=636332456&s=612x612&w=0&h=ckwh8_U-ET3QSyJUAaceejX6WGdljVy-E87PWO-Sok8=");
                            listUrl.add("https://www.google.com/url?sa=i&url=https%3A%2F%2Fmedium.com%2F%40adiyagil%2Fbest-machine-learning-online-courses-top-5-options-c167dbb8ad45&psig=AOvVaw1oI7LUAa6nksqVdwlhi77B&ust=1648407327341000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCKDS3_-55PYCFQAAAAAdAAAAABAQ");

                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();

                            for (int i = 0; i < listUrl.size(); i++) {
                                DefaultSliderView sliderView = new DefaultSliderView(LecturesHome.this);
                                // if you want show image only / without description text use DefaultSliderView instead

                                // initialize SliderLayout
                                sliderView
                                        .image(listUrl.get(i))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(LecturesHome.this);

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
        //slider.addOnPageChangeListener(Content_home.this);
        slider.stopCyclingWhenTouch(false);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}