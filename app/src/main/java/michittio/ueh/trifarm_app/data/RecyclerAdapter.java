package michittio.ueh.trifarm_app.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

import michittio.ueh.trifarm_app.R;

public class RecyclerAdapter extends FirebaseRecyclerAdapter<Product,RecyclerAdapter.MyViewHoder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHoder holder, int position, @NonNull Product model) {
        holder.txt_name.setText(model.getName());
        holder.txt_price.setText(model.getPrice());

        Glide.with(holder.imageView.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHoder(view);
    }


    public class MyViewHoder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txt_name,txt_price;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recImage);
            txt_name = itemView.findViewById(R.id.recName);
            txt_price = itemView.findViewById(R.id.recPrice);
        }
    }
}
