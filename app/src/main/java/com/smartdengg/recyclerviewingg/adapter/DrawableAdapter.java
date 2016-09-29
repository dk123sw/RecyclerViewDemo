package com.smartdengg.recyclerviewingg.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smartdengg.recyclerviewingg.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * RecyclerView.Adapter - 处理数据集合并负责绑定视图
 */
public class DrawableAdapter extends RecyclerView.Adapter<DrawableAdapter.ViewHolder> {

  public static final int ORANGE = 1;
  public static final int BLUE = 2;
  public static final int PURPLE = 3;

//  @Retention: 表示需要在什么级别保存该注解信息 SOURCE: 注解将被编译器丢弃
/*
      @IntDef
      使用整型常量代替枚举类型（性能考虑），例如我们有一个IceCreamFlavourManager类，它具有三种模式的操作：
    VANILLA，CHOCOLATE和STRAWBERRY。我们可以定义一个名为@Flavour的新注解，并使用@IntDef指
    定它可以接受的值类型。
    public class IceCreamFlavourManager {

    private int flavour;

    public static final int VANILLA = 0;
    public static final int CHOCOLATE = 1;
    public static final int STRAWBERRY = 2;

    @IntDef({VANILLA, CHOCOLATE, STRAWBERRY})
    public @interface Flavour {
    }

    @Flavour
    public int getFlavour() {
        return flavour;
    }

    public void setFlavour(@Flavour int flavour) {
        this.flavour = flavour;
    }

}
   */
  @Retention(RetentionPolicy.SOURCE) @IntDef({ ORANGE, BLUE, PURPLE }) private @interface TYPE {}

  private Context context;
//  条目列表
  private List<Integer> items;
  private int type;

  public static DrawableAdapter created(Context context, List<Integer> items, @TYPE int type) {
    return new DrawableAdapter(context, items, type);
  }

  private DrawableAdapter(Context context, List<Integer> items, @TYPE int type) {
    this.context = context;
    this.items = items;
    this.type = type;
  }

//  此类作用为创建Item视图，并返回相应的ViewHolder
  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (type == ORANGE) {
      return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.drawable_card_orange_item, parent, false));
    }
    if (type == BLUE) {
      return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.drawable_card_bule_item, parent, false));
    }
    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.drawable_card_purple_item, parent, false));
  }

//  绑定数据到正确的Item视图上
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bitmapIv.setImageDrawable(context.getResources().getDrawable(items.get(position)));
  }

//  返回该Adapter所持有的Item数量
  @Override public int getItemCount() {
    return (this.items != null) ? items.size() : 0;
  }

//  返回头部
  public boolean isHeader(int position) {
    return position == 0;
  }

  public void updateItems(List<Integer> items) {
    this.items = items;
//    当数据集合发生改变时，我们通过调用.notifyDataSetChanged()，来刷新列表。
//    但这样做会触发列表的重绘，所以并不会出现任何动画效果
    DrawableAdapter.this.notifyDataSetChanged();
  }

  //  ViewHolder - 持有所有的用于绑定数据或者需要操作的View
  public static final class ViewHolder extends RecyclerView.ViewHolder {

//    不同布局里相同名字的三种布局   操作View
    @NonNull @Bind(R.id.drawable_card_item_iv) protected ImageView bitmapIv;

//    绑定数据
    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(ViewHolder.this, itemView);
    }
  }
}
