package de.gimik.app.allpresanapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.AdvantageActivity;
import de.gimik.app.allpresanapp.BarrioExpertActivity;
import de.gimik.app.allpresanapp.BarrioExpertLIPOActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.model.ItemMenu;

/**
 * Created by quyenDT on 9/22/2015.
 */
public class TechnologieFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String TAG = TechnologieFragment.class.getSimpleName();

    @Bind(R.id.lv_technologie)
    ListView lv_technologie;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_technologie, container, false);
        ButterKnife.bind(this, v);
        setTitle(R.string.technologie);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setTitle(R.string.technologie);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        setTitle(R.string.technologie);
        List<ItemMenu> items = new ArrayList();
        items.add(new ItemMenu(getString(R.string.barrio_expert)));
        items.add(new ItemMenu(getString(R.string.barrio_expert_lipo)));
        items.add(new ItemMenu(getString(R.string.advantages)));

        AppAreaAdapter adapter = new AppAreaAdapter(getActivity(), 0, items);
        lv_technologie.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_technologie.setOnItemClickListener(this);
    }

    class AppAreaAdapter extends ArrayAdapter<ItemMenu> {
        private Context context;
        private List<ItemMenu> items;
        private LayoutInflater inflater;

        public AppAreaAdapter(Context context, int resource, List<ItemMenu> items) {
            super(context, resource, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_list_view, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            vh.updateView(position);

            return convertView;
        }

        class ViewHolder {
            //private ImageView ivIcon;
            private TextView tvTitle;
            private ImageView ivRightBlack;
            private ImageView ivRightWhite;

            public ViewHolder(View view) {
                //ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                ivRightBlack = (ImageView) view.findViewById(R.id.ivRightBlack);
                ivRightWhite = (ImageView) view.findViewById(R.id.ivRightWhite);
            }

            public void updateView(int position) {
                ItemMenu item = items.get(position);
                //ivIcon.setImageResource(item.getResourceIcon());
                tvTitle.setText(item.getTitle());
                ivRightBlack.setVisibility(item.selected ? View.GONE : View.VISIBLE);
                ivRightWhite.setVisibility(!item.selected ? View.GONE : View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                Intent intent0 = new Intent(getActivity(), BarrioExpertActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(getActivity(), BarrioExpertLIPOActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(getActivity(), AdvantageActivity.class);
                startActivity(intent2);
                break;

        }

    }
}
