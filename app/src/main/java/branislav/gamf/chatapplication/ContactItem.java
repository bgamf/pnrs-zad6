package branislav.gamf.chatapplication;

import android.graphics.drawable.Drawable;

public class ContactItem {
    private String firstLetter;
    private String fullName;
    private Drawable image;

    public ContactItem(String fullName, Drawable image) {
        this.firstLetter = fullName.substring(0,1);
        this.fullName = fullName;
        this.image = image;


    }



    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
