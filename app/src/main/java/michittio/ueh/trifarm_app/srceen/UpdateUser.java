package michittio.ueh.trifarm_app.srceen;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.Product;
import michittio.ueh.trifarm_app.data.User;

public class UpdateUser extends AppCompatActivity {

    private EditText edtFullName;
    private EditText edtPhone;
    private EditText edtDateOfBirth;
    private EditText edtAddress;
    private Button btnSave;
    private ImageView imageAvartar;
    private DatabaseReference databaseReference;
    private StorageReference storageReference ;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        initui();


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            imageAvartar.setImageURI(imageUri);
                        } else {
                            Toast.makeText(UpdateUser.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        imageAvartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                } else  {
                    Toast.makeText(UpdateUser.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initui() {
        edtFullName = findViewById(R.id.edt_fullname);
        edtPhone = findViewById(R.id.edt_phone);
        edtDateOfBirth = findViewById(R.id.edt_dateOfBirth);
        edtAddress = findViewById(R.id.edt_address);
        btnSave = findViewById(R.id.btn_save);
        imageAvartar = findViewById(R.id.upload_Avatar);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void uploadToFirebase(Uri uri) {
        String fullName = edtFullName.getText().toString();
        String phoneNumber = edtPhone.getText().toString();
        String dateOfBirth = edtDateOfBirth.getText().toString();
        String address = edtAddress.getText().toString();

        // Lấy reference đến Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("avatars");
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        // Tải ảnh lên Firebase Storage
        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Lấy đường dẫn (URL) của ảnh vừa tải lên
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Lấy email và mật khẩu từ SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
                        String userId = sharedPreferences.getString("key", "");

                        // Cập nhật các trường dữ liệu mới vào người dùng trong Firebase Realtime Database
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        User user = new User();
                        user.setFullName(fullName);
                        user.setPhoneNumber(phoneNumber);
                        user.setDateOfBirth(dateOfBirth);
                        user.setAddress(address);
                        user.setAvatar(uri.toString());

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("fullName", fullName);
                        updateData.put("phoneNumber", phoneNumber);
                        updateData.put("dateOfBirth", dateOfBirth);
                        updateData.put("address", address);
                        updateData.put("avatar", uri.toString());

                        userRef.updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
            }
        });
    }

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}

