package ru.digios.sms2net;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import ru.digios.sms2net.core.Message;
import ru.digios.sms2net.core.Settings;
import ru.digios.sms2net.core.SmsService;

public class MainActivity extends AppCompatActivity {

    private Settings settings = null;
    private Button startServiceButton = null;
    private Button stopServiceButton = null;

    private EditText whiteListEditText = null;
    private EditText hostToConnectEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings(this);

        whiteListEditText = (EditText) findViewById(R.id.whiteListEditText);
        hostToConnectEditText = (EditText) findViewById(R.id.hostToConnectEditText);

        startServiceButton = (Button) findViewById(R.id.startServiceButton);
        stopServiceButton = (Button) findViewById(R.id.stopServiceButton);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, SmsService.class));
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, SmsService.class));
            }
        });

        loadSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    private void loadSettings() {
        whiteListEditText.setText(settings.getWhitePhoneList());
        hostToConnectEditText.setText(settings.getHostToConnect());

        startServiceButton.setEnabled(isServiceStarted());
    }

    private void saveSettings() {
        settings.setWhitePhoneList(whiteListEditText.getText().toString());
        settings.setHostToConnect(hostToConnectEditText.getText().toString());
    }

    private boolean isServiceStarted() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);

        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            Log.i("Service", "Process " + rsi.process + " with component "
                    + rsi.service.getClassName());
        }

        return true;
    }
}
