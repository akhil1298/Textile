package fourever.textile.mainclasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fourever.textile.adapter.Post_Service_Adapter;
import fourever.textile.customgallary.Action;
import fourever.textile.customgallary.CustomGallery;
import fourever.textile.customgallary.CustomGalleryActivity;
import fourever.textile.customgallary.GalleryselectedAdapterRecycler;
import fourever.textile.entity.post_service_entity;
import fourever.textile.miscs.FileUploader;
import fourever.textile.miscs.PutUtility;
import fourever.textile.miscs.customtoast;

public class Create_NewsFeed extends AppCompatActivity {

    private int RESULT_LOAD_IMAGE = 1;
    ImageLoader imageLoader;

    String action, res = "";
    EditText txtdesc;

    //GridView gridGallery;
    public static RecyclerView gridGallery;
    Handler handler;
    GalleryselectedAdapterRecycler adapter;

    Create_NewsFeed context = null;

    public static ViewSwitcher viewSwitcher;
    public static ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
    private int deviceHeight;
    private int deviceWidth;

    String buysell[] = {"Buy", "Sell"};
    Post_Service_Adapter post_service_adapter;

    ArrayAdapter<String> buysellAdapter, categoryAdapter;
    private Spinner buyselSpinner, categorySpinner;

    Button post;
    ArrayList<CustomGallery> selectedItems;
    Handler hn = new Handler();

    // String posttype = "B";
    String ServiceId;
    private ArrayList<post_service_entity> Services;
    private SharedPreferences Loginprefs;
    private String userid;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_newsfeed);

        context = this;
        txtdesc = (EditText) findViewById(R.id.txtdesc);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Loginprefs = getApplicationContext().getSharedPreferences("logindetail", 0);
        userid = Loginprefs.getString("user_id", null);
        if (userid == null) {
            Intent intent = new Intent(Create_NewsFeed.this, Login.class);
            startActivity(intent);
            finish();
        }

        post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = txtdesc.getText().toString();

                if (!TextUtils.isEmpty(desc) && ((adapter.getItemCount() - 1) > 0)) {
                    if ((adapter.getItemCount() - 1) > 0) {

                        hn.post(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<String> imgPaths = new ArrayList<String>();
                                for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                                    CustomGallery items = adapter.getItem(i);
                                    imgPaths.add(items.sdcardPath);
                                }
                                // uploadMultiFile(imgPaths);
                                if (!TextUtils.isEmpty(ServiceId)) {
                                    ArrayList<String> param = new ArrayList<String>();
                                    param.add(userid); // userid
                                    param.add(txtdesc.getText().toString()); //text description
                                    // param.add(posttype); // posttype
                                    param.add(ServiceId); // Service ID
                                    //uploadFile(imgPaths);
                                    new ServiceSync().execute(imgPaths, param);
                                } else {
                                    customtoast.ShowToast(getApplicationContext(), "Please select Service.", R.layout.red_toast);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select atleast one image.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    txtdesc.setError("Please something about your product.");
                }
            }
        });

        setSpinner();
        initImageLoader();
        init();

       /*
        upload_img_list = (RecyclerView) findViewById(R.id.upload_img);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        upload_img_list.setLayoutManager(gridLayoutManager);
       */
    }

    private void setSpinner() {
       /* buyselSpinner = (Spinner) findViewById(R.id.buysell);
        buysellAdapter = new ArrayAdapter<String>(this, R.layout.spinneritem, buysell);
        buysellAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        buyselSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), buysell[position], Toast.LENGTH_SHORT).show();
                if (buysell[position].equals("Buy"))
                    posttype = "B";
                else
                    posttype = "S";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        buyselSpinner.setAdapter(buysellAdapter);*/

        //  ----------------------------------------------------------------------------------------------------

        categorySpinner = (Spinner) findViewById(R.id.category);
        //  categoryAdapter = new ArrayAdapter<String>(this, R.layout.spinneritem, category);
        new getServices().execute();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ServiceId = Services.get(position).getServiceid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //  categorySpinner.setAdapter(categoryAdapter);
    }

    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
                    CACHE_DIR);

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    getBaseContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

        } catch (Exception e) {
        }
    }

   /*
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }
    */

    private void init() {

        handler = new Handler();
        //gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery = (RecyclerView) findViewById(R.id.gridGallery);
        //gridGallery.setFastScrollEnabled(true);
        // adapter = new GalleryselectedAdapter(getApplicationContext(), imageLoader);
        adapter = new GalleryselectedAdapterRecycler(Create_NewsFeed.this, imageLoader);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        gridGallery.setLayoutManager(layoutManager);

        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        /*adapter.setOnItemClickListener(new GalleryselectedAdapterRecycler.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });*/

        /*gridGallery.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == adapter.getItemCount() - 1) {
                    selectMultiple(view);
                }

                if (position >= 1) { }
            }
        }));*/
    }

    public void choosepic() {
        int str1 = dataT.size();
        if (str1 > 0) {
            for (int i = 0; i <= dataT.size(); i++) {
                CustomGallery item = dataT.get(i);
                if (item.sdcardPath.equals("addimg")) {
                    dataT.remove(i);
                }
            }
        }

        Intent i = new Intent(Create_NewsFeed.this, CustomGalleryActivity.class);
        i.setAction(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(i, 200);
    }

    public void selectMultiple(View v) {
        int str1 = dataT.size();
        if (str1 > 0) {
            for (int i = 0; i <= dataT.size(); i++) {
                CustomGallery item = dataT.get(i);
                if (item.sdcardPath.equals("addimg")) {
                    dataT.remove(i);
                }
            }
        }

        Intent i = new Intent(Create_NewsFeed.this, CustomGalleryActivity.class);
        i.setAction(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(i, 200);

        /*
            Intent intent = new Intent();
            intent.setType("image*//*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            adapter.clear();
            dataT.clear();

            viewSwitcher.setDisplayedChild(1);
            String single_path = data.getStringExtra("single_path");
            //imageLoader.displayImage("file://" + single_path, imgSinglePick);

        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            /*adapter.clear();
            dataT.clear();
            adapter.clearCache();*/

            String[] all_path = data.getStringArrayExtra("all_path");

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;
                dataT.add(item);
            }

            CustomGallery item1 = new CustomGallery();
            item1.sdcardPath = "addimg";
            dataT.add(item1);

            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
            adapter.notifyDataSetChanged();

            gridGallery.scrollToPosition(adapter.getItemCount() - 1);
            gridGallery.setAdapter(adapter);

            if (dataT.size() >= 1) {
                // DisplayMetrics displaymetrics = new DisplayMetrics();
                // getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                //int height = displaymetrics.heightPixels / 2;
                int height = (int) getResources().getDimension(R.dimen.photo_height_selected) + 15;

                gridGallery.setMinimumHeight(height);
                gridGallery.getLayoutParams().height = height;
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private class ServiceSync extends AsyncTask<ArrayList<String>, Integer, List<String>> {

        private String res;
        private String boundary;
        private String LINE_FEED = "\r\n";
        private Context context;
        private HttpURLConnection httpConn;
        private String charset = "UTF-8";
        ;
        private OutputStream outputStream;
        private PrintWriter writer;
        ProgressDialog mProgressDialog;
        String requestURL = "http://192.168.0.150:550/TextileApp/webservice/create_post.php";

        int count;

        @Override
        protected void onPreExecute() {
            res = null;
            count = 0;
            //mProgressDialog = ProgressDialog.show(Create_NewsFeed.this, "","Uploading image");
            mProgressDialog = new ProgressDialog(Create_NewsFeed.this);
            mProgressDialog.setMessage("Posting.. please wait...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.show();

            try {
                // creates a unique boundary based on time stamp
                boundary = "===" + System.currentTimeMillis() + "===";

                URL url = new URL(requestURL);
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setUseCaches(false);
                httpConn.setDoOutput(true); // indicates POST method
                httpConn.setDoInput(true);

                httpConn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);
                httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
                httpConn.setRequestProperty("Test", "Bonjour");
                outputStream = httpConn.getOutputStream();
                writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                        true);

                writer.append("User-Agent" + ": " + "CodeJava").append(LINE_FEED);
                writer.flush();
                writer.append("Test-Header" + ": " + "Header-Value").append(LINE_FEED);
                writer.flush();

            } catch (Exception e) {
            }
        }

        @Override
        protected List<String> doInBackground(ArrayList<String>... imgPaths) {
            try {

                // Start parameter
                addFormField("user_id", imgPaths[1].get(0)); //pass user_id
                addFormField("description", imgPaths[1].get(1)); //pass description
                addFormField("category_id", imgPaths[1].get(2)); //pass service id
                //End parameter

                File sourceFile[] = new File[imgPaths[0].size()];
                for (int i = 0; i < imgPaths[0].size(); i++) {
                    sourceFile[i] = new File(imgPaths[0].get(i));
                }

                for (int i = 0; i < imgPaths[0].size(); i++) {
                    addFilePart("uploaded_file[]", sourceFile[i]);
                    count++;
                }
                return multipartfinish();

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(List<String> ress) {
            try {
                if (ress == null) {
                    //finish();
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to upload files", Toast.LENGTH_SHORT).show();
                }

                if (ress.size() > 0) {
                    for (int i = 0; i < ress.size(); i++) {
                        dataT.clear();
                        customtoast.ShowToast(Create_NewsFeed.this, "Posted successfully.", R.layout.blue_toast);
                        Intent intent = new Intent(Create_NewsFeed.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    adapter.clearCache();
                    adapter.clear();
                }

                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                adapter.clearCache();
                adapter.clear();
                objEx.printStackTrace();
            }
        }


        public void addFilePart(String fieldName, File uploadFile)
                throws IOException {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String ext = uploadFile.getName().substring(uploadFile.getName().lastIndexOf(".") + 1);
            String fileName = simpleDateFormat.format(new Date()) + count + "." + ext;

            // String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);

            BitmapFactory.Options options = new BitmapFactory.Options();
            //swt the color scheme to something less memory consuming
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //scale the image by factor 2
            options.inSampleSize = 2;

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (ext.equals("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream); //compress to which format you want.
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream); //compress to which format you want.
            }

            // byte [] buffer = stream.toByteArray();

            InputStream in = new ByteArrayInputStream(stream.toByteArray());
            int totalfilesize = in.available();

            byte[] buffer = new byte[4096];

            int bytesRead = -1;
            int bytesend = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                int progress = (int) ((bytesend / (float) totalfilesize) * 100);
                bytesend += bytesRead;
                if (bytesRead <= totalfilesize) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                publishProgress(progress);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        public void addFormField(String name, String value) {
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                    .append(LINE_FEED);
            writer.append("Content-Type: text/plain; charset=" + charset).append(
                    LINE_FEED);
            writer.append(LINE_FEED);
            writer.append(value).append(LINE_FEED);
            writer.flush();
        }

        public List<String> multipartfinish() throws IOException {
            List<String> response = new ArrayList<String>();

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                writer.print("");
                writer.flush();
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }

            return response;
        }

    }

    @Override
    public void onBackPressed() {
        dataT.clear();
        finish();
    }

    private class getServices extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;
        ArrayList<String> datas;

        @Override
        protected void onPreExecute() {
            res = null;
            mProgressDialog = ProgressDialog.show(
                    Create_NewsFeed.this, "",
                    "Loading...");

            mProgressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {

            PutUtility objClient = new PutUtility();
            try {
                res = objClient.getData("http://192.168.0.150:550/TextileApp/webservice/get_all_services.php");
            } catch (Exception e) {
                Toast.makeText(Create_NewsFeed.this, String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String ress) {
            try {
                /*if (ress == null) {
                    mProgressDialog.dismiss();
                }*/

                JSONArray jArray = new JSONArray(ress.toString());
                if (jArray.length() == 0) {
                    mProgressDialog.dismiss();
                } else {
                    Services = new ArrayList<post_service_entity>();
                    datas = new ArrayList<String>();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        post_service_entity service = new post_service_entity();
                        service.setServiceid(json.getString("id"));
                        service.setServicename(json.getString("name"));
                        service.setServicedetail(json.getString("detail"));
                        Services.add(service);
                        datas.add(json.getString("name"));
                    }

                    /*post_service_adapter = new Post_Service_Adapter(Create_NewsFeed.this, R.layout.spinner_items,Services, getResources());
                    categoryAdapter.setDropDownViewResource
                            (R.layout.spinner_items);*/

                    categoryAdapter = new ArrayAdapter<String>(context, R.layout.spinneritem, datas);
                    categoryAdapter.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(categoryAdapter);

                    /*categorySpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinneritem,
                            datas));*/
                   /* categoryAdapter.setDropDownViewResource
                            (R.layout.spinner_items);*/
                    /*categorySpinner.setAdapter(new ArrayAdapter<String>(context,
                            R.layout.spinner_items,
                            datas));*/
                }
                mProgressDialog.dismiss();
            } catch (Exception objEx) {
                objEx.printStackTrace();
                mProgressDialog.dismiss();
            }
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}
