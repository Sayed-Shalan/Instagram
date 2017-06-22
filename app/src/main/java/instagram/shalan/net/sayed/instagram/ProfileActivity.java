package instagram.shalan.net.sayed.instagram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import instagram.shalan.net.sayed.instagram.adapters.ViewPagerAdapter;
import instagram.shalan.net.sayed.instagram.fragments.CameraFragment;
import instagram.shalan.net.sayed.instagram.fragments.SearchFragment;
import instagram.shalan.net.sayed.instagram.fragments.GridViewFragment;
import instagram.shalan.net.sayed.instagram.fragments.HomeFragment;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;

public class ProfileActivity extends AppCompatActivity {
    ImageView pp;
    TextView person_name;
    TextView person_state;
    Button editBtn;
    public static Activity fa;
    MysharedPreferences mysharedPreferences;
    private TabLayout mTabLayout ;
    private ViewPager mViewPager ;
    int id;
    FloatingActionButton logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fa=this;
        ////////////////////////////////////////////////////////////////
        pp=(ImageView) findViewById(R.id.ppImg);
        person_name=(TextView)findViewById(R.id.personname);
        person_state=(TextView)findViewById(R.id.personstate);
        editBtn=(Button)findViewById(R.id.editProfile) ;
        logoutBtn =(FloatingActionButton)findViewById(R.id.logoutBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,EditProfile.class);
                startActivity(i);
            }
        });
        ///////////////////////////////////////////////////////////////
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MysharedPreferences(getApplicationContext()).deleteUser();
                Intent i = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        ///////////////////////////////////////////////////////////////
        mysharedPreferences=new MysharedPreferences(getApplicationContext());
        if(mysharedPreferences.getUser().getEmail()=="")
        {
            Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else {
///////////////////////////////////////////////////////////////////////////////
            mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
            mViewPager = (ViewPager) findViewById(R.id.viewPager);
            setUpTabs();
            setUpTabsOnSelect();
///////////////////////////////////////////////////////////////////////////////


           person_name.setText(mysharedPreferences.getUser().getUsername());
            person_state.setText(mysharedPreferences.getUser().getStatus());

           Glide.with(getApplicationContext())
                   .load("http://192.168.43.188/instagram/instaimage/"+mysharedPreferences.getUser().getPp())
                   .fitCenter()
                   .into(pp);
           id = mysharedPreferences.getUser().getId();
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private  void  setUpTabs(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GridViewFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new SearchFragment());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.grid);
        mTabLayout.getTabAt(1).setIcon(R.drawable.linear);
        mTabLayout.getTabAt(2).setIcon(R.drawable.takephoto);
        mTabLayout.getTabAt(3).setIcon(R.drawable.notifucation);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setUpTabsOnSelect() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().clearColorFilter();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
//////////////////////////////////////////////////////////////////////////////////////


}
