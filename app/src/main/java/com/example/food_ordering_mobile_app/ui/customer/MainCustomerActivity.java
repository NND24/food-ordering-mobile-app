package com.example.food_ordering_mobile_app.ui.customer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.ViewpagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.activity.EdgeToEdge;


public class MainCustomerActivity extends AppCompatActivity {
    ViewPager mViewPager;
    BottomNavigationView mBottomNavigationView;
    private long backPressedTime;
    private Toast backPressedToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_customer);

        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewpagerAdapter);
        mViewPager.setCurrentItem(2);
        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.messages).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.orders).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.favorites).setChecked(true);
                        break;
                    case 4:
                        mBottomNavigationView.getMenu().findItem(R.id.account).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.messages:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.orders:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.home:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.favorites:
                        mViewPager.setCurrentItem(3);
                        break;
                    case R.id.account:
                        mViewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            // Nếu thời gian cách nhau dưới 2 giây, thoát ứng dụng
            super.onBackPressed();
            return;
        } else {
            // Nếu lần nhấn đầu tiên, hiển thị thông báo
            backPressedToast = Toast.makeText(getApplicationContext(), "Nhấn lại lần nữa để thoát", Toast.LENGTH_SHORT);
            backPressedToast.show();
        }

        backPressedTime = System.currentTimeMillis(); // Cập nhật thời gian lần nhấn nút back
    }
}