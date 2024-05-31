package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.SharedPreferencesKt;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String TAG = "login_page";

    private FirebaseFirestore db;

    // After sucessful login

//    public void createCustomer(String name){
//        db.collection("Customer").document(user.getDisplayName())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        DocumentSnapshot document = task.getResult();
//                        String name = document.getString("name");
//                        String StudentEmail = document.getString("email");
//
//                        Customer c = new Customer(name,StudentEmail,user.getUid());
//                        Log.d(TAG, "joe joe joe joe"+c.name);
//                        // implement shared prefrences
//                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        editor.putString("name",c.name);
//                        Log.d(TAG,"Sucessfully added the customer's name");
//                        editor.putString("email",c.studentId);
//                        Log.d(TAG,"Sucessfully added the student's email");
//                        editor.apply(); // save the data to shared prefrences
//
//
//
//
//
//
//                    }
//                });
//    }

    // method to validate if user entered the correct password
    public void validateCurrentUser(Exception e, FirebaseUser user){
        // upcast the exception to the superclass
        if(e instanceof  FirebaseAuthException){
            FirebaseAuthException authException = (FirebaseAuthException) e;
            String errorMessage = authException.getErrorCode();
            switch (errorMessage){
                // invalid credential
                case "ERROR_INVALID_CREDENTIAL":
                    Toast.makeText(LoginActivity.this,"Please enter the correct email",Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR_WRONG_PASSWORD":
                    Toast.makeText(LoginActivity.this,"Please enter the correct password",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // return the firebase authenticator instance

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hardcoded password validation (database CRUD implemented later)
        EditText inputStudentEmail = findViewById(R.id.inputStudentEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        Button loginBtn = findViewById(R.id.login);
        TextView signupTxt = findViewById(R.id.signupText);

        signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignupActivity = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(toSignupActivity);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputStudentEmail.getText().toString();
                String password = inputPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "StudentEmail & Password cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    try{
                                        if (task.isSuccessful()) {
//                                            createCustomer(currentUser);
                                            Toast.makeText(LoginActivity.this, "Successful login.", Toast.LENGTH_SHORT).show();
                                            Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);

                                            Bundle newBundle = new Bundle();

                                            startActivity(toMainActivity);
                                        }
                                    }
                                    catch (Exception e){
                                        Log.d(TAG,e.getMessage());
                                    }
                                    task.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            validateCurrentUser(e, currentUser);
                                        }
                                    });



                                }
                            });
                }
            }
        });
        /*if(currentUser != null){
            loginBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    String email = inputStudentID.getText().toString();
                    String password = inputPassword.getText().toString();
                    // implement login firebase authentication
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, display message for successful and pass data to mainActivity
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this,"Login is successful welcome back",Toast.LENGTH_SHORT).show();
                                        Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                        toMainActivity.putExtra("name",user.getDisplayName());
                                        startActivity(toMainActivity);
                                        Log.i(TAG,user.getDisplayName());


                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }*/
    }
}