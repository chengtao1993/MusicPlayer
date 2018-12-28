package com.iwanghang.drmplayer;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.iwanghang.drmplayer.utils.ImageUtils;
import com.iwanghang.drmplayer.utils.MediaUtils;
import com.iwanghang.drmplayer.vo.Mp3Info;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;

import static com.iwanghang.drmplayer.PlayService.ORDER_PLAY;
import static com.iwanghang.drmplayer.PlayService.RANDOM_PLAY;
import static com.iwanghang.drmplayer.PlayService.SINGLE_PLAY;

/**
 *PlayActivity 点击MyMusicListFragment(本地音乐)底部UI中的专辑封面图片打开的Activity
 */
public class PlayActivity extends BaseActivity implements OnClickListener,OnSeekBarChangeListener{
    private TextView textView1_title;//歌名
    private ImageView imageView1_ablum;//专辑封面图片
    private SeekBar seekBar1;//进度条
    private TextView textView1_start_time,textView1_end_time;//开始时间,结束时间
    private ImageView imageView1_play_mode;//菜单
    private ImageView imageView3_previous,imageView2_play_pause,imageView1_next;//上一首,播放暂停,下一首
    private ImageView imageView1_ablum_reflection;//专辑封面图片倒影
    private ImageView imageView1_favorite;//收藏按钮

    //private ArrayList<Mp3Info> mp3Infos;
    //private int position;//当前播放的位置
    private boolean isPause = false;//当前播放的是否为暂停状态
    private static final int UPDATE_TIME = 0x1;//更新播放事件的标记

    DRMPlayerApp app;//取出全局对象 方便调用
//    private ViewPager viewPager;
//    private MyPagerAdapter adapter;
//    private MyMusicListFragment myMusicListFragment;
//    private NetMusicListFragment netMusicListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        //取出全局对象 方便调用
        app = (DRMPlayerApp) getApplication();
        //初始化界面信息
        textView1_title = (TextView) findViewById(R.id.textView1_title);//歌名
        imageView1_ablum = (ImageView) findViewById(R.id.imageView1_ablum);//专辑封面图片
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);//进度条
        textView1_start_time = (TextView) findViewById(R.id.textView1_start_time);//开始时间
        textView1_end_time = (TextView) findViewById(R.id.textView1_end_time);//结束时间
        imageView1_play_mode = (ImageView) findViewById(R.id.imageView1_play_mode);//菜单
        imageView3_previous = (ImageView) findViewById(R.id.imageView3_previous);//上一首
        imageView2_play_pause = (ImageView) findViewById(R.id.imageView2_play_pause);//播放暂停
        imageView1_next = (ImageView) findViewById(R.id.imageView1_next);//下一首
        imageView1_ablum_reflection = (ImageView) findViewById(R.id.imageView1_ablum_reflection);//专辑封面图片倒影
        imageView1_favorite = (ImageView) findViewById(R.id.imageView1_favorite);//收藏按钮
        //注册按钮点击监听事件
        imageView1_play_mode.setOnClickListener(this);
        imageView2_play_pause.setOnClickListener(this);
        imageView3_previous.setOnClickListener(this);
        imageView1_next.setOnClickListener(this);
        seekBar1.setOnSeekBarChangeListener(this);
        imageView1_favorite.setOnClickListener(this);

        //mp3Infos = MediaUtils.getMp3Infos(this);
        //bindPlayService();//绑定服务,异步过程,绑定后需要取得相应的值,来更新界面
        myHandler = new MyHandler(this);

        //以下直接调用change()是不行的,因为异步问题,会发生NullPointerException空指针异常
        //从MyMusicListFragment的专辑封面图片点击时间传过来的position(当前播放的位置)
        //position = getIntent().getIntExtra("position",0);
        //change(position);

        //通过在BaseActivity中绑定Service,添加如下代码实现change()
        //musicUpdatrListener.onChange(playService.getCurrentProgeress());

        //从MyMusicListFragment的专辑封面图片点击时间传过来的isPause(当前播放的是否为暂停状态)
        //isPause = getIntent().getBooleanExtra("isPause",false);
    }

//    private void initViewPager(){//专辑封面图片Pager与歌词Pager
//        View album_image_layout = getLayoutInflater().inflate(R.layout.album_image_layout,null);
//        //初始化界面信息
//        textView1_title = (TextView) findViewById(R.id.textView1_title);//歌名
//        imageView1_ablum = (ImageView) findViewById(R.id.imageView1_ablum);//专辑封面图片
//        views.add(album_image_layout);//添加专辑封面图片Pager
//        views.add(getLayoutInflater().inflate(R.layout.lrc_layout));//添加歌词Pager
//        viewPager.setAdapter(new MyPagerAdapter());
//    }

    //把播放服务的绑定和解绑放在onResume,onPause里,好处是,每次回到当前Activity就获取一次播放状态
    @Override
    protected void onResume() {
        super.onResume();
        bindPlayService();//绑定服务
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindPlayService();//解绑服务
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindPlayService();//解绑服务
    }

    //Handler用于更新已经播放时间
    private static MyHandler myHandler;

    //进度条改变 (fromUser 是否来自用户的改变 , 而不是程序自身控制的改变)
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            //playService.pause();//暂停
            playService.seekTo(progress);//寻找指定的时间位置 (跳到某个时间点进行播放)
            //playService.start();//播放
        }
    }

    //进度条开始触摸
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    //进度条停止触摸
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    static class MyHandler extends Handler{
        private PlayActivity playActivity;
        public MyHandler(PlayActivity playActivity){
            this.playActivity = playActivity;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (playActivity!=null){
                switch (msg.what){
                    case UPDATE_TIME://更新时间(已经播放时间)
                        playActivity.textView1_start_time.setText(MediaUtils.formatTime(msg.arg1));
                        break;
                }
            }
        }
    }

    @Override
    public void publish(int progress) {
        //以下是是直接调用线程,但是不能这样做,会报错,线程异常
        //textView1_start_time.setText(MediaUtils.formatTime(progress));
        //所以,我们需要使用Handler
        Message msg = myHandler.obtainMessage(UPDATE_TIME);//用于更新已经播放时间
        msg.arg1 = progress;//用于更新已经播放时间
        myHandler.sendMessage(msg);//用于更新已经播放时间

        seekBar1.setProgress(progress);
    }

    @Override
    public void change(int position) {//初始化,独立播放界面的歌曲切换后的初始化界面上的歌曲信息
        //if (this.playService.isPlaying()) {//获取是否为播放状态
        System.out.println("PlayActivity #100 position = " + position);
        Mp3Info mp3Info = playService.mp3Infos.get(position);
        textView1_title.setText(mp3Info.getTitle());//设置歌名
        //获取专辑封面图片
        Bitmap albumBitmap = MediaUtils.getArtwork(this, mp3Info.getId(), mp3Info.getAlbumId(), true, false);
        //改变播放界面专辑封面图片
        imageView1_ablum.setImageBitmap(albumBitmap);
        textView1_end_time.setText(MediaUtils.formatTime(mp3Info.getDuration()));//设置结束时间
        imageView2_play_pause.setImageResource(R.mipmap.app_music_pause);//设置暂停图片
        seekBar1.setProgress(0);//设置当前进度为0
        seekBar1.setMax((int)mp3Info.getDuration());//设置进度条最大值为MP3总时间
        if (playService.isPlaying()){
            imageView2_play_pause.setImageResource(R.mipmap.app_music_pause);
        }else {
            imageView2_play_pause.setImageResource(R.mipmap.app_music_play);
        }

        if(imageView1_ablum != null) {
            imageView1_ablum_reflection.setImageBitmap(ImageUtils.createReflectionBitmapForSingle(albumBitmap));//显示倒影
        }
        switch (playService.getPlay_mode()){
            case ORDER_PLAY://顺序播放
                imageView1_play_mode.setImageResource(R.mipmap.app_mode_order);
                //imageView2_play_pause.setTag(ORDER_PLAY);
                break;
            case PlayService.RANDOM_PLAY://随机播放
                imageView1_play_mode.setImageResource(R.mipmap.app_mode_random);
                //imageView2_play_pause.setTag(RANDOM_PLAY);
                break;
            case PlayService.SINGLE_PLAY://单曲循环
                imageView1_play_mode.setImageResource(R.mipmap.app_mode_single);
                //imageView2_play_pause.setTag(SINGLE_PLAY);
                break;
            default:
                break;
        }
        //初始化收藏状态
        try {
            Mp3Info loveMp3Info = app.dbUtils.findFirst(Selector.from(Mp3Info.class)
                    .where("mp3InfoId","=",mp3Info.getMp3InfoId()));//查出歌曲,SQL语句
            if (loveMp3Info!=null){
                imageView1_favorite.setImageResource(R.mipmap.app_love_selected);
            }else {
                imageView1_favorite.setImageResource(R.mipmap.app_love_unselected);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

        //点击事件
        @Override
        public void onClick(View v) {
            switch (v.getId()){
            case R.id.imageView2_play_pause: {//播放暂停按钮
                if (playService.isPlaying()) {//如果是播放状态
                    imageView2_play_pause.setImageResource(R.mipmap.app_music_play);//设置播放图片
                    playService.pause();
                } else {
                    if (playService.isPause()) {
                        imageView2_play_pause.setImageResource(R.mipmap.app_music_pause);//设置暂停图片
                        playService.start();//播放事件
                    } else {
                        //只有打开APP没有点击任何歌曲,同时,直接点击暂停播放按钮时.才会调用
                        //playService.play(0);

                        //只有打开APP没有点击任何歌曲,同时,直接点击暂停播放按钮时.才会调用
                        //默认播放的是,在PlayService的onCreate中 恢复状态值
                        playService.play(playService.getCurrentPosition());
                    }
                }
                break;
            }
            case R.id.imageView1_next:{//下一首按钮
                playService.next();//下一首
                break;
            }
            case R.id.imageView3_previous:{//上一首按钮
                playService.prev();//上一首
                break;
            }
            case R.id.imageView1_play_mode:{//循环模式按钮
                //int mode = (int) imageView1_play_mode.getTag();
                /**
                 * 以下Tosat内容,在strings.xml里,添加对应代码
                 *<string name="order_play">顺序播放</string>
                 *<string name="random_play">随机播放</string>
                 *<string name="single_play">单曲循环</string>
                 */
                switch (playService.getPlay_mode()){
                    case ORDER_PLAY:
                        imageView1_play_mode.setImageResource(R.mipmap.app_mode_random);
                        //imageView1_play_mode.setTag(RANDOM_PLAY);
                        playService.setPlay_mode(RANDOM_PLAY);
                        //Toast.makeText(getApplicationContext(),"随机播放",Toast.LENGTH_SHORT).show();//这句也可以
                        //Toast.makeText(PlayActivity.this, "随机播放", Toast.LENGTH_SHORT).show();//这句也可以
                        Toast.makeText(PlayActivity.this, getString(R.string.random_play), Toast.LENGTH_SHORT).show();
                        break;
                    case RANDOM_PLAY:
                        imageView1_play_mode.setImageResource(R.mipmap.app_mode_single);
                        //imageView1_play_mode.setTag(SINGLE_PLAY);
                        playService.setPlay_mode(SINGLE_PLAY);
                        //Toast.makeText(getApplicationContext(),"单曲循环",Toast.LENGTH_SHORT).show();//这句也可以
                        //Toast.makeText(PlayActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();//这句也可以
                        Toast.makeText(PlayActivity.this, getString(R.string.single_play), Toast.LENGTH_SHORT).show();
                        break;
                    case SINGLE_PLAY:
                        imageView1_play_mode.setImageResource(R.mipmap.app_mode_order);
                        //imageView1_play_mode.setTag(ORDER_PLAY);
                        playService.setPlay_mode(ORDER_PLAY);
                        //Toast.makeText(getApplicationContext(),"顺序播放",Toast.LENGTH_SHORT).show();//这句也可以
                        //Toast.makeText(PlayActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();//这句也可以
                        Toast.makeText(PlayActivity.this, getString(R.string.order_play), Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            }
            case R.id.imageView1_favorite:{//收藏按钮  //在vo.Mp3Info里  private long mp3InfoId;//在收藏音乐时用于保存原始ID
                Mp3Info mp3Info = playService.mp3Infos.get(playService.getCurrentPosition());//查出歌曲
                try {
                    //Mp3Info loveMp3Info = app.dbUtils.findById(Mp3Info.class,mp3Info.getId());//查出歌曲,错误的
                    Mp3Info loveMp3Info = app.dbUtils.findFirst(Selector.from(Mp3Info.class)
                            .where("mp3InfoId","=",mp3Info.getMp3InfoId()));//查出歌曲,SQL语句
                    if (loveMp3Info==null){//不在音乐收藏数据库中
                        mp3Info.setMp3InfoId(mp3Info.getId());
                        //在音乐收藏数据库 保存音乐
                        app.dbUtils.save(mp3Info);
                        imageView1_favorite.setImageResource(R.mipmap.app_love_selected);
                        Toast.makeText(PlayActivity.this, getString(R.string.love_selected), Toast.LENGTH_SHORT).show();
                    }else {//在音乐收藏数据库中
                        //在音乐收藏数据库 删除音乐
                        app.dbUtils.deleteById(Mp3Info.class,loveMp3Info.getId());
                        imageView1_favorite.setImageResource(R.mipmap.app_love_unselected);
                        Toast.makeText(PlayActivity.this, getString(R.string.love_unselected), Toast.LENGTH_SHORT).show();
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
    }
}
