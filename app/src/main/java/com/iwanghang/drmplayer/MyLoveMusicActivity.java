package com.iwanghang.drmplayer;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.iwanghang.drmplayer.adapter.MyMusicListAdapter;
import com.iwanghang.drmplayer.vo.Mp3Info;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;

/**
 * Created by iwanghang on 16/4/27.
 * 我的收藏音乐列表界面
 */
public class MyLoveMusicActivity extends BaseActivity implements OnItemClickListener {

    private ListView listView_love;
    private DRMPlayerApp app;
    private ArrayList<Mp3Info> loveMp3Infos;
    private MyMusicListAdapter adapter;
    private boolean isChange = false;//表示当前播放列表是否为收藏列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (DRMPlayerApp) getApplication();
        setContentView(R.layout.avtivity_my_love_music_list);//绑定布局
        listView_love = (ListView) findViewById(R.id.listView_love);//实例化布局
        listView_love.setOnItemClickListener(this);
        initData();//初始化数据
        
        //listView_love.setAdapter();
    }

    private void initData() {//初始化数据
        try {
            loveMp3Infos = (ArrayList<Mp3Info>) app.dbUtils.findAll(Mp3Info.class);//查找所有已收藏音乐
            adapter = new MyMusicListAdapter(this,loveMp3Infos);
            listView_love.setAdapter(adapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void publish(int progress) {

    }

    @Override
    public void change(int progress) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //如果当前播放列表不是收藏列表
        if (playService.getChangePlayList() != playService.LOVE_MUSIC_LIST){
            playService.setMp3Infos(loveMp3Infos);//播放列表切换为收藏列表
            playService.setChangePlayList(playService.LOVE_MUSIC_LIST);
        }
        playService.play(position);
    }
}
