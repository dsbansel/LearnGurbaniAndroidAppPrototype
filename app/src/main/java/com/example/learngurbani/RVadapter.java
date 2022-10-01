package com.example.learngurbani;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//setting the recycler view for the leaderboard

public class RVadapter extends RecyclerView.Adapter<RVadapter.MyViewHolder> {

    Context context;
    ArrayList<UsersModel> usersArrayList;
    String filter;

    //constructor
    public RVadapter(Context context, ArrayList<UsersModel> usersArrayList, String filter) {
        this.context = context;
        this.usersArrayList = usersArrayList;
        this.filter = filter;
    }

    //viewholder for the recyclerview
    @NonNull
    @Override
    public RVadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.rv_single_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RVadapter.MyViewHolder holder, int position) {

        //get current user and set it as the name for the textview
        UsersModel user = usersArrayList.get(position);

        holder.username.setText(user.username);

        //depending on which filter, set the textview for the value as the associated stat
        if (filter.equals("avgScore")) {
            holder.avgScore.setText(String.valueOf(user.avgScore));
        } else if (filter.equals("gamesPlayed")) {
            holder.avgScore.setText(String.valueOf(user.gamesPlayed));
        } else if (filter.equals("totalScore")) {
            holder.avgScore.setText(String.valueOf(user.totalScore));
        } else if (filter.equals("highScore")) {
            holder.avgScore.setText(String.valueOf(user.highScore));
        }

        //displaying the position of the each user
        holder.rvPos.setText(String.valueOf(position + 1));

    }

    //number of items
    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    //inner class setting the textviews and viewholder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, avgScore, rvPos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.rvText1);
            avgScore = itemView.findViewById(R.id.rvText2);
            rvPos = itemView.findViewById(R.id.rvPos);
        }
    }

}
