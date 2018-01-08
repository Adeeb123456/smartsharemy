package mysmartshare.com.smartsharemy;

/**
 * Created by guestsAll on 12/30/2017.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import mysmartshare.com.smartsharemy.adapters.RecyclerAdapter;
import mysmartshare.com.smartsharemy.adapters.RecyclerSubAdapter;
import mysmartshare.com.smartsharemy.adapters.SmallGalleryAdapter;
import mysmartshare.com.smartsharemy.model.Album;
import mysmartshare.com.smartsharemy.model.GalleryLocObj;
import mysmartshare.com.smartsharemy.model.GalleryObj;

/**
 * Created by Bilal on 6/23/2016.
 */
public class SmartShareService extends Service {

    public static boolean isAppAllowed = false;
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private String lastLockedApp = "";
    private static final int request_permission_code =200;
    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;

    private static ScheduledExecutorService scheduler;
    private ArrayList<String> packList = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;
    public List<Album> albumsList = new ArrayList<>();
    private View imagesDialog;

    private RecyclerView recycle_sub_albums;
    private String selectedAlbumID = "";
    private String selectedSubAlbumID = "";
    private GridView gridView;
    private boolean isLoadingCompleted;
    // private List<GalleryObj> totalGalleryImages = new ArrayList<>();
    private boolean flag_loading;

    private ArrayList<Album> subAlbumsList = new ArrayList<>();
    private ArrayList<Album> firstSubAlbumsList = new ArrayList<>();
    private ArrayList<GalleryObj> preGalleryImages = new ArrayList<>();
    private String selectedShareApp = "";
    private boolean isUserCloseApp = false;
    //private RecyclerAdapter recyvlerAdapter;
    private View selectedView;

    private View coming_soon_viwe;
    private RecyclerView recyclerView;
    private View lastRecyclerSelectedView;
    private View lastSubRecyclerSelectedView;
    private ImageView closeView;
    private GestureDetector gestureDetector;
    private Realm realm;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //isAppAllowed = false;


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //getAlbumsList();
        //getTopImages(0);

        //Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_LONG).show();
        isAppAllowed = false;

        packList.add("com.facebook.lite");
        //packList.add("com.google.android.gm");
        packList.add("com.whatsapp");

        packList.add("com.facebook.orca");

        packList.add("com.skype.raider");
        packList.add("com.viber.voip");
        packList.add("com.imo.android.imoim");
        packList.add("jp.naver.line.android");
        packList.add("com.bsb.hike");
        packList.add("com.google.android.apps.plus");
        packList.add("com.sgiggle.production");
        packList.add("com.facebook.katana");


        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_TIME_TICK);

        realm = Realm.getInstance(getApplicationContext());


        registerReceivers();

        startScheduler();

        RealmResults<Album> result = realm.where(Album.class).equalTo("Type", "album").findAll();

        int size = result.size();

        if (result.size() == 0)
            fetchAllData();
        else
            new LoadImageTask().execute();

        super.onStartCommand(intent, flags, startId);

        //return START_NOT_STICKY;
        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "destroy", Toast.LENGTH_LONG).show();

        endScheduler();


        unRegisterReceivers();

        hideChatHead();


    }

    private void fetchAllData() {


        RequestQueue queue = Volley.newRequestQueue(this);

        int screenW = getScreenWidth();

        int reqW = screenW / 3;

        int reqH = reqW / 2;

        String url = Utilities.getAllDataUrl(reqH, reqW);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {

                            String r = response;

                            Log.i("res", r);

                            JSONObject resp = new JSONObject(response);


                            JSONArray jsonArray = resp.getJSONArray("album");
                            Log.i("debug","bg JsonArray Album: "+jsonArray);
                            albumsList.clear();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String s = jsonObject.toString();
                                Album album = new Album(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getString("thumb_img"), jsonObject.getString("thumb_dimg"), true);

                                realm.beginTransaction();
                                realm.copyToRealm(album);

                                realm.commitTransaction();
                            }

                            JSONArray subAlbumArray = resp.getJSONArray("subalbum");
                            Log.i("debug","bg Json sub AlbumArray: "+subAlbumArray);
                            for (int i = 0; i < subAlbumArray.length(); i++) {
                                JSONObject jsonObject = subAlbumArray.getJSONObject(i);
                                String s = jsonObject.toString();
                                Album album = new Album(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getString("thumb_img"), jsonObject.getString("thumb_dimg"), jsonObject.getString("album_id"), true);
                                realm.beginTransaction();
                                realm.copyToRealm(album);
                                realm.commitTransaction();

                            }


                            JSONArray imagesList = resp.getJSONArray("images");
                            Log.i("debug","bg Json imagesList: "+imagesList);
                            for (int i = 0; i < imagesList.length(); i++) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                try {
                                    GalleryLocObj galleryLocObj = objectMapper.readValue(imagesList.get(i).toString(), GalleryLocObj.class);
                                    realm.beginTransaction();
                                    realm.copyToRealm(galleryLocObj);
                                    realm.commitTransaction();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            //realm.close();


                            new LoadImageTask().execute();


                            //Toast.makeText(getActivity(), country, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException nl) {

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void startLoadingImages() {


        Realm myRealmObj = Realm.getInstance(getApplicationContext());

        RealmResults<Album> allAlbums = myRealmObj.where(Album.class).findAll();
        //LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
        for (int i = 0; i < allAlbums.size(); i++) {
            Album alb = allAlbums.get(i);
            String url = alb.getImageUrl();
            String slcted = alb.getSelectedImageUrl();

            myRealmObj.beginTransaction();


            if (alb.getImageBytes() == null || alb.getImageBytes().length == 0) {
                byte[] bitmap = getBitmapFromURL(url);
                alb.setImageBytes(bitmap);
                //
                //  Bitmap bitmap = getBitmapFromURL(url);
                //cache.put(url,bitmap);
            }

            if (alb.getSltdImageBytes() == null || alb.getSltdImageBytes().length == 0) {
                byte[] bitmap = getBitmapFromURL(slcted);
                alb.setSltdImageBytes(bitmap);

            }

            myRealmObj.copyToRealm(allAlbums);
            myRealmObj.commitTransaction();


        }

    }


    private void startScheduler() {
        if (scheduler != null)
            scheduler.shutdownNow();
        scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        // call service

                        //showChatHeader();
                        Message message = new Message();

                        msg_handler.sendMessage(message);
                    }
                }, 0, 1, TimeUnit.SECONDS);
    }


    private void endScheduler() {
        if (scheduler != null) {

            scheduler.shutdownNow();
        }
    }


    private void showChatHeader() {
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);


// get the info from the currently running task

        String mPackageName = "";


        if (Build.VERSION.SDK_INT > 20) {
           /* mPackageName = am.getRunningAppProcesses().get(0).processName;*/
            if(hasPermission())
            {
                String currentActivity=getUsageStatsForegroundActivityName();
               /* Toast.makeText(this, "currentActivityis===="+currentActivity, Toast.LENGTH_SHORT).show();*/
                mPackageName=currentActivity;
            }
            else
            {
               /* Toast.makeText(this, "not have permission", Toast.LENGTH_SHORT).show();*/
                mPackageName="cac";

            }
        } else {
            mPackageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        }

        if (!mPackageName.equals(selectedShareApp)) {
            isUserCloseApp = false;
        }

        //Log.i("pName", mPackageName + "");

        if (mPackageName.equals("com.google.android.gms.ads") || mPackageName.equals("com.mobitsolutions.applockadvancephonesecurity") || mPackageName.equals("system:ui") || mPackageName.equals("android")) {
            Log.i("ignore", "ignore");

            return;
        }


        //packList.add("com.android.settings");

      /*  if(lastLockedApp.equals(mPackageName))
            return;*/

        //Log.i("pack",mpackageName);
        if (packList.contains(mPackageName)) {
            if (!isUserCloseApp) {
                showShareHead();
                selectedShareApp = mPackageName;
            }

        } else {
           /* showShareHead();*/
            hideChatHead();
        }
    }

    BroadcastReceiver mybroadcast = new BroadcastReceiver() {
        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("[BroadcastReceiver]", "MyReceiver");

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                startScheduler();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("[BroadcastReceiver]", "Screen OFF");
                endScheduler();
            }

        }
    };

    private void registerReceivers() {
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    private void unRegisterReceivers() {
        unregisterReceiver(mybroadcast);
    }

    private void showShareHead() {

        if (chatHead != null)
            return;


        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels - 200;
        screenWidth = displaymetrics.widthPixels - 200;


        chatHead = new ImageView(this);

        chatHead.setImageResource(R.drawable.face1);


        params = new WindowManager.LayoutParams(
                dpToPx(65),
                dpToPx(65),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 20;
        params.y = 100;

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                gestureDetector.onTouchEvent(event);


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();


                        break;
                    case MotionEvent.ACTION_UP:
                        if (closeView != null)
                            closeView.setVisibility(View.GONE);
                        float endX = event.getX();
                        float endY = event.getY();


                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        closeView.setVisibility(View.GONE);

                        break;

                  /*  case MotionEvent.ACTION_BUTTON_PRESS:
                        if(imagesDialog==null)
                        {
                            lastAlbumPosition = 0;
                            lastRecyclerSelectedView = null;
                            lastSubAlbumPosition = 0;

                            showSharedImagesDialog();}
                        else{
                            hideImagesDialog();
                        }

                        break;*/

                    case MotionEvent.ACTION_MOVE:

                        if (isViewOverlapping(closeView, chatHead))
                            hideChatHead(true);

                        if (closeView != null)
                            closeView.setVisibility(View.VISIBLE);
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);

                        Log.i("y", params.y + "");
                        Log.i("x", params.x + "");


                        Log.i("x axis", params.x + "");
                        Log.i("y axis", params.y + "");
                        if (chatHead != null)
                            windowManager.updateViewLayout(chatHead, params);

                       /* if (params.y < 10)
                            hideChatHead(true);
                        else if (params.x < 10)
                            hideChatHead(true);
                        else if (params.x >= screenWidth)
                            hideChatHead(true);
                        else if (params.y >= screenHeight)
                            hideChatHead(true);
                        else {
                            Log.i("x axis", params.x + "");
                            Log.i("y axis", params.y + "");
                            windowManager.updateViewLayout(chatHead, params);
                        }*/
                        return false;
                }
                return false;
            }
        });
        try {
            addCloseButton();
            windowManager.addView(chatHead, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSharedImagesDialog() {


        if (imagesDialog != null)
            return;

        final LayoutInflater mInflater = (LayoutInflater)
                this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        imagesDialog = mInflater.inflate(R.layout.share_dialog, null);

        imagesDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImagesDialog();
            }
        });

        imagesDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView = (RecyclerView) imagesDialog.findViewById(R.id.recycle_albums);
        recycle_sub_albums = (RecyclerView) imagesDialog.findViewById(R.id.recycle_sub_albums);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //view.setSelected(true);


                        //getSubAlbumsList(albumsList.get(position).getId());
                        RealmResults<Album> list = realm.where(Album.class).equalTo("albumId", albumsList.get(position).getId()).findAll();
                        subAlbumsList.clear();
                        for (int i = 0; i < list.size(); i++) {

                            subAlbumsList.add(list.get(i));
                        }
                        Log.i("size", list.size() + "");
                        Log.i("val", albumsList.get(position).getId() + "");
                        recycle_sub_albums.setAdapter(new RecyclerSubAdapter(getApplicationContext(), list));


                        selectedAlbumID = albumsList.get(position).getId();


                        //View lastView = recyclerView.getChildAt(lastAlbumPosition);


                        if (lastRecyclerSelectedView != null) {
                            lastRecyclerSelectedView.findViewById(R.id.drawer_icon).setVisibility(View.GONE);
                            lastRecyclerSelectedView.findViewById(R.id.selectedImageView).setVisibility(View.VISIBLE);
                        } else {
                            //Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
                            recyclerView.getChildAt(0).findViewById(R.id.drawer_icon).setVisibility(View.GONE);
                            recyclerView.getChildAt(0).findViewById(R.id.selectedImageView).setVisibility(View.VISIBLE);
                        }


                        view.findViewById(R.id.drawer_icon).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.selectedImageView).setVisibility(View.GONE);


                        lastRecyclerSelectedView = view;

                        setFirstSubAlbumAndGallery(selectedAlbumID);

                        lastSubRecyclerSelectedView = null;


                    }
                })
        );


        recycle_sub_albums.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //view.setSelected(true);


                        if (lastSubRecyclerSelectedView != null) {
                            lastSubRecyclerSelectedView.findViewById(R.id.drawer_icon).setVisibility(View.GONE);
                            lastSubRecyclerSelectedView.findViewById(R.id.selectedImageView).setVisibility(View.VISIBLE);
                        } else {
                            //Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
                            recycle_sub_albums.getChildAt(0).findViewById(R.id.drawer_icon).setVisibility(View.GONE);
                            recycle_sub_albums.getChildAt(0).findViewById(R.id.selectedImageView).setVisibility(View.VISIBLE);
                        }

                        view.findViewById(R.id.drawer_icon).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.selectedImageView).setVisibility(View.GONE);

                        lastSubRecyclerSelectedView = view;


                        getThumbNails(selectedAlbumID, subAlbumsList.get(position).getId());


                    }
                })
        );


        gridView = (GridView) imagesDialog.findViewById(R.id.gridView);
        coming_soon_viwe = imagesDialog.findViewById(R.id.coming_soon);


    /*    SmallGalleryAdapter adapter = new SmallGalleryAdapter(selectedShareApp, getApplicationContext(), preGalleryImages);
        gridView.setAdapter(adapter);*/


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(), "clic", Toast.LENGTH_SHORT).show();


            }
        });

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recycle_sub_albums.setLayoutManager(layoutManager2);

        int s = albumsList.size();


        Log.i("sssssss", s + "");
        realm = Realm.getInstance(getApplicationContext());
        albumsList = realm.where(Album.class).equalTo("Type", "album").findAll();
        recyclerView.setAdapter(new RecyclerAdapter(this, realm.where(Album.class).equalTo("Type", "album").findAll()));

        selectedAlbumID = albumsList.get(0).getId();


        setFirstSubAlbumAndGallery(albumsList.get(0).getId());

        WindowManager.LayoutParams paramss = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);


        paramss.gravity = Gravity.TOP | Gravity.CENTER;
        paramss.x = 0;
        paramss.y = displaymetrics.heightPixels - dpToPx(210);
        paramss.horizontalMargin = 10;

        Log.i("yyyy", displaymetrics.heightPixels - dpToPx(200) + "");


        int hi = imagesDialog.getHeight();


        windowManager.addView(imagesDialog, paramss);


        WindowManager.LayoutParams newparams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


    }


    public int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    private void hideChatHead() {
        recyclerView = null;
        if (chatHead != null) {
            windowManager.removeView(chatHead);
            chatHead = null;
        }

        if (imagesDialog != null) {
            windowManager.removeView(imagesDialog);
            imagesDialog = null;
        }

        if (closeView != null) {
            windowManager.removeView(closeView);
            closeView = null;
        }
    }


    private void hideChatHead(boolean isHideByUser) {
        recyclerView = null;
        isUserCloseApp = isHideByUser;
        if (chatHead != null) {
            windowManager.removeView(chatHead);
            chatHead = null;
        }

        if (imagesDialog != null) {
            windowManager.removeView(imagesDialog);
            imagesDialog = null;
        }

        if (closeView != null) {
            windowManager.removeView(closeView);
            closeView = null;
        }
    }

    private void hideImagesDialog() {
        recyclerView = null;
        if (imagesDialog != null) {
            windowManager.removeView(imagesDialog);
            imagesDialog = null;
        }


    }

    Handler msg_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showChatHeader();

        }
    };


    public boolean checkInternetServices() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }



    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    private void getTopImages(int pageNo) {


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "";


        url = Utilities.getTopShared(getScreenWidth() / 4, pageNo);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.


                        try {


                            String r = response;

                            JSONObject jsonObject = new JSONObject(r);

                            String code = jsonObject.getString("code");

                            if (!code.equals("100")) {


                                return;
                            }

                            Log.i("response", r);

                            ObjectMapper mapper = new ObjectMapper();


                            preGalleryImages = mapper.readValue(jsonObject.getJSONArray("data").toString(), new TypeReference<List<GalleryObj>>() {
                            });


                        } catch (NullPointerException nl) {

                        } catch (Exception s) {
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void clearPiviousImages() {
        ArrayList<GalleryLocObj> galleryObjs = new ArrayList<>();
        SmallGalleryAdapter adapter = new SmallGalleryAdapter(selectedShareApp, getApplicationContext(), galleryObjs);
        gridView.setAdapter(adapter);
    }

    private void getThumbNails(String id, String subID) {
        Log.i("values are", id + "<=>" + subID);
        RealmResults<GalleryLocObj> list = realm.where(GalleryLocObj.class).equalTo("albumId", id).beginGroup().equalTo("subAlbId", subID).endGroup().findAll();
        if (list.size() == 0) {
            coming_soon_viwe.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        } else {
            coming_soon_viwe.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new SmallGalleryAdapter(selectedShareApp, getApplicationContext(), list));
        }
    }


    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            chatHead.clearAnimation();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(chatHead.getWidth(), chatHead.getHeight());

            chatHead.setLayoutParams(lp);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    private void addCloseButton() {

        if (closeView != null)
            windowManager.removeView(closeView);

        WindowManager.LayoutParams newParams = new WindowManager.LayoutParams(
                dpToPx(65),
                dpToPx(65),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        newParams.gravity = Gravity.BOTTOM | Gravity.CENTER;


        closeView = new ImageView(getApplicationContext());
        closeView.setImageResource(R.drawable.ic_circle_cancel);
        windowManager.addView(closeView, newParams);
        closeView.setVisibility(View.GONE);
    }

    private boolean isViewOverlapping(ImageView firstView, ImageView secondView) {
        try {
            int[] firstPosition = new int[2];
            int[] secondPosition = new int[2];

            firstView.measure(View.MeasureSpec.AT_MOST,
                    View.MeasureSpec.AT_MOST);

            firstView.getLocationOnScreen(firstPosition);

            secondView.getLocationOnScreen(secondPosition);

            int r = firstView.getMeasuredWidth() + firstPosition[1];
            int l = secondPosition[1];

            if (Math.abs(firstPosition[0] - secondPosition[0]) < 30 && Math.abs(firstPosition[1] - secondPosition[1]) < 30)
                return true;
            else
                return false;
        } catch (Exception exception) {
            return true;
        }

/*

        System.out.println(r + " : r + l : " + l);

        return r == l && (r != 0 && l != 0);*/
    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            if (imagesDialog == null) {
                lastSubRecyclerSelectedView = null;
                lastRecyclerSelectedView = null;


                showSharedImagesDialog();
                addCloseButton();
            } else {
                hideImagesDialog();
            }
            Log.i("single conf", "sing");
            return true;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    private void setFirstSubAlbumAndGallery(String id) {

        RealmResults<Album> list = realm.where(Album.class).equalTo("albumId", id).findAll();

        setGlobalAlbums(list);

        RecyclerSubAdapter albumAdapter = new RecyclerSubAdapter(getApplicationContext(), realm.where(Album.class).equalTo("albumId", id).findAll());

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        recycle_sub_albums.setAdapter(albumAdapter);

        getThumbNails(selectedAlbumID, subAlbumsList.get(0).getId());

    }


    private void setGlobalAlbums(RealmResults<Album> list) {
        subAlbumsList.clear();
        for (int i = 0; i < list.size(); i++) {
            subAlbumsList.add(list.get(i));
        }
    }


    public byte[] getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class LoadImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            startLoadingImages();
            return null;
        }
    }

    public boolean storeImage(Bitmap imageData, String filename, boolean showMessage) {
        // get path to external storage (SD card)

        File sdIconStorageDir = null;

        if (showMessage)
  /*       sdIconStorageDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/");*/
            sdIconStorageDir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "ChatMoodStickers" + "/");
        else
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


            if (showMessage)
                Toast.makeText(getApplicationContext(), "Image has been saved in " + sdIconStorageDir, Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getUsageStatsForegroundActivityName() {


        UsageStatsManager mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 1000 * 60;

        // result
        String topActivity = null;

        // We get usage stats for the last minute
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);

        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                topActivity = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
               /* topActivity = mySortedMap.get(size).getPackageName();*/
               /* Logger logger=null;
                logger.info("topActivity: " + topActivity);*/
            }
        }
        if (topActivity != null)
            return topActivity;
        else
            return "ACTIVITY_NOT_FOUND";

    }

}





