package com.smartdengg.recyclerviewingg.activity.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.smartdengg.recyclerviewingg.BaseActivity;
import com.smartdengg.recyclerviewingg.IconsHelper;
import com.smartdengg.recyclerviewingg.R;
import com.smartdengg.recyclerviewingg.adapter.DrawableAdapter;
import com.smartdengg.recyclerviewingg.decoration.MarginDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.BindString;

/**
 * @利用下拉刷新瀑布流
 */
public class SwipeRefreshActivity extends BaseActivity {

  private static final String TAG = SwipeRefreshActivity.class.getSimpleName();

  private static final int ICON = 0;
  private static final int CAT_ICON = 1;
  private static final int BEE_ICON = 2;

  @BindString(R.string.swipe_recyclerView) protected String title;

  @NonNull @Bind(R.id.swipe_layout_sr) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @Bind(R.id.main_recycler_layout_rv) protected RecyclerView recyclerView;

  private DrawableAdapter drawableAdapter;

//  主线程
  private Handler handler = new Handler(Looper.getMainLooper());

  private static final Integer[] icons =
      { IconsHelper.getRandomIcon(), IconsHelper.getRandomCatIcon(), IconsHelper.getRandomBeeIcon() };

  /*
  * SwipeRefreshLayout控件值允许有一个子元素：我们想滑动刷新的对象。它使用Listener机制来告之持有SwipeRefreshLayout
  * 的组建某个事件发生了，也就是说假如是activity持有SwipeRefreshLayout，那么activity就必须实现一个接口来接收通知，
  * 这个接口中需要实现的主要是onRefresh()方法
  * */
  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override public void onRefresh() {
//    isRefreshing(): 检查是否处于刷新状态
//    下拉刷新
      if (swipeRefreshLayout.isRefreshing()) {
        SwipeRefreshActivity.this.postItems();
      }
    }
  };

  public static void navigateToSwipeRefreshActivity(BaseActivity startingActivity) {
    startingActivity.startActivity(new Intent(startingActivity, SwipeRefreshActivity.class));
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recycler_swipe_layout);
    SwipeRefreshActivity.this.initView();
  }

  private void initView() {

    swipeRefreshLayout.setColorSchemeResources(IconsHelper.colors);
//  setOnRefreshListener(OnRefreshListener): 为布局添加一个Listener
    swipeRefreshLayout.setOnRefreshListener(refreshListener);
    swipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
//      setRefreshing(boolean): 显示或隐藏刷新进度条
        swipeRefreshLayout.setRefreshing(true);
      }
    });

    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    recyclerView.addItemDecoration(new MarginDecoration(SwipeRefreshActivity.this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(staggeredGridLayoutManager);

    SwipeRefreshActivity.this.postItems();
  }

  @CheckResult @NonNull private List<Integer> installItems() {

    List<Integer> items = new ArrayList<>();

    for (int i = 0; i < 40; i++) {

      switch (new Random().nextInt(icons.length)) {
        case ICON:
          items.add(IconsHelper.getRandomIcon());
          break;
        case CAT_ICON:
          items.add(IconsHelper.getRandomCatIcon());
          break;
        case BEE_ICON:
          items.add(IconsHelper.getRandomBeeIcon());
          break;
      }
    }

    return items;
  }

  private void postItems() {
    handler.postDelayed(new Runnable() {
      @Override public void run() {

        swipeRefreshLayout.setRefreshing(false);
        if (drawableAdapter == null) {
          drawableAdapter = DrawableAdapter.created(SwipeRefreshActivity.this, installItems(), DrawableAdapter.PURPLE);
          recyclerView.setAdapter(drawableAdapter);
        } else {
          SwipeRefreshActivity.this.drawableAdapter.updateItems(SwipeRefreshActivity.this.installItems());
        }
        recyclerView.smoothScrollToPosition(0);
      }
    }, 4 * 1000);
  }

  @Override public void setupToolbar() {
    getSupportActionBar().setTitle(title);

    SwipeRefreshActivity.this.setNavigationIcon(toolbar, R.drawable.ic_arrow_back);
    SwipeRefreshActivity.this.polishDrawable(toolbar.getNavigationIcon(), android.R.color.white);
  }

  @Override protected void onDestroy() {
    swipeRefreshLayout.setOnRefreshListener(null);
    handler.removeCallbacksAndMessages(null);
    super.onDestroy();
  }
}
