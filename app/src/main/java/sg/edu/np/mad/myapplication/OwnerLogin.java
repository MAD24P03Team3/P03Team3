package sg.edu.np.mad.myapplication;

import android.content.Intent;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OwnerLogin extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // create interface basically not a class but methods and behaviours that ar implmented in classes



    // define the method implement it

    private static FirebaseAuth mAuth;
    private static FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ownerlogin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        EditText etemail = findViewById(R.id.etowneremail);
        EditText etpassword = findViewById(R.id.etownerpassword);

        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etemail.getText().toString();
                String password = etemail.getText().toString();
                // TODO : 1. Pass the user's input into the data validation check if the user exists in the firestore db
                /*        2. Pass the user's input to validate user exist
                *               - Success (go to intent)
                *               - Unsucessful (display toast) */
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            db.collection("Owner").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        Intent toOwnerMain= new Intent(OwnerLogin.this, ownerHome.class);
                                        Toast.makeText(OwnerLogin.this,"The owner has sucessfully login",Toast.LENGTH_SHORT).show();
                                        startActivity(toOwnerMain);

                                    }

                                }
                            });
                        }
                        else{
                            Toast.makeText(OwnerLogin.this,"The owner does not exists",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}