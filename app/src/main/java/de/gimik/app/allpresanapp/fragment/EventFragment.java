package de.gimik.app.allpresanapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import de.gimik.app.allpresanapp.EventDetailsActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.EventDbAdapter;
import de.gimik.app.allpresanapp.model.Event;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.List;

/**
 * Created by QuyenDT on 9/22/2015.
 */
public class EventFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String TAG_NEWS = EventFragment.class.getSimpleName() + "_NEWS";
    public static final String TAG_AKADEMIE = EventFragment.class.getSimpleName() + "_AKADEMIE";


    @Bind(R.id.lv_events)
    ListView lv_events;
    private EventItemAdapter adapter;
    private EventDbAdapter eventDbAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        int eventType;
        if (bundle != null) {
            eventType = bundle.getInt(Constant.EVENT_TYPE);
            if (eventType == 0)
                setTitle(R.string.news);
            if (eventType == 1)
                setTitle(R.string.allpresan_academy);
            eventDbAdapter = getEventDbAdapter();
            List<Event> events = eventDbAdapter.queryForType(eventType);
            if (CollectionUtils.isNotEmpty(events)) {
                adapter = new EventItemAdapter(getActivity(), events);
                lv_events.setAdapter(adapter);
                lv_events.setOnItemClickListener(this);
                adapter.setNotifyOnChange(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        setTitle(R.string.event_training);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    private void gotoEventDetail(Event item) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.EVENT_ID, item.getId());
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public class EventItemAdapter extends ArrayAdapter<Event> {
        private List<Event> objects;
        private LayoutInflater inflater;
        private static final int TYPE_EVENT = 0;
        private static final int TYPE_HEADER = 1;

        public EventItemAdapter(Context context, List<Event> objects) {
            super(context, 0, objects);
            this.objects = objects;
            this.inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Event getItem(int position) {
            return objects.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position) instanceof Event) {
                return TYPE_EVENT;
            }

            return TYPE_HEADER;
        }

        @Override
        public boolean isEnabled(int position) {
            return (getItemViewType(position) == TYPE_EVENT);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            EventViewHolder holder;
            if (convertView == null) {
                switch (type) {
                    case TYPE_EVENT:
                        convertView = inflater.inflate(R.layout.row_event, parent,
                                false);
                        holder = new EventViewHolder(convertView);
                        convertView.setTag(holder);
                        break;
                    case TYPE_HEADER:
                        convertView = inflater.inflate(R.layout.row_header_event, parent,
                                false);
                        break;
                }

            }
            switch (type) {
                case TYPE_EVENT:
                    holder = (EventViewHolder) convertView.getTag();
                    if (holder != null)
                        holder.updateRow(position);
                    break;
            }
            return convertView;
        }

        class EventViewHolder {
            ImageView imageView;
            TextView txtDate;
            TextView txtName;
            TextView txtPlace;
            TextView txtSummary;

            public EventViewHolder(View row) {
                imageView = (ImageView) row.findViewById(R.id.imageView);
                txtDate = (TextView) row.findViewById(R.id.txtDate);
                txtName = (TextView) row.findViewById(R.id.txtName);
                txtPlace = (TextView) row.findViewById(R.id.txtPlace);
                txtSummary = (TextView) row.findViewById(R.id.txtSummary);
            }

            public void updateRow(final int position) {
                final Event event = objects.get(position);
                txtName.setText(event.getName());
                txtDate.setText(event.getDate());
                txtPlace.setText(event.getPlace());
                txtSummary.setText(event.getSummary());
                String url = event.getImage();

                if (!Utils.isNullOrEmpty(url)) {
                    File imgFile = new File(getDownloadBasePath(), event.getLocalImage());
                    if (!imgFile.exists()) {
                        Ion.with(getActivity())
                                .load(url)
                                .write(imgFile)
                                .setCallback(new FutureCallback<File>() {
                                    ImageView imageView;

                                    public FutureCallback<File> init(ImageView imageView) {
                                        this.imageView = imageView;
                                        return this;
                                    }

                                    @Override
                                    public void onCompleted(Exception e, File file) {
                                        if (file != null)
                                            Ion.with(imageView).load(file.toURI().toString());
                                        else
                                            imageView.setVisibility(View.GONE);
                                    }
                                }.init(imageView));
                    } else {
                        Ion.with(imageView).load(imgFile.toURI().toString());
                    }
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event event = (Event) parent.getItemAtPosition(position);
        gotoEventDetail(event);

    }
}
