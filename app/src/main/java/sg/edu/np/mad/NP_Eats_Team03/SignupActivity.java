package sg.edu.np.mad.NP_Eats_Team03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.NP_Eats_Team03.R;


public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final String TAG = "signUp_page";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    // Handle email and password validation

    // Check if password is strong
    private boolean validatePassword(String password){
        // Check if password contains password and numbers
        boolean upper = false;
        boolean lower = false;
        boolean alphanumeric = false;
        for(Character c : password.toCharArray()){
            if(Character.isUpperCase(c) ){
                upper = true;
            }
            if(Character.isLowerCase(c)){
                lower = true;
            }

            if(Character.isLetterOrDigit(c)){
                alphanumeric = true;
            }
        }
        if(upper && lower && alphanumeric){
            return true;
        }
        else{
            Toast.makeText(SignupActivity.this,"Please enter a strong and unique password",Toast.LENGTH_SHORT).show();
        }

        // Check if password is alphanumeric
        return false;
    }

    // Check if the fields required are not empty
    private boolean notEmpty(String password,String email, String name) {
        if (password.equals(null) || email.equals(null) || name.equals(null)) {
            Log.d(TAG,"Empty fields");
            return false;

        }
        return true;

    }

    // Handle fireauth exceptions, existing user email and password
    private void userExist(Exception e) {
        String ec;
        if(e instanceof  FirebaseAuthException){
            // upcasting the subclass e to superclass FirebaseAuthException
            FirebaseAuthException err = (FirebaseAuthException) e;
            ec = err.getErrorCode();
            if(ec.equals("ERROR_INVALID_EMAIL")){
                Toast.makeText(SignupActivity.this,"Please enter your email with an @ ending .com",Toast.LENGTH_SHORT).show();
            }

            if(ec.equals("ERROR_WEAK_PASSWORD")){
                Toast.makeText(SignupActivity.this,"Please enter a strong password min 6 characters",Toast.LENGTH_SHORT).show();
            }
            if(ec.equals("ERROR_EMAIL_ALREADY_IN_USE")){
                Toast.makeText(SignupActivity.this,"Email already exists",Toast.LENGTH_SHORT).show();
            }



            if(ec.equals("ERROR_PASSWORD_ALREADY_IN_USE")){
                Toast.makeText(SignupActivity.this,"The password already exist please select a new one",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Add user details to firestore database, create a new document with user's name once user sign up
    private void addUserToDb(Customer c,String email, FirebaseFirestore db){
        //Add data fields and data value
        Map<String,Object> data = new HashMap<>();
        data.put("name",c.name);
        data.put("cid",c.cid);
        data.put("Student email",email);
        data.put("vouchers", new ArrayList<>());
        data.put("points",0);
        data.put("Likes",c.likes);
        data.put("current orders",c.currentOrder);
        data.put("order history",c.orderHistory);

        // Get the reffrence document and handle the adding of data
        db.collection("Customer").document(email)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "New user added to firestore db!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Unable to add new user to firestre db");
                    }
                });






    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the firebase instance of the current user
        mAuth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button signupBtn = findViewById(R.id.signup);
        EditText inputUsername = findViewById(R.id.inputUsername);
        EditText inputStudentEmail = findViewById(R.id.inputStudentEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        TextView loginTxt = findViewById(R.id.loginText);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputUsername.getText().toString();
                String email = inputStudentEmail.getText().toString().toLowerCase();
                String password = inputPassword.getText().toString();

                if (notEmpty(password,email,name)) {
                    Log.d(TAG,"Not empty fields");
                    if(validatePassword(password)){
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        try{
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "createUserWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                // Add current user to the database

                                                // create new customer class

                                                Customer c = Customer.getInstance(name, email, password, user.getUid());


                                                addUserToDb(c,email,db);
                                                // update the user's profile
                                                updateUserProfile(user, name);
                                                Intent goToLogin = new Intent(SignupActivity.this, LoginActivity.class);
                                                startActivity(goToLogin);
                                                Toast.makeText(SignupActivity.this, "Account created.", Toast.LENGTH_SHORT).show();


                                            }
                                            task.addOnFailureListener(exception -> {
                                                userExist(exception);
                                            });


                                        }
                                        catch (Exception e ){
                                            e.getStackTrace();
                                        }

                                    }
                                });
                    }
                    else{
                        Toast.makeText(SignupActivity.this,"Please use a strong password",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignupActivity.this,"Please fill in all the required fields",Toast.LENGTH_SHORT).show();
                }


            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignupActivity = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(toSignupActivity);
                finish();
            }
        });
    }

    public void updateUserProfile(FirebaseUser user, String name ){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    // **New Method to Send Verification Email**
    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Verification email sent.");
                        } else {
                            Log.e(TAG, "Failed to send verification email.", task.getException());
                        }
                    }
                });
    }
}