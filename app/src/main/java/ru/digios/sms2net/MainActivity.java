package ru.digios.sms2net;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.digios.sms2net.core.Message;
import ru.digios.sms2net.core.SmsService;
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

                Message message = new Message();
                message.setText("fjgfks");

                Intent mIntent = new Intent(MainActivity.this, SmsService.class);
                mIntent.putExtra("message", message);
                context.startService(mIntent);


                //startService(new Intent(MainActivity.this, SmsService.class));
                //SmsStorage storage = new SmsStorage(context);
                //storage.getSms();
            }
        });
    }
}
