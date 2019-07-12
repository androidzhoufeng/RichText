package com.zf.richtext;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zf.richtext.adapter.RichAdapter;
import com.zf.richtext.bean.FontStyle;
import com.zf.richtext.bean.RichSettingBean;
import com.zf.richtext.bean.SpinnerBean;
import com.zf.richtext.help.CustomHtml;
import com.zf.richtext.help.RichEditImageGetter;
import com.zf.richtext.help.RichTextHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 功能点：
 * 1、加粗、斜体、下划线、删除线
 * 2、设置字体大小：大（18sp）、中（16sp、默认）、小（14sp）
 * 3、字体颜色（默认颜色：#333333）
 * 4、插入图片
 * 5、编辑内容生成html
 * 6、html转为spanned
 */
public class RichTextFragment extends Fragment {
    private EditText editText;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rich_text, container, false);
        editText = view.findViewById(R.id.editText);
        recyclerView = view.findViewById(R.id.recyclerView);
        RichTextHelper.getInstance().initView(editText);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        List<RichSettingBean> data = new ArrayList<>();
        data.add(new RichSettingBean("B", null));
        data.add(new RichSettingBean("I", null));
        data.add(new RichSettingBean("U", null));
        data.add(new RichSettingBean("S", null));
        data.add(new RichSettingBean(R.drawable.icon_image, null));
        data.add(new RichSettingBean("",
                new SpinnerBean[]{new SpinnerBean(1, "小", String.valueOf(FontStyle.SMALL),false),
                        new SpinnerBean(1, "中", String.valueOf(FontStyle.NORMAL),true),
                        new SpinnerBean(1, "大", String.valueOf(FontStyle.BIG),false)}));
        data.add(new RichSettingBean("",
                new SpinnerBean[]{new SpinnerBean(2, "黑", FontStyle.BLACK,true),
                        new SpinnerBean(2, "蓝", FontStyle.BLUE,true),
                        new SpinnerBean(2, "灰", FontStyle.GREY,true),
                        new SpinnerBean(2, "红", FontStyle.RED,true)}));
        data.add(new RichSettingBean("",
                new SpinnerBean[]{new SpinnerBean(3, String.valueOf(R.drawable.icon_left),String.valueOf(FontStyle.LEFT),true),
                        new SpinnerBean(3, String.valueOf(R.drawable.icon_right), String.valueOf(FontStyle.RIGHT),false),
                        new SpinnerBean(3, String.valueOf(R.drawable.icon_center), String.valueOf(FontStyle.CENTER),false)}));
        data.add(new RichSettingBean(R.drawable.icon_menu, null));
        RichAdapter richAdapter = new RichAdapter(data);
        recyclerView.setAdapter(richAdapter);
        richAdapter.setOnItemClickListener((adapter, position, content) -> {
            RichSettingBean item = data.get(position);
            switch (position) {
                case 0:
                    item.setSelected(!item.isSelected());
                    RichTextHelper.getInstance().setBold(item.isSelected());
                    richAdapter.notifyItemChanged(position);
                    break;
                case 1:
                    item.setSelected(!item.isSelected());
                    RichTextHelper.getInstance().setItalic(item.isSelected());
                    richAdapter.notifyItemChanged(position);
                    break;
                case 2:
                    item.setSelected(!item.isSelected());
                    RichTextHelper.getInstance().setUnderline(item.isSelected());
                    richAdapter.notifyItemChanged(position);
                    break;
                case 3:
                    item.setSelected(!item.isSelected());
                    RichTextHelper.getInstance().setStreak(item.isSelected());
                    richAdapter.notifyItemChanged(position);
                    break;
                case 4://图片
                    RichTextHelper.getInstance().setImg("https://www.baidu.com/img/bd_logo1.png?where=super", getActivity());
                    break;
                case 5:
                    RichTextHelper.getInstance().setFontSize(Integer.valueOf(content));
                    break;
                case 6:
                    RichTextHelper.getInstance().setFontColor(Color.parseColor(content));
                    break;
                case 7://对齐方式（左、居中、右）
                    RichTextHelper.getInstance().setFontAlignment(Integer.valueOf(content));
                    break;
                case 8://菜单
                    item.setSelected(!item.isSelected());
                    RichTextHelper.getInstance().setBullet(item.isSelected());
                    richAdapter.notifyItemChanged(position);
                    break;
            }
        });
    }

    public String getHtml() {
        String html = CustomHtml.toHtml(editText.getEditableText(), CustomHtml.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
        Log.d(Constant.TTAG, html);
        return html;
    }

    public void setHtml(String html) {
        Spanned spanned = CustomHtml.fromHtml(html, CustomHtml.FROM_HTML_MODE_LEGACY, new RichEditImageGetter(getActivity(), editText), null);
        editText.setText(spanned);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RichTextHelper.getInstance().clearView();
    }

}
