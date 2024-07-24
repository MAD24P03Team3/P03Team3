package sg.edu.np.mad.NP_Eats_Team03;
import sg.edu.np.mad.NP_Eats_Team03.Navigation.MapBoxRouteHandler;
import sg.edu.np.mad.NP_Eats_Team03.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "login_page";

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";

    // store account infromation details in shared prefrences
    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, email);
        editor.apply();
    }

    // method to validate if user entered the correct password
    public void validateCurrentUser(Exception e, FirebaseUser user) {
        // upcast the exception to the superclass
        if (e instanceof FirebaseAuthException) {
            FirebaseAuthException authException = (FirebaseAuthException) e;
            String errorMessage = authException.getErrorCode();
            switch (errorMessage) {
                // invalid credential
                case "ERROR_INVALID_CREDENTIAL":
                    Toast.makeText(LoginActivity.this, "Please enter the correct email", Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR_WRONG_PASSWORD":
                    Toast.makeText(LoginActivity.this, "Please enter the correct password", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText inputStudentEmail = findViewById(R.id.inputStudentEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        Button loginBtn = findViewById(R.id.login);
        TextView signupTxt = findViewById(R.id.signupText);
        TextView owner = findViewById(R.id.ownerLogin);

        signupTxt.setOnClickListener(v -> {
            Intent toSignupActivity = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(toSignupActivity);
            finish();
        });

        owner.setOnClickListener(v -> {
            Intent toOwner = new Intent(LoginActivity.this,OwnerLogin.class);
            startActivity(toOwner);
            finish();
        });

        loginBtn.setOnClickListener(v -> {
            String email = inputStudentEmail.getText().toString().toLowerCase();
            String password = inputPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "StudentEmail & Password cannot be empty.", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // retrive the current user
                                FirebaseUser user = mAuth.getCurrentUser();
                                // user exists in FireBaseAuth
                                if (user != null) {
                                    db.collection("Customer").document(email)
                                            .get()
                                            .addOnCompleteListener(userTask -> {
                                                if (userTask.isSuccessful()) {
                                                    DocumentSnapshot document = userTask.getResult();
                                                    if (document.exists()) { // document does not exist TODO
                                                        String name = document.getString("name");
                                                        String studentEmail = document.getString("Student email");
                                                        Customer currentCustomer = Customer.getInstance(name, studentEmail, password, user.getUid());
                                                        // Save the customer instance to SharedPreferences
                                                        currentCustomer.saveInstance(LoginActivity.this);
                                                        Log.d(TAG,currentCustomer.name);
                                                        saveEmailToSharedPreferences(studentEmail);
                                                        // indicate sucessful login by user and start a new Intnet to Home Page
                                                        Toast.makeText(LoginActivity.this, "Successful login.", Toast.LENGTH_SHORT).show();
                                                        Intent toMainActivity = new Intent(LoginActivity.this, MainActivity2.class);
                                                        startActivity(toMainActivity);
                                                        finish();
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                        Toast.makeText(LoginActivity.this, "No user data found.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", userTask.getException());
                                                    Toast.makeText(LoginActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        // lamda function to pass the exception encounteered to validateCurrentUser
                        .addOnFailureListener(e -> validateCurrentUser(e, currentUser));
            }
        });
    }
}
