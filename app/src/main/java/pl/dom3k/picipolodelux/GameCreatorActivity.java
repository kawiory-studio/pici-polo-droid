package pl.dom3k.picipolodelux;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class GameCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_creator);


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
            Snackbar.make(view, "This name is already taken. Pick another one, please.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        else if(response.equals("error")){
            Snackbar.make(view, "Something is no yes.",
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
