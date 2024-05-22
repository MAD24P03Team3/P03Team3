package sg.edu.np.mad.myapplication;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String TAG = "login_page";

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
        // Hardcoded password validation (database CRUD implmented later)
        EditText etStudentId = findViewById(R.id.etSId);

        EditText etpsswd = findViewById(R.id.etpsswd);


        Button loginBtn = findViewById(R.id.login);


        TextView signUp = findViewById(R.id.signUpText);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            }
        });
//        Not wrong but a bug in logic
        // Need to allow users to go to login page even tough currentUser is null
        if(currentUser != null){
            loginBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    String email = etStudentId.getText().toString();
                    String password = etpsswd.getText().toString();
                    // implment login firebase authentication
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, display message for sucessful and pass data to mainActivity
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
        }


    }









}