package com.finalproject.ui;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.finalproject.Const;
import com.finalproject.HireAdapter;
import com.finalproject.R;
import com.finalproject.model.Hire;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.finalproject.Const.UserId;
import static com.finalproject.Const.UserType;


public class HiresFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Hire> hireList;
    private HireAdapter adapter;
    String userId, userType;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_hires, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Const.SHAREDPREFERENCE, MODE_PRIVATE);
        userId = sharedPreferences.getString(UserId, "0");
        Log.e("USERID",userId);
        userType = "U";sharedPreferences.getString(UserType, "U");
        hireList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(3), true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("hires");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                hireList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Log.e("Count", snapshot.getChildrenCount() + "");
                    Hire hire = dataSnapshot.getValue(Hire.class);

                    if (TextUtils.equals(hire.getUserid(), userId) && !TextUtils.equals(userType, "U")) {
                        Log.e("Count", snapshot.getChildrenCount() + "1");
                        hireList.add(hire);
                    } else if (TextUtils.equals(hire.getHiredUserId(), userId) && TextUtils.equals(userType, "U")) {
                        Log.e("Count", snapshot.getChildrenCount() + "2");
                        Log.e("Count", hire.getHiredUserId());
                        hireList.add(hire);
                    }


                }

                adapter = new HireAdapter(getContext(), hireList, userType);

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        return root;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
