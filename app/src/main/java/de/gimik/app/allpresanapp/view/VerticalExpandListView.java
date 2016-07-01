package de.gimik.app.allpresanapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class VerticalExpandListView extends ListView {
	public VerticalExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalExpandListView(Context context) {
        super(context);
    }

    public VerticalExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                									 MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
