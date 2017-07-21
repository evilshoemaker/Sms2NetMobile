package ru.digios.sms2net;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.digios.sms2net.core.Message;
import ru.digios.sms2net.core.MessageDatabaseHelper;
import ru.digios.sms2net.core.MessageNetwork;
import ru.digios.sms2net.core.MessageUploader;
import ru.digios.sms2net.core.Settings;
import ru.digios.sms2net.core.SmsService;

public class MainActivity extends AppCompatActivity {
    private static final Logger logger = ru.digios.sms2net.core.Log.getLogger(MainActivity.class);

    private Settings settings = null;
    private Button startServiceButton = null;
    private Button stopServiceButton = null;
    private Button clearDatabaseButton = null;

    private Button button = null;

    private EditText whiteListEditText = null;
    private EditText hostToConnectEditText = null;
    private EditText intervalEditText = null;

    private TextView versionText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logger.info("Activity start");

        setContentView(R.layout.activity_main);

        settings = new Settings(this);

        whiteListEditText = (EditText) findViewById(R.id.whiteListEditText);
        hostToConnectEditText = (EditText) findViewById(R.id.hostToConnectEditText);
        intervalEditText = (EditText) findViewById(R.id.intervalEditText);

        versionText = (TextView) findViewById(R.id.versionTextView);

        whiteListEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    saveSettings();
                }
            }
        });
        hostToConnectEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    saveSettings();
                }
            }
        });
        intervalEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    saveSettings();
                }
            }
        });

        startServiceButton = (Button) findViewById(R.id.startServiceButton);
        stopServiceButton = (Button) findViewById(R.id.stopServiceButton);
        clearDatabaseButton = (Button) findViewById(R.id.clearDatabaseButton);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, SmsService.class));
                startServiceButton.setEnabled(!isServiceStarted(SmsService.class));
                stopServiceButton.setEnabled(isServiceStarted(SmsService.class));
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, SmsService.class));
                startServiceButton.setEnabled(!isServiceStarted(SmsService.class));
                stopServiceButton.setEnabled(isServiceStarted(SmsService.class));
            }
        });

        clearDatabaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createAndShowRemoveQuestionDialog();
            }
        });

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TestTask().execute();
            }
        });

        loadSettings();

        getAppVersion();
    }

    private void getAppVersion() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionText.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
        intervalEditText.setText(String.valueOf(settings.getSendInterval()));

        startServiceButton.setEnabled(!isServiceStarted(SmsService.class));
        stopServiceButton.setEnabled(isServiceStarted(SmsService.class));
    }

    private void saveSettings() {
        settings.setWhitePhoneList(whiteListEditText.getText().toString());
        settings.setHostToConnect(hostToConnectEditText.getText().toString());

        String interval = intervalEditText.getText().toString();
        settings.setSendInterval(Integer.parseInt(interval.isEmpty() ? "2" : interval));
    }

    private boolean isServiceStarted(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
        /*ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);

        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            Log.i("Service", "Process " + rsi.process + " with component "
                    + rsi.service.getClassName());
        }

        return true;*/
    }

    private void createAndShowRemoveQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Удалите все сообщения с номера с которого происходит сбор сообщений. Продолжить очистку?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Да",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAllMessagesFromDatabase();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Нет",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteAllMessagesFromDatabase(){
        MessageDatabaseHelper messageDatabase = new MessageDatabaseHelper(MainActivity.this);
        messageDatabase.deleteAll();
        Toast.makeText(this, "Очищено", Toast.LENGTH_SHORT).show();
    }


    class TestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            List<Message> messages = new ArrayList<Message>();
            MessageNetwork mn = new MessageNetwork();

            Message m1 = new Message();
            m1.setDate(new Date());
            m1.setPhoneNumber("+79528803212");
            m1.setText("dlfsdjhls");

            Message m2 = new Message();
            m2.setDate(new Date());
            m2.setPhoneNumber("+79528803245");
            m2.setText("sdfpw wef");

            messages.add(m1);
            messages.add(m2);

            //mn.send(messages);

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            /*if (result != null && result.indexOf("OK") > -1)
                setContentView(R.layout.activity_result);
            else {
                setContentView(R.layout.activity_error);
            }*/
            //Log.d("", "Response: " + result);
        }
    }
}
