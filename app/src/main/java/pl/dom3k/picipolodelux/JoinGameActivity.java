package pl.dom3k.picipolodelux;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class JoinGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
    }

    public void onJoinClick(View view){
        Log.e("butt","Click!");
        EditText et = (EditText) findViewById(R.id.game_name);
        String name = et != null ? et.getText().toString() : null;

        if(name == null || name.isEmpty()){
            Snackbar.make(view, "Name a game you want to join.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        String response = null;
        try {
            Log.i("pickServer","Asking server...");
            response = ServerConnector.joinGame(name,getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("pickServer","Server responded: "+response);

        if(response==null||(!response.equals("ok")&&!response.equals("already"))){
            String err;
            if(response.equals("full")) err = "The game is full.";
            else if (response.equals("nonexisting")) err = "Game with this name does not exist.";
            else err = "Some error occured, non-specific one. Where is our God?";
            Snackbar.make(view, err,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        Intent i = new Intent(this,JustGameActivity.class);
        i.putExtra("GAMENAME",name);
        startActivity(i);
        finish();
    }
}
