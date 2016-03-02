package com.cbt.guessmusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.cbt.guessmusic.R;
import com.cbt.guessmusic.model.IWordButtonClickListener;
import com.cbt.guessmusic.model.WordButton;

import java.util.ArrayList;

/**
 * Created by caobotao on 16/2/20.
 */
public class WordGridView extends GridView {
    private gridAdapter mAdapter;
    private Animation mScaleAnimation;
    private IWordButtonClickListener mWordButtonClickListener;
    public static final int WORDS_COUNT = 10;

    public WordGridView(Context context) {
        this(context,null);
    }

    public WordGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WordGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //更新文字框数据
    public void updateData(ArrayList<WordButton> list) {
        //重新设置数据源
        mAdapter = new gridAdapter(this.getContext(),list);
        this.setAdapter(mAdapter);
    }

    //注册文字按钮的点击事件
    public void setWordButtonClickListener(IWordButtonClickListener mWordButtonClickListener) {
        this.mWordButtonClickListener = mWordButtonClickListener;
    }

    class gridAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<WordButton> list;

        public gridAdapter(Context context,ArrayList<WordButton> list) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final WordButton holder;
            if (convertView == null) {
                mScaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
                mScaleAnimation.setStartOffset(position * 50);

                convertView = mInflater.inflate(R.layout.gridview_item, null);
                /**
                 * 添加判空,否则点击GridView的第一个按钮不会隐藏
                 */
                holder = list.get(position );
                holder.setIndex(position);
                if (holder.getButton() == null) {
                    holder.setButton((Button) convertView.findViewById(R.id.item_btn));
                    holder.getButton().setText(holder.getWordString());
                    holder.getButton().setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mWordButtonClickListener.onWordButtonClick(holder);
                        }
                    });
                }
                convertView.setTag(holder);
            } else {
                holder = (WordButton) convertView.getTag();
            }

            convertView.startAnimation(mScaleAnimation);
            return convertView;
        }
    }

}
