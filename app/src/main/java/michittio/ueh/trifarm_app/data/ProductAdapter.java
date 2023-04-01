package michittio.ueh.trifarm_app.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import michittio.ueh.trifarm_app.R;

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
        TextView gridCaption = view.findViewById(R.id.gird_name);


        Glide.with(context).load(listProduct.get(i).getImage()).into(gridImage);
        gridCaption.setText(listProduct.get(i).getName());

        return view;
    }

    public void searchProductList(ArrayList<Product>  searchList) {
        listProduct = searchList;
        notifyDataSetChanged();
    }
}
