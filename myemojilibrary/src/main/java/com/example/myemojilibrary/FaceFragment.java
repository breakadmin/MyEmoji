package com.example.myemojilibrary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FaceFragment extends Fragment {
    int PageSize;//每行显示的最大数量
    EditText Text;
    public void init(EditText Text,int PageSize){
        this.Text=Text;
        this.PageSize=PageSize;
    }


    ViewPager emojiPanel;

    LinearLayout ViewGroup;

    LinearLayout MyLinearLayout;
    List<Map<String,Integer>> emojis;
    private int totalPage;//总的页数
    List<GridView> gridViews = new ArrayList<GridView>();
    ImageView[] pointView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoji, null);
        MyLinearLayout = (LinearLayout) view.findViewById(R.id.MyLinearLayout);
        ViewGroup = (LinearLayout) view.findViewById(R.id.ViewGroup);
        emojiPanel = (ViewPager) view.findViewById(R.id.emoji_panel);

        Text.setSelection(Text.getText().length());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        MyLinearLayout.setLayoutParams(new LinearLayout.LayoutParams
                (dm.widthPixels, dm.heightPixels / 3));

        initDatas();
        setLabelPoint();
        return view;
    }



    public void initDatas() {
        EmojiUtils emojiDatas = new EmojiUtils();
        emojis = emojiDatas.initDatas();//获取表情包资源


        totalPage = (int) Math.ceil(emojis.size() * 1.0 / PageSize);//总数据÷每页最大显示数

        for (int i = 0; i < totalPage; i++) {
            GridView gv = new GridView(getContext());
            gv.setNumColumns(7);
            gv.setVerticalSpacing(30);
            gv.setGravity(Gravity.VERTICAL_GRAVITY_MASK);
            gv.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除点击时的背景色
            gv.setAdapter(new MyEmojiAdapter(getContext(), emojis, i, PageSize));


            gridViews.add(gv);
            final int position = i;
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int pos = i + position * PageSize;//重新计算位置
                    if (pos < emojis.size()) {
                        if (i != PageSize) {
                            Text.append(new EmojiUtils().disPlayEmoji(emojis, getContext(), pos));
                        } else {
                            String text = Text.getText().toString();
                            if (text.isEmpty()) {
                                return;
                            }
                            if (!Text.isFocused()) {//获取焦点
                                Text.setSelection(Text.length());
                                Text.requestFocus();
                            }
                            if ("]".equals(text.substring(text.length() - 1, text.length()))) {
                                int index = text.lastIndexOf("[");
                                Text.getText().delete(index, text.length());
                            } else {
                                int index = Text.getSelectionStart();
                                Text.getText().delete(index - 1, index);

                            }
                        }
                    } else {
                        String text = Text.getText().toString();
                        if (text.isEmpty()) {
                            return;
                        }
                        if (!Text.isFocused()) {//获取焦点
                            Text.setSelection(Text.length());
                            Text.requestFocus();
                        }
                        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
                            int index = text.lastIndexOf("[");
                            Text.getText().delete(index, text.length());
                        } else {

                            int index = Text.getSelectionStart();
                            Text.getText().delete(index - 1, index);

                        }

                    }


                }
            });
        }
        emojiPanel.setAdapter(new MyViewAdapter(getContext(), gridViews));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    class MyClickListener implements View.OnClickListener {
        int Position;

        public MyClickListener(int Position) {
            this.Position = Position;
        }

        @Override
        public void onClick(View view) {
            emojiPanel.setCurrentItem(Position, false);
        }
    }

    /**
     * 设置选中的"·"的状态，是否选中
     *
     * @param selectItems
     */
    private void setPointState(int selectItems) {
        for (int i = 0; i < totalPage; i++) {
            if (i == selectItems) {
                pointView[i].setBackgroundResource(R.mipmap.indicator_point_select);
            } else {
                pointView[i].setBackgroundResource(R.mipmap.indicator_point_normal);
            }
        }
    }

    /**
     * 小点的设置与监听
     */
    private void setLabelPoint() {

        //添加"·"的图片
        pointView = new ImageView[totalPage];
        for (int i = 0; i < totalPage; i++) {
            ImageView view = new ImageView(getContext());
            pointView[i] = view;
            pointView[i].setOnClickListener(new MyClickListener(i));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            ViewGroup.addView(view, layoutParams);

        }
        setPointState(0);
        //监听ViewPager的变化，从而更新·(小点)
        emojiPanel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setPointState(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    class MyViewAdapter extends PagerAdapter {
        Context context;
        List<GridView> data;

        public MyViewAdapter(Context context, List<GridView> data) {
            this.context = context;
            this.data = data;
        }

        // 显示多少个页面
        @Override
        public int getCount() {
            return gridViews.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化显示的条目对象
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // return super.instantiateItem(container, position);
            container.addView(gridViews.get(position)); // 添加到ViewPager容器

            return gridViews.get(position);
        }


        // 销毁条目对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView(gridViews.get(position));
        }
    }

}
