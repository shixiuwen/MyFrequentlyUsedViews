package cn.sxvisual.myfrequentlyusedviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.sxvisual.myfrequentlyusedviews.R;

/**
 * @author ShiXiuwen
 * @version V1.0 <自定义横向的键值控件，左边是标题，右边是值，左右自定义是否显示icon>
 * <pre>
 *         <declare-styleable name="CustomKeyValueView">
 *  *         <attr name="ckv_title" format="string" />
 *  *         <attr name="ckv_value" format="string" />
 *  *         <attr name="ckv_icon_start_visible" format="boolean" />
 *  *         <attr name="ckv_icon_end_visible" format="boolean" />
 *  *         <attr name="ckv_value_editable" format="boolean" />
 *  *         <attr name="ckv_key_length" format="dimension" />
 *  *         <attr name="ckv_icon_start_res" format="reference" />
 *  *         <attr name="ckv_icon_start_size" format="dimension" />
 *  *         <attr name="ckv_icon_end_res" format="reference" />
 *  *         <attr name="ckv_icon_end_size" format="dimension" />
 *  *         <attr name="ckv_hint" format="string" />
 *  *         <attr name="ckv_divider_visible" format="boolean" />
 *  *     </declare-styleable>
 *     </pre>
 * @date 2019/2/18 0018
 */
public class CustomKeyValueView extends LinearLayout {

    private ImageView ivIconStart;
    private TextView tvKey;
    private TextView tvValue;
    private EditText etValue;
    private ImageView ivIconEnd;
    private View viewDivider;
    private boolean valueEditable;
    private boolean iconEndClickable;

    public CustomKeyValueView(Context context) {
        this(context, null);
    }

    public CustomKeyValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomKeyValueView);
        String title = typedArray.getString(R.styleable.CustomKeyValueView_ckv_title);
        String value = typedArray.getString(R.styleable.CustomKeyValueView_ckv_value);
        String hint = typedArray.getString(R.styleable.CustomKeyValueView_ckv_hint);
        valueEditable = typedArray.getBoolean(R.styleable.CustomKeyValueView_ckv_value_editable, false);
        int gravity = typedArray.getInt(R.styleable.CustomKeyValueView_ckv_edit_value_gravity, -1);
        boolean iconStartVisible = typedArray.getBoolean(R.styleable.CustomKeyValueView_ckv_icon_start_visible, false);
        float iconStartSize = typedArray.getDimension(R.styleable.CustomKeyValueView_ckv_icon_start_size, -1);
        Drawable iconStartDrawable = typedArray.getDrawable(R.styleable.CustomKeyValueView_ckv_icon_start_res);
        Drawable iconEndDrawable = typedArray.getDrawable(R.styleable.CustomKeyValueView_ckv_icon_end_res);
        boolean iconEndVisible = typedArray.getBoolean(R.styleable.CustomKeyValueView_ckv_icon_end_visible, true);
        float iconEndSize = typedArray.getDimension(R.styleable.CustomKeyValueView_ckv_icon_end_size, -1);
        iconEndClickable = typedArray.getBoolean(R.styleable.CustomKeyValueView_ckv_icon_end_clickable, false);
        float keyLength = typedArray.getDimension(R.styleable.CustomKeyValueView_ckv_key_length, -1);
        boolean dividerVisible = typedArray.getBoolean(R.styleable.CustomKeyValueView_ckv_divider_visible, true);
        String digits = typedArray.getString(R.styleable.CustomKeyValueView_ckv_digits);
        int inputType = typedArray.getInt(R.styleable.CustomKeyValueView_ckv_input_type, -1);
        typedArray.recycle();

        tvKey.setText(title);
        tvValue.setText(value);
        etValue.setText(value);
        etValue.setHint(hint);
        tvValue.setHint(hint);
        tvValue.setVisibility(valueEditable ? View.GONE : View.VISIBLE);
        etValue.setVisibility(valueEditable ? View.VISIBLE : View.GONE);
        viewDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);

        //值的gravity是靠左显示还是靠右显示，如果可以编辑，默认靠右显示
        if (valueEditable) {
            if (gravity != -1) {
                etValue.setGravity(Gravity.CENTER_VERTICAL | gravity);
            }
        }
        //前面图标的设置
        if (iconStartVisible) {
            ivIconStart.setVisibility(View.VISIBLE);
            ivIconStart.setImageDrawable(iconStartDrawable);
            if (iconStartSize != -1) {
                ViewGroup.LayoutParams layoutParams = ivIconStart.getLayoutParams();
                layoutParams.width = (int) iconStartSize;
                layoutParams.height = (int) iconStartSize;
            }
        } else {
            ivIconStart.setVisibility(View.GONE);
        }

        //后面图标的设置
        if (iconEndVisible) {
            ivIconEnd.setVisibility(View.VISIBLE);
            if (iconEndDrawable != null) {
                ivIconEnd.setImageDrawable(iconEndDrawable);
            }
            if (iconEndSize != -1) {
                ViewGroup.LayoutParams layoutParams = ivIconEnd.getLayoutParams();
                layoutParams.width = (int) iconEndSize;
                layoutParams.height = (int) iconEndSize;
            }
            int lineCount = tvValue.getLineCount();
            if (lineCount > 1) {
                tvValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            } else {
                tvValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            }
        } else {
            ivIconEnd.setVisibility(View.GONE);
        }

        //设置key的文本显示长度
        if (keyLength != -1) {
            ViewGroup.LayoutParams layoutParams = tvKey.getLayoutParams();
            layoutParams.width = (int) keyLength;
        }

        if (iconEndClickable) {
            ivIconEnd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onIconClickListener != null) {
                        onIconClickListener.onIconClick(CustomKeyValueView.this);
                    }
                }
            });
        }

        //输入框接收输入类型设置
        if (!TextUtils.isEmpty(digits)) {
            etValue.setKeyListener(DigitsKeyListener.getInstance(digits));
        }
        if (inputType != -1) {
            etValue.setInputType(inputType);
        }
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_custom_key_value_, this);
        ivIconStart = view.findViewById(R.id.iv_icon_start);
        tvKey = view.findViewById(R.id.tv_key);
        tvValue = view.findViewById(R.id.tv_value);
        etValue = view.findViewById(R.id.et_value);
        ivIconEnd = view.findViewById(R.id.iv_icon_end);
        viewDivider = view.findViewById(R.id.view_divider);

        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etValue.getLineCount() > 1) {
                    etValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                } else {
                    etValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                }
            }
        });
    }

    /**
     * 设置值
     *
     * @param value
     */
    public void setValue(String value) {
        if (valueEditable) {
            etValue.setText(value);
        } else {
            tvValue.setText(value);
        }
        if (etValue.getLineCount() > 1) {
            etValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        } else {
            etValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        }
        if (tvValue.getLineCount() > 1) {
            etValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        } else {
            etValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        }
    }

    public void setHint(String hint) {
        if (valueEditable) {
            etValue.setHint(hint);
        } else {
            tvValue.setHint(hint);
        }
    }

    /**
     * 获取值
     *
     * @return
     */
    public String getValue() {
        return valueEditable ? etValue.getText().toString() : tvValue.getText().toString();
    }

    public String getHint() {
        return valueEditable ? etValue.getHint().toString() : tvValue.getHint().toString();
    }

    public void setKey(String key) {
        tvKey.setText(key);
    }

    public boolean isValueEditable() {
        return valueEditable;
    }

    public void setValueEditable(boolean valueEditable) {
        this.valueEditable = valueEditable;
        tvValue.setVisibility(this.valueEditable ? View.GONE : View.VISIBLE);
        etValue.setVisibility(this.valueEditable ? View.VISIBLE : View.GONE);
    }

    private OnIconClickListener onIconClickListener;

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }

    public interface OnIconClickListener {
        void onIconClick(CustomKeyValueView customKeyValueView);
    }
}
