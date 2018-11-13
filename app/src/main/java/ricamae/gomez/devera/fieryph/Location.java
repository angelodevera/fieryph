package ricamae.gomez.devera.fieryph;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
    }

    public void call(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL , Uri.parse("tel:" + "911"));
        startActivity(intent);
    }

    public void messaging (View v){
        String loc = "TxtFire: Reporting fire here at ";
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", loc);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("address", "09959312345"); // add textfire
        startActivity(sendIntent);
    }
}
