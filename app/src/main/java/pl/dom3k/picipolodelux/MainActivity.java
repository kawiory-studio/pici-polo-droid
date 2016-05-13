package pl.dom3k.picipolodelux;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        try {
            if(PlayerState.getUsername(context)==null||
                    !ServerConnector
                            .userLogin(PlayerState.getUsername(context),PlayerState.getDeviceID(context))
                            .equals("ok")){
                Intent i = new Intent(this, PickNameActivity.class);
                startActivity(i);
                finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onNewGameClick(View view){
        Intent i = new Intent(this,GameCreatorActivity.class);
        startActivity(i);
    }

    public void onJoinGameClick(View view){
        Intent i = new Intent(this,JoinGameActivity.class);
        startActivity(i);
    }

    public void onSettingsClick(View view){
        Intent i = new Intent(this, PickNameActivity.class);
        startActivity(i);
        finish();
    }

    public void onHowToClick(View view){

    }
}
