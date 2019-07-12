package com.zf.richtext.bean;

public class SpinnerBean {
    public int id;
    public String name;
    public String content;
    public boolean isSelected;

    public SpinnerBean(int id, String name, String content,boolean isSelected) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.isSelected = isSelected;
    }


}
