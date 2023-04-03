package michittio.ueh.trifarm_app.srceen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_recycler_view);


        recyclerView = findViewById(R.id.recyclerView);

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

        searchView = findViewById(R.id.search);
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





}
