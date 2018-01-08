package mysmartshare.com.smartsharemy.adapters;

/**
 * Created by Bilal on 7/26/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



import java.util.List;

import mysmartshare.com.smartsharemy.R;
import mysmartshare.com.smartsharemy.Utilities;
import mysmartshare.com.smartsharemy.model.Album;
import mysmartshare.com.smartsharemy.views.CustomNetworkImageView;

public class RecyclerSubAdapter extends RecyclerView.Adapter<RecyclerSubAdapter.CustomViewHolder> {
    private List<Album> feedItemList;
    private Context mContext;
    private int selectedPosition = 0;

    public RecyclerSubAdapter(Context context, List<Album> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sub_recycler_album, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Album feedItem = feedItemList.get(i);


        //Setting text view title

        //Utilities.loadImage(mContext, feedItem.getImageUrl(), customViewHolder.imageView,true);
        //Utilities.loadImage(mContext, feedItem.getSelectedImageUrl(), customViewHolder.selectedImageView, true);

        Bitmap unslctd = getSavedBitmap(feedItem, false);
        Bitmap slct = getSavedBitmap(feedItem, true);

        if (unslctd == null)
            Utilities.loadImage(mContext, feedItem.getImageUrl(), customViewHolder.imageView, true);
        else
            customViewHolder.imageView.setLocalImageBitmap(getResizedBitmap(unslctd));

        if (slct == null)
            Utilities.loadImage(mContext, feedItem.getSelectedImageUrl(), customViewHolder.selectedImageView, true);
        else
            customViewHolder.selectedImageView.setLocalImageBitmap(getResizedBitmap(slct));

        if (selectedPosition==i) {


            customViewHolder.imageView.setVisibility(View.VISIBLE);
            customViewHolder.selectedImageView.setVisibility(View.GONE);


        } else
        {
            customViewHolder.imageView.setVisibility(View.GONE);
            customViewHolder.selectedImageView.setVisibility(View.VISIBLE);
        }
        customViewHolder.parent_line.setTag(i);

        customViewHolder.parent_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer)v.getTag();
                selectedPosition = pos;
                Log.i("its workin","yes it is");
            }
        });

        customViewHolder.bg.setSelected(feedItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected CustomNetworkImageView imageView;
        protected CustomNetworkImageView selectedImageView;
        protected LinearLayout bg;
        protected LinearLayout parent_line;


        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (CustomNetworkImageView) view.findViewById(R.id.drawer_icon);
            this.selectedImageView = (CustomNetworkImageView) view.findViewById(R.id.selectedImageView);
            this.bg = (LinearLayout) view.findViewById(R.id.bg);
            this.parent_line = (LinearLayout)view.findViewById(R.id.parent_line);

        }

        @Override
        public void onClick(View v) {


            /*
            if(feedItemList.get(getAdapterPosition()).isSelected())
            {
                Album obj = feedItemList.get(getAdapterPosition());
                obj.setSelected(false);
                feedItemList.set(getAdapterPosition(), obj);
            }
            else
            {
                Album obj = feedItemList.get(getAdapterPosition());
                obj.setSelected(true);
                feedItemList.set(getAdapterPosition(), obj);
            }*/
        }
    }


    private Bitmap getSavedBitmap(Album album, boolean isSlect) {
        try {
            byte[] image = null;
            if (isSlect)
                image = album.getSltdImageBytes();
            else
                image = album.getImageBytes();
            BitmapFactory.Options options = new BitmapFactory.Options();

            return BitmapFactory.decodeByteArray(image, 0, image.length, options);
        } catch (Exception d) {
            d.printStackTrace();
        }
        return null;
    }

    private Bitmap getResizedBitmap(Bitmap bitmap) {

        return Bitmap.createScaledBitmap(bitmap, 80, 80, false);}

}