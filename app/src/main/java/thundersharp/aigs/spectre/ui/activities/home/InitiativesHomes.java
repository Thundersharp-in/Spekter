package thundersharp.aigs.spectre.ui.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.InitiativeAdapter;
import thundersharp.aigs.spectre.core.models.SliderModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.activities.exhibition.ExhibitionHome;

public class InitiativesHomes extends AppCompatActivity  implements BaseSliderView.OnSliderClickListener{

    private SliderLayout slider;
    RelativeLayout preAnimation,mainContents;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiatives_homes);

        slider = findViewById(R.id.slider);
        mainContents = findViewById(R.id.container);
        preAnimation  = findViewById(R.id.preAnim);
        recyclerView = findViewById(R.id.recyclerInit);

        setPreAnimation(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPreAnimation(false);
                recyclerView.setAdapter(new InitiativeAdapter(InitiativesHomes.this,getRandData()));
            }
        },3000);

        setupCrousel();
    }

    private List<Object> getRandData(){
        return new ArrayList<Object>(Arrays.asList("","","",""));
    }

    private synchronized void setPreAnimation(boolean animation) {
        if (animation) {
            mainContents.setVisibility(View.GONE);
            preAnimation.setVisibility(View.VISIBLE);
        } else {

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
                .child(CONSTANTS.DATABASE_INITIATIVE_SLIDER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                DefaultSliderView sliderView = new DefaultSliderView(InitiativesHomes.this);
                                sliderView
                                        .image(dataSnapshot.child("URL").getValue(String.class))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(InitiativesHomes.this);

                                /* add your extra information */
                                SliderModel model = dataSnapshot.getValue(SliderModel.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.clear();
                                bundle1.putSerializable("data", model);
                                sliderView.bundle(bundle1);

                                slider.addSlider(sliderView);
                            }

                        } else {
                            listUrl.add("https://www.oie.int/app/uploads/2020/07/asfinitiative-sm-10.png");
                            listUrl.add("https://shawee.io/wp-content/uploads/2019/06/Ac%CC%A7o%CC%83es-para-Mulheres-1.png");

                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();

                            for (int i = 0; i < listUrl.size(); i++) {
                                DefaultSliderView sliderView = new DefaultSliderView(InitiativesHomes.this);
                                // if you want show image only / without description text use DefaultSliderView instead

                                // initialize SliderLayout
                                sliderView
                                        .image(listUrl.get(i))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(InitiativesHomes.this);

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