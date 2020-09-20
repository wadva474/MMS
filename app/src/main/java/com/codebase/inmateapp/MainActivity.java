package com.codebase.inmateapp;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.codebase.inmateapp.ui.fragments.MessagesFragment;
import com.codebase.inmateapp.ui.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static FragmentManager fragmentManager;
    private final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int REQUIRED_PERMISSIONS_RESULT = 1111;
    private final int SET_DEFAULT_SMS_APP_RESULT = 1112;
    private final int WHITELIST_FROM_BATTERY_RESULT = 1113;
    private BottomNavigationView bottomNavigationView;

//    private MessagesViewModel messagesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_messages, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//        messagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);

    }

    public void HideShowBottomNavigationBar(Fragment fragment) {
        if (fragment instanceof MessagesFragment
                || fragment instanceof SettingsFragment) {
            bottomNavigationView.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @AfterPermissionGranted(REQUIRED_PERMISSIONS_RESULT)
    private void readyInmate() {
        if (!EasyPermissions.hasPermissions(this, REQUIRED_PERMISSIONS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.required_permissions_rationale),
                    REQUIRED_PERMISSIONS_RESULT, REQUIRED_PERMISSIONS);
        } else if (!isDefaultSmsApp()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    RoleManager roleManager = getSystemService(RoleManager.class);
                    if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
                        if (!roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                            Intent roleRequestIntent = roleManager.createRequestRoleIntent(
                                    RoleManager.ROLE_SMS);
                            startActivityForResult(roleRequestIntent, SET_DEFAULT_SMS_APP_RESULT);
                        }
                    }
                } else {
                    startActivityForResult(new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                            .putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName()), SET_DEFAULT_SMS_APP_RESULT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        readyInmate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_DEFAULT_SMS_APP_RESULT && resultCode == Activity.RESULT_OK) {
            readyInmate();
        }
    }

    private boolean isDefaultSmsApp() {
        return getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(this));
    }

}