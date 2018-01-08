package mysmartshare.com.smartsharemy.model;

/**
 * Created by Bilal on 7/19/2016.
 */

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import java.util.HashMap;
import java.util.Map;

import mysmartshare.com.smartsharemy.Utilities;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "id",
        "img",
        "thumb_img",
        "title",
        "descr",
        "disp_order",
        "status"
})
public class GalleryObj {

    @JsonProperty("id")
    private String id;
    @JsonProperty("img")
    private String img;
    @JsonProperty("title")
    private String title;
    @JsonProperty("descr")
    private String descr;
    @JsonProperty("disp_order")
    private String dispOrder;
    @JsonProperty("status")
    private String status;
    @JsonProperty("thumb_img")
    private String thumb_img;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public GalleryObj() {

    }

    public GalleryObj(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    private boolean isEmpty=false;

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The img
     */
    @JsonProperty("img")
    public String getImg() {

        String v = img.substring(2);
        Log.i("conv valu",Utilities.Image_BASE_URL+v);
        return Utilities.Image_BASE_URL+v;


    }

    /**
     *
     * @param img
     * The img
     */
    @JsonProperty("img")
    public void setImg(String img) {
        this.img = img;
    }

    /**
     *
     * @return
     * The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The descr
     */
    @JsonProperty("descr")
    public String getDescr() {
        return descr;
    }

    /**
     *
     * @param descr
     * The descr
     */
    @JsonProperty("descr")
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     *
     * @return
     * The dispOrder
     */
    @JsonProperty("disp_order")
    public String getDispOrder() {
        return dispOrder;
    }

    /**
     *
     * @param dispOrder
     * The disp_order
     */
    @JsonProperty("disp_order")
    public void setDispOrder(String dispOrder) {
        this.dispOrder = dispOrder;
    }

    /**
     *
     * @return
     * The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getThumb_img() {
try {
    return Utilities.Image_BASE_URL + thumb_img.replace(" ", "%20");
}
catch (Exception e){
    return  "";
}
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}