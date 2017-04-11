package com.taurus.playerbaselibrary.ui;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.baseframe.ui.activity.ToolsActivity;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.callback.OnAdCallBack;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.CornerCutCover;
import com.kk.taurus.playerbase.cover.DefaultCoverCollections;
import com.kk.taurus.playerbase.cover.base.BasePlayerControllerCover;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.PlayData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BaseAdPlayer;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.callback.OnCompleteCallBack;
import com.taurus.playerbaselibrary.cover.PlayCompleteCover;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends ToolsActivity implements OnPlayerEventListener {

    private BasePlayer mPlayer;
    private DefaultCoverCollections mCoverCollections;
    private VideoItem item;
    private VideoData videoData;
    private PlayCompleteCover completeCover;

    @Override
    public void loadState() {

    }

    @Override
    public View getContentView(Bundle savedInstanceState) {
        return View.inflate(this,R.layout.activity_main,null);
    }

    @Override
    public void parseIntent() {
        super.parseIntent();
        item = (VideoItem) getIntent().getSerializableExtra("data");
        videoData = new VideoData(item.getPath());
    }

    @Override
    public void initData() {
        super.initData();
        fullScreen();
        mPlayer = (DefaultPlayer) findViewById(R.id.player);

        mCoverCollections = new DefaultCoverCollections(this);
        mCoverCollections.buildDefault().addCover(PlayCompleteCover.KEY,completeCover = new PlayCompleteCover(this,null));

//        CornerCutCover cornerCutCover = new CornerCutCover(this,null);
//        cornerCutCover.setCornerRadius(80);
//        cornerCutCover.setCornerBgColor(Color.WHITE);
//        mCoverCollections.addCornerCutCover(cornerCutCover);

        mPlayer.bindCoverCollections(mCoverCollections);

        BasePlayerControllerCover controllerCover = mCoverCollections.getCover(BasePlayerControllerCover.KEY);
        controllerCover.setVideoTitle(item.getDisplayName());
        controllerCover.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(completeCover!=null){
            completeCover.setOnCompleteListener(new OnCompleteCallBack(){
                @Override
                public void onReplay(PlayCompleteCover completeCover) {
                    super.onReplay(completeCover);
                    mPlayer.rePlay(0);
                }
            });
        }

        mPlayer.setOnPlayerEventListener(this);
        normalStart();
//        testAdStart();
    }

    private void normalStart(){
        mPlayer.setDataSource(videoData);
        mPlayer.start();
    }

    private void testAdStart(){
        PlayData playData = new PlayData(videoData);
        List<BaseAdVideo> adVideos = new ArrayList<>();
        adVideos.add(new BaseAdVideo("http://172.16.218.64:8080/batamu_trans19.mp4"));
        adVideos.add(new BaseAdVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        playData.setAdVideos(adVideos);
        mPlayer.playData(playData,new OnAdCallBack(){
            @Override
            public void onAdPlay(BaseAdPlayer adPlayer, BaseAdVideo adVideo) {
                super.onAdPlay(adPlayer, adVideo);
            }

            @Override
            public void onAdPlayComplete(BaseAdVideo adVideo, boolean isAllComplete) {
                Toast.makeText(PlayerActivity.this, adVideo.getData(), Toast.LENGTH_SHORT).show();
                super.onAdPlayComplete(adVideo, isAllComplete);
            }

            @Override
            public void onVideoStart(BaseAdPlayer adPlayer) {
                super.onVideoStart(adPlayer);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mPlayer!=null){
            mPlayer.doConfigChange(newConfig);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPlayer!=null){
            mPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPlayer!=null){
            mPlayer.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy();
        }
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:

                break;
        }
    }
}