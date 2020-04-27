package com.thedarkshadows.passcodeview;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int LOCK_REQUEST_CODE = 221;
    private static final int SECURITY_SETTING_REQUEST_CODE = 233;

    Switch switchAppLock;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCHAPPLOCK = "switchapplock";
    private boolean switchOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchAppLock = findViewById(R.id.sAppLock);

        switchAppLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        loadData();
        updateViews();

        if (switchOnOff) {
            authenticateApp();
        }

    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCHAPPLOCK, switchAppLock.isChecked());
        editor.apply();

        if (!switchOnOff) {
            Toast.makeText(this, "AppLock Enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "AppLock Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCHAPPLOCK, true);
    }

    public void updateViews() {
        switchAppLock.setChecked(switchOnOff);
    }

    private void authenticateApp() {

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Intent i = keyguardManager.createConfirmDeviceCredentialIntent(null, null);
            try {
                startActivityForResult(i, LOCK_REQUEST_CODE);
            } catch (Exception e) {

                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                try {

                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE);
                } catch (Exception ex) {

                    Toast.makeText(this, "Please set the screen lock Manually by navigating to : Settings>Security.", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}