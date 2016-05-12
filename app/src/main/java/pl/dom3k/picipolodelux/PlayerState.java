package pl.dom3k.picipolodelux;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

/**
 * Created by domek on 11.05.16.
 */
public class PlayerState {
    private static String username = "=!NAMELESS~!";
    private static String deviceID = "=!";

    public static String getDeviceID(Context context){
        if(deviceID.equals("=!"))
            deviceID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceID;
    }

    public static String getUsername(Context context) {
        if(username.equals("=!NAMELESS~!")) {
            String name = context.getSharedPreferences("POLOLUX", Context.MODE_PRIVATE)
                    .getString("USERNAME","=!NAMELESS~!");
            if(name.equals("=!NAMELESS~!")) return null;
            else
                username = name;
        }
        return username;
    }

    public static void setUsername(String username, Context context) {
        PlayerState.username = username;
        SharedPreferences.Editor editor = context
                .getSharedPreferences("POLOLUX", Context.MODE_PRIVATE)
                .edit().putString("USERNAME", username);
        editor.apply();
    }
}
