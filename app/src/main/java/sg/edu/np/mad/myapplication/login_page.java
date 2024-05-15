package sg.edu.np.mad.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class login_page extends AppCompatActivity {

    String StudentId = "S1089067J";
    String passwd = "NQ2KZINAS8HE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
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
                Intent intent = new Intent(getApplicationContext(), signUp_page.class);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String userId = etStudentId.getText().toString();
                String userPsswd = etpsswd.getText().toString();
                if(userId != null && userPsswd != null){
                    if(userId.equals(StudentId) && userPsswd.equals(passwd)){
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Unsucessful Login Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Login Required",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }









}