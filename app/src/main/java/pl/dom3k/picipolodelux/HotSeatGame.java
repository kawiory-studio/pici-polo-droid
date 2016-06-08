package pl.dom3k.picipolodelux;

import java.util.Date;
import java.util.Random;

/**
 * Created by Januszek on 2016-06-08.
 */
public class HotSeatGame {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotSeatGame game = (HotSeatGame) o;
        return name.equals(game.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    String name;
    String[] players;
    long[] points;
    Change lastChange=null;
    long startTime = 0;
    long timeLimit = -1;
    boolean timeMode=false;
    int currentPlayer;
    int turnCount = 0;
    int turnLimit = -1;
    boolean turnMode=false;

    HotSeatGame(String name,String[] players,String modes)throws Exception{
        this.name = name;
        this.players = players;
        points = new long[2];
        points[0]=0;
        points[1]=0;
        currentPlayer = 0;
        parseModes(modes);
        startTime = new Date().getTime();
    }

    /**
     * Handle given modes and set parameters for game.
     * @param modes string with modes. See <a href=https://github.com/kawiory-studio/pici-polo-server/blob/master/src/pl/dom3k/picipolo/server/PICIProcotol>PICIProtocol</a> for details.
     * @throws Exception if string is in wrong format.
     */
    public void parseModes(String modes)throws Exception{
        String[] modesT = modes.split(",");
        for(String mode: modesT){
            String[] innerMode = mode.split("|");
            if(innerMode.length>0){
                if(innerMode[0].startsWith("turns")){
                    turnMode=true;
                    turnLimit=Integer.parseInt(innerMode[1]);
                }else if(innerMode[0].startsWith("time")){
                    timeMode=true;
                    timeLimit=Integer.parseInt(innerMode[1])*1000;
                }
            }
        }
    }


    /**
     * Make move in game.
     * @param number number chosen for current move.
     * @param cardNumber card chosen by user (currently fixed 1).
     * @return String - based on game logic.
     * @throws Exception
     */
    public String makeMove(int number,int cardNumber)throws Exception{
        long currTime = new Date().getTime();
        long lastingTime = currTime-startTime;
        if ((turnMode&&turnCount>=turnLimit)||(timeMode&&(timeLimit<lastingTime))){
            return fillEndGame();
        }
        Change change;
        int zero = new Random().nextInt()%100;
        int one = new Random().nextInt()%100;
        long old = points[currentPlayer];
        String sign;
        String otherSign;
        turnCount++;
        if (cardNumber<1){
            sign = useSign(currentPlayer,number,zero,true);
            otherSign = useSign(currentPlayer,number,one,false);
        }else{
            sign = useSign(currentPlayer,number,one,true);
            otherSign = useSign(currentPlayer,number,zero,false);
        }
        change = new Change(points[currentPlayer],points[currentPlayer]-old,sign,players[currentPlayer],number,otherSign);
        lastChange=change;
        currentPlayer = (currentPlayer +1)%2;

        return fillMoveResults(lastingTime);
    }

    /**
     * Interpret given sign and number.
     * @param playerIndex index of current player.
     * @param number number chosen by player.
     * @param signNumber randomized number for choosing sign.
     * @param flag if current player's points should change based on sign and number.
     * @return string containing used sign.
     * @throws Exception
     */
    private String useSign(int playerIndex,int number, int signNumber, boolean flag)throws Exception{
        String sign;
        if (signNumber<35){
            sign = "+";
            if (flag) points[playerIndex]+=number;
        }else if (signNumber<65){
            sign = "-";
            if (flag) points[playerIndex]-=number;
        }else if (signNumber<80){
            sign = "*";
            if (flag) points[playerIndex]*=number;
        }else if(signNumber<95&&number!=0){
            sign = "/";
            if (flag) points[playerIndex]/=number;
        }else{
            sign = "=";
            if (flag) points[playerIndex]=number;
        }
        return sign;
    }

    /**
     * Return current state of game.
     * @return Returnable - State, GameLonely (if there is still not enough players) or Done.
     * @throws Exception
     */
    public String fillState()throws Exception{
        long currTime = new Date().getTime();
        long lastingTime = currTime-startTime;
        if (currentPlayer<0) return "lonely";
        if ((turnMode&&turnCount>=turnLimit)||(timeMode&&(timeLimit<lastingTime))){
            return fillEndGame();
        }
        String[] tabP = new String[players.length];
        long[] tabR = new long[points.length];
        for (int i=0;i<players.length;i++){
            tabP[i]=players[i];
        }
        System.arraycopy(points, 0, tabR, 0, points.length);
        StringBuilder sB = new StringBuilder().append("state:").append(name).append(":").append(players[currentPlayer]).append(":");
        for (String name:tabP) sB.append(name).append(":");
        for (long points:tabR) sB.append(points).append(":");
        sB.append(turnCount);
        if (turnLimit>-1) sB.append("|").append(turnLimit);
        sB.append(":");
        sB.append(lastingTime/1000);
        if (timeLimit/1000>-1) sB.append("|").append(timeLimit/1000);
        sB.append(":");
        return sB.toString();
    }

    /**
     * Returns information about current move.
     * @param lastingTime
     * @return MoveResults or Forbidden.
     */
    private String fillMoveResults(long lastingTime){
        if (lastChange==null)return "forbidden";
        String[] tabP = new String[players.length];
        long[] tabR = new long[points.length];
        for (int i=0;i<players.length;i++){
            tabP[i]=players[i];
        }
        System.arraycopy(points, 0, tabR, 0, points.length);
        StringBuilder sB = new StringBuilder().append("results:").append(name).append(":").append(lastChange.getSign()).append(":").append(lastChange.getOtherSign()).append(":").append(lastChange.getDiff()).append(":").append(players[currentPlayer]).append(":");
        for (String name:tabP) sB.append(name).append(":");
        for (long points:tabR) sB.append(points).append(":");
        sB.append(turnCount);
        if (turnLimit>-1) sB.append("|").append(turnLimit);
        sB.append(":");
        sB.append(lastingTime/1000);
        if (timeLimit/1000>-1) sB.append("|").append(timeLimit/1000);
        sB.append(":");
        return sB.toString();
    }

    /**
     * Returns information about ended game.
     * @return GameEnded.
     */
    private String fillEndGame(){
        int victorIndex=0;
        for(int i =0;i<points.length;i++) if (points[i]>points[victorIndex]) victorIndex = i;
        String[] tabP = new String[players.length];
        long[] tabR = new long[points.length];
        for (int i=0;i<players.length;i++){
            tabP[i]=players[i];
        }
        System.arraycopy(points, 0, tabR, 0, points.length);
        StringBuilder sB = new StringBuilder().append("done:").append(name).append(":").append(players[victorIndex]).append(":").append(points[victorIndex]).append(":").append(turnCount).append(":").append(startTime).append(":");
        for (String name:tabP) sB.append(name).append(":");
        for (long points:tabR) sB.append(points).append(":");
        return sB.toString();
    }

    /**
     * Gives number of players inside game.
     * @return players count.
     */
    public int getPlayersCount(){
        int out =0;
        for(String user:players) if (user!=null) out++;
        return out;
    }

    /**
     * Gives maximum number of players that game can contain.
     * @return maximum number of players.
     */
    public int getMaxPlayers(){
        return players.length;
    }

    public String getCurrentName(){
        return players[currentPlayer];
    }
}

class Change {

    private long result;
    private long diff;
    private int number;
    private String sign;
    private String playerName;
    private String otherSign;

    public Change(long result, long diff, String sign,String playerName,int number,String otherSign) {
        this.result = result;
        this.diff = diff;
        this.sign = sign;
        this.playerName=playerName;
        this.number=number;
        this.otherSign=otherSign;
    }

    public long getResult() {
        return result;
    }

    public long getDiff() {
        return diff;
    }

    public String getSign() {
        return sign;
    }

    public String getOtherSign() {
        return otherSign;
    }

    public int getNumber() {
        return number;
    }

    public String getPlayerName() {
        return playerName;
    }

}
