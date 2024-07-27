package sg.edu.np.mad.NP_Eats_Team03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final String TAG = "VerificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();
        TextView emailTextView = findViewById(R.id.emailTextView);
        Button checkVerificationBtn = findViewById(R.id.checkVerificationBtn);

        // **Get email from intent**
        String email = getIntent().getStringExtra("email");
        emailTextView.setText("Verification email sent to: " + email);

        checkVerificationBtn.setOnClickListener(v -> {
            checkEmailVerification();
        });
    }

    // **Method to Check Email Verification**
    private void checkEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (user.isEmailVerified()) {
                            // Navigate to LoginActivity if email is verified
                            Intent goToLogin = new Intent(VerificationActivity.this, LoginActivity.class);
                            startActivity(goToLogin);
                            Toast.makeText(VerificationActivity.this, "Email verified. You can now log in.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(VerificationActivity.this, "Email not verified yet. Please check your inbox.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Failed to reload user.", task.getException());
                    }
                }
            });
        }
    }
}
