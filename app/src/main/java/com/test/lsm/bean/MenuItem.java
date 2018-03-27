package com.test.lsm.bean;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/26
 */

public class MenuItem {

    private int position;
    private int iconId;
    private String title;

    public MenuItem(int position , String title) {
        this.position = position;
        this.title = title;
    }

    public MenuItem(int position , int iconId, String title) {
        this.position = position;
        this.iconId = iconId;
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
