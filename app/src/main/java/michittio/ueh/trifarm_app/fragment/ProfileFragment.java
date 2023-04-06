package michittio.ueh.trifarm_app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import michittio.ueh.trifarm_app.R;

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
    private Button btnLogout;

    public ProfileFragment() {}

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
        initui();
        context = container.getContext();
        logOut();
        return view;
    }

    private void initui() {
        btnLogout = view.findViewById(R.id.btn_logout);
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