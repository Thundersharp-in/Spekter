package thundersharp.aigs.spectre.ui.activities.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.InitiativesListner;
import thundersharp.aigs.spectre.core.models.Initiative;
import thundersharp.aigs.spectre.core.models.SliderModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

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
        findViewById(R.id.rrr).setOnClickListener(n->finish());
        findViewById(R.id.notification).setOnClickListener(y->showInfo());

        setPreAnimation(true);

        DatabaseHelpers
                .getInstance()
                .fetchAllInitiatives(new InitiativesListner() {
                    @Override
                    public void OnInitiativesFetched(List<Initiative> initiativeList) {
                        setPreAnimation(false);
                        recyclerView.setAdapter(new InitiativeAdapter(InitiativesHomes.this,initiativeList));
                    }

                    @Override
                    public void OnDataFetchError(Exception e) {
                        Toast.makeText(InitiativesHomes.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        setupCrousel();
    }

    private void showInfo() {
        new AlertDialog.Builder(this)
                .setMessage("")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private List<Initiative> getRandData(){
        return new ArrayList<Initiative>(Arrays.asList(
                new Initiative("Beti bachao Beti Padhao","Pledge","https://google.com","","1589174136000","Initiative started by AIGS",""),
                new Initiative("Save dogs Initiative","External","https://google.com","","158917413877","Initiative started by AIGS","https://content3.jdmagicbox.com/comp/bangalore/h9/080pxx80.xx80.171027163415.c2h9/catalogue/save-animals-bangalore-dog-adoption-centres-y61crzorvt.jpg?clr=333333"),
                new Initiative("Swaksha Bharat initiative","Rights","https://google.com","","1589174136003","Initiative started by AIGS","https://www.theindianwire.com/wp-content/uploads/2019/10/swachh-bharat-abhiyan.jpg"),
                new Initiative("Save Trees Initiative","Pledge","https://google.com","","1589174136078","Initiative started by AIGS","https://cdn.mycrafts.com/i/1/9/31/drawing-tutorial-save-trees-kF8f-o.jpg")));
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