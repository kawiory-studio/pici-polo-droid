package pl.dom3k.picipolodelux;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

/**
 * Created by domek on 11.05.16.
 */
public class PlayerState {
    private static String username = "=!NAMELESS";
    private static String deviceID = "=!";

    public static void setDeviceID(String deviceID) {
        PlayerState.deviceID = deviceID;
    }

    public static String getDeviceID(){
        return deviceID;
    }
}
