package thundersharp.aigs.spectre.ui.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ViewPagerAdapter;
import thundersharp.aigs.spectre.ui.fragments.knowUs.AboutApp;
import thundersharp.aigs.spectre.ui.fragments.knowUs.AboutSpekter;
import thundersharp.aigs.spectre.ui.fragments.knowUs.Advisors;
import thundersharp.aigs.spectre.ui.fragments.knowUs.Commitee;
import thundersharp.aigs.spectre.ui.fragments.knowUs.Organisers;
import thundersharp.aigs.spectre.ui.fragments.knowUs.Sponsors;

public class KnowUs extends AppCompatActivity {

    private RelativeLayout loader,mainContents;
    private ViewPager viewPager;
    private TabLayout tab_layout;

    CardView aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_us);

        loader = findViewById(R.id.container);
        mainContents = findViewById(R.id.mainContents);
        viewPager = findViewById(R.id.pager);
        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);


        tab_layout.addTab(tab_layout.newTab().setText("About App"));
        tab_layout.addTab(tab_layout.newTab().setText("About Spekter"));
        tab_layout.addTab(tab_layout.newTab().setText("Committee"));
        tab_layout.addTab(tab_layout.newTab().setText("Organisers"));
        tab_layout.addTab(tab_layout.newTab().setText("Sponsors"));
        //tab_layout.addTab(tab_layout.newTab().setText("Advisors"));

        getTabs();

        setPreAnimation(true);

        new Handler().postDelayed(() -> setPreAnimation(false),2000);

    }

    private synchronized void setPreAnimation(boolean animation) {
        if (animation) {
            mainContents.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            mainContents.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }

    private void getTabs(){
        new Handler().post(() -> {
            final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),tab_layout.getTabCount());
            viewPagerAdapter.addFragment(new AboutApp(),"About App");
            viewPagerAdapter.addFragment(new AboutSpekter(),"About Spekter");
            viewPagerAdapter.addFragment(new Commitee(),"Committee");
            viewPagerAdapter.addFragment(new Organisers(),"Organisers");
            viewPagerAdapter.addFragment(new Sponsors(),"Sponsors");
            //viewPagerAdapter.addFragment(new Advisors(),"Advisors");


            viewPager.setAdapter(viewPagerAdapter);
            tab_layout.setupWithViewPager(viewPager);
            tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        });
    }

}