package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerLogin extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private static final String PREFS_NAME = "owner";
    private static final String KEY_NAME = "email";

    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, email);
        editor.apply();
    }
    /*
    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ownerlogin);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etEmail = findViewById(R.id.etOwnerEmail);
        EditText etPassword = findViewById(R.id.etOwnerPassword);
        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(OwnerLogin.this, "Email and Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // First check if email exists in the "Owner" collection
                db.collection("Owner").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            // If email exists in "Owner" collection, proceed with FirebaseAuth signIn
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent toOwnerMain = new Intent(OwnerLogin.this, ownerHome.class);
                                        saveEmailToSharedPreferences(email);
                                        Toast.makeText(OwnerLogin.this, "The owner has successfully logged in", Toast.LENGTH_SHORT).show();
                                        startActivity(toOwnerMain);
                                        finish();
                                    } else {
                                        Toast.makeText(OwnerLogin.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // If email does not exist in "Owner" collection, show a toast message
                            Toast.makeText(OwnerLogin.this, "Owner does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
