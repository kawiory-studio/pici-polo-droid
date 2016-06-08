package pl.dom3k.picipolodelux;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class HotseatGameActivity extends AppCompatActivity {
    TextView playerAName;
    TextView playerBName;
    TextView firstCount;
    TextView secondCount;
    TextView whoseTurn;
    TextView state;
    EditText number;
    TextView lastTurnPerson;
    TextView rolledNum;
    TextView rolledSign;
    TextView rolledDiff;

    String gameName=null;

    public HotseatGameActivity(){
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_game);

        playerAName = (TextView) findViewById(R.id.player1);
        playerBName = (TextView) findViewById(R.id.player2);
        firstCount = (TextView) findViewById(R.id.fst_count);
        secondCount = (TextView) findViewById(R.id.scnd_count);
        whoseTurn = (TextView) findViewById(R.id.whose_turn);
        state = (TextView) findViewById(R.id.state);
        number = (EditText) findViewById(R.id.pici_val);
        lastTurnPerson = (TextView) findViewById(R.id.lastTurnPerson);
        rolledNum = (TextView) findViewById(R.id.rolledNum);
        rolledSign = (TextView) findViewById(R.id.rolledSign);
        rolledDiff = (TextView) findViewById(R.id.rolledDiff);
        state = (TextView) findViewById(R.id.state);
        state = (TextView) findViewById(R.id.state);
        state = (TextView) findViewById(R.id.state);

        gameName = getIntent().getStringExtra("GAMENAME");
        playerAName.setText(getIntent().getStringExtra("LEFTPNAME"));
        playerBName.setText(getIntent().getStringExtra("RIGHTPNAME"));

        String gameState = null;

        try {
            gameState = HotSeatStorage.getResult(gameName);
        } catch (Exception e) {
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
        firstCount.setText(stateParts[5]);
        secondCount.setText(stateParts[6]);
        lastTurnPerson.setText(R.string.game_begun_notif);

        String stateS = stateParts[2].equals(PlayerState.getUsername(getApplicationContext()))?
                getString(R.string.your_turn_desc) : (String.format(getString(R.string.turn_for_desc),stateParts[2]));
        state.setText(stateS);

    }

    public void onNumberPick(View view) {
        Button bt = (Button) view;
        String num = bt.getText().toString();
        number.setText(num);

        onRollClick(view);
    }

    public void onRollClick(View view) {
        String value = number.getText().toString();
        if(value.isEmpty()){
            state.setText(R.string.pick_to_roll);
            return;
        }
        String serverRes = null;
        try {
            serverRes = HotSeatStorage.makeMove(gameName,Integer.parseInt(value),1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(serverRes==null) return;
        if(serverRes.equals("forbidden")){
            state.setText(R.string.illegal_move_desc);
        }
        else{
            String[] splitted = serverRes.split(":");
            state.setText(String.format(getString(R.string.your_result_desc),splitted[2],splitted[4],splitted[5]));
            whoseTurn.setText(splitted[5]);
            firstCount.setText(splitted[8]);
            secondCount.setText(splitted[9]);
            lastTurnPerson.setText(getString(R.string.whose_last,PlayerState.getUsername(getApplicationContext())));
            rolledNum.setText(value);
            rolledSign.setText(splitted[2]);
            rolledDiff.setText(splitted[4]);
        }
    }
}
