package pl.dom3k.picipolodelux;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(PlayerState.getUsername(getApplicationContext())==null){
            Intent i = new Intent(this, PickNameActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void onNewGameClick(View view){
        Intent i = new Intent(this,JustGameActivity.class);
        startActivity(i);
    }

    public void onHowToClick(View view){

    }
}
