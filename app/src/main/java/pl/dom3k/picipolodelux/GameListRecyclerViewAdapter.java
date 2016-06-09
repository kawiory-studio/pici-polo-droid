package pl.dom3k.picipolodelux;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Domek on 08.06.2016.
 */
public class GameListRecyclerViewAdapter extends RecyclerView.Adapter<GameListRecyclerViewAdapter.GameOnListViewHolder> {


    interface ListableGame{
        String getGameName();
        String getTurn();
        boolean isMyTurn();
        int getPlayerCount();
        int getMaxPlayers();
    }

    class HotseatGame implements ListableGame{
        String name;
        int playerCount;
        int maxCount;

        HotseatGame(String listString){
            name = listString.split(",")[0];
        }

        @Override
        public String getGameName() {
            return name;
        }

        @Override
        public String getTurn() { //TODO
            return null;
        }

        @Override
        public boolean isMyTurn() { //TODO
            return false;
        }

        @Override
        public int getPlayerCount() {
            return 2;
        }

        @Override
        public int getMaxPlayers() {
            return 2;
        }
    }

    class MyGame implements ListableGame{

        String name;
        int playerCount;
        int maxPlayers;
        String whoseTurn;

        MyGame(String listString){
            String[] splitted = listString.split(",");
            name = splitted[0];
            String players = splitted[1];
            playerCount = Integer.parseInt(players.split("|")[0]);
            maxPlayers  = Integer.parseInt(players.split("|")[1]);
            whoseTurn   = splitted[2];
        }

        @Override
        public String getGameName() {
            return name;
        }

        @Override
        public String getTurn() {
            return whoseTurn;
        }

        @Override
        public boolean isMyTurn() {
            return PlayerState.getUsername(MainActivity.context).equals(whoseTurn);
        }

        @Override
        public int getPlayerCount() {
            return playerCount;
        }

        @Override
        public int getMaxPlayers() {
            return maxPlayers;
        }
    }

    class PublicGame implements ListableGame{

        String name;
        int playerCount;
        int maxPlayers;

        PublicGame(String listString){
            String[] splitted = listString.split(",");
            name = splitted[0];
            String players = splitted[1];
            playerCount = Integer.parseInt(players.split("|")[0]);
            maxPlayers  = Integer.parseInt(players.split("|")[1]);
        }

        @Override
        public String getGameName() {
            return name;
        }

        @Override
        public String getTurn() {
            return null;
        }

        @Override
        public boolean isMyTurn() {
            return false;
        }

        @Override
        public int getPlayerCount() {
            return playerCount;
        }

        @Override
        public int getMaxPlayers() {
            return maxPlayers;
        }
    }

    ArrayList<ListableGame> listOvGames;

    public GameListRecyclerViewAdapter(String hotSeatGames, String yourGames, String publicGames){
        listOvGames = new ArrayList<>();
        String[] hotSeatsList = hotSeatGames.split(":");
        String[] yourList = yourGames.split(":");
        String[] publicList = publicGames.split(":");

        for(int i=1;i<hotSeatsList.length;i++)
            listOvGames.add(new HotseatGame(hotSeatsList[i]));

        for(int i=1;i<yourList.length;i++)
            listOvGames.add(new MyGame(yourList[i]));

        for(int i=1;i<publicList.length;i++)
            listOvGames.add(new PublicGame(publicList[i]));
    }

    @Override
    public GameOnListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameOnListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_game,parent,false));
//        return null;
    }

    public void onBindViewHolder(GameOnListViewHolder holder, int position) {
        ListableGame curr = listOvGames.get(position);

        holder.mItem = curr;
        holder.gameName.setText(curr.getGameName());
        if(curr instanceof MyGame)
            holder.gameStatus.setText(String.format("%d/%d, %s",
                curr.getPlayerCount(),curr.getMaxPlayers(),curr.isMyTurn()?"Your turn!":curr.getTurn()));
        else if(curr instanceof HotseatGame)
            holder.gameStatus.setText(String.format("%s","Hotseat game"));
        else
            holder.gameStatus.setText(String.format("%d/%d",
                    curr.getPlayerCount(),curr.getMaxPlayers()));

    }

    @Override
    public int getItemCount() {
        return listOvGames.size();
    }


    public class GameOnListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ListableGame mItem = null;
        public final Context mContext;
        public final TextView gameName;
        public final TextView gameStatus;

        public GameOnListViewHolder(View view) {
            super(view);
            mView = view;
            mContext= view.getContext();
            gameName = (TextView) view.findViewById(R.id.gameName);
            gameStatus = (TextView) view.findViewById(R.id.gameStatoos);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
