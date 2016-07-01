package de.gimik.app.allpresanapp.model;

/**
 * Created by Mr.Binh on 10/6/2015.
 */
public class ItemMenu {
    public String title;
    public int resourceIcon;
    public int activeResourceIcon;
    public Object object;
    public boolean selected;
    public int code;

    public ItemMenu(){

    }
    public ItemMenu(String title) {
        this.title = title;
    }

    public ItemMenu(int code, String title) {
        this.code = code;
        this.title = title;
    }

    public ItemMenu(String title, int resourceIcon, int activeResourceIcon) {
        this.title = title;
        this.resourceIcon = resourceIcon;
        this.activeResourceIcon = activeResourceIcon;
    }

    public ItemMenu(int code, String title, int resourceIcon, int activeResourceIcon) {
        this.code = code;
        this.title = title;
        this.resourceIcon = resourceIcon;
        this.activeResourceIcon = activeResourceIcon;
    }

    public int getResourceIcon() {
        return resourceIcon;
    }

    public String getTitle() {
        return title;
    }
}
