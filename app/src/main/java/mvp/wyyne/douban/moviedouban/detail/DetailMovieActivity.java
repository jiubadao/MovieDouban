package mvp.wyyne.douban.moviedouban.detail;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mvp.wyyne.douban.moviedouban.R;
import mvp.wyyne.douban.moviedouban.adapter.CastAdapter;
import mvp.wyyne.douban.moviedouban.adapter.PhotoAdapter;
import mvp.wyyne.douban.moviedouban.api.RvItemOnClick;
import mvp.wyyne.douban.moviedouban.api.bean.Article;
import mvp.wyyne.douban.moviedouban.api.bean.Casts;
import mvp.wyyne.douban.moviedouban.api.bean.Directors;
import mvp.wyyne.douban.moviedouban.api.bean.Photos;
import mvp.wyyne.douban.moviedouban.api.bean.Trailers;
import mvp.wyyne.douban.moviedouban.detail.head.DetailMovieHeadFragment;
import mvp.wyyne.douban.moviedouban.home.BaseActivity;
import mvp.wyyne.douban.moviedouban.utils.StringUtils;
import mvp.wyyne.douban.moviedouban.widget.ExpandableTextView;
import mvp.wyyne.douban.moviedouban.widget.ObservableScrollView;
import mvp.wyyne.douban.moviedouban.widget.RecycleViewUtils;

/**
 * Created by XXW on 2017/6/18.
 */

public class DetailMovieActivity extends BaseActivity<DetailMoviePresent> implements
        IDetailMain, AppBarLayout.OnOffsetChangedListener {
    public static final String DETAIL_TAG = "detail";
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_img_title)
    TextView mTvImgTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.ll_title)
    RelativeLayout mLlTitle;
    @BindView(R.id.ll_layout)
    LinearLayout mLayout;
    @BindView(R.id.tl_bar)
    Toolbar mTlBar;
    @BindView(R.id.vp_detail)
    ViewPager mPager;
    @BindView(R.id.tl_detail)
    TabLayout mTabLayout;
    @BindView(R.id.abl_layout)
    AppBarLayout mBarLayout;
    @BindView(R.id.nv_detail)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.iv_avatars)
    ImageView mIvAvatars;
    @BindView(R.id.fl_avatars_bg)
    FrameLayout mFlAvatarsBg;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    private String mSubjectsId;
    private Bitmap mDrawableBitmap;
    private Palette.Builder mPalette;
    private Article mArticle;
    private int boundHeight;
    private Palette.Swatch swatch;
    private FragmentManager mManager;
    private FragmentTransaction mTransaction;

    @Override
    protected void refresh() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void initView() {
        if (getIntent().getStringExtra(DETAIL_TAG) != null) {
            mSubjectsId = getIntent().getStringExtra(DETAIL_TAG);
            Log.d("XXW", "mList------->" + mSubjectsId);
        }
        setSupportActionBar(mTlBar);
        //NestedScrollView中嵌套ViewPager不显示 设置为true
        mNestedScrollView.setFillViewport(true);
        mBarLayout.addOnOffsetChangedListener(this);
        mPresent = new DetailMoviePresent(this, getSupportFragmentManager());
        mPresent.getArticle(mSubjectsId);


        //初始化TabLayout
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText("评论"));
        mTabLayout.addTab(mTabLayout.newTab().setText("讨论区"));
        mTabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBlack)));

    }


    @Override
    public void initMovieImg(Article article) {
        mManager = getSupportFragmentManager();
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.fl_head, DetailMovieHeadFragment.getInstance(article));
        mTransaction.commit();

        mArticle = article;
        setBackGroudBg(article.getImages().getLarge());

    }

    @Override
    public void initMovieGrade() {
        String url = mArticle.getImages().getLarge();
        Glide.with(this).load(url).into(mIvAvatars);
        mPresent.initPage(mPager);
    }

    @Override
    public void onBindPage() {
        mTvTitle.setText(mArticle.getTitle());
        mTabLayout.setupWithViewPager(mPager);
    }


    //设置背景图片和设置电影海报图片
    public void setBackGroudBg(String url) {
        Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    mDrawableBitmap = resource;
                    //设置颜色调试器
                    mPalette = Palette.from(mDrawableBitmap);
                    //颜色调试器回调监听
                    mPalette.generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            swatch = palette.getMutedSwatch();
                            if (swatch != null) {
                                Log.d("XXW", "noinit------" + swatch.getRgb() + "---"
                                        + swatch.getBodyTextColor() + "---" + swatch.getPopulation() + "---"
                                        + swatch.getTitleTextColor() + "---"
                                );
                                mFlAvatarsBg.setBackgroundColor(swatch.getRgb());
                                mLlTitle.setBackgroundColor(Color.TRANSPARENT);
                                mLayout.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void show() {
        mLodingView.setVisibility(View.VISIBLE);
        mFlContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        mFlContent.setVisibility(View.GONE);
        mLodingView.setVisibility(View.GONE);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int y = Math.abs(verticalOffset);
        if (mIvAvatars != null) {
            boundHeight = mIvAvatars.getHeight();
            if (y <= 0) {
                titleHide();
//                Log.d("XXW", "onOffsetChanged-----<0" + verticalOffset);
                mLlTitle.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或者美工提供
            } else if (y > 0 && y <= boundHeight) {
//                Log.d("XXW", "onOffsetChanged----->0  <height"+"------"+verticalOffset);
                mLlTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTranslucence));
            } else {
                titleShow();
//                Log.d("XXW", "onOffsetChanged----->height---" + verticalOffset);
                mLlTitle.setBackgroundColor(swatch.getRgb());
            }
        }

    }


    public void titleHide() {
        mTvImgTitle.setVisibility(View.VISIBLE);
        mTvTitle.setVisibility(View.GONE);
    }

    public void titleShow() {
        mTvImgTitle.setVisibility(View.GONE);
        mTvTitle.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}
