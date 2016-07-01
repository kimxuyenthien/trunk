package de.gimik.app.allpresanapp.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.model.ProductWorld;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<ProductWorld> {

    private BaseActivity context;
    private int layoutResourceId;
    private List<ProductWorld> data = new ArrayList();

    public GridViewAdapter(BaseActivity context, int layoutResourceId, List<ProductWorld> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ProductWorld item = data.get(position);
        holder.imageTitle.setText(item.getName());
        if (item.getId() == null || item.getId() == 0) {
            holder.image.setImageResource(R.drawable.ingredients);
        } else if (!Utils.isNullOrEmpty(item.getImage())) {
            File imgFile = new File(context.getDownloadBasePath(), item.getLocalImage());
            Utils.loadImageFromFile(context, holder.image, imgFile, item.getImage());
        } else {
            holder.image.setImageResource(R.drawable.u2);
        }
        if (item.getName().toLowerCase().contains(Constant.PRODUCT_WORLD_PEDIONE)) {
            holder.imageTitle.setTextColor(Color.parseColor(Constant.PRODUCT_WORLD_COLOR));
        }
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}