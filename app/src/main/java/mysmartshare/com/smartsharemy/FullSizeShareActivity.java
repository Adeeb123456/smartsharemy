package mysmartshare.com.smartsharemy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mysmartshare.com.smartsharemy.networkutil.VolleySingleton;
import mysmartshare.com.smartsharemy.views.IndeterminateTransparentProgressDialog;

public class FullSizeShareActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PACKAGE_NAME = "packName";
    public static final String IMAGE_ID = "id";
    public static final String IMAGE_URL = "img_url";
    private IndeterminateTransparentProgressDialog progressDialog;
    private static final String FILE_NAME = "Schedule.png";
    ImageView imageView;
    private String packName;
    Button btn_send;
    private EditText editText;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_share);

        editText = (EditText) findViewById(R.id.editText);

        imageView = (ImageView) findViewById(R.id.imageView);

        btn_send = (Button) findViewById(R.id.btn_send);
        packName = getIntent().getStringExtra(PACKAGE_NAME);
        id = getIntent().getStringExtra(IMAGE_ID);
        String imageUrl = getIntent().getStringExtra(IMAGE_URL);

        btn_send.setOnClickListener(this);


        loadImage(imageUrl);
    }

    @Override
    public void onClick(View v) {


        sendShareFeedback(id);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {""};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.setType("image/png");

        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myAppDir/", FILE_NAME);

        //a = filelocation.isHidden();
        Boolean b = filelocation.exists();

        Uri path = Uri.fromFile(filelocation);

// the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        emailIntent.setPackage(packName);
// the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, editText.getText().toString());
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
        finish();
    }

    private void loadImage(String url) {


        progressDialog = IndeterminateTransparentProgressDialog.show(FullSizeShareActivity.this, true, false);


        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                        if (isFinishing())
                            return;

                        if (progressDialog != null)
                            progressDialog.cancel();

                        btn_send.setEnabled(true);


                        imageView.setImageBitmap(getResizedBitmap(bitmap));


                        storeImage(bitmap, FILE_NAME);
                        //storeImage(bitmap, names.get(viewpager.getCurrentItem()).replace(" ","-")+".png", true);


                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null)
                            progressDialog.cancel();
                    }
                });


// Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public boolean storeImage(Bitmap imageData, String filename) {
        // get path to external storage (SD card)

        File sdIconStorageDir = null;


        sdIconStorageDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/myAppDir/");
        // create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();
        try {
            String filePath = sdIconStorageDir.toString() + File.separator + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            //Toast.makeText(m_cont, "Image Saved at----" + filePath, Toast.LENGTH_LONG).show();
            // choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();


        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }


    private Bitmap getResizedBitmap(Bitmap bitmap) {
        int height = 0;
        int width = 0;


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;


        Bitmap background = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        float originalWidth = bitmap.getWidth(), originalHeight = bitmap.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = width / originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, transformation, paint);
        return background;
    }


    private void sendShareFeedback(String mId) {
        //progressDialog = IndeterminateTransparentProgressDialog.show(this, true, false);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "";

        url = Utilities.BaseUrl + "/picgallery/admin/api.php?act=sharedimage&imgid=" + mId;


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
