package michittio.ueh.trifarm_app.srceen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import michittio.ueh.trifarm_app.R;

public class ProductDetail extends AppCompatActivity {
    ImageView imageView;
    TextView tv_detail_name, tv_detail_description,
            tv_detail_price,tv_detail_quanlity,tv_total;
    ImageView mImagePlus,mImageMinus;
    private int mCount = 1;
    private int mTotal = 0;
    private String price ;
    private DecimalFormat myFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initui();

        rednderData();

       totalQuantity();
    }

    private void rednderData() {
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        price = intent.getStringExtra("price");

        //format price
        DecimalFormat myFormatter = new DecimalFormat("###,###đ");
        int priceFormat = Integer.parseInt(price);

        // Hiển thị ảnh sản phẩm bằng Glide
        Glide.with(this).load(image).into(imageView);

        // Hiển thị các thông tin khác của sản phẩm
        tv_detail_name.setText(name);
        tv_detail_description.setText(description);
        tv_detail_price.setText(myFormatter.format(priceFormat));
        tv_total.setText(myFormatter.format(priceFormat * mCount ));
    }

    private void initui() {
        imageView = findViewById(R.id.img_detailproduct);
        tv_detail_name = findViewById(R.id.txt_detaiName);
        tv_detail_description = findViewById(R.id.txt_detailDes);
        tv_detail_price = findViewById(R.id.txt_detaiPrice);
        tv_detail_quanlity = findViewById(R.id.txt_quanlity);
        tv_total = findViewById(R.id.txt_total);
        mImagePlus =  findViewById(R.id.btn_plus);
        mImageMinus = findViewById(R.id.btn_minus);

    }



    private void totalQuantity() {
        // Đặt OnClickListener cho ImageView "plus"
        mImagePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng biến đếm và cập nhật giá trị TextView

                DecimalFormat myFormatter = new DecimalFormat("###,###đ");
                int priceFormat = Integer.parseInt(price);
                mCount++;
                tv_detail_quanlity.setText(String.valueOf(mCount));
                mTotal = priceFormat * mCount;
                tv_total.setText(myFormatter.format(mTotal));

            }
        });

        // Đặt OnClickListener cho ImageView "minus"
        mImageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giảm biến đếm và cập nhật giá trị TextView
                if (mCount > 0) {
                    mCount--;
                    DecimalFormat myFormatter = new DecimalFormat("###,###đ");
                    int priceFormat = Integer.parseInt(price);
                    tv_detail_quanlity.setText(String.valueOf(mCount));
                    tv_detail_quanlity.setText(String.valueOf(mCount));
                    mTotal = priceFormat * mCount;
                    tv_total.setText(myFormatter.format(mTotal));
                }
            }
        });
    }
}