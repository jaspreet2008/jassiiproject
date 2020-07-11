package com.finalproject;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.finalproject.Const.UserId;
import static com.finalproject.Const.UserType;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    String userId, userType;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(Const.SHAREDPREFERENCE, MODE_PRIVATE);
        userId = sharedPreferences.getString(UserId, "0");
        userType = sharedPreferences.getString(UserType, "U");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_baby_sitters, R.id.nav_nurse, R.id.nav_search_babysitter, R.id.nav_search_nurse, R.id.nav_hires, R.id.nav_profile, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu nav_Menu = navigationView.getMenu();

        if (TextUtils.equals(userType, "B")) {
            nav_Menu.findItem(R.id.nav_baby_sitters).setVisible(false);
            nav_Menu.findItem(R.id.nav_search_babysitter).setVisible(false);
        } else if (TextUtils.equals(userType, "N")) {
            nav_Menu.findItem(R.id.nav_nurse).setVisible(false);
            nav_Menu.findItem(R.id.nav_search_nurse).setVisible(false);
        } else if (TextUtils.equals(userType, "U")) {
            nav_Menu.findItem(R.id.nav_baby_sitters).setVisible(true);
            nav_Menu.findItem(R.id.nav_nurse).setVisible(true);
            nav_Menu.findItem(R.id.nav_search_babysitter).setVisible(true);
            nav_Menu.findItem(R.id.nav_search_nurse).setVisible(true);

        }

        View hView = navigationView.getHeaderView(0);
        final CircleImageView circleImageView = hView.findViewById(R.id.imageView);


        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        storageRef.child("uploads/" + userId + ".jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(circleImageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}