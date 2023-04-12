package michittio.ueh.trifarm_app.srceen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import michittio.ueh.trifarm_app.MainActivity;
import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.CartAdapter;

public class OrderCart extends AppCompatActivity {

    private Button btnHome;
    private TextView txtTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);
        btnHome = (Button) findViewById(R.id.btn_home);
        txtTotal = findViewById(R.id.txtTotal);

        Intent intent = getIntent();
        String total = intent.getStringExtra("totalOrder");
        txtTotal.setText(total);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderCart.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}