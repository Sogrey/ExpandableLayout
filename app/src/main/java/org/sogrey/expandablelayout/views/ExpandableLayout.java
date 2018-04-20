package org.sogrey.expandablelayout.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.sogrey.expandablelayout.R;

public class ExpandableLayout extends LinearLayout {

    private Context mContext;
    private LinearLayout mHandleView;
    private LinearLayout mContentView;
    private ImageButton mIconExpand;
    int mContentHeight = 0;
    int mTitleHeight = 0;
    private boolean isExpand;
    private Animation animationDown;
    private Animation animationUp;

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mContentHeight == 0) {
            this.mContentView.measure(widthMeasureSpec, 0);
            this.mContentHeight = this.mContentView.getMeasuredHeight();
        }
        if (this.mTitleHeight == 0) {
            this.mHandleView.measure(widthMeasureSpec, 0);
            this.mTitleHeight = this.mHandleView.getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //在这里直接写不便于封装
//        this.mHandleView = (LinearLayout) this
//                .findViewById(R.id.lyt_parent);//点击展开的父类布局
//        this.mContentView = (LinearLayout) this.findViewById(R.id.lyt_expand_content);//要展开的布局
//        this.mIconExpand = (ImageButton) this.findViewById(R.id.btn_expand);//图片
//
//        this.mIconExpand.setOnClickListener(new ExpandListener());//展开、隐藏监听
//        this.mHandleView.setOnClickListener(new ExpandListener());//展开、隐藏监听
//        //this.mContentView.setOnClickListener(new ExpandListener());
//        mContentView.setVisibility(View.GONE);
    }

    private class ExpandListener implements OnClickListener {
        @Override
        public final void onClick(View paramView) {
            //clearAnimation是view的方法
            clearAnimation();
            if (!isExpand) {
                if (animationDown == null) {
                    animationDown = new DropDownAnim(mContentView,
                            mContentHeight, true);
                    animationDown.setDuration(200); // SUPPRESS CHECKSTYLE
                }
                startAnimation(animationDown);
                mContentView.startAnimation(AnimationUtils.loadAnimation(
                        mContext, R.anim.animalpha));
                mIconExpand.setImageResource(R.drawable.ic_shouqi);
                isExpand = true;
                if (mOnExpandListenerListener != null) {
                    mOnExpandListenerListener.post(isExpand);
                }
            } else {
                isExpand = false;
                if (animationUp == null) {
                    animationUp = new DropDownAnim(mContentView,
                            mContentHeight, false);
                    animationUp.setDuration(200); // SUPPRESS CHECKSTYLE
                }
                startAnimation(animationUp);
                mIconExpand.setImageResource(R.drawable.ic_zhankai);
                if (mOnExpandListenerListener != null) {
                    mOnExpandListenerListener.post(isExpand);
                }
            }
        }
    }

    class DropDownAnim extends Animation {
        /**
         * 目标的高度
         */
        private int targetHeight;
        /**
         * 目标view
         */
        private View view;
        /**
         * 是否向下展开
         */
        private boolean down;

        /**
         * 构造方法
         *
         * @param targetview 需要被展现的view
         * @param vieweight  目的高
         * @param isdown     true:向下展开，false:收起
         */
        public DropDownAnim(View targetview, int vieweight, boolean isdown) {
            this.view = targetview;
            this.targetHeight = vieweight;
            this.down = isdown;
        }

        //down的时候，interpolatedTime从0增长到1，这样newHeight也从0增长到targetHeight
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            int newHeight;
            if (down) {
                newHeight = (int) (targetHeight * interpolatedTime);
            } else {
                newHeight = (int) (targetHeight * (1 - interpolatedTime));
            }
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    /**
     * 在这设置方便封装
     * @param resParentLayoutId 父布局容器ID
     * @param resExpandLayoutId 可展开内容的布局ID
     * @param resExpandImageId  展开图标imageView/imageButton的Id
     */
    public void setIds(int resParentLayoutId, int resExpandLayoutId, int resExpandImageId) {
        this.mHandleView = (LinearLayout) this
                .findViewById(resParentLayoutId);//点击展开的父类布局
        this.mContentView = (LinearLayout) this.findViewById(resExpandLayoutId);//要展开的布局
        this.mIconExpand = (ImageButton) this.findViewById(resExpandImageId);//图片

        this.mIconExpand.setOnClickListener(new ExpandListener());//展开、隐藏监听
        this.mHandleView.setOnClickListener(new ExpandListener());//展开、隐藏监听
        //this.mContentView.setOnClickListener(new ExpandListener());
        mContentView.setVisibility(View.GONE);
    }

    OnExpandListenerListener mOnExpandListenerListener;

    /**
     * 设置展开监听
     *
     * @param l
     */
    public void setOnExpandListenerListener(OnExpandListenerListener l) {
        mOnExpandListenerListener = l;
    }

    /**
     * 设置默认的展开状态
     *
     * @param isExpand
     */
    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        mContentView.setVisibility(isExpand ? View.VISIBLE : View.GONE);
    }

    public interface OnExpandListenerListener {
        void post(boolean isExpand);
    }
}