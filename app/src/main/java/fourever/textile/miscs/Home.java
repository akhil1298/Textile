package fourever.textile.miscs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import fourever.textile.mainclasses.R;

public class Home extends AppCompatActivity {

    Timer myTimer;
    LinearLayout mainLayout,knwusLayout,topmembers;

    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
    private int[] color1 = new int[] { R.color.accent, R.color.edittintcolor };
    static final int WAITTIME = 1000;
    int currentColor = 0;

    ImageView changinglayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        knwusLayout = (LinearLayout)findViewById(R.id.knwusLayout);
        topmembers = (LinearLayout)findViewById(R.id.topmembers);

        knwusLayout.getBackground().setAlpha(128);
        topmembers.getBackground().setAlpha(128);

        changinglayout = (ImageView)findViewById(R.id.changinglayout);
       /* MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();
        myTimer.schedule(myTask, 0, 5000);*/
       // animateImageView(changinglayout);

    }

    public void animateImageView(final ImageView v, int color) {
        final int orange = ContextCompat.getColor(this,color);
        final ValueAnimator colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = adjustAlpha(orange, mul);
                v.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
               // v.setBackgroundColor(alphaOrange);
                if (mul == 0.0) {
                    v.setColorFilter(null);
                   // v.setBackgroundColor(0);
                }
            }
        });

        colorAnim.setDuration(5000);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    class MyTimerTask extends TimerTask {

        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            currentColor++;
                            if (currentColor > colors.length)
                                currentColor = 0;

                            if (currentColor < colors.length) {
                               animateImageView(changinglayout, color1[currentColor]);
                            }
                            //mainLayout.setBackgroundColor(colors[currentColor]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            myTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            myTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}