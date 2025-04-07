package com.example.hoahoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.hoahoc.adapter.Photo;
import com.example.hoahoc.adapter.PhotoViewPagerAdapter;
import com.example.hoahoc.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;

    private List<Photo> photos;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        // Gán layout bằng View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbarBaigiang1);

        mViewPager = findViewById(R.id.viewPager);
        mCircleIndicator = findViewById(R.id.indicator);
        photos = getPhotos();

        PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(photos);
        mViewPager.setAdapter(adapter);

        mCircleIndicator.setViewPager(mViewPager);

        // Xử lý sự kiện khi bấm Floating Action Button

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Cấu hình Navigation
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_feedback, R.id.nav_save, R.id.nav_setting) // Bỏ nav_share để tránh lỗi
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Đăng ký sự kiện chọn item trong NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_share) {
                shareApp();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            if (itemId == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack
                startActivity(intent);
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return handled || super.onOptionsItemSelected(item);
        });

    }

    private List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo(R.drawable.hoa_1));
        photos.add(new Photo(R.drawable.hoa_2));
        photos.add(new Photo(R.drawable.hoa_3));
        photos.add(new Photo(R.drawable.hoa_4));
        return photos;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void shareApp() {
        String appLink = "https://play.google.com/store/apps/details?id=com.example.hoahoc"; // Link thực tế

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tải ứng dụng Hoa Học");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Tải ngay ứng dụng Hoa Học tại: " + appLink);

        startActivity(Intent.createChooser(shareIntent, "Chia sẻ ứng dụng qua"));
    }
}
