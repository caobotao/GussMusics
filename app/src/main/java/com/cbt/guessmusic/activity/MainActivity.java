package com.cbt.guessmusic.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cbt.guessmusic.R;
import com.cbt.guessmusic.data.Const;
import com.cbt.guessmusic.model.IWordButtonClickListener;
import com.cbt.guessmusic.model.Song;
import com.cbt.guessmusic.model.WordButton;
import com.cbt.guessmusic.util.LogUtil;
import com.cbt.guessmusic.util.Util;
import com.cbt.guessmusic.view.WordGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by caobotao on 16/1/30.
 */
public class MainActivity extends Activity implements IWordButtonClickListener {
    private static final String TAG = "MainActivity";

    //正确答案
    private static final int STATUS_ANSWER_RIGHT = 1;
    //错误答案
    private static final int STATUS_ANSWER_WRONG = 2;
    //不完整
    private static final int STATUS_ANSWER_LACK  = 3;


    //声明动画相关的变量
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;


    //播放音乐的按钮,盘片图片,控制杆图片
    private ImageButton mIbtnPlayStart;
    private ImageView mViewPan;
    private ImageView mViewPanBar;

    //文字框的文字容器
    private ArrayList<WordButton> mAllWords;
    private ArrayList<WordButton> mSelectedWords;
    private WordGridView mGridView;

    //已选择文字框的UI容器
    private LinearLayout mWordsLayout;

    //当前歌曲
    private Song mCurrentSong;
    //当前关卡索引
    private int mCurrentStageIndex = -1;

    //处理闪烁文字的handler
    private Handler sparkWordsHandler;
    private static final int SPARK_TIMES = 6;
    private static final String COLOR_STR = "color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
        initCurrentStageData();
    }

    //获取当前关卡歌曲信息
    private Song getStageSong(int stageIndex) {
        String[] songInfo = Const.SONGS_INFO[stageIndex];
        String songFIleName = songInfo[Const.INDEX_FILE_NAME];
        String songName = songInfo[Const.INDEX_SONG_NAME];
        Song song = new Song(songName,songFIleName);
        return song;
    }

    //初始化当前关的文字框数据
    private void initCurrentStageData() {
        //读取当前关的歌曲信息
        mCurrentSong = getStageSong(++mCurrentStageIndex);

        //获得数据
        mAllWords = initAllWord();

        //更新数据
        mGridView.updateData(mAllWords);

        //初始化已选狂文字
        mSelectedWords = initSelectedWord();

        LayoutParams layoutParams = new LayoutParams(140,140);
        for (int i = 0;i < mSelectedWords.size(); i ++) {
            mWordsLayout.addView(mSelectedWords.get(i).getButton(),layoutParams);
        }
    }

    /**
     * 初始化待选文字框
     */
    private ArrayList<WordButton> initAllWord(){
        ArrayList<WordButton> data = new ArrayList<>();
        String[] allWords = generateWords();

        //获取所有的文字信息
        for (int i = 0;i < WordGridView.WORDS_COUNT;i++) {
            WordButton wordButton = new WordButton();
            wordButton.setIndex(i);
            wordButton.setWordString(allWords[i]);
            data.add(wordButton);
        }
        return data;
    }

    /**
     * 初始化已选文字框
     */
    private ArrayList<WordButton> initSelectedWord() {
        ArrayList<WordButton> data = new ArrayList<>();
        //获取所有的文字信息
        for (int i = 0;i < mCurrentSong.getSongNameLength();i++) {
            View view = Util.getView(MainActivity.this, R.layout.gridview_item);

            final WordButton holder = new WordButton();
            holder.setButton((Button) view.findViewById(R.id.item_btn));

            holder.getButton().setTextColor(Color.WHITE);
            holder.getButton().setText("");
            holder.setVisible(false);
            holder.getButton().setBackgroundResource(R.mipmap.game_wordblank);
            holder.getButton().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清除被点击的已选文字框的文字
                    if (!"".equals(holder.getWordString())){
                        clearSelectedWords(holder);
                    }
                }
            });
            data.add(holder);
        }
        return data;
    }

    //生成所有的待选文字
    private String[] generateWords() {
        String[] words = new String[WordGridView.WORDS_COUNT];
        //存入歌名
        for (int i = 0;i < mCurrentSong.getSongNameLength();i ++) {
            words[i] = mCurrentSong.getSongNameChars()[i] + "";
        }
        //存入随机汉字
        for (int i = mCurrentSong.getSongNameLength(); i < WordGridView.WORDS_COUNT; i ++) {
            words[i] = Util.getRandomChineseChar() + "";
        }

        //打乱汉字顺序
        List<String> wordList = (List<String>) Arrays.asList(words);
        Collections.shuffle(wordList);
        for (int i = 0;i < words.length;i ++) {
            words[i] = wordList.get(i);
        }

        return words;
    }

    //初始化事件
    private void initEvent() {
        mBarInAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            //当控制杆移入动画结束时开始盘片旋转动画
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPanAnim.setAnimationListener(new AnimationListener() {
            @Override
            //当盘片动画开始时隐藏播放按钮
            public void onAnimationStart(Animation animation) {
                mIbtnPlayStart.setVisibility(View.GONE);
            }

            @Override
            //当盘片动画结束时开始控制杆移出动画
            public void onAnimationEnd(Animation animation) {
                mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarOutAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            //当控制杆移出动画结束时设置播放按钮为可见
            public void onAnimationEnd(Animation animation) {
                mIbtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mIbtnPlayStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayButton();
            }
        });

        mGridView.setWordButtonClickListener(this);
    }

    //处理点击播放按钮的逻辑
    private void handlePlayButton() {
        //开始控制杆移入的动画
        mViewPanBar.startAnimation(mBarInAnim);
    }



    //初始化控件
    private void initView() {
        mIbtnPlayStart = (ImageButton) findViewById(R.id.ibtn_play_start);
        mViewPan = (ImageView) findViewById(R.id.iv_game_disc);
        mViewPanBar = (ImageView) findViewById(R.id.iv_index_pin);

        mGridView = (WordGridView) findViewById(R.id.grid_view);

        mWordsLayout = (LinearLayout) findViewById(R.id.word_select_container);
    }

    //初始化数据
    private void initData() {
        //初始化动画
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);

        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mBarInLin);

        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setInterpolator(mBarOutLin);

        sparkWordsHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == 0x101) {
                    int color = msg.getData().getInt(COLOR_STR);
                    //间隔更改按钮文字颜色达到闪烁的效果
                    for (int i = 0; i < mSelectedWords.size(); i ++) {
                        mSelectedWords.get(i).getButton().setTextColor(color);
                    }
                }
            }
        };
    }

    @Override
    protected void onPause() {
        //当Activity被中断时清除动画
        mViewPan.clearAnimation();
        super.onPause();
    }

    //文字按钮点击事件
    @Override
    public void onWordButtonClick(WordButton wordButton) {
//        Toast.makeText(MainActivity.this, wordButton.getIndex() + "", Toast.LENGTH_SHORT).show();
        setSelectedWords(wordButton);

        //获得答案状态
        int answerResult = checkAnswer();
        LogUtil.d(TAG,answerResult + "");
        //根据答案结果进行相应的处理
        switch (answerResult) {
            //过关并获得奖励
            case STATUS_ANSWER_RIGHT:
                break;

            //闪烁文字并进行提示
            case STATUS_ANSWER_WRONG:
                sparkWords();
                break;

            case STATUS_ANSWER_LACK:
                for (int i = 0; i < mSelectedWords.size(); i ++) {
                    mSelectedWords.get(i).getButton().setTextColor(Color.WHITE);
                }
                break;
        }
    }

    //设置单个已选文字框
    private void setSelectedWords(WordButton wordButton) {
        for (int i = 0;i < mSelectedWords.size();i ++) {
            //如果此已选框尚未被填充汉字
            if (mSelectedWords.get(i).getWordString().length() == 0) {
                //设置此已选框的文字及可见性等属性
                mSelectedWords.get(i).setWordString(wordButton.getWordString());
                mSelectedWords.get(i).getButton().setText(wordButton.getWordString());
                mSelectedWords.get(i).setVisible(true);
                //记录索引
                mSelectedWords.get(i).setIndex(wordButton.getIndex());
                LogUtil.d(TAG, wordButton.getIndex() + "");

                //设置待选框的可见性
                setWordButtonVisibility(wordButton,View.INVISIBLE);
                break;
            }
        }
    }

    //清除单个已选文字框
    private void clearSelectedWords(WordButton wordButton) {
        //设置已选文字框的属性
        wordButton.getButton().setText("");
        wordButton.setVisible(false);
        wordButton.setWordString("");

        //设置待选文字框的属性
        setWordButtonVisibility(mAllWords.get(wordButton.getIndex()),View.VISIBLE);
    }

    //设置WordButton的可见性
    private void setWordButtonVisibility(WordButton wordButton,int visibility) {
        wordButton.getButton().setVisibility(visibility);
        wordButton.setVisible(visibility == View.VISIBLE ? true : false);
        LogUtil.d(TAG,wordButton.getVisible() + "");
    }

    //检查答案
    private int checkAnswer() {
        //先检查已选的汉字数与歌曲名汉字数是否匹配
        for (int i = 0;i < mSelectedWords.size();i ++ ) {
            if (mSelectedWords.get(i).getWordString().length() == 0) {
                return STATUS_ANSWER_LACK;
            }
        }

        //如果匹配,再检查正确与否
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i < mSelectedWords.size(); i ++) {
            sb.append(mSelectedWords.get(i).getWordString());
        }

        return (sb.toString().equals(mCurrentSong.getSongName()))
                ? STATUS_ANSWER_RIGHT : STATUS_ANSWER_WRONG;
    }

    //闪烁文字
    private void sparkWords() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean change = false;
                for (int i = 0; i < SPARK_TIMES; i ++) {
                    Message message = sparkWordsHandler.obtainMessage();
                    message.arg1 = 0x101;
                    Bundle bundle = new Bundle();
                    bundle.putInt(COLOR_STR, change ? Color.RED : Color.WHITE);
                    message.setData(bundle);
                    sparkWordsHandler.sendMessage(message);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    change = !change;
                }
            }
        }).start();
    }
}
