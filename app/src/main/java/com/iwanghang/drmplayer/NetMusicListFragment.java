package com.iwanghang.drmplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.iwanghang.drmplayer.adapter.MyMusicListAdapter;
import com.iwanghang.drmplayer.vo.Mp3Info;

import java.util.ArrayList;

/**
 * Created by iwanghang on 16/4/16.
 */
public class NetMusicListFragment extends Fragment implements OnItemClickListener,OnClickListener {

    private ListView listView_my_music;
    private ImageView imageView_album;//专辑封面图片
    private TextView textView_songName,textView2_singer;//歌名,歌手
    private ImageView imageView2_play_pause,imageView3_next;//暂停播放,下一首
    private ArrayList<Mp3Info> mp3Infos;
    private MyMusicListAdapter myMusicListAdapter;

    private MainActivity mainActivity;

    //private boolean isPause = false;//歌曲播放中的暂停状态

    private int position = 0;//当前播放的位置,提供给PlayActivity

    //onAttach(),当fragment被绑定到activity时被调用(Activity会被传入.).
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public static NetMusicListFragment newInstance() {
        NetMusicListFragment net = new NetMusicListFragment();
        return net;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.avtivity_my_love_music_list,null);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
