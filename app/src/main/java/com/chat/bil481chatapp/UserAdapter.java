package com.chat.bil481chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> mUsers;
    private Context mContext;
    private RecyclerViewClickListener itemListener;


    public UserAdapter(Context context,List<User> users,RecyclerViewClickListener recyclerViewClickListener) {
        mUsers = users;
        mContext = context;
        this.itemListener = recyclerViewClickListener;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View userView = inflater.inflate(R.layout.user_list_item,parent,false);
        return new UserViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = mUsers.get(position);

        CircleImageView circleImageView = holder.profileImage;
        TextView username = holder.tvUsername;

        circleImageView.setImageResource(R.drawable.profile);
        username.setText(user.getUsername());

    }



    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CircleImageView profileImage;
        public TextView tvUsername;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            itemListener.recyclerViewListClicked(v,position);
        }
    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }



}