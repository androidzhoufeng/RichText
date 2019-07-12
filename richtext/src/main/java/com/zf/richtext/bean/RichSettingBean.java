package com.zf.richtext.bean;

import java.util.List;

public class RichSettingBean {
    private String content;
    private int image;
    private SpinnerBean [] arrays;
    private boolean isSelected;

    public RichSettingBean(String content,SpinnerBean [] arrays) {
        this.content = content;
        this.arrays = arrays;
    }

    public RichSettingBean(int image,SpinnerBean [] arrays) {
        this.image = image;
        this.arrays = arrays;
    }

    public String getContent() {
        return content;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SpinnerBean [] getArrays() {
        return arrays;
    }

    public void setArrays(SpinnerBean [] arrays) {
        this.arrays = arrays;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
