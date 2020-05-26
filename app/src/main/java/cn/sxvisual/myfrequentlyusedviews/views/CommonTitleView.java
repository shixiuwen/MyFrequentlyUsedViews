package cn.sxvisual.myfrequentlyusedviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import cn.sxvisual.myfrequentlyusedviews.R;

/**
 * 通用头部，包括标题，返回键，右上交的icon
 */
public class CommonTitleView extends ConstraintLayout implements View.OnClickListener {
    private ViewGroup llRoot;
    private ViewGroup llOption;
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivIcon;
    private ImageView ivIconSub;
    private TextView tvRight;

    private OnCommonTitleBackClickListener onCommonTitleBackClickListener;
    private OnCommonTitleIconClickListener onCommonTitleIconClickListener;
    private OnCommonTitleTxSubmitClickListener mOnCommonTitleTxSubmitClickListener;
    private OnCommonTitleIconSubClickListener mOnCommonTitleIconSubClickListener;

    private OnCommonTitleTxClickListener onCommonTitleTxClickListener;

    public CommonTitleView(Context context) {
        super(context, null);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_common_title, this);
        llRoot = view.findViewById(R.id.ll_root);
        llOption = view.findViewById(R.id.ll_option);
        tvTitle = view.findViewById(R.id.tv_title);
        ivBack = view.findViewById(R.id.iv_back);
        ivIcon = view.findViewById(R.id.iv_icon);
        ivIconSub = view.findViewById(R.id.iv_icon_sub);
        tvRight = view.findViewById(R.id.tv_right);
        ivBack.setOnClickListener(this);
        ivIcon.setOnClickListener(this);
        ivIconSub.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        //重置返回按钮宽度
        post(() -> {
            int width = llOption.getWidth();
            ViewGroup.LayoutParams layoutParams = ivBack.getLayoutParams();
            layoutParams.width = width;
            ivBack.setLayoutParams(layoutParams);
        });
    }

    public CommonTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleView);
        String title = typedArray.getString(R.styleable.CommonTitleView_title);
        int iconBack = typedArray.getResourceId(R.styleable.CommonTitleView_back_visible, R.drawable.icon_back);
        int iconRight = typedArray.getResourceId(R.styleable.CommonTitleView_icon, R.drawable.icon_about_normal);
        int iconRightSub = typedArray.getResourceId(R.styleable.CommonTitleView_icon_sub, R.drawable.icon_about_normal);
        boolean backVisible = typedArray.getBoolean(R.styleable.CommonTitleView_back_visible, true);
        boolean iconRightVisible = typedArray.getBoolean(R.styleable.CommonTitleView_icon_visible, false);
        boolean iconRightSubVisible = typedArray.getBoolean(R.styleable.CommonTitleView_icon_sub_visible, false);
        String txSubName = typedArray.getString(R.styleable.CommonTitleView_tx_sub_name);
        boolean txSubVisible = typedArray.getBoolean(R.styleable.CommonTitleView_tx_sub_visible, false);
        int bgColor = typedArray.getColor(R.styleable.CommonTitleView_ctv_background, ContextCompat.getColor(context, R.color.colorPrimary));

        tvTitle.setText(title);
        ivBack.setImageResource(iconBack);
        ivBack.setVisibility(backVisible ? View.VISIBLE : View.INVISIBLE);
        ivIcon.setImageResource(iconRight);
        ivIconSub.setImageResource(iconRightSub);
        ivIcon.setVisibility(iconRightVisible ? View.VISIBLE : View.GONE);
        ivIconSub.setVisibility(iconRightSubVisible ? View.VISIBLE : View.GONE);
        tvRight.setText(txSubName);
        tvRight.setVisibility(txSubVisible ? VISIBLE : GONE);
        llRoot.setBackgroundColor(bgColor);

        typedArray.recycle();
    }

    /**
     * 动态设置标题
     *
     * @param s
     */
    public void setTvTitle(String s) {
        tvTitle.setText(s);
    }

    /**
     * 获取右边按钮的显示文字
     *
     * @return
     */
    public String getSubCommitTitleTx() {
        return tvRight.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (onCommonTitleBackClickListener != null) {
                    onCommonTitleBackClickListener.onTitleBackClick();
                }
                break;
            case R.id.iv_icon:
                if (onCommonTitleIconClickListener != null) {
                    onCommonTitleIconClickListener.onTitleIconClick();
                }
                break;
            case R.id.iv_icon_sub:
                if (mOnCommonTitleIconSubClickListener != null) {
                    mOnCommonTitleIconSubClickListener.onTitleSubmitSubClick();
                }
                break;
            case R.id.tv_right:
                if (mOnCommonTitleTxSubmitClickListener != null) {
                    mOnCommonTitleTxSubmitClickListener.onTitleSubmitClick();
                }
                break;
        }
    }

    /**
     * 动态设置右边文字按钮的内容
     *
     * @param subTxName
     */
    public void setTxSubName(String subTxName) {
        this.tvRight.setVisibility(VISIBLE);
        this.tvRight.setText(subTxName);
    }

    /**
     * 动态设置Icon
     *
     * @param resourceId
     */
    public void setIvIconSub(int resourceId) {
        this.ivIconSub.setImageResource(resourceId);
    }

    public void setIvIconSubVisible(int visible) {
        this.ivIconSub.setVisibility(visible);
    }

    public void setOnCommonTitleBackClickListener(OnCommonTitleBackClickListener onCommonTitleBackClickListener) {
        this.onCommonTitleBackClickListener = onCommonTitleBackClickListener;
    }

    public void setOnCommonTitleIconClickListener(OnCommonTitleIconClickListener omOnCommonTitleIconClickListener) {
        this.onCommonTitleIconClickListener = omOnCommonTitleIconClickListener;
    }

    public void setOnCommonTitleTxSubmitClickListener(OnCommonTitleTxSubmitClickListener onCommonTitleTxSubmitClickListener) {
        this.mOnCommonTitleTxSubmitClickListener = onCommonTitleTxSubmitClickListener;
    }

    public void setOnCommonTitleIconSubClickListener(OnCommonTitleIconSubClickListener onCommonTitleIconSubClickListener) {
        this.mOnCommonTitleIconSubClickListener = onCommonTitleIconSubClickListener;
    }

    public void setOnCommonTitleTxClickListener(OnCommonTitleTxClickListener onCommonTitleTxClickListener) {
        this.onCommonTitleTxClickListener = onCommonTitleTxClickListener;
    }

    public interface OnCommonTitleBackClickListener {
        void onTitleBackClick();
    }

    public interface OnCommonTitleIconClickListener {
        void onTitleIconClick();
    }

    public interface OnCommonTitleTxSubmitClickListener {
        void onTitleSubmitClick();
    }

    public interface OnCommonTitleIconSubClickListener {
        void onTitleSubmitSubClick();
    }

    public interface OnCommonTitleTxClickListener {
        void onTitleTxClick();
    }
}
