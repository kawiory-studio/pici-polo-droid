package pl.dom3k.picipolodelux;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;

public class WaitingRoomActivity extends AppCompatActivity {

    String gameName = null;

    class StateChecker extends AsyncTask<String, String, String> {
        int count = 0;

        @Override
        protected String doInBackground(String... params) {
            String serRes = null;
            do{
                SystemClock.sleep(1000);
                try {
                    serRes = ServerConnector.currState(gameName,getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                publishProgress(String.format("Still waiting, for %d seconds...",count++));
            } while(serRes==null||serRes.startsWith("lonely")||serRes.split(":")[2].isEmpty());

            publishProgress(String.format("Connected. Playing with %s",serRes.split(":")[4]));
            SystemClock.sleep(1000);
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            TextView status = (TextView) findViewById(R.id.state);
            if (status != null) {
                status.setText(progress[0]);
            }
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(WaitingRoomActivity.this, JustGameActivity.class);
            intent.putExtra("GAMENAME",gameName);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        gameName = getIntent().getStringExtra("GAMENAME");

        TextView name = (TextView) findViewById(R.id.gamename);
        if (name != null) {
            name.setText(gameName);
        }

        new StateChecker().execute();
    }
}
