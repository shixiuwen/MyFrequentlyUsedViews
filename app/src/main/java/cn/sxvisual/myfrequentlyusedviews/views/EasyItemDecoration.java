package cn.sxvisual.myfrequentlyusedviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Shixiuwen
 * @version 1.0
 * 通用的RecyclerView分割线，未实现GridLayoutManger分割线样式，根据需求添加其他内容
 */
public class EasyItemDecoration extends RecyclerView.ItemDecoration {

    private int orientation;
    public static int ORIENTATION_HORIZONTAL = 0;
    public static int ORIENTATION_VERTICAL = 1;
    private int dividerHeight;   //分割线竖直方向上宽度
    private int dividerWidth;   //分割线水平方向上高度
    private boolean containFirst;
    private boolean containLast;
    private boolean isTransparent;
    private Paint dividerPaint;
    private int dividerColor;

    private EasyItemDecoration(Builder builder) {
        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        //设置分割线颜色
        if (builder.isTransparent) {
            dividerPaint.setColor(Color.argb(0, 1, 1, 1));
        } else {
            dividerPaint.setColor(builder.color);
        }
        //是否透明
        isTransparent = builder.isTransparent;
        //分割线颜色
        dividerColor = builder.color;
        //设置分割线竖直方向高度
        dividerHeight = builder.dividerHeight;
        //设置分割线水平方向高度
        dividerWidth = builder.dividerWidth;
        //是否显示在顶部
        containFirst = builder.containFirst;
        //是否显示在底部
        containLast = builder.containLast;
        //设置方向
        orientation = builder.orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (orientation == ORIENTATION_HORIZONTAL) {
            outRect.set(dividerWidth, 0, 0, 0);
            if (containLast) {
                if (childAdapterPosition == adapter.getItemCount() - 1) {
                    outRect.set(dividerWidth, 0, dividerWidth, 0);
                }
            }
            if (!containFirst) {
                if (childAdapterPosition == 0) {
                    outRect.set(0, 0, 0, 0);
                }
            }
        } else {
            outRect.set(0, dividerHeight, 0, 0);
            if (containLast) {
                if (childAdapterPosition == adapter.getItemCount() - 1) {
                    outRect.set(0, dividerHeight, 0, dividerHeight);
                }
            }
            if (!containFirst) {
                if (childAdapterPosition == 0) {
                    outRect.set(0, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (isTransparent || dividerPaint.getAlpha() == 0) {
            return;
        }
        int paddingLeft = parent.getPaddingLeft();
        int paddingRight = parent.getPaddingRight();
        int paddingTop = parent.getPaddingTop();
        int paddingBottom = parent.getPaddingBottom();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int left = view.getLeft();
            int right = view.getRight();
            int top = view.getTop();
            int bottom = view.getBottom();
            if (orientation == ORIENTATION_HORIZONTAL) {
                if (i == 0) {
                    if (!containFirst) {
                        continue;
                    }
                } else if (i == parent.getChildCount() - 1) {
                    if (containLast) {
                        c.drawRect(right, paddingTop, right + dividerWidth, parent.getBottom() - paddingBottom, dividerPaint);
                    }
                }
                c.drawRect(left - dividerWidth, paddingTop, left, parent.getBottom() - paddingBottom, dividerPaint);
            } else {
                if (i == 0) {                                     //第一个
                    if (!containFirst) {
                        continue;
                    }
                } else if (i == parent.getChildCount() - 1) {      //最后一个
                    if (containLast) {
                        //在底部绘制一个，顶部的也正常绘制
                        c.drawRect(paddingLeft, bottom, parent.getRight() - paddingRight, bottom + dividerHeight, dividerPaint);
                    }
                }
                //正常绘制
                c.drawRect(paddingLeft, top - dividerHeight, parent.getRight() - paddingRight, top, dividerPaint);
            }

        }
    }

    public static class Builder {
        private Context context;
        private boolean containFirst = true;
        private boolean containLast = true;
        private boolean isTransparent = true;
        private int color = Color.parseColor("#00FFFFFF");
        private int dividerHeight = 10;
        private int dividerWidth = 10;
        private int orientation = EasyItemDecoration.ORIENTATION_VERTICAL;

        public Builder(Context context) {
            this.context = context;
        }

        public EasyItemDecoration create() {
            return new EasyItemDecoration(this);
        }

        public Builder setContainFirst(boolean containFirst) {
            this.containFirst = containFirst;
            return this;
        }

        public Builder setContainLast(boolean containLast) {
            this.containLast = containLast;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setDividerHeight(int dividerHeight) {
            this.dividerHeight = dividerHeight;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTransparent(boolean transparent) {
            isTransparent = transparent;
            return this;
        }

        public Builder setDividerWidth(int dividerWidth) {
            this.dividerWidth = dividerWidth;
            return this;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }
    }
}
