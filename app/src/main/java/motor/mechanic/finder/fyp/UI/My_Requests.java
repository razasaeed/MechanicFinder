package motor.mechanic.finder.fyp.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import motor.mechanic.finder.fyp.Adapters.RequestsAdapter;
import motor.mechanic.finder.fyp.Adapters.ViewPagerAdapter;
import motor.mechanic.finder.fyp.DataModels.MechanicRequestDataModel;
import motor.mechanic.finder.fyp.Fragments.My_Mechanics_Requests_Fragment;
import motor.mechanic.finder.fyp.Fragments.My_Workshop_Requests_Fragment;
import motor.mechanic.finder.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class My_Requests extends AppCompatActivity {

    ViewPagerAdapter adapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    My_Mechanics_Requests_Fragment myMechanicsRequestsFragment;
    My_Workshop_Requests_Fragment myWorkshopRequestsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_requests);
//        activity_myworkshop_requests
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        myMechanicsRequestsFragment = new My_Mechanics_Requests_Fragment();
        myWorkshopRequestsFragment = new My_Workshop_Requests_Fragment();
        adapter.addFragment(myMechanicsRequestsFragment, "Mechanics");
        adapter.addFragment(myWorkshopRequestsFragment, "Workshops");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new My_Mechanics_Requests_Fragment();
                case 1:
                    return new My_Workshop_Requests_Fragment();
                default:
                    return new My_Mechanics_Requests_Fragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Mechanics";
                case 1:
                    return "Workshops";

            }
            return null;
        }
    }

}
