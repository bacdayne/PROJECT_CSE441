package com.example.hoahoc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

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

        // Kiểm tra đăng nhập
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Gán layout bằng ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbarBaigiang1);

        // Khởi tạo ViewPager
        mViewPager = findViewById(R.id.viewPager);
        mCircleIndicator = findViewById(R.id.indicator);
        photos = getPhotos();
        PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(photos);
        mViewPager.setAdapter(adapter);
        mCircleIndicator.setViewPager(mViewPager);

        // Thiết lập Navigation
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_rate, R.id.nav_save, R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Bắt sự kiện chọn item menu
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_rate) {
                showRatingDialog();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            if (itemId == R.id.nav_share) {
                shareApp();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            if(itemId == R.id.nav_manage_lesson){
                Intent intent = new Intent(MainActivity.this, ManageLessonsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            if (itemId == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            drawer.closeDrawer(GravityCompat.START);
            return handled || super.onOptionsItemSelected(item);
        });

        // Ẩn menu admin mặc định
        Menu menu = binding.navView.getMenu();
        menu.findItem(R.id.nav_manage_lesson).setVisible(false);

        // Kiểm tra vai trò người dùng
        checkUserRole();
    }

    private void checkUserRole() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Acount").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String role = snapshot.child("role").getValue(String.class);
                        if ("admin".equals(role)) {
                            Menu menu = binding.navView.getMenu();
                            menu.findItem(R.id.nav_manage_lesson).setVisible(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Lỗi lấy quyền: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đánh giá ứng dụng");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        builder.setView(dialogView);

        builder.setPositiveButton("Gửi", (dialog, which) -> {
            float rating = ratingBar.getRating();
            if (rating >= 4.5) {
                Toast.makeText(this, "Cảm ơn bạn rất nhiều vì đánh giá tuyệt vời! 🌟", Toast.LENGTH_SHORT).show();
            } else if (rating >= 3) {
                Toast.makeText(this, "Cảm ơn bạn đã đánh giá. Chúng mình sẽ cố gắng cải thiện!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Rất tiếc vì trải nghiệm của bạn chưa tốt. Chúng mình sẽ cải thiện!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
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
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void shareApp() {
        String appLink = "https://play.google.com/store/apps/details?id=com.example.hoahoc";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tải ứng dụng Hoa Học");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Tải ngay ứng dụng Hoa Học tại: " + appLink);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ ứng dụng qua"));
    }
}
