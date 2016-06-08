package pl.dom3k.picipolodelux;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Domek on 08.06.2016.
 */
public class GameListRecyclerViewAdapter extends RecyclerView.Adapter {

    ArrayList<String> listOfGames = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class DirectViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final Context mContext;

        boolean isSet = false;

        public DirectViewHolder(View view) {
            super(view);
            mView = view;
            mContext= view.getContext();

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
