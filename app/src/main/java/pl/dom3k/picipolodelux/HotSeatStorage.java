package pl.dom3k.picipolodelux;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Januszek on 2016-06-08.
 */
public class HotSeatStorage {
    private static HashMap<String,HotSeatGame> hotSeatMap = new HashMap<>();
    private static LinkedList<HotSeatGame> hotSeatGames = new LinkedList<>();

    /**
     * Add game with given parameters to server.
     * @param name game name. If empty or null, then random name is generated.
     * @param modes string with modes and theirs parameters. See <a href=https://github.com/kawiory-studio/pici-polo-server/blob/master/src/pl/dom3k/picipolo/server/PICIProcotol>PICIProtocol</a> for details.
     * @return Returnable - GameCreated, Taken or Error.
     * @throws Exception
     */
    public static synchronized String addGame(String name,String[] users,String modes)throws Exception{
        HotSeatGame game = null;
        String newName;
                game = hotSeatMap.get(name);
                if (game != null) return "taken";
                if (name==null||name.trim().equals("")){
                    newName = getRandomName();
                    if (newName==null) return "error";
                }else{
                    newName = name;
                }
                game = new HotSeatGame(newName, users ,modes);
                hotSeatMap.put(newName, game);
                hotSeatGames.add(game);
        return "create:"+newName+":";
    }

    /**
     * Currently unused.
     * @param name name of game to remove.
     * @return Returnable - currently null.
     * @throws Exception
     */
    public static synchronized String removeGame(String name)throws Exception{
        HotSeatGame game;
                game = hotSeatMap.get(name);
                if (game == null) {
                    return null;
                }
                hotSeatMap.remove(name);
                hotSeatGames.remove(hotSeatGames.indexOf(game));
        return null;
    }

    /**
     * Make a move in given game as given player.
     * @param gameName name of game.
     * @param number number chosen by player.
     * @param cardNumber card number chosen by player.
     * @return Returnable - Results, Forbidden or Done.
     * @throws Exception
     */
    public static synchronized String makeMove(String gameName,int number,int cardNumber)throws Exception{
        HotSeatGame game;
        String output;
                if((game = hotSeatMap.get(gameName))==null)return "forbidden";
                output = game.makeMove(number,cardNumber);
        return output;
    }

    /**
     * Returns state for given game.
     * @param gameName game name.
     * @return Returnable - State, Forbidden or Error.
     * @throws Exception
     */
    public static synchronized String getResult(String gameName)throws Exception{
        String output;
            HotSeatGame game;
            if ((game = hotSeatMap.get(gameName)) == null) return "error";
            output = game.fillState();
        return output;
    }

    /**
     * Returns String with list of public games.
     * @return
     * @throws Exception
     */
    public static synchronized String listPublic()throws Exception{
        String output;
            String[] tab = new String[hotSeatGames.size()];
            int[] tabNum = new int[hotSeatGames.size()];
            int[] tabMax = new int[hotSeatGames.size()];
            int i = 0;
            for(HotSeatGame game:hotSeatGames){
                tab[i]=game.getName();
                tabNum[i] = game.getPlayersCount();
                tabMax[i++] = game.getMaxPlayers();
            }
        StringBuilder sB = new StringBuilder().append("hotseat:");
        for (i=0;i<tab.length;i++){
            sB.append(tab[i]).append(",").append(tabNum[i]).append("|").append(tabMax[i]).append(":");
        }
        return sB.toString();
    }

    /**
     * Gets random name for game.
     * @return
     * @throws Exception
     */
    private static synchronized String getRandomName()throws Exception{
        boolean flag = true;
            String name=null;
            while(flag) {
                int number = 20 + new Random().nextInt() % 10000;
                name = "Burak" + number;
                if (hotSeatMap.get(name)==null)flag = false;
            }
            return name;
    }
}
