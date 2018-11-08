package com.finalproject.childmonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.childmonitor.ObjectClass.Parent;
import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailET, userNamerET, passwordET, mobileNumberET;
    private Button signUpButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference rootReference;
    private TextView signInView;

    String  userName, mobileNumber,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailET = findViewById(R.id.email_et);
        userNamerET = findViewById(R.id.user_name_et);
        passwordET = findViewById(R.id.pass_et);
        mobileNumberET = findViewById(R.id.mobile_et);
        signUpButton = findViewById(R.id.signup_btn);
        signInView = findViewById(R.id.goto_signin_view);

        mobileNumberET.setText("+880");
        mobileNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+880")){
                    mobileNumberET.setText("+880");
                    Selection.setSelection(mobileNumberET.getText(), mobileNumberET.getText().length());
                }
            }
        });


        auth = FirebaseAuth.getInstance();


        rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.keepSynced(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up");


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        signInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }


    public void attemptSignUp() {

        // Reset errors.
        emailET.setError(null);
        userNamerET.setError(null);

        // Store values at the time of the login attempt.
         email = emailET.getText().toString();
        userName = userNamerET.getText().toString();
        String password = passwordET.getText().toString();
        mobileNumber = mobileNumberET.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordET.setError(getString(R.string.error_invalid_password));
            focusView = passwordET;
            cancel = true;
        }
        if (!TextUtils.isEmpty(mobileNumber) && !ismobilenNumberValid(mobileNumber)) {
            mobileNumberET.setError(getString(R.string.error_invalid_number));
            focusView = mobileNumberET;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailET.setError(getString(R.string.error_field_required));
            focusView = emailET;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailET.setError(getString(R.string.error_invalid_email));
            focusView = emailET;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //task to sign up

            progressDialog.show();

            if (user == null) {

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            user = auth.getCurrentUser();

                            Toast.makeText(SignUpActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                            createUserInformation();

                            progressDialog.dismiss();

                        }else {
                            Toast.makeText(SignUpActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }


        }


    }


    private boolean isEmailValid(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();


    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    private boolean ismobilenNumberValid(String mobileNumber) {
        //TODO: Replace this with your own logic
        return mobileNumber.length() == 14;
    }

    private void createUserInformation() {

        String key = rootReference.child("Parents").child(user.getUid()).push().getKey();
        Parent parentInformation = new Parent(userName, email,mobileNumber);
        rootReference.child("Parents").child(user.getUid()).child("Information").setValue(parentInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent= new Intent( SignUpActivity.this,MainActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SignUpActivity.this, "User Creation failed, Please try again", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
