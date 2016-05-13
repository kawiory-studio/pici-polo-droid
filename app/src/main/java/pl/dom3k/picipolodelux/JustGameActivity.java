package pl.dom3k.picipolodelux;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class JustGameActivity extends AppCompatActivity {
    TextView playerAName;
    TextView playerBName;
    TextView firstCount;
    TextView secondCount;
    TextView whoseTurn;
    TextView state;
    EditText number;

    String gameName=null;

    public JustGameActivity(){
        super();
        playerAName = (TextView) findViewById(R.id.player1);
        playerBName = (TextView) findViewById(R.id.player2);
        firstCount = (TextView) findViewById(R.id.fst_count);
        secondCount = (TextView) findViewById(R.id.scnd_count);
        whoseTurn = (TextView) findViewById(R.id.whose_turn);
        state = (TextView) findViewById(R.id.state);
        number = (EditText) findViewById(R.id.pici_val);
    }

    class OpponentMoveWaiter extends AsyncTask<String, String, String> {
        int count = 0;

        @Override
        protected String doInBackground(String... params) {
            String serRes = null;
            do {
                SystemClock.sleep(1000);
                try {
                    serRes = ServerConnector.currState(gameName, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                publishProgress(String.format("Waiting for turn of your opponent. (%d seconds)", count++));
            } while (serRes==null||serRes.equals("idle"));

            String[] twoParts = serRes.split(";");
            String[] diffs = twoParts[0].split(":");
            String[] states = twoParts[1].split(":");

            publishProgress(String.format("%s picked %s number and rolled %s sign, which gives him %s difference" +
                    "in his game value. Turn for %s.",diffs[2],diffs[3],diffs[4],states[0]),states[0],states[3],states[4]);

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            if (state != null) {
                state.setText(progress[0]);

                if(progress.length==4){
                    whoseTurn.setText(progress[1]);
                    firstCount.setText(progress[2]);
                    secondCount.setText(progress[3]);
                }
            }
        }

        protected void onPostExecute(String result) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_game);

//        JustGameActivity();

        gameName = getIntent().getStringExtra("GAMENAME");
        String gameState = null;
        try {
            gameState = ServerConnector.currState(gameName,getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView name = (TextView) findViewById(R.id.gamename);
        if (name != null) {
            name.setText(gameName);
        }
        String[] stateParts = gameState.split(":");
        playerAName.setText(stateParts[3]);
        playerBName.setText(stateParts[4]);
        whoseTurn.setText(stateParts[2]);

        String stateS = stateParts[2].equals(PlayerState.getUsername(getApplicationContext()))?
                "Your turn. Pick a number." : (String.format("Turn of player %s.",stateParts[2]));
        state.setText(stateS);
        if(!stateParts[2].equals(PlayerState.getUsername(getApplicationContext()))){
            new OpponentMoveWaiter().execute();
        }

    }

    public void onRollClick(View view) {
        String value = number.getText().toString();
        if(value.isEmpty()){
            state.setText("Put in a number if you want to roll.");
            return;
        }
        String serverRes = null;
        try {
            serverRes = ServerConnector.playersMove(gameName,Integer.parseInt(value),1,getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(serverRes==null) return;
        if(serverRes.equals("forbidden")){
            state.setText("You can't move now. We have to wait.");
        }
        else{
            String[] splitted = serverRes.split(";");
            state.setText(String.format("You rolled %s sign, which leaves you with %s difference" +
                    "in your game value. Turn for %s.",splitted[2],splitted[4],splitted[5]));
            whoseTurn.setText(splitted[5]);
            firstCount.setText(splitted[8]);
            secondCount.setText(splitted[9]);

            new OpponentMoveWaiter().execute();
        }
    }
}
