package fourever.textile.miscs;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by akhil on 18/8/15.
 */
public class NonSwipableViewPager extends ViewPager {

    public NonSwipableViewPager(Context context) {
        super(context);
    }

    public NonSwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
// Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
// Never allow swiping to switch between pages
        return false;
    }
}