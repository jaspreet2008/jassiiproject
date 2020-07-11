package com.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.finalproject.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private User user;
    private Context context;
    private List<User> userList;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvName, tvEmail;
        CardView cardView;
        CircleImageView circleImageView;


        ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvEmail = view.findViewById(R.id.tvEmail);
            cardView = view.findViewById(R.id.cardView);
            circleImageView = view.findViewById(R.id.profile_image);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = userList.get(getAdapterPosition());
                    Intent i = new Intent(context, HireActivity.class);
                    i.putExtra("userid", user.getUserId());
                    i.putExtra("name", user.getName());
                    context.startActivity(i);
                }
            });


        }


    }

    public UserAdapter(Context mContext, List<User> userList) {
        this.context = mContext;
        this.userList = userList;

    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_babysitter, parent, false);


        return new UserAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder holder, final int position) {

        user = userList.get(position);
        holder.tvName.setText(user.getName());
        holder.tvEmail.setText(user.getEmail());


        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        storageRef.child("uploads/" + user.getUserId() + ".jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(holder.circleImageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}