package com.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.finalproject.model.User;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private AppCompatEditText etName, etEmail, etMobile, etAddress, etCity, etPassword, etConfirm, etPrice;
    private String name, email, address, city, mobile, password, confirm, userType, price = "0",gender;
    RadioButton rdbUser, rdbNurse, rdbBabySitters, rdbMale, rdbFemale;
    private int UserCount = 0;
    private ContentLoadingProgressBar progressBar;
    private FirebaseAuth mAuth;
    CardView cardView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirmPassword);
        etPrice = findViewById(R.id.etPrice);

        cardView = findViewById(R.id.cardView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.hide();

        rdbUser = findViewById(R.id.rdbUser);
        rdbNurse = findViewById(R.id.rdbNurse);
        rdbBabySitters = findViewById(R.id.rdbBabySitters);

        rdbMale = findViewById(R.id.rdbMale);
        rdbFemale = findViewById(R.id.rdbFemale);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.rdbUser) {
                    cardView.setVisibility(View.GONE);
                } else {
                    cardView.setVisibility(View.VISIBLE);
                }
            }

        });

        AppCompatButton btnAdd = findViewById(R.id.btnAddUser);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Objects.requireNonNull(etName.getText()).toString();
                email = Objects.requireNonNull(etEmail.getText()).toString();
                mobile = Objects.requireNonNull(etMobile.getText()).toString();
                address = Objects.requireNonNull(etAddress.getText()).toString();
                city = Objects.requireNonNull(etCity.getText()).toString();
                password = Objects.requireNonNull(etPassword.getText()).toString();
                confirm = Objects.requireNonNull(etConfirm.getText()).toString();
                price = Objects.requireNonNull(etPrice.getText()).toString();

                if (TextUtils.isEmpty(name)) {
                    etName.setError("This field is empty!");
                    return;
                }
                // blan space is for error messages
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("This field is empty!");
                    return;
                }
                if (mobile.length() != 10) {
                    etMobile.setError("the Phone number length should be 10");
                    return;

                }
                if (password.length() < 6) {
                    etPassword.setError("Minimum Length 6");
                    return;
                }
                if (!TextUtils.equals(password, confirm)) {

                    etConfirm.setError("Password does not match !");
                    return;
                }
            /*    if (TextUtils.isEmpty(price)) {*/
            /*        etPrice.setError("");*/
            /*        return;*/
            /*    }*/
                if (rdbUser.isChecked()) {
                    userType = "U";
                } else if (rdbBabySitters.isChecked()) {
                    userType = "B";
                } else if (rdbNurse.isChecked()) {
                    userType = "N";
                }
                     if(rdbMale.isChecked()){
                         gender="Male";
                     }else if(rdbFemale.isChecked()){
                         gender="Female";
                     }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

                databaseReference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                dataSnapshot.getChildren().iterator().hasNext()) {
                            if (UserCount == 0) {
                                Toast.makeText(SignUpActivity.this, "Email already exist!", Toast.LENGTH_LONG).show();

                            } else if (UserCount == -1) {
                                UserCount = 0;
                            }

                        } else {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                            String userId = mDatabase.push().getKey();
                            User user = new User(userId, name, email, mobile, address, city, userType, password, price,gender);
                            mDatabase.child(Objects.requireNonNull(userId)).setValue(user);
                            progressBar.hide();
                            Toast.makeText(SignUpActivity.this, "User Added Successfully!", Toast.LENGTH_LONG).show();
                            etName.setText("");
                            etEmail.setText("");
                            etMobile.setText("");
                            etAddress.setText("");
                            etCity.setText("");
                            etPassword.setText("");
                            etConfirm.setText("");
                            UserCount = -1;

                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information

                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(i);
                                                finish();

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                //   Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(i);
                                                finish();

                                            }


                                        }
                                    });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });


            }
        });
    }
}

