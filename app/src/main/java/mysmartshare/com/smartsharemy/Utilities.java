package mysmartshare.com.smartsharemy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.view.Display;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import mysmartshare.com.smartsharemy.networkutil.VolleySingleton;


/**
 * Created by Bilal on 7/14/2016.
 */
public class Utilities {

    public static final boolean SHOW_ADS = true;
    public static final String BaseUrl = "http://198.50.129.105";
    public static final String ALBUMS_URL = "/picgallery/admin/api.php?act=albums";
    public static final String Image_BASE_URL = "http://198.50.129.105/picgallery/";

    public static final String ALBUM_URL = BaseUrl + ALBUMS_URL + "&th=80&tw=80";


    public static String getAllDataUrl(int height, int width) {
        return BaseUrl + "/picgallery/admin/api.php?act=alldata&ih=80&iw=80&th=" + height + "&tw=" + width;
    }

    public static String getSubAlbumUrl(String id) {


        return BaseUrl + "/picgallery/admin/api.php?act=subalbums&album_id=" + id + "&th=80&tw=80";
    }


    public static String getAlbumsUrl(int width, String albumid, String subAlbum) {


        int reqW = width / 3;
        int reqH = reqW / 2;

        return BaseUrl + "/picgallery/admin/api.php?act=alimages&album=" + albumid + "&subalbum=" + subAlbum + "&th=" + reqH + "&tw=" + reqW;
    }

    public static String getAlbumsUrl(Activity activity, String albumid, String subAlbum, int pageNo) {
        //return BaseUrl + "/picgallery/admin/api.php?act=images&album=" + galleryID + "&page=" + pageNo+"&th=200&tw=200";

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return BaseUrl + "/picgallery/admin/api.php?act=images&album=" + albumid + "&subalbum=" + subAlbum + "&page=" + pageNo + "&th=" + width / 2 + "&tw=" + width;
    }

    public static String getNewAlbumUrl(Activity activity, int pageNo) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return BaseUrl + "/picgallery/admin/api.php?act=images_date&page=" + pageNo + "&th=" + width / 2 + "&tw=" + width;
    }


    public static String getTopShared(int width, int pageNo) {


        return BaseUrl + "/picgallery/admin/api.php?act=images_top&page=" + pageNo + "&th=" + width / 2 + "&tw=" + width;
    }

    public static String getTopShared(Activity activity, int pageNo) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return BaseUrl + "/picgallery/admin/api.php?act=images_top&page=" + pageNo + "&th=" + width / 2 + "&tw=" + width;
    }

    public static String getRandom(Activity activity, int pageNo) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return BaseUrl + "/picgallery/admin/api.php?act=randomimages&page=" + pageNo + "&th=" + width / 2 + "&tw=" + width;
    }

    public static void loadImage(Context context, String url, NetworkImageView networkImageView) {
        ImageLoader mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        networkImageView.setImageUrl(url, mImageLoader);
        networkImageView.setErrorImageResId(R.drawable.black);
        networkImageView.setDefaultImageResId(R.drawable.black);
    }


    public static void loadImage(Context context, String url, NetworkImageView networkImageView, boolean val) {
        ImageLoader mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        networkImageView.setErrorImageResId(R.drawable.circle_loading);
        networkImageView.setDefaultImageResId(R.drawable.circle_loading);
        networkImageView.setImageUrl(url, mImageLoader);
    }


    public static void showInternetServicesErrorPopup(Activity activity) {


        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Connectivity Issue");
        alertDialog.setMessage("Please connect to internet");

        alertDialog.show();


        //showPopup(activity.getString(R.string.internet_error_title), activity.getString(R.string.internet_error_message), "", activity.getString(R.string.label_ok), REQUEST_INTERNET);
    }

    public static void showDefaultErrorDialog(Activity activity, String message) {


        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
        alertDialog.show();


        //showPopup(activity.getString(R.string.internet_error_title), activity.getString(R.string.internet_error_message), "", activity.getString(R.string.label_ok), REQUEST_INTERNET);
    }


}
