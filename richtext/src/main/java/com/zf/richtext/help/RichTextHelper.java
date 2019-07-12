package com.zf.richtext.help;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BulletSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.zf.richtext.Constant;
import com.zf.richtext.bean.FontStyle;
import com.zf.richtext.bean.SpanPart;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * 富文本帮助类
 */
public class RichTextHelper {

    private static volatile RichTextHelper INSTANCE;
    public static final int EXCLUD_MODE = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;  //(a,b)
    public static final int EXCLUD_INCLUD_MODE = Spannable.SPAN_EXCLUSIVE_INCLUSIVE;//(a,b]
    public static final int INCLUD_INCLUD_MODE = Spannable.SPAN_INCLUSIVE_INCLUSIVE;// [a,b]
    private EditText editText;

    private RichTextHelper() {
    }

    public static RichTextHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (RichTextHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RichTextHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void initView(EditText text) {
        editText = text;
        editText.setOnEditorActionListener((v, actionId, event) ->{
            //当actionId == XX_SEND 或者 XX_DONE时都触发  
            //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发  
            //注意，这是一定要判断event != null。因为在某些输入法上会返回null。  
            if ((event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    checkLineSpan();
            }
          return false;
        });
    }

    /**
     * 加粗
     */
    public void setBold(boolean isBold) {
        setStyleSpan(isBold, Typeface.BOLD);
    }

    /**
     * 斜体
     *
     * @param isItalic
     */
    public void setItalic(boolean isItalic) {
        setStyleSpan(isItalic, Typeface.ITALIC);
    }

    /**
     * 下划线
     *
     * @param isUnderline
     */
    public void setUnderline(boolean isUnderline) {
        setUnderlineSpan(isUnderline);
    }


    public void setStreak(boolean isStreak) {
        setStreakSpan(isStreak);
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    public void setFontSize(int size) {
        setFontSizeSpan(size);
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void setFontColor(int color) {
        setFontColorSpan(color);
    }

    /**
     * 设置文字对齐方式
     *
     * @param alignment
     */
    public void setFontAlignment(int alignment) {
        setFontAlignmentSpan(alignment);
    }

    /**
     * 设置菜单
     *
     */
    public void setBullet(boolean isBullet) {
        setBulletSpan(isBullet);
    }

    public void setImg(String path, Context mContext) {
        if (!TextUtils.isEmpty(path)) {
            ImagePlate plate = new ImagePlate(editText, mContext);
            plate.image(path);
        }
    }

    /**
     * bold italic
     *
     * @param isSet
     * @param type
     */
    private void setStyleSpan(boolean isSet, int type) {
        FontStyle fontStyle = new FontStyle();
        if (type == Typeface.BOLD) {
            fontStyle.isBold = true;
        } else if (type == Typeface.ITALIC) {
            fontStyle.isItalic = true;
        }
        setSpan(fontStyle, isSet, StyleSpan.class);
    }

    /**
     * underline
     *
     * @param isSet
     */
    private void setUnderlineSpan(boolean isSet) {
        FontStyle fontStyle = new FontStyle();
        fontStyle.isUnderline = true;
        setSpan(fontStyle, isSet, UnderlineSpan.class);
    }

    /**
     * 中划线
     *
     * @param isSet
     */
    private void setStreakSpan(boolean isSet) {
        FontStyle fontStyle = new FontStyle();
        fontStyle.isStreak = true;
        setSpan(fontStyle, isSet, StrikethroughSpan.class);
    }

    /**
     * 设置 字体大小
     *
     * @param size
     */
    private void setFontSizeSpan(int size) {
        if (size == 0) {
            size = FontStyle.NORMAL;
        }
        FontStyle fontStyle = new FontStyle();
        fontStyle.fontSize = size;
        setSpan(fontStyle, true, AbsoluteSizeSpan.class);
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    private void setFontColorSpan(int color) {
        if (color == 0) {
            color = Color.parseColor(FontStyle.BLACK);
        }
        FontStyle fontStyle = new FontStyle();
        fontStyle.color = color;
        setSpan(fontStyle, true, ForegroundColorSpan.class);
    }

    /**
     * 设置文字对齐方式
     *
     * @param alignment
     */
    private void setFontAlignmentSpan(int alignment) {
        String [] text = editText.getText().toString().split("\n");
        int index = editText.getSelectionStart();
        int currentLine = 0;
        int start = 0;
        int end;
        for (int i = 0; i < text.length; i++) {
            if (index>=start&&index<(start+text[i].length()+1)){
                currentLine = i;
                break;
            }
            start += text[i].length()+1;
        }
        if (start == index){
            end = index;
        }else {
            end = start+text[currentLine].length();
        }
        Layout.Alignment align = Layout.Alignment.ALIGN_NORMAL;
        if (alignment==FontStyle.CENTER){
            align = Layout.Alignment.ALIGN_CENTER;
        }else  if (alignment==FontStyle.RIGHT){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                align = Layout.Alignment.ALIGN_RIGHT;
            }else {
                align = Layout.Alignment.ALIGN_OPPOSITE;
            }
        }else {
            align = Layout.Alignment.ALIGN_NORMAL;
        }
        if (start>=end){
            editText.getEditableText().insert(start," ");
        }
        editText.getEditableText().setSpan(new AlignmentSpan.Standard(align), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 菜单
     *
     */
    private void setBulletSpan(boolean isBullet) {
        String [] text = editText.getText().toString().split("\n");
        int index = editText.getSelectionStart();
        int currentLine = 0;
        int start = 0;
        int end;
        for (int i = 0; i < text.length; i++) {
            if (index>=start&&index<(start+text[i].length()+1)){
                currentLine = i;
                break;
            }
            start += text[i].length()+1;
        }
        if (start == index){
            end = index;
        }else {
          end = start+text[currentLine].length();
        }
        Log.d(Constant.TTAG,"===>"+editText.getText().toString().length()+",==index=>"+index+",==currentLine=>"+currentLine+",==start=>"+start);

    }

    private void checkLineSpan(){

    }

    /**
     * 通用set Span
     *
     * @param fontStyle
     * @param isSet
     * @param tClass
     * @param <T>
     */
    private <T> void setSpan(FontStyle fontStyle, boolean isSet, Class<T> tClass) {
        Log.d("setSpan", "");
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        int mode = EXCLUD_INCLUD_MODE;
        T[] spans = editText.getEditableText().getSpans(start, end, tClass);
        //获取
        List<SpanPart> spanStyles = getOldFontSytles(spans, fontStyle);
        for (SpanPart spanStyle : spanStyles) {
            if (spanStyle.start < start) {
                if (start == end) {
                    mode = EXCLUD_MODE;
                }
                editText.getEditableText().setSpan(getInitSpan(spanStyle), spanStyle.start, start, mode);
            }
            if (spanStyle.end > end) {
                editText.getEditableText().setSpan(getInitSpan(spanStyle), end, spanStyle.end, mode);
            }
        }
        if (isSet) {
            if (start == end) {
                mode = INCLUD_INCLUD_MODE;
            }
            editText.getEditableText().setSpan(getInitSpan(fontStyle), start, end, mode);
        }
    }

    /**
     * 获取当前 选中 spans
     *
     * @param spans
     * @param fontStyle
     * @param <T>
     * @return
     */
    private <T> List<SpanPart> getOldFontSytles(T[] spans, FontStyle fontStyle) {
        List<SpanPart> spanStyles = new ArrayList<>();
        for (T span : spans) {
            boolean isRemove = false;
            if (span instanceof StyleSpan) {//特殊处理 styleSpan
                int style_type = ((StyleSpan) span).getStyle();
                if ((fontStyle.isBold && style_type == Typeface.BOLD)
                        || (fontStyle.isItalic && style_type == Typeface.ITALIC)) {
                    isRemove = true;
                }
            } else {
                isRemove = true;
            }
            if (isRemove) {
                SpanPart spanStyle = new SpanPart(fontStyle);
                spanStyle.start = editText.getEditableText().getSpanStart(span);
                spanStyle.end = editText.getEditableText().getSpanEnd(span);
                if (span instanceof AbsoluteSizeSpan) {
                    spanStyle.fontSize = ((AbsoluteSizeSpan) span).getSize();
                } else if (span instanceof ForegroundColorSpan) {
                    spanStyle.color = ((ForegroundColorSpan) span).getForegroundColor();
                }
                spanStyles.add(spanStyle);
                editText.getEditableText().removeSpan(span);
            }
        }
        return spanStyles;
    }

    /**
     * 返回 初始化 span
     *
     * @param fontStyle
     * @return
     */
    private Object getInitSpan(FontStyle fontStyle) {
        if (fontStyle.isBold) {
            return new StyleSpan(Typeface.BOLD);
        } else if (fontStyle.isItalic) {
            return new StyleSpan(Typeface.ITALIC);
        } else if (fontStyle.isUnderline) {
            return new UnderlineSpan();
        } else if (fontStyle.isStreak) {
            return new StrikethroughSpan();
        } else if (fontStyle.fontSize > 0) {
            return new AbsoluteSizeSpan(fontStyle.fontSize, true);
        } else if (fontStyle.color != 0) {
            return new ForegroundColorSpan(fontStyle.color);
        }
        return null;
    }

    /**
     * 获取某位置的  样式
     *
     * @param start
     * @param end
     * @return
     */
    private FontStyle getFontStyle(int start, int end) {
        FontStyle fontStyle = new FontStyle();
        CharacterStyle[] characterStyles = editText.getEditableText().getSpans(start, end, CharacterStyle.class);
        for (CharacterStyle style : characterStyles) {
            if (style instanceof StyleSpan) {
                int type = ((StyleSpan) style).getStyle();
                if (type == Typeface.BOLD) {
                    fontStyle.isBold = true;
                } else if (type == Typeface.ITALIC) {
                    fontStyle.isItalic = true;
                }
            } else if (style instanceof UnderlineSpan) {
                fontStyle.isUnderline = true;
            } else if (style instanceof StrikethroughSpan) {
                fontStyle.isStreak = true;
            } else if (style instanceof AbsoluteSizeSpan) {
                fontStyle.fontSize = ((AbsoluteSizeSpan) style).getSize();
            } else if (style instanceof ForegroundColorSpan) {
                fontStyle.color = ((ForegroundColorSpan) style).getForegroundColor();
            }
        }
        return fontStyle;
    }

    public void clearView() {
        editText = null;
    }

}
