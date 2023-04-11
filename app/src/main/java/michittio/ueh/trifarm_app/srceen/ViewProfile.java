package michittio.ueh.trifarm_app.srceen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import michittio.ueh.trifarm_app.R;

public class ViewProfile extends AppCompatActivity {
    private ImageView imgAvatar;
    private TextView txtFullName;
    private TextView txtPhone;
    private TextView txtDateOfBirth;
    private TextView txtAddress;
    private TextView txtNickName;
    private TextView txtEmail;
    private ImageView btnSetting;
    private Context context;
    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        initui();
        nextUpdateProfile();
        logOut();

        SharedPreferences sharedPreferences = getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key","");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy dữ liệu từ DataSnapshot và gán vào các thành phần giao diện
                String avatarUrl = snapshot.child("avatar").getValue(String.class);
                String fullName = snapshot.child("fullName").getValue(String.class);
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                String dateOfBirth = snapshot.child("dateOfBirth").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                String nickname = snapshot.child("nickname").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);

                Picasso.get().load(avatarUrl).into(imgAvatar);
                txtFullName.setText(fullName);txtPhone.setText(phone);
                txtDateOfBirth.setText(dateOfBirth);
                txtAddress.setText(address);
                txtNickName.setText(nickname);
                txtEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nextUpdateProfile() {
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateUser.class);
                context.startActivity(intent);
            }
        });
    }

    private void initui() {
        btnLogout = findViewById(R.id.btn_logout);
        btnSetting = findViewById(R.id.btn_setting);
        txtFullName = findViewById(R.id.txt_fullname);
        txtAddress = findViewById(R.id.txt_address);
        txtPhone = findViewById(R.id.txt_phone);
        txtEmail = findViewById(R.id.txt_email);
        txtNickName = findViewById(R.id.txt_nickname);
        txtDateOfBirth = findViewById(R.id.txt_dateofbirth);
        imgAvatar = findViewById(R.id.img_avatar);
    }

    private void logOut() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

    }
    private boolean checkSharedPreferencesExistence(String keyEmail) {
        SharedPreferences sharedPreferences =getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
        return sharedPreferences.contains(keyEmail); // Kiểm tra khóa key có tồn tại trong SharedPreferences hay không
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Xử lý đăng xuất
            // Xóa email và mật khẩu từ SharedPreferences
            if(checkSharedPreferencesExistence("email")) {
                SharedPreferences sharedPreferences = getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.apply();

                // Đóng tất cả các hoạt động của ứng dụng
                finishAffinity();
                // Thoát ứng dụng
                System.exit(0);
            } else {
                Toast.makeText(context, "Logout fail", Toast.LENGTH_SHORT).show();

            }

        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Đóng hộp thoại
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}