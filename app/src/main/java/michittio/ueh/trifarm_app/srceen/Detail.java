package michittio.ueh.trifarm_app.srceen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.Product;

public class Detail extends AppCompatActivity {
    ImageView imageView;
    TextView tv_detail_name, tv_detail_description,tv_detail_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.iv_detail);
        tv_detail_name = findViewById(R.id.tv_detail_title);
        tv_detail_description = findViewById(R.id.tv_detail_description);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String price = intent.getStringExtra("price");

        // Hiển thị ảnh sản phẩm bằng Glide

        Glide.with(this).load(image).into(imageView);

        // Hiển thị các thông tin khác của sản phẩm
        tv_detail_name.setText(name);
        tv_detail_description.setText(description);




    }
}