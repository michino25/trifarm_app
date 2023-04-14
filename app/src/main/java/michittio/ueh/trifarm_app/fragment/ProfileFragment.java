package michittio.ueh.trifarm_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
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

import michittio.ueh.trifarm_app.MainActivity;
import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.srceen.Login;
import michittio.ueh.trifarm_app.srceen.Product_RecyclerView;
import michittio.ueh.trifarm_app.srceen.UpdateUser;
import michittio.ueh.trifarm_app.srceen.ViewProfile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Context context;
    private TextView txtUpdateProfile, txtViewProfile;
    private Button btnLogout;
    private ImageView imgAvatar;
    private TextView txtFullName;
    private ImageView imgSell;

    public ProfileFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        initui();
        logOut();
        nextPageAdmin();

        ((MainActivity) getActivity()).updateStatusBarColor("#4CA71E");


        txtUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateUser.class);
                context.startActivity(intent);
            }
        });

        txtViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfile.class);
                context.startActivity(intent);
            }
        });

        imgAvatar.setOutlineProvider(new ViewOutlineProvider() {

            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        imgAvatar.setClipToOutline(true);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy dữ liệu từ DataSnapshot và gán vào các thành phần giao diện
                String avatarUrl = snapshot.child("avatar").getValue(String.class);
                String fullName = snapshot.child("fullName").getValue(String.class);


                Picasso.get().load(avatarUrl).into(imgAvatar);
                txtFullName.setText(fullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void nextPageAdmin() {
        imgSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Product_RecyclerView.class);
                context.startActivity(intent);
            }
        });
    }

    private void initui() {
        txtUpdateProfile = view.findViewById(R.id.btn_updateProfile);
        txtViewProfile = view.findViewById(R.id.btn_viewProfile);
        btnLogout = view.findViewById(R.id.btn_logout);
        txtFullName = view.findViewById(R.id.txt_name_profile);
        imgAvatar = view.findViewById(R.id.imv_avt_profile);
        imgSell = view.findViewById(R.id.txt_sell_trifarm);

    }

    private void logOut() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Xử lý đăng xuất
            // Xóa email và mật khẩu từ SharedPreferences
            if (checkSharedPreferencesExistence("email")) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("key");
                editor.apply();

                SharedPreferences sharedPreferencesCart = getActivity().getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorCart = sharedPreferencesCart.edit();
                editorCart.remove("cart");
                editorCart.apply();



                Intent intent = new Intent(context, Login.class);
                context.startActivity(intent);
                // Đóng tất cả các hoạt động của ứng dụng
                //getActivity().finishAffinity();
                // Thoát ứng dụng
                //System.exit(0);
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

    private boolean checkSharedPreferencesExistence(String keyEmail) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
        // Kiểm tra khóa key có tồn tại trong SharedPreferences hay không
        return sharedPreferences.contains(keyEmail);
    }


}