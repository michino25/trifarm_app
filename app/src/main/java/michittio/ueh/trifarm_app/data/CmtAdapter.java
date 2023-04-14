package michittio.ueh.trifarm_app.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

            convertView.setTag(dataItem);
        } else {
            dataItem = (MyView) convertView.getTag();
        }

        Picasso.get().load(commentArrayList.get(position).getAvt()).resize(256, 256).centerCrop().into(dataItem.iv_avt);
        dataItem.tv_name.setText(commentArrayList.get(position).getUsername());
        dataItem.tv_cmt.setText(commentArrayList.get(position).getCmt());

        String likeStr = "Hữu ích (" + commentArrayList.get(position).getLike()  +")";
        dataItem.tv_like.setText(likeStr);

        return convertView;
    }


    private static class MyView {

        ImageView iv_avt;
        TextView tv_name;
        TextView tv_cmt;
        TextView tv_like;

    }

}
