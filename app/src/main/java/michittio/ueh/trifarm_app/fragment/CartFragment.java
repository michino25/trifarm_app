package michittio.ueh.trifarm_app.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.CartAdapter;
import michittio.ueh.trifarm_app.data.Category;
import michittio.ueh.trifarm_app.data.CategoryAdapter;
import michittio.ueh.trifarm_app.data.ProductCart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private GridView gridViewCart;
    private ArrayList<ProductCart> cartArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gridViewCart = getView().findViewById(R.id.gridViewCart);


        CartAdapter adapter = new CartAdapter(getListCart(), getActivity().getApplicationContext());
        gridViewCart.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    private ArrayList<ProductCart> getListCart() {
        ArrayList<ProductCart> cartArrayList = new ArrayList<>();
        cartArrayList.add(new ProductCart(1, "Trà hoa hồng", 104000, 2, "https://salt.tikicdn.com/ts/tmp/59/23/20/0cfc8ba492a85e28355f174f800c1ded.jpg"));
        cartArrayList.add(new ProductCart(2, "Cà Chua", 23000, 5, "https://minhcaumart.vn/media/com_eshop/products/c%20chua%20sach%20an%20ton.jpg"));
        cartArrayList.add(new ProductCart(3, "Dưa leo baby hữu cơ", 27000, 2, "https://salt.tikicdn.com/cache/750x750/ts/product/f5/b0/35/ccf72a5415bdb6b4e162c6d1d8a525dd.jpg"));
        cartArrayList.add(new ProductCart(4, "Nếp cái hoa vàng Vinh Hiển Việt Nam", 130000, 1, "https://cdn.tgdd.vn/Products/Images/2513/227004/bhx/nep-cai-hoa-vang-vinh-hien-tui-1kg-202008150913276084.jpg"));
        cartArrayList.add(new ProductCart(5, "Sữa Hạnh Nhân Hạt Chia", 28000, 2, "https://bizweb.dktcdn.net/100/139/060/products/hanh-nhan-hat-chia-khong-duong-1l.png"));
        cartArrayList.add(new ProductCart(6, "Nấm mèo sấy lạnh", 82000, 1, "https://i.imgur.com/TAJNGF8.jpeg"));
        cartArrayList.add(new ProductCart(7, "Đậu Hũ Trắng", 9000, 3, "https://cooponline.vn/wp-content/uploads/2021/07/dau-hu-trang.jpg"));
        cartArrayList.add(new ProductCart(8, "Bưởi da xanh", 39000, 2, "https://product.hstatic.net/200000281397/product/upload_23d2dccfc5d54319887e2cc627bb648b_grande.jpg"));

        return cartArrayList;
    }


}