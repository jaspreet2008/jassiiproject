package com.finalproject;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.NavUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.finalproject.model.Hire;
import com.finalproject.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.finalproject.Const.UserId;

public class HireActivity extends AppCompatActivity {

    String userId, HiredUserId, NameHired, name;
    AppCompatEditText tvName, tvEmail, tvAddress, tvPhone, tvCity, tvPrice,tvGender;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarTo = Calendar.getInstance();
    EditText etFrom, etTo;

    Button btnHire, btnRating;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);
        userId = getIntent().getStringExtra("userid");
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences(Const.SHAREDPREFERENCE, MODE_PRIVATE);
        HiredUserId = sharedPreferences.getString(UserId, "0");
        NameHired = sharedPreferences.getString(Const.Name, "");


        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvCity = findViewById(R.id.tvCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvGender = findViewById(R.id.tvGender);

        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        btnRating = findViewById(R.id.btnRatings);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(HireActivity.this, RatingActivity.class);
                r.putExtra("userid",userId);
                startActivity(r);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);

                    if (TextUtils.equals(user.getUserId(), userId)) {

                        tvName.setText(user.getName());
                        tvEmail.setText(user.getEmail());
                        tvAddress.setText(user.getAddress());
                        tvPhone.setText(user.getMobile());
                        tvCity.setText(user.getCity());
                        tvPrice.setText(String.format("%s$ Per Day", user.getPrice()));
                        tvGender.setText(String.format("Gender: %s", tvGender.getText()));

                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

                etFrom.setText(sdf.format(myCalendar.getTime()));
            }

        };

        etFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(HireActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarTo.set(Calendar.YEAR, year);
                myCalendarTo.set(Calendar.MONTH, monthOfYear);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

                etTo.setText(sdf.format(myCalendarTo.getTime()));
            }

        };

        etTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(HireActivity.this, dateTo, myCalendarTo
                        .get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnHire = findViewById(R.id.btnHire);
        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etFrom.getText().toString())) {
                    etFrom.setError("ENTER A VALID DATE !");
                    return;
                }
                if (TextUtils.isEmpty(etTo.getText().toString())) {
                    etTo.setError("ENTER A VALID DATE !");
                    return;
                }

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("hires");
                String id = mDatabase.push().getKey();
                Hire hire = new Hire(id, HiredUserId, userId, NameHired, name, etFrom.getText().toString(), etTo.getText().toString());
                mDatabase.child(Objects.requireNonNull(id)).setValue(hire);
                Toast.makeText(HireActivity.this, "Successfully Hired", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
