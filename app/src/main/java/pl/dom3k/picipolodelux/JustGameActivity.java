package pl.dom3k.picipolodelux;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JustGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_game);

        String gameName = getIntent().getStringExtra("GAMENAME");

        TextView name = (TextView) findViewById(R.id.gamename);
        if (name != null) {
            name.setText(gameName);
        }


    }
}
