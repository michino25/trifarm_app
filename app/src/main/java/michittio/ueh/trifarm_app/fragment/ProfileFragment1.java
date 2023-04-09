package michittio.ueh.trifarm_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.srceen.UpdateUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
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

    public ProfileFragment1() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment1 newInstance(String param1, String param2) {
        ProfileFragment1 fragment = new ProfileFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = container.getContext();
//        initui();
//        nextUpdateProfile();
//        logOut();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
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
                txtFullName.setText(fullName);
                txtPhone.setText(phone);
                txtDateOfBirth.setText(dateOfBirth);
                txtAddress.setText(address);
                txtNickName.setText(nickname);
                txtEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void nextUpdateProfile() {
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateUser.class);
                startActivity(intent);
            }
        });
    }

    private void initui() {
        btnLogout = view.findViewById(R.id.btn_logout);
        btnSetting = view.findViewById(R.id.btn_setting);
        txtFullName = view.findViewById(R.id.txt_fullname);
        txtAddress = view.findViewById(R.id.txt_address);
        txtPhone = view.findViewById(R.id.txt_phone);
        txtEmail = view.findViewById(R.id.txt_email);
        txtNickName = view.findViewById(R.id.txt_nickname);
        txtDateOfBirth = view.findViewById(R.id.txt_dateofbirth);
        imgAvatar = view.findViewById(R.id.img_avatar);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
        return sharedPreferences.contains(keyEmail); // Kiểm tra khóa key có tồn tại trong SharedPreferences hay không
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Xử lý đăng xuất
            // Xóa email và mật khẩu từ SharedPreferences
            if(checkSharedPreferencesExistence("email")) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.apply();

                // Đóng tất cả các hoạt động của ứng dụng
                getActivity().finishAffinity();
                // Thoát ứng dụng
                System.exit(0);
            } else {
                Toast.makeText(getActivity(), "Logout fail", Toast.LENGTH_SHORT).show();

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