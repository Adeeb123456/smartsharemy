package mysmartshare.com.smartsharemy.model;

/**
 * Created by Bilal on 7/18/2016.
 */



import io.realm.RealmObject;
import mysmartshare.com.smartsharemy.Utilities;


public class Album extends RealmObject {


    private String id;

    private String title;

    private String imageUrl;

    private String selectedImageUrl;

    private boolean haschilds=false;

    private boolean isHasChildImages=false;

    private boolean isSelected = true;

    private int totalCounts = 0;

    private String Type ="";

    private String albumId;

    private byte[] imageBytes;

    private byte[] sltdImageBytes;

    public Album(){

    }


    public Album(String id, String title, String imageUrl, String hasChild, String hasChildImag, int totalCounts, String selectedImageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;

        if (hasChild.equals("yes"))
            haschilds = true;
        else haschilds = false;

        if (hasChildImag.equals("yes"))
            this.isHasChildImages = true;
        else
            this.isHasChildImages = false;

        this.totalCounts = totalCounts;

        this.selectedImageUrl = selectedImageUrl;

    }


    public Album(String id, String title, String imageUrl, String hasChildImag, int totalCounts, String selectedImageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.selectedImageUrl = selectedImageUrl;



        if (hasChildImag.equals("yes"))
            this.isHasChildImages = true;
        else
            this.isHasChildImages = false;


        this.totalCounts = totalCounts;
    }
    public Album(String id, String title, String imageUrl, String hasChild, String hasChildImag, String selectedImageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;

        if (hasChild.equals("yes"))
            haschilds = true;
        else haschilds = false;

        if (hasChildImag.equals("yes"))
            this.isHasChildImages = true;
        else
            this.isHasChildImages = false;

        this.selectedImageUrl = selectedImageUrl;

    }

    public Album(String id, String title, String imageUrl, String hasChild, String hasChildImag) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;

        if (hasChild.equals("yes"))
            haschilds = true;
        else haschilds = false;

        if (hasChildImag.equals("yes"))
            this.isHasChildImages = true;
        else
            this.isHasChildImages = false;

    }

    public Album(String id, String title, String imageUrl, String hasChildImag) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;



        if (hasChildImag.equals("yes"))
            this.isHasChildImages = true;
        else
            this.isHasChildImages = false;

    }

    public Album(String id, String title, String imageUrl, String selectedImageUrl, boolean a) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.selectedImageUrl = selectedImageUrl;
        this.Type= "album";
    }

    public Album(String id, String title, String imageUrl, String selectedImageUrl, String AlbumID , boolean a) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.selectedImageUrl = selectedImageUrl;
        this.albumId = AlbumID;
        this.Type= "subalbum";
    }

    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The title
     */

    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */

    public void setTitle(String title) {
        this.title = title;

    }


    public String getImageUrl() {
        //String v = imageUrl.substring(2);
        //Log.i("conv valu", Utilities.Image_BASE_URL+v);


        return Utilities.Image_BASE_URL + imageUrl.replace(" ", "%20");
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHaschilds() {
        return haschilds;
    }

    public void setHaschilds(boolean haschilds) {
        this.haschilds = haschilds;
    }

    public boolean isHasChildImages() {
        return isHasChildImages;
    }

    public void setHasChildImages(boolean hasChildImages) {
        isHasChildImages = hasChildImages;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(int totalCounts) {
        this.totalCounts = totalCounts;
    }

    public String getSelectedImageUrl() {


        return Utilities.Image_BASE_URL + selectedImageUrl.replace(" ", "%20");

    }

    public void setSelectedImageUrl(String selectedImageUrl) {
        this.selectedImageUrl = selectedImageUrl;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public byte[] getSltdImageBytes() {
        return sltdImageBytes;
    }

    public void setSltdImageBytes(byte[] sltdImageBytes) {
        this.sltdImageBytes = sltdImageBytes;
    }
}