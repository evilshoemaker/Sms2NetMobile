package ru.digios.sms2net;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.digios.sms2net.core.Message;
import ru.digios.sms2net.core.Settings;
import ru.digios.sms2net.core.SmsService;
import ru.digios.sms2net.core.SmsStorage;

public class MainActivity extends AppCompatActivity {

    private Settings settings = null;
    private Button button = null;

    private EditText whiteListEditText = null;
    private EditText hostToConnectEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings(MainActivity.this);

        whiteListEditText = (EditText) findViewById(R.id.whiteListEditText);
        hostToConnectEditText = (EditText) findViewById(R.id.hostToConnectEditText);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Message message = new Message();
                message.setText("fjgfks");

                /*Intent mIntent = new Intent(MainActivity.this, SmsService.class);
                mIntent.putExtra("message", message);
                mIntent.startService(mIntent);*/


                //startService(new Intent(MainActivity.this, SmsService.class));
                //SmsStorage storage = new SmsStorage(context);
                //storage.getSms();
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
    }

    private void saveSettings() {
        settings.setWhitePhoneList(whiteListEditText.getText().toString());
        settings.setHostToConnect(hostToConnectEditText.getText().toString());
    }
}
