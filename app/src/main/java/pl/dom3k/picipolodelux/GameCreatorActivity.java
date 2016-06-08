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
        String name = nameET != null ? nameET.getText().toString() : "";

        CheckBox tick = (CheckBox) findViewById(R.id.is_hotseat);

        if(tick.isChecked()){
            EditText leftPlayerName = (EditText) findViewById(R.id.leftPlayerName);
            EditText rightPlayerName = (EditText) findViewById(R.id.rightPlayerName);
            String p1 = leftPlayerName.getText().toString();
            String p2 = rightPlayerName.getText().toString();
            if (p1.equals("")||p2.equals("")){
                Snackbar.make(view, "FILL NAME FIEELDS",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }
            String response;
            try {
                if ((response = HotSeatStorage.addGame(name, new String[]{p1, p2}, "")).startsWith("create")) {
                    name = response.split(":")[1];
                }else{
                    Snackbar.make(view, R.string.taken_name_notif,
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
            }catch(Exception e){
                Snackbar.make(view, R.string.sth_no_yes,
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }
            Intent i = new Intent(this,HotseatGameActivity.class);
            i.putExtra("GAMENAME",name);
            i.putExtra("LEFTPNAME",p1);
            i.putExtra("RIGHTPNAME",p2);
            startActivity(i);
            finish();
            return;
        }

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
            return;
        }

        Log.i("creator",String.format("Response for: %s is: >%s<",name,response));
        name = response.split(":")[1];

        Log.i("creator",String.format("Response is: >%s<, name: %s",response,name));
        Intent i = new Intent(this,WaitingRoomActivity.class);
        i.putExtra("GAMENAME",name);
        startActivity(i);
        finish();
    }
}
