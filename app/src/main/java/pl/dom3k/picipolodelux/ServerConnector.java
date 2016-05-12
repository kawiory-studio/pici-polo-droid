package pl.dom3k.picipolodelux;

import android.content.Context;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by domek on 12.05.16.
 * Class responsible for connection between application and server. For details on whys and wheres,
 * see https://github.com/kawiory-studio/pici-polo-server/blob/master/src/pl/dom3k/picipolo/server/PICIProcotol
 */
public class ServerConnector {

    private static final String ADDR = "dom3k.pl";
    private static final int    PORT = 5432;

    private static String serverDialogue(String query) throws IOException {
        /*TODO - move all internet operations OUT THE UI thread. Since they're pretty lightweight,
          at this moment, for sake of testing and "getting stuff done", we may assume it's safe
          to keep it this way for a moment, especially knowing that server is ought to respond
          immediately for any query.*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Socket s = new Socket(ADDR,PORT);
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter bw = new PrintWriter(s.getOutputStream(),true);

        bw.println(query);
        bw.flush();

        String servAnsw = br.readLine();

        br.close();
        bw.close();

        return servAnsw;
    }

    static String userLogin(String userName, String userID) throws IOException {
        return serverDialogue(String.format("user:%s:%s",userName,userID));
    }

    static String createPublicGame(String gameName, Context context) throws IOException {
        return serverDialogue(String.format("create:%s:%s;public:%s",
                PlayerState.getUsername(context),PlayerState.getDeviceID(context),gameName));
    }

    static String playersMove(String gameName, int number, int cardNumber, Context context) throws IOException {
        return serverDialogue(String.format("move:%s:%s:%s:%d:%d",
                PlayerState.getUsername(context),PlayerState.getDeviceID(context),gameName,number,cardNumber));
    }

    static String turnAsk(String gameName, Context context) throws IOException {
        return serverDialogue(String.format("waiting:%s:%s:%s",
                PlayerState.getUsername(context),PlayerState.getDeviceID(context),gameName));
    }

    static String currState(String gameName, Context context) throws IOException {
        return serverDialogue(String.format("state:%s:%s:%s",
                PlayerState.getUsername(context),PlayerState.getDeviceID(context),gameName));
    }

    static String joinGame(String gameName, Context context) throws IOException {
        return serverDialogue(String.format("join:%s:%s:%s",
                PlayerState.getUsername(context),PlayerState.getDeviceID(context),gameName));
    }

    static String getPubsList(String gameName, Context context) throws IOException {
        return serverDialogue(String.format("public:%s:%s",
                PlayerState.getUsername(context),PlayerState.getDeviceID(context)));
    }
}
