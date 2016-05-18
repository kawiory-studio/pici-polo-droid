package pl.dom3k.picipolodelux;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class PickNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_name);
    }

    public void onButtClick(View view){
        Log.e("butt","Click!");
        EditText et = (EditText) findViewById(R.id.creat_name);
        String name = et != null ? et.getText().toString() : null;

        if(name == null || name.isEmpty()){
            Snackbar.make(view, R.string.pick_name_fsure, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if(name.equals("=!NAMELESS~!")){
            Snackbar.make(view, R.string.lol_forbidden_name,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        String response = null;
        try {
            Log.i("pickServer","Asking server...");
            response = ServerConnector.userLogin(name,PlayerState.getDeviceID(getApplicationContext()));
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(view, R.string.sth_no_yes,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            finish();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            return;
        }
        Log.i("pickServer","Server responded: "+response);

        if(response.equals("taken")){
            Snackbar.make(view, R.string.name_taken_pick_notif,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        PlayerState.setUsername(name,getApplicationContext());
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
