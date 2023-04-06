package michittio.ueh.trifarm_app.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import michittio.ueh.trifarm_app.R;

public class CategoryAdapter extends BaseAdapter {
    private ArrayList<Category> categories;
    private Context context;

    public CategoryAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyView dataitem;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            dataitem = new MyView();
            convertView = inflater.inflate(R.layout.category_item, null);
            dataitem.iv_photo = convertView.findViewById(R.id.imv_photo);
            dataitem.tv_caption = convertView.findViewById(R.id.tv_title);
            convertView.setTag(dataitem);
        } else {
            dataitem = (MyView) convertView.getTag();
        }

        //new DownloadImage(dataitem.iv_photo).execute(categories.get(position).getSource_photo());
        Picasso.get().load(categories.get(position).getImage()).resize(256, 256).centerCrop().into(dataitem.iv_photo);
        dataitem.tv_caption.setText(categories.get(position).getName());
        return convertView;
    }

    private static class MyView {
        ImageView iv_photo;
        TextView tv_caption;
    }
}
