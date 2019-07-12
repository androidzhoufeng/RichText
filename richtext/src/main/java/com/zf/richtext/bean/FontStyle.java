package com.zf.richtext.bean;

public class FontStyle {

    public final static int SMALL = 12;
    public final static int NORMAL = 16;
    public final static int BIG = 20;

    public final static String BLACK = "#FF212121";
    public final static String GREY = "#FF878787";
    public final static String RED = "#FFF64C4C";
    public final static String BLUE = "#FF007AFF";

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int CENTER = 2;

    public boolean isBold;
    public boolean isItalic;
    public boolean isUnderline;
    public boolean isStreak;
    public int fontSize;
    public int color;
    public boolean isBullet;
}
