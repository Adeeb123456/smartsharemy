package mysmartshare.com.smartsharemy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;


import java.util.ArrayList;
import java.util.List;

import mysmartshare.com.smartsharemy.FullSizeShareActivity;
import mysmartshare.com.smartsharemy.R;
import mysmartshare.com.smartsharemy.model.GalleryLocObj;
import mysmartshare.com.smartsharemy.networkutil.VolleySingleton;
import mysmartshare.com.smartsharemy.views.RectImageView;

/**
 * Created by Maxxsol on 3/20/2015.
 */
public class SmallGalleryAdapter extends ArrayAdapter<GalleryLocObj> {
    private final ImageLoader mImageLoader;
    private List<GalleryLocObj> list = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;
    private boolean showButtonLoad;
    private Holder holder;
    String mPackageName = "";


    public SmallGalleryAdapter(String packName, Context context, List<GalleryLocObj> values) {
        super(context, 0, values);
        mContext = context;
        list = values;
        mPackageName = packName;
        mImageLoader = VolleySingleton.getInstance(mContext).getImageLoader();

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        holder = new Holder();


        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.list_item_rect_galley, null);


            holder.gallery_img = (RectImageView) view.findViewById(R.id.gallery_img);


            view.setTag(holder);

        } else
            holder = (Holder) view.getTag();


        Log.i("xyx", list.get(position).getThumbImg());



            //Utilities.loadImage(mContext,list.get(position).getThumb_img(),holder.gallery_img);
            holder.gallery_img.setTag(position);
            holder.gallery_img.setImageUrl(list.get(position).getThumbImg(), mImageLoader);
            holder.gallery_img.setErrorImageResId(R.drawable.loading_im);
            holder.gallery_img.setDefaultImageResId(R.drawable.loading_im);

        holder.gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int pos = (int) v.getTag();
                    GalleryLocObj galleryObj = list.get(pos);

                        Intent intent = new Intent(mContext, FullSizeShareActivity.class);
                        intent.putExtra(FullSizeShareActivity.PACKAGE_NAME, mPackageName);
                        intent.putExtra(FullSizeShareActivity.IMAGE_ID, galleryObj.getId());
                        intent.putExtra(FullSizeShareActivity.IMAGE_URL, galleryObj.getImg());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        mContext.startActivity(intent);

                }catch (Exception s){
                    Toast.makeText(getContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });


        view.setTag(holder);


        return view;

    }

    public class Holder {
        RectImageView gallery_img;


    }
}
