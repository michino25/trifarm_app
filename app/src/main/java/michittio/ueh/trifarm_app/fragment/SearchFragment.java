package michittio.ueh.trifarm_app.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import michittio.ueh.trifarm_app.MainActivity;
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
    private RadioGroup radioGroup;
    private int checkedRadioButtonId;
    private RadioButton checkedRadioButton;
    private String idCategory,keySearch;
    private Bundle bundle;
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
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        bundle = getArguments();
        radioGroup = view.findViewById(R.id.rdg_list);
        checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        checkedRadioButton = view.findViewById(checkedRadioButtonId);
        adapter = new ProductAdapter(getContext(), productList);
        gridView.setAdapter(adapter);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        thiscontext = container.getContext();

        ((MainActivity)getActivity()).updateStatusBarColor("#FFFFFF");

        //ánh xạ
        initui();



        radioGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Xóa lắng nghe sự kiện layout
                radioGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Kiểm tra giá trị bundle
                if (bundle != null) {
                    idCategory = bundle.getString("idCategory");
                    // Thiết lập trạng thái của RadioButton type trong SearchView
                    radioGroup.check(R.id.rd_type);
                    searchIdCategory();
                    searchData();

                } else {
                    // Thiết lập trạng thái của RadioButton popular trong SearchView
                    radioGroup.check(R.id.rd_type);
                    RadioButton radioButton = view.findViewById(R.id.rd_type);
                    radioButton.setText("All Product");
                    loadData();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Gọi phương thức radioCheck() với radio button được chọn
                radioCheck(checkedId);
            }
        });




        return view;




    }

    private void hideInterface() {
        loadData();
    }

    private void radioCheck(int radioButtonId) {
        switch (radioButtonId) {
            case R.id.rd_popular:
                // Xử lý lọc dữ liệu theo item "Phổ Biến"
                keySearch = "@Phổ biến";
                searchView.setQuery(keySearch,false);
                // Sắp xếp giảm dần theo số lượng
                Collections.sort(productList, new Comparator<Product>() {
                    @Override
                    public int compare(Product product1, Product product2) {

                        return Integer.compare(Integer.parseInt(product2.getStar()),Integer.parseInt(product1.getStar()));
                    }
                });

                adapter.searchDataList(productList);
                searchData();
                break;
            case R.id.rd_selling:
                // Xử lý lọc dữ liệu theo item "Bán Chạy"
                keySearch = "@Bán chạy";
                searchView.setQuery(keySearch,false);
                Collections.sort(productList, new Comparator<Product>() {
                    @Override
                    public int compare(Product product1, Product product2) {

                        return Integer.compare(Integer.parseInt(product2.getSold()),Integer.parseInt(product1.getSold()));
                    }
                });
                adapter.searchDataList(productList);
                searchData();

                break;
            case R.id.rd_discount:
                // Xử lý lọc dữ liệu theo item "Giảm Giá"
                keySearch = "@Giảm giá";
                searchView.setQuery(keySearch,false);
                Collections.sort(productList, new Comparator<Product>() {
                    @Override
                    public int compare(Product product1, Product product2) {

                        return Float.compare(product2.getNewPrice(), product1.getNewPrice());
                    }
                });
                adapter.searchDataList(productList);
                searchData();
                break;
            case R.id.rd_type:
                if(bundle !=  null) {
                    keySearch = "@"+idCategory;
                } else {
                    keySearch = "@All product";
                }

                searchView.setQuery(keySearch,false);
                for (Product product : productList) {
                    if (product.getName().toLowerCase().contains(keySearch.toLowerCase())) {
                        productList.add(product);
                    }

                }
                adapter.searchDataList(productList);
                searchData();
                break;
            default:
                loadData();
                break;
        }
    }

    private void loadData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                adapter = new ProductAdapter(thiscontext, productList);

                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void searchData() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Kiểm tra xem người dùng đã nhập gì vào SearchView
                if (newText.isEmpty()) {
                    adapter.searchDataList(productList);
                } else {
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

    }
    private void searchIdCategory() {
            databaseReference.orderByChild("id_category").equalTo(idCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Xử lý dữ liệu sản phẩm có idCategory bằng 1
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        // Lấy dữ liệu của sản phẩm
                        Product product = productSnapshot.getValue(Product.class);
                        productList.add(product);
                        // Hiển thị sản phẩm trong giao diện người dùng
                        adapter.searchDataList(productList);
                        gridView.setAdapter(adapter);
                        // ...
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

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


}