package michittio.ueh.trifarm_app.srceen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.ProductCart;

public class ProductDetail extends AppCompatActivity {
    ImageView imageView;
    TextView tv_detail_name, tv_detail_name2, tv_detail_description,
            tv_detail_price, tv_old_price, tv_detail_quantity, tv_detail_sold;
    ImageView mImagePlus, mImageMinus;
    private Button btnAddCart;
    private int mCount = 1;
    private int mTotal = 0;
    private String price;
    private String image;
    private String description;
    private String sold;
    private String name;
    private String id;
    private DecimalFormat myFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();

        renderData();

        totalQuantity();

        addToCart();
    }

    @SuppressLint("SetTextI18n")
    private void renderData() {
        Intent intent = getIntent();
        image = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        sold = intent.getStringExtra("sold");
        price = intent.getStringExtra("price");
        id = intent.getStringExtra("id");

        //format price
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        int priceFormat = Integer.parseInt(price);

        // Hiển thị ảnh sản phẩm bằng Glide
        Glide.with(this).load(image).into(imageView);

        // Hiển thị các thông tin khác của sản phẩm
        tv_detail_name.setText(name);
        tv_detail_name2.setText(name);
        tv_detail_description.setText(description);
        tv_detail_price.setText(myFormatter.format(priceFormat));
        tv_old_price.setPaintFlags(tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_detail_sold.setText(sold + "k");
//        tv_total.setText(myFormatter.format((long) priceFormat * mCount));
    }

    private void init() {
        imageView = findViewById(R.id.img_detailproduct);
        tv_detail_name = findViewById(R.id.txt_detailName);
        tv_detail_name2 = findViewById(R.id.txt_detailName2);
        tv_detail_description = findViewById(R.id.txt_detailDes);
        tv_detail_price = findViewById(R.id.txt_detailPrice);
        tv_old_price = findViewById(R.id.txt_oldPrice);
        tv_detail_quantity = findViewById(R.id.txt_quantity);
//        tv_total = findViewById(R.id.txt_total);
        tv_detail_sold = findViewById(R.id.txt_detailSold);
        mImagePlus = findViewById(R.id.btn_plus);
        mImageMinus = findViewById(R.id.btn_minus);
        btnAddCart = findViewById(R.id.btn_addCart);

    }

    private void addToCart() {
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thời gian hiện tại
                long currentTimeMillis = System.currentTimeMillis();
                // Tính thời gian hết hạn (5 phút sau thời điểm hiện tại)
                long expiryTimeMillis = currentTimeMillis + (15 * 60 * 1000);
                ProductCart productCart = new ProductCart(id,name,price,tv_detail_quantity.getText().toString(),image,expiryTimeMillis,true);
                if (productCart != null) {
                    addToCart(productCart);
                    Toast.makeText(ProductDetail.this, "Add cart success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ProductDetail.this, "Add cart fail", Toast.LENGTH_SHORT).show();

                }

            }
        });
    };

    public void addToCart(ProductCart product) {
        // Lấy danh sách sản phẩm trong giỏ hàng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        // Lấy giá trị dạng JSON từ SharedPreferences
        String cartJson = sharedPreferences.getString("cart", "");
        List<ProductCart> cartList = new ArrayList<>();

        // Chuyển đổi dữ liệu JSON thành danh sách sản phẩm
        if (!TextUtils.isEmpty(cartJson)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ProductCart>>() {}.getType();
            cartList = gson.fromJson(cartJson, type);
        }

        // Thêm sản phẩm mới vào danh sách
        cartList.add(product);

        // Chuyển danh sách sản phẩm thành chuỗi JSON
        Gson gson = new Gson();
        String newCartJson = gson.toJson(cartList);

        // Lưu danh sách sản phẩm mới vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart", newCartJson);
        editor.apply();
    }


    private void totalQuantity() {
        // Đặt OnClickListener cho ImageView "plus"
        mImagePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng biến đếm và cập nhật giá trị TextView

                DecimalFormat myFormatter = new DecimalFormat("###,###");
                int priceFormat = Integer.parseInt(price);
                mCount++;
                tv_detail_quantity.setText(String.valueOf(mCount));
                mTotal = priceFormat * mCount;
//                tv_total.setText(myFormatter.format(mTotal));

            }
        });

        // Đặt OnClickListener cho ImageView "minus"
        mImageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giảm biến đếm và cập nhật giá trị TextView
                if (mCount > 0) {
                    mCount--;
                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    int priceFormat = Integer.parseInt(price);
                    tv_detail_quantity.setText(String.valueOf(mCount));
                    mTotal = priceFormat * mCount;
                    //tv_total.setText(myFormatter.format(mTotal));
                }
            }
        });
    }
}