package com.smartdengg.recyclerviewingg.activity.grid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.smartdengg.recyclerviewingg.BaseActivity;
import com.smartdengg.recyclerviewingg.IconsHelper;
import com.smartdengg.recyclerviewingg.R;
import com.smartdengg.recyclerviewingg.adapter.DrawableAdapter;
import com.smartdengg.recyclerviewingg.decoration.MarginDecoration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.BindString;

/**
 * Created by SmartDengg on 2016/1/29.
 *
 */
public class GridStandarOrHeaderdActivity extends BaseActivity {

  @BindString(R.string.grid_recyclerView_header) protected String title;

  private static final String TYPE = "TYPE";

  public static final int STANDARD = 1;
  public static final int HEADER = 2;

  @Retention(RetentionPolicy.SOURCE) @IntDef({ STANDARD, HEADER }) private @interface TYPE {}

  @NonNull @BindString(R.string.grid_recyclerView_standard) protected String standardTitle;
  @NonNull @BindString(R.string.grid_recyclerView_header) protected String HeaderTitle;

  @NonNull @Bind(R.id.main_recycler_layout_rv) protected RecyclerView recyclerView;

  public static void navigateToStandardOrHeaderActivity(BaseActivity startingActivity, @TYPE int type) {
    Intent intent = new Intent(startingActivity, GridStandarOrHeaderdActivity.class);
    intent.putExtra(TYPE, type);

    startingActivity.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recycler_main_layout);

    int type = getIntent().getIntExtra(TYPE, STANDARD);
    GridStandarOrHeaderdActivity.this.initView(type);

    getSupportActionBar().setTitle(type == STANDARD ? standardTitle : HeaderTitle);
  }

  private void initView(int type) {

//    Parameters:
//    context (GridStandarOrHeaderdActivity.this) - Current context, will be used to access resources.
//              spanCount (2) - The number of columns in the
    final GridLayoutManager gridLayoutManager = new GridLayoutManager(GridStandarOrHeaderdActivity.this, 2);
    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    gridLayoutManager.setSmoothScrollbarEnabled(true);

//    ItemDecoration - 负责绘制Item附近的分割线
//    这里MarginDecoration的构造类需要一个Context传值 (这里也可以定义一个ItemDecoration类，这样就不需要传值)
    recyclerView.addItemDecoration(new MarginDecoration(GridStandarOrHeaderdActivity.this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(gridLayoutManager);
    final DrawableAdapter drawableAdapter =
        DrawableAdapter.created(GridStandarOrHeaderdActivity.this, IconsHelper.BEE_ICONS, DrawableAdapter.BLUE);

    if (type == HEADER) {
      gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
//        这句话在这里永远是等于1
          return drawableAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
        }
      });
    }

    recyclerView.setAdapter(drawableAdapter);
  }

  @Override public void setupToolbar() {

    GridStandarOrHeaderdActivity.this.setNavigationIcon(toolbar, R.drawable.ic_arrow_back);
    GridStandarOrHeaderdActivity.this.polishDrawable(toolbar.getNavigationIcon(), android.R.color.white);
  }
}
