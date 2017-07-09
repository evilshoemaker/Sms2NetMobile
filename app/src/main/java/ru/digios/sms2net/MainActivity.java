package ru.digios.sms2net;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.digios.sms2net.core.SmsStorage;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsStorage storage = new SmsStorage(context);
                storage.getSms();
            }
        });
    }
}
