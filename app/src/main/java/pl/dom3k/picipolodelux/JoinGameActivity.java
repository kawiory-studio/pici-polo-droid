package pl.dom3k.picipolodelux;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class JoinGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listOfGames);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new GameListRecyclerViewAdapter());
    }

    public void onJoinClick(View view){
        Log.e("butt","Click!");
        EditText et = (EditText) findViewById(R.id.game_name);
        String name = et != null ? et.getText().toString() : null;

        if(name == null || name.isEmpty()){
            Snackbar.make(view, R.string.give_name_notif, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        String response = null;
        try {
            Log.i("pickServer","Asking server...");
            response = ServerConnector.joinGame(name,getApplicationContext());
        } catch (IOException e) {
            Snackbar.make(view, R.string.sth_no_yes,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            finish();
            e.printStackTrace();
            return;
        }
        Log.i("pickServer","Server responded: "+response);

        if(response==null||(!response.equals("ok")&&!response.equals("already"))){
            String err;
            if(response.equals("full")) err = getString(R.string.game_full_notif);
            else if (response.equals("nonexistent")) err = getString(R.string.game_not_exist_notif);
            else err = getString(R.string.non_specific_err);
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
