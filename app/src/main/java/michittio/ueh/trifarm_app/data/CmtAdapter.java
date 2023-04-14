package michittio.ueh.trifarm_app.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import michittio.ueh.trifarm_app.OnProductItemClickListener;
import michittio.ueh.trifarm_app.R;

public class CmtAdapter extends BaseAdapter {
    private ArrayList<Comment> commentArrayList;
    private Context context;
    private OnProductItemClickListener itemClickListener;

    public OnProductItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnProductItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CmtAdapter(ArrayList<Comment> commentArrayList, Context context) {
        this.commentArrayList = commentArrayList;
        this.context = context;
    }

    public CmtAdapter() {
    }


    @Override
    public int getCount() {
        return commentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyView dataItem;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            dataItem = new MyView();
            convertView = inflater.inflate(R.layout.cmt_item, null);

            dataItem.iv_avt = convertView.findViewById(R.id.imv_avt_cmt);
            dataItem.tv_name = convertView.findViewById(R.id.tv_name);
            dataItem.tv_cmt = convertView.findViewById(R.id.tv_cmt);
            dataItem.tv_like = convertView.findViewById(R.id.tv_like);
            dataItem.tv_time = convertView.findViewById(R.id.tv_time);

            convertView.setTag(dataItem);
        } else {
            dataItem = (MyView) convertView.getTag();
        }

        Picasso.get().load(commentArrayList.get(position).getAvt()).resize(256, 256).centerCrop().into(dataItem.iv_avt);
        dataItem.tv_name.setText(commentArrayList.get(position).getUsername());
        dataItem.tv_cmt.setText(commentArrayList.get(position).getCmt());

        String idproduct = commentArrayList.get(position).getIdProduct();
        String key = commentArrayList.get(position).getId();

        dataItem.tv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("Products").child(idproduct).child(key);
                commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int currentLike = snapshot.child("like").getValue(Integer.class);
                            int number = currentLike + 1;
                            commentRef.child("like").setValue(String.valueOf(number));
                            Toast.makeText(context, "Bạn đã thích một bình luận", Toast.LENGTH_SHORT).show();
                        }
                        Integer currentLike = snapshot.child("like").getValue(Integer.class); // Đọc giá trị hiện tại của trường like
                        if (currentLike != null) {
                            commentRef.child("like").setValue(currentLike + 1); // Tăng giá trị lên 1 đơn vị và ghi lên Firebase Realtime Database
                        }
                        Toast.makeText(context, "Bạn đã thích một bình luận", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        String likeStr = "Hữu ích (" + commentArrayList.get(position).getLike()  +")";
        dataItem.tv_like.setText(likeStr);
        dataItem.tv_time.setText(commentArrayList.get(position).getTime());

        return convertView;
    }


    private static class MyView {

        ImageView iv_avt;
        TextView tv_name;
        TextView tv_cmt;
        TextView tv_like;
        TextView tv_time;

    }

}
