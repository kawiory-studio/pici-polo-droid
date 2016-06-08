package pl.dom3k.picipolodelux;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;

public class GameCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_creator);


    }

    public void onHotSeatChanged(View view){
        CheckBox tick = (CheckBox) findViewById(R.id.is_hotseat);
        LinearLayout hotSeatSettings = (LinearLayout) findViewById(R.id.hotSeatSets);
        if(tick.isChecked()){
            hotSeatSettings.setVisibility(View.VISIBLE);
        }
        else
            hotSeatSettings.setVisibility(View.GONE);
    }

    public void onCreateClick(View view){
        EditText nameET = (EditText) findViewById(R.id.creat_name);
        String name = nameET != null ? nameET.getText().toString() : null;

        String response = null;
        try {
            response = ServerConnector.createPublicGame(name,getApplicationContext());
        } catch (IOException e) {
            response = "error";
            e.printStackTrace();
        }
        if(response.equals("taken")){
            Snackbar.make(view, R.string.taken_name_notif,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        else if(response.equals("error")){
            Snackbar.make(view, R.string.sth_no_yes,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            finish();
            return;
        }

        Log.i("creator",String.format("Response for: %s is: >%s<",name,response));
        name = response.split(":")[1];

        Log.i("creator",String.format("Response is: >%s<, name: %s",response,name));
        Intent i = new Intent(this,WaitingRoomActivity.class);
        i.putExtra("GAMENAME",name);
        startActivity(i);
    }
}
