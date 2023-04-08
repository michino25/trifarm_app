package michittio.ueh.trifarm_app.srceen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import michittio.ueh.trifarm_app.R;

public class ProductDetail extends AppCompatActivity {
    ImageView imageView;
    TextView tv_detail_name, tv_detail_name2, tv_detail_description,
            tv_detail_price, tv_old_price, tv_detail_quantity, tv_detail_sold;
    ImageView mImagePlus, mImageMinus;
    private int mCount = 1;
    private int mTotal = 0;
    private String price;
    private DecimalFormat myFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();

        renderData();

        totalQuantity();
    }

    @SuppressLint("SetTextI18n")
    private void renderData() {
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String sold = intent.getStringExtra("sold");
        price = intent.getStringExtra("price");

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
                    tv_detail_quantity.setText(String.valueOf(mCount));
                    mTotal = priceFormat * mCount;
//                    tv_total.setText(myFormatter.format(mTotal));
                }
            }
        });
    }
}