package com.finalproject;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.finalproject.model.Hire;
import com.finalproject.model.Rating;

import java.util.List;
import java.util.Objects;

public class HireAdapter extends RecyclerView.Adapter<HireAdapter.ViewHolder> {

    private Hire hire;
    private Context context;
    private List<Hire> hireList;
    String userType;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvName, tvFrom, tvTo;
        CardView cardView;
        Button btnCancel, btnReview;

        ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvFrom = view.findViewById(R.id.tvFrom);
            tvTo = view.findViewById(R.id.tvTo);
            cardView = view.findViewById(R.id.cardView);
            btnReview = view.findViewById(R.id.btnReview);
            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hire = hireList.get(getAdapterPosition());
                    ratingPopUp(hire);
                }
            });
            btnCancel = view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hire = hireList.get(getAdapterPosition());
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query applesQuery = ref.child("hires").orderByChild("id").equalTo(hire.getId());

                                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                appleSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("TAG", "onCancelled", databaseError.toException());
                                        }
                                    });


                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });


        }


    }

    public HireAdapter(Context mContext, List<Hire> hireList, String userType) {
        this.context = mContext;
        this.hireList = hireList;
        this.userType = userType;

    }

    @NonNull
    @Override
    public HireAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hire, parent, false);


        return new HireAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final HireAdapter.ViewHolder holder, final int position) {

        hire = hireList.get(position);
        if (TextUtils.equals(userType, "U")) {
            holder.tvName.setText(hire.getName());
            holder.btnReview.setVisibility(View.VISIBLE);
        } else {
            holder.tvName.setText(hire.getHiredName());
            holder.btnReview.setVisibility(View.GONE);
        }

        holder.tvFrom.setText(String.format("From Date: %s", hire.getFromDate()));
        holder.tvTo.setText(String.format("To Date: %s", hire.getToDate()));


    }

    @Override
    public int getItemCount() {
        return hireList.size();
    }

    private void ratingPopUp(final Hire hire) {

        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(R.layout.dialog_rating);
        dialog.setCancelable(true);

        final TextView tvLater = dialog.findViewById(R.id.tvLater);
        final TextView tvSubmit = dialog.findViewById(R.id.tvSubmit);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        final EditText etFeedback = dialog.findViewById(R.id.etFeedback);


        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();


            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                if (rating == 0) {
                    Toast.makeText(context, "Please select Stars", Toast.LENGTH_LONG).show();
                }else{
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ratings");
                    String ratingId = mDatabase.push().getKey();
                    //id, message, star, userId, givenUser, givenUserId;

                    Rating rate = new Rating(ratingId, etFeedback.getText().toString(), String.valueOf(rating),
                            hire.getUserid(), hire.getHiredName(),hire.getHiredUserId());
                    mDatabase.child(Objects.requireNonNull(ratingId)).setValue(rate);
                    Toast.makeText(context, "Rated Successfully!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            }
        });
        dialog.show();


    }

}