package michittio.ueh.trifarm_app.srceen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.Product;
import michittio.ueh.trifarm_app.data.RecyclerAdapter;

public class Product_RecyclerView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private SearchView searchView;
    private List<Product> productList;
    private FloatingActionButton btnAdd;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_recycler_view);

        initui();
        nextUpload();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Product_RecyclerView.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        productList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(Product_RecyclerView.this,productList);
        recyclerView.setAdapter(recyclerAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Product product = itemSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

    }
    private void searchList(String text){
        ArrayList<Product> searchList = new ArrayList<>();
        for (Product product: productList){
            if (product.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(product);
            }
        }
        recyclerAdapter.searchDataList(searchList);
    }

    private void initui() {
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btn_add);
        searchView = findViewById(R.id.search);
    }



    private void nextUpload() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_RecyclerView.this,UploadProduct.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
