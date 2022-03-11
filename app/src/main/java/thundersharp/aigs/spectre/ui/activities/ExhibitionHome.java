package thundersharp.aigs.spectre.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestOptions;
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
import thundersharp.aigs.spectre.core.adapters.ProjectsHolderAdapter;
import thundersharp.aigs.spectre.core.exceptions.ArgumentsMissingException;
import thundersharp.aigs.spectre.core.models.ProjectShortDescription;
import thundersharp.aigs.spectre.core.models.SliderModel;
import thundersharp.aigs.spectre.core.models.TicketsData;
import thundersharp.aigs.spectre.core.starters.Tickets;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.fragments.home.HomeFragment;

public class ExhibitionHome extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private SliderLayout slider;
    private AppCompatButton book;
    private NestedScrollView mainContents;
    private RelativeLayout preAnimation;
    private RecyclerView recyclerProjects;
    private List<ProjectShortDescription> projectShortDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_home);

        book = findViewById(R.id.book);
        mainContents = findViewById(R.id.mainScroll);
        preAnimation = findViewById(R.id.mainContainer);
        slider = findViewById(R.id.slider);

        recyclerProjects = findViewById(R.id.recyclerProjects);

        setPreAnimation(true);
        setupCrousel();

        //TODO CHECK FOR EXISTING BOOKING
        ((ImageView)findViewById(R.id.notification)).setOnClickListener(k-> {
            try {
                Tickets
                        .getInstance(this)
                        .setTicketsData(new TicketsData("","","","","","","","",null,""))
                        .showTickets();
            } catch (ArgumentsMissingException e) {
                e.printStackTrace();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPreAnimation(false);
            }
        },4000);

        recyclerProjects.setLayoutManager(new GridLayoutManager(this,2));
        recyclerProjects.setAdapter(new ProjectsHolderAdapter(getProjectShortDescriptions(10)));

    }

    private List<ProjectShortDescription> getProjectShortDescriptions(int size){
        projectShortDescriptions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            projectShortDescriptions.add(new ProjectShortDescription("Hey "+i,"",""+i));
        }

        return projectShortDescriptions;
    }

    private synchronized void setPreAnimation(boolean animation){
        if (animation){
            book.setVisibility(View.GONE);
            mainContents.setVisibility(View.GONE);
            preAnimation.setVisibility(View.VISIBLE);
        }else {
            book.setVisibility(View.VISIBLE);
            mainContents.setVisibility(View.VISIBLE);
            preAnimation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }



    private void setupCrousel(){
        ArrayList<String> listUrl = new ArrayList<>();
        Bundle bundle = new Bundle();

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_EXHIBITION_SLIDER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();
                            for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                                DefaultSliderView sliderView = new DefaultSliderView(ExhibitionHome.this);
                                sliderView
                                        .image(dataSnapshot.child("URL").getValue(String.class))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(ExhibitionHome.this);

                                //add your extra information
                                SliderModel model = dataSnapshot.getValue(SliderModel.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.clear();
                                bundle1.putSerializable("data", model);
                                //bundle1.putInt("PAGE",dataSnapshot.child("PAGE").getValue(Integer.class));
                                sliderView.bundle(bundle1);

                                slider.addSlider(sliderView);
                            }

                        }else {
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
        //slider.addOnPageChangeListener(Content_home.this);
        slider.stopCyclingWhenTouch(false);
    }

}