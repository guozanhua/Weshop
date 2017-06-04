package com.yiwen.weshop.bean;

/**
 * User: Yiwen(https://github.com/yiwent)
 * Date: 2017-05-02
 * Time: 08:31
 * FIXME
 */
public class Tab {
    private int   title;
    private int   icon;
    private Class fragment;

    public Tab(int title, int icon, Class fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
