package michittio.ueh.trifarm_app.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.Product;
import michittio.ueh.trifarm_app.data.ProductAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    private Context thiscontext;
    private SearchView searchView ;
    private ProductAdapter adapter;
    private GridView gridView;
    private ArrayList<Product> productList;
    private ArrayList<Product> searchList;
    private boolean isSearch = false;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");


    public SearchFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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



    private void initui() {
        gridView = view.findViewById(R.id.gv_search);
        searchView = view.findViewById(R.id.edt_search);
        // Khởi tạo danh sách dữ liệu
        productList = new ArrayList<>();
        // Khởi tạo danh sách tìm kiếm
        searchList = new ArrayList<>();
        // Khởi tạo Adapter cho GridView
        adapter = new ProductAdapter(getContext(), productList);
        gridView.setAdapter(adapter);
    }

    private void searchData() {

    }

    private void loadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                adapter = new ProductAdapter(thiscontext, productList);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        thiscontext = container.getContext();

        //ánh xạ
        initui();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Kiểm tra xem người dùng đã nhập gì vào SearchView
                if (newText.isEmpty()) {
                    isSearch = false;
                    // Nếu không có gì thì hiển thị danh sách gốc
                    adapter.searchDataList(productList);
                } else {
                    isSearch = true;
                    // Nếu có gì thì tìm kiếm và hiển thị danh sách tìm kiếm
                    searchList.clear();
                    for (Product product : productList) {
                        if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                            searchList.add(product);
                        }
                    }
                    adapter.searchDataList(searchList);
                }
                return false;
            }
        });

        // Load dữ liệu từ Firebase Realtime Database
        loadData();

        return view;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Tìm SearchView trong Fragment
        searchView = view.findViewById(R.id.edt_search);

        // Mở SearchView và bật bàn phím ảo
        searchView.setIconified(false);
        searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
        super.onViewCreated(view, savedInstanceState);

    }

    public void searchList(String text){
        ArrayList<Product> searchList = new ArrayList<>();
        for (Product dataClass: searchList){
            if (dataClass.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}