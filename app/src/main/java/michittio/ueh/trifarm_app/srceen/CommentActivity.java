package michittio.ueh.trifarm_app.srceen;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.Category;
import michittio.ueh.trifarm_app.data.CmtAdapter;
import michittio.ueh.trifarm_app.data.Comment;
import michittio.ueh.trifarm_app.data.Product;
import michittio.ueh.trifarm_app.data.ProductAdapter;

public class CommentActivity extends AppCompatActivity {

    private GridView gridViewCmt;
    private TextView txtAddComt;
    private EditText edtContent;
    private DatabaseReference productsRef;
    private String idProduct, usename, avatarUrl, useId, commentPost;
    private SharedPreferences sharedPreferences;
    private SharedPreferences infor;
    private ArrayList<Comment> comments;
    private ArrayList<String> likeData;
    private int startLike;
    private ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initui();


        txtAddComt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
                edtContent.setText("");
            }
        });

        CmtAdapter cmtAdapter = new CmtAdapter(comments, CommentActivity.this);
        gridViewCmt.setAdapter(cmtAdapter);


        eventListener = productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Comment comment = itemSnapshot.getValue(Comment.class);
                    comment.setLike(likeData.get(startLike + comments.size()));
                    comments.add(comment);
                }
                cmtAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void postComment() {
        useId = sharedPreferences.getString("key", "");
        usename = infor.getString("fullname", "");
        avatarUrl = infor.getString("avatar", "");
        commentPost = edtContent.getText().toString();
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = sdf.format(currentTime);

        String cid = productsRef.push().getKey();

        Comment comment = new Comment(cid, useId, usename, avatarUrl, commentPost, "0", formattedTime, idProduct);
        productsRef.child(cid).setValue(comment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Thêm dữ liệu thành công
                        Toast.makeText(CommentActivity.this, "Comment success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xảy ra lỗi khi thêm dữ liệu
                        Toast.makeText(CommentActivity.this, "Comment fail", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void initui() {
        gridViewCmt = findViewById(R.id.gridViewCmt);
        txtAddComt = findViewById(R.id.txt_addComment);
        edtContent = findViewById(R.id.edt_content);
        Intent intent = getIntent();
        comments = new ArrayList<>();
        idProduct = intent.getStringExtra("idProduct");

        startLike = Integer.parseInt(intent.getStringExtra("startLike"));

        String likeDataStr = intent.getStringExtra("likeData");
        likeData = new ArrayList<>();
        likeData.addAll(ProductDetail.JsonToArrayList(likeDataStr));

        sharedPreferences = getSharedPreferences("SaveUser", Context.MODE_PRIVATE);
        infor = getSharedPreferences("Info", Context.MODE_PRIVATE);
        productsRef = FirebaseDatabase.getInstance().getReference("Products").child(idProduct).child("Comments");
    }


}