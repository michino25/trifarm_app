package michittio.ueh.trifarm_app.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import michittio.ueh.trifarm_app.CartTotalListener;
import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.fragment.CartFragment;

public class CartAdapter extends BaseAdapter {
    private ArrayList<ProductCart> productCartList;
    private Context context;
    private CartTotalListener cartTotalListener;


    public CartAdapter(ArrayList<ProductCart> productCartList, Context context) {
        this.productCartList = productCartList;
        this.context = context;
    }
    public void setCartTotalListener(CartTotalListener listener) {
        this.cartTotalListener = listener;
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
        return 0;
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
            dataItem.tv_quantity = convertView.findViewById(R.id.txt_quantity_cart);
            dataItem.btnMinus = convertView.findViewById(R.id.btn_minus_cart);
            dataItem.btnPlus = convertView.findViewById(R.id.btn_plus_cart);
            dataItem.btnDelete = convertView.findViewById(R.id.btn_deleteCart);
            convertView.setTag(dataItem);
        } else {
            dataItem = (MyView) convertView.getTag();
        }


        DecimalFormat myFormatter = new DecimalFormat("###,###");
        int price = Integer.parseInt(productCartList.get(position).getPrice());
        int quantity = Integer.parseInt(productCartList.get(position).getQuantity());
        total(productCartList);
        Picasso.get().load(productCartList.get(position).getImage()).resize(256, 256).centerCrop().into(dataItem.iv_photo);
        dataItem.tv_name.setText(productCartList.get(position).getName());
        dataItem.tv_price.setText(myFormatter.format(price));
        dataItem.tv_quantity.setText(myFormatter.format(quantity));



        dataItem.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = productCartList.get(position).getId() ;
                updateProductCartStatus(productId, false);
                Toast.makeText(context, productId, Toast.LENGTH_SHORT).show();
            }
        });



        dataItem.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(dataItem.tv_quantity.getText().toString());
                int currentPirce = price;
                int newQuantity = currentQuantity + 1;
                dataItem.tv_quantity.setText(myFormatter.format(newQuantity));
                for(int i = 0;i<productCartList.size();i++) {
                    productCartList.get(i).setQuantity(String.valueOf(newQuantity));
                }

            }
        });

        dataItem.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(dataItem.tv_quantity.getText().toString());
                int currentPirce = price;
                int newQuantity = currentQuantity - 1;
                dataItem.tv_quantity.setText(myFormatter.format(newQuantity));
                for(int i = 0;i<productCartList.size();i++) {
                    productCartList.get(i).setQuantity(String.valueOf(newQuantity));
                }

            }
        });


        return convertView;
    }


    public void updateProductCartStatus(String id, boolean status) {
        for (ProductCart productCart : productCartList) {
            if (productCart.getId() == id) {
                productCart.setStatus(status);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public boolean removeProductCart(int position) {
        if (position >= 0 && position < productCartList.size()) {
            productCartList.get(position).setStatus(false);
            notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }

    public void total(ArrayList<ProductCart> productCartList ) {
        int total = 0;
       for (int i = 0;i<productCartList.size();i++) {
           total += Integer.parseInt(productCartList.get(i).price ) * Integer.parseInt(productCartList.get(i).getQuantity());
       }
        cartTotalListener.onCartTotalChanged(total);
    }

    public String myFormat(int number, int mode) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");

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
        ImageView btnPlus,btnMinus;
        ImageView btnDelete;

    }
}
