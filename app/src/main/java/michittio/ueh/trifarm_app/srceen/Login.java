package michittio.ueh.trifarm_app.srceen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import michittio.ueh.trifarm_app.MainActivity;
import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.User;

public class Login extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtPassword;
    private TextView txtsignupRedirectText;
    private Button btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initui();
        nextSignUp();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();

                if(validateEmail(email) && validatePassword(password)) {
                    // Search for user in Firebase Realtime Database
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
                    Query query = databaseRef.orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean found = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null && user.getPassword().equals(password) ) {
                                    found = true;
                                    progressBar.setVisibility(View.VISIBLE);
                                    if(user.getRule().equals("admin")) {
                                        // Login successful
                                        Toast.makeText(Login.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, Product_RecyclerView.class);
                                        startActivity(intent);
                                    } else {
                                        if (saveUser(user.getEmail(),user.getPassword()) ) {
                                            Toast.makeText(Login.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(Login.this, "Login Errol.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            }

                            if (!found) {
                                // Login failed
                                Toast.makeText(Login.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, "Null", Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }

        });
    }
    private boolean saveUser(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            // Kiểm tra email và password không rỗng
            SharedPreferences sharedPreferences = getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

    private void nextSignUp() {

        txtsignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });
    }


    private void initui() {
        edtEmail = findViewById(R.id.edt_login_email);
        edtPassword = findViewById(R.id.edt_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtsignupRedirectText = findViewById(R.id.txt_signupRedirectText);
        progressBar = findViewById(R.id.progressBar);
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email format");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }
}