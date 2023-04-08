package michittio.ueh.trifarm_app.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import michittio.ueh.trifarm_app.R;

public class CartAdapter extends BaseAdapter {
    private ArrayList<ProductCart> productCartList;
    private Context context;

    public CartAdapter(ArrayList<ProductCart> productCartList, Context context) {
        this.productCartList = productCartList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productCartList.size();
    }

    @Override
    public Object getItem(int position) {
        return productCartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productCartList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyView dataItem;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            dataItem = new MyView();
            convertView = inflater.inflate(R.layout.cart_item, null);
            dataItem.iv_photo = convertView.findViewById(R.id.imv_photo);
            dataItem.tv_name = convertView.findViewById(R.id.tv_title);
            dataItem.tv_price = convertView.findViewById(R.id.tv_price);
            dataItem.tv_quantity = convertView.findViewById(R.id.tv_quantity);
            convertView.setTag(dataItem);
        } else {
            dataItem = (MyView) convertView.getTag();
        }

        Picasso.get().load(productCartList.get(position).getImage()).resize(256, 256).centerCrop().into(dataItem.iv_photo);
        dataItem.tv_name.setText(productCartList.get(position).getName());
        dataItem.tv_price.setText(myFormat(productCartList.get(position).getPrice(), 2));
        dataItem.tv_quantity.setText(myFormat(productCartList.get(position).getQuantity(), 1));

        return convertView;
    }

    public String myFormat(int number, int mode) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");

//        mode:
//        1: int to string
//        2: int to string price

        if (mode == 1)
            return String.valueOf(number);
        if (mode == 2)
            return myFormatter.format(number);
        return "";
    }

    private static class MyView {
        ImageView iv_photo;
        TextView tv_name;
        TextView tv_price;
        TextView tv_quantity;
    }
}
