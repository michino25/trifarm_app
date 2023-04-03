package michittio.ueh.trifarm_app.srceen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.Product;
import michittio.ueh.trifarm_app.data.RecyclerAdapter;

public class Product_RecyclerView extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    SearchView searchView;
    List<Product> productList;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_recycler_view);

        initui();
        nextUpload();

        // Set up Layout Manager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up FirebaseRecyclerOptions and FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), Product.class)
                        .build();
        recyclerAdapter = new RecyclerAdapter(options);

        // Set up RecyclerView with the FirebaseRecyclerAdapter
        recyclerView.setAdapter(recyclerAdapter);


        searchView.clearFocus();
        //search data
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do nothing on query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the product list based on the search query
                searchList(newText);
                return true;
            }
        });

    }

    private void initui() {
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btn_add);
        searchView = findViewById(R.id.search);
    }

    private void searchList(String query) {
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("name").startAt(query).endAt(query + "\uf8ff"), Product.class)
                        .build();
        recyclerAdapter = new RecyclerAdapter(options);
        recyclerAdapter.startListening();
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
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





}
