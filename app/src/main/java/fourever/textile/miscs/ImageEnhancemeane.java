package fourever.textile.miscs;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import fourever.textile.mainclasses.R;

public class ImageEnhancemeane extends AppCompatActivity {

    private static final int[] FROM_COLOR = new int[]{49, 179, 110};
    private static final int THRESHOLD = 3;
    private ImageView imgStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enhancemeane);

        imgStatus = (ImageView) findViewById(R.id.backImage);
        final Drawable drawable = ContextCompat.getDrawable(ImageEnhancemeane.this, R.drawable.testimg);
        imgStatus.setImageDrawable(drawable);


        //setSepiaColorFilter(imgStatus.getDrawable());

          //setBlackAndWhiteColorFilter(imgStatus.getDrawable());
         //adjustHue(imgStatus.getDrawable(),12);
        //adjustBrightness(imgStatus.getDrawable(),50);
       //adjustContrast(imgStatus.getDrawable(), 30);
       // adjustSaturation(imgStatus.getDrawable(), 30);


     //   Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.testimg);

        // Memory Consumption
        /*Bitmap convertBM = decodeSampledBitmapFromResource(getResources(), R.drawable.testimg, 100, 100);
        imgStatus.setImageBitmap(createSepiaToningEffect(convertBM, 2, 0.2, 0.5, 0.59));*/
        new ApplyEffects().execute();


        /*imgStatus.setImageBitmap(createSepiaToningEffect(bm,150,0.7,0.3,0.12));*/
        /*imgStatus.setImageBitmap(createSepiaToningEffect(bm,2,0.2,0.5,0.59)); //gray scale  */
        /*imgStatus.setImageBitmap(createSepiaToningEffect(bm, 50, 0.2, 0.5, 0.59));*/


        // Color boost technique is basically based on color filtering, which is to increase the intensity of a single color channel.
                // 1 = RED , 2 = GREEN , 3 = BLUE
        /*imgStatus.setImageBitmap(boost(bm, 1, Float.parseFloat("0.50")));*/
        /*imgStatus.setImageBitmap(boost(bm, 2, Float.parseFloat("0.50")));
        imgStatus.setImageBitmap(boost(bm, 3, Float.parseFloat("0.67")));*/
    }

    private class ApplyEffects extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog mProgressDialog;
        DisplayMetrics metrics;
        Bitmap bm;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(ImageEnhancemeane.this,
                    "", "Loading..");
            metrics = getResources().getDisplayMetrics();
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.testimg);
            imgStatus.setImageBitmap(bm);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap convertBM = decodeSampledBitmapFromResource(getResources(), R.drawable.testimg, 150 , 150 );
          //  Bitmap convertBM = BitmapFactory.decodeResource(getResources(), R.drawable.testimg);
            Bitmap outBM = createSepiaToningEffect(convertBM, 2, 0.2, 0.5, 0.59);
            return outBM;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgStatus.setImageBitmap(bitmap);
            mProgressDialog.dismiss();
        }
    }


 //Effect with colormatrix
    private static void setNoColorFilter(Drawable drawable) {
        if (drawable == null)
            return;
        drawable.setColorFilter(null);
    }
    private static final double DELTA_INDEX[] = {
            0, 0.01, 0.02, 0.04, 0.05, 0.06, 0.07, 0.08, 0.1, 0.11, 0.12, 0.14, 0.15, 0.16, 0.17, 0.18,
            0.20, 0.21, 0.22, 0.24, 0.25, 0.27, 0.28, 0.30, 0.32, 0.34, 0.36, 0.38, 0.40, 0.42, 0.44,
            0.46, 0.48, 0.5, 0.53, 0.56, 0.59, 0.62, 0.65, 0.68, 0.71, 0.74, 0.77, 0.80, 0.83, 0.86, 0.89,
            0.92, 0.95, 0.98, 1.0, 1.06, 1.12, 1.18, 1.24, 1.30, 1.36, 1.42, 1.48, 1.54, 1.60, 1.66, 1.72,
            1.78, 1.84, 1.90, 1.96, 2.0, 2.12, 2.25, 2.37, 2.50, 2.62, 2.75, 2.87, 3.0, 3.2, 3.4, 3.6,
            3.8, 4.0, 4.3, 4.7, 4.9, 5.0, 5.5, 6.0, 6.5, 6.8, 7.0, 7.3, 7.5, 7.8, 8.0, 8.4, 8.7, 9.0, 9.4,
            9.6, 9.8, 10.0
    };
    public static void adjustContrast(Drawable drawable, int value) {
        if (drawable == null)
            return;

        ColorMatrix cm = new ColorMatrix();

        value = (int) cleanValue(value, 100);
        if (value == 0) {
            return;
        }
        float x;
        if (value < 0) {
            x = 127 + value / 100 * 127;
        } else {
            x = value % 1;
            if (x == 0) {
                x = (float) DELTA_INDEX[value];
            } else {
                x = (float) DELTA_INDEX[(value << 0)] * (1 - x)
                        + (float) DELTA_INDEX[(value << 0) + 1] * x;
            }
            x = x * 127 + 127;
        }

        float[] mat = new float[]{
                x / 127, 0, 0, 0, 0.5f * (127 - x), 0, x / 127, 0, 0, 0.5f * (127 - x), 0, 0,
                x / 127, 0, 0.5f * (127 - x), 0, 0, 0, 1, 0, 0, 0, 0, 0, 1
        };
        cm.postConcat(new ColorMatrix(mat));
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(filter);
    }

    public static void adjustSaturation(Drawable drawable, float value) {
        if (drawable == null)
            return;

        ColorMatrix cm = new ColorMatrix();

        value = cleanValue(value, 100);
        if (value == 0) {
            return;
        }

        float x = 1 + ((value > 0) ? 3 * value / 100 : value / 100);
        float lumR = 0.3086f;
        float lumG = 0.6094f;
        float lumB = 0.0820f;

        float[] mat = new float[]{
                lumR * (1 - x) + x, lumG * (1 - x), lumB * (1 - x), 0, 0, lumR * (1 - x),
                lumG * (1 - x) + x, lumB * (1 - x), 0, 0, lumR * (1 - x), lumG * (1 - x),
                lumB * (1 - x) + x, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1
        };
        cm.postConcat(new ColorMatrix(mat));
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(filter);
    }

    private static void setBlackAndWhiteColorFilter(Drawable drawable) {
        if (drawable == null)
            return;

        final ColorMatrix matrixA = new ColorMatrix();
        matrixA.setSaturation(0);

        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrixA);
        drawable.setColorFilter(filter);
    }

    public static void adjustBrightness(Drawable drawable, float value) {
        if (drawable == null)
            return;

        ColorMatrix cm = new ColorMatrix();
        value = cleanValue(value, 100);
        if (value == 0) {
            return;
        }

        float[] mat = new float[]{
                1, 0, 0, 0, value, 0, 1, 0, 0, value, 0, 0, 1, 0, value, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                1
        };
        cm.postConcat(new ColorMatrix(mat));
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(filter);
    }

    private static void setSepiaColorFilter(Drawable drawable) {
        if (drawable == null)
            return;

        final ColorMatrix matrixA = new ColorMatrix();
        matrixA.setSaturation(0);

        final ColorMatrix matrixB = new ColorMatrix();
        matrixB.setScale(1f, .95f, .82f, 1.0f);
        matrixA.setConcat(matrixB, matrixA);

        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrixA);
        Paint paint = new Paint();
        paint.setColorFilter(filter);

        drawable.setColorFilter(filter);
    }

    public static void adjustHue(Drawable drawable, float value)
    {
        ColorMatrix cm = new ColorMatrix();

        value = cleanValue(value, 180f) / 180f * (float) Math.PI;
        if (value == 0)
        {
            return;
        }
        float cosVal = (float) Math.cos(value);
        float sinVal = (float) Math.sin(value);
        float lumR = 0.213f;
        float lumG = 0.715f;
        float lumB = 0.072f;
        float[] mat = new float[]
                {
                        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,
                        0f, 0f, 0f, 1f, 0f,
                        0f, 0f, 0f, 0f, 1f };
        cm.postConcat(new ColorMatrix(mat));
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(filter);
    }

    protected static float cleanValue(float p_val, float p_limit)
    {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
  //End Effect with colormatrix


  //Effect without colormatrix
    public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
        // image size

        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // constant grayscale
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // get color on each channel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // apply grayscale sample
                B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // apply intensity level for sepid-toning on each channel
                R += (depth * red);
                if (R > 255) {
                    R = 255;
                }

                G += (depth * green);
                if (G > 255) {
                    G = 255;
                }

                B += (depth * blue);
                if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        src.recycle();
        src = null;
        // return final image
        return bmOut;
    }

    public static Bitmap boost(Bitmap src, int type, float percent) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                if(type == 1) {  // Red
                    R = (int)(R * (1 + percent));
                    if(R > 255) R = 255;
                }
                else if(type == 2) { // Green
                    G = (int)(G * (1 + percent));
                    if(G > 255) G = 255;
                }
                else if(type == 3) { // BLUE
                    B = (int)(B * (1 + percent));
                    if(B > 255) B = 255;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
