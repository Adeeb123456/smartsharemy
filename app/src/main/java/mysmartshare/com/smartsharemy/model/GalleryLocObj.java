package mysmartshare.com.smartsharemy.model;

/**
 * Created by BilalKhalid on 8/7/2016.
 */

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import io.realm.RealmObject;
import mysmartshare.com.smartsharemy.Utilities;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "id",
        "sub_alb_id",
        "album_id",
        "title",
        "img",
        "thumb_img",
        "descr",
        "share_count"
})
public class GalleryLocObj extends RealmObject {

    @JsonProperty("id")
    private String id;
    @JsonProperty("sub_alb_id")
    private String subAlbId;
    @JsonProperty("album_id")
    private String albumId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("img")
    private String img;
    @JsonProperty("thumb_img")
    private String thumbImg;
    @JsonProperty("descr")
    private String descr;
    @JsonProperty("share_count")
    private String shareCount;

    public GalleryLocObj() {
    }


    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The subAlbId
     */
    @JsonProperty("sub_alb_id")
    public String getSubAlbId() {
        return subAlbId;
    }

    /**
     * @param subAlbId The sub_alb_id
     */
    @JsonProperty("sub_alb_id")
    public void setSubAlbId(String subAlbId) {
        this.subAlbId = subAlbId;
    }

    /**
     * @return The albumId
     */
    @JsonProperty("album_id")
    public String getAlbumId() {
        return albumId;
    }

    /**
     * @param albumId The album_id
     */
    @JsonProperty("album_id")
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    /**
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The img
     */
    @JsonProperty("img")
    public String getImg() {
        String v = img.substring(2);
        Log.i("conv valu", Utilities.Image_BASE_URL + v);
        return Utilities.Image_BASE_URL + v;
    }

    /**
     * @param img The img
     */
    @JsonProperty("img")
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return The thumbImg
     */
    @JsonProperty("thumb_img")
    public String getThumbImg() {
        try {
            return Utilities.Image_BASE_URL + thumbImg.replace(" ", "%20");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param thumbImg The thumb_img
     */
    @JsonProperty("thumb_img")
    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    /**
     * @return The descr
     */
    @JsonProperty("descr")
    public String getDescr() {
        return descr;
    }

    /**
     * @param descr The descr
     */
    @JsonProperty("descr")
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * @return The shareCount
     */
    @JsonProperty("share_count")
    public String getShareCount() {
        return shareCount;
    }

    /**
     * @param shareCount The share_count
     */
    @JsonProperty("share_count")
    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }



}


