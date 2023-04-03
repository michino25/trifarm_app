package michittio.ueh.trifarm_app.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.srceen.Detail;
import michittio.ueh.trifarm_app.srceen.ProductDetail;

public class ProductAdapter extends BaseAdapter {
    private ArrayList<Product> listProduct;
    private Context context;
    LayoutInflater layoutInflater;

    public ProductAdapter(Context context, ArrayList<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @Override
    public int getCount() {
        return listProduct.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null){
            view = layoutInflater.inflate(R.layout.girdview_item, null);
        }

        ImageView gridImage = view.findViewById(R.id.grid_image);
        TextView gridName = view.findViewById(R.id.gird_name);
        TextView gridPrice = view.findViewById(R.id.gird_price);
        TextView gridSold= view.findViewById(R.id.gird_sold);


        DecimalFormat myFormatter = new DecimalFormat("###,###đ");
        int price = Integer.parseInt(listProduct.get(i).getPrice());

        String sold =listProduct.get(i).getSold() +"k";


        Glide.with(context).load(listProduct.get(i).getImage()).into(gridImage);
        gridName.setText(listProduct.get(i).getName());
        gridPrice.setText(myFormatter.format(price));
        gridSold.setText(sold);

        // Set OnClickListener for each item in GridView
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle onClick event here
                // For example, start a new activity to display the details of the clicked item
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra("image",listProduct.get(i).getImage());
                intent.putExtra("name", listProduct.get(i).getName());
                intent.putExtra("description",listProduct.get(i).getDescription());
                intent.putExtra("price", listProduct.get(i).getPrice());
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void searchProductList(ArrayList<Product> searchList) {
        listProduct.clear();
        listProduct.addAll(searchList);
        notifyDataSetChanged();
    }
}
