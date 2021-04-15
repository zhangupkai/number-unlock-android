package com.example.numberunlock.panel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.numberunlock.R;

import java.util.ArrayList;
import java.util.List;

import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;


public class EyeUnlockPanel extends LinearLayout {
    private String[] numArr = new String[]{"1","2","3","4","5","6","7","8","9", "", "0"};

    private int mPaddingLeftRight;
    private int mPaddingTopBottom;
    //4个密码位ImageView
    private ArrayList<CircleImageView> mResultIvList;

    private LinearLayout inputResultView;
    //存储当前输入内容
    private StringBuilder mPassWord;

    // 存储输入的重按轻按信息
    private StringBuffer mForce;

    // 存储每次输入的按压时长
    private List<Long> mDuration = new ArrayList<>();

    //振动效果
    private Vibrator mVibrator;
    //整个键盘的颜色
    private int mPanelColor;
    //4个密码位的宽度
    private int mResultIvRadius;
    //数字键盘的每个圆的宽度
    private int mNumRadius;
    //每个圆的边界宽度
    private int mStrokeWidth;

    public EyeUnlockPanel(Context context) {
        this(context, null);
    }

    public EyeUnlockPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EyeUnlockPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaddingLeftRight = dip2px(context, 21);
        mPaddingTopBottom = dip2px(context, 10);
        mPanelColor = Color.BLACK; //颜色代码可采用Color.parse("#000000");
        mResultIvRadius = dip2px(context, 20);
        mNumRadius = dip2px(context, 66);
        mStrokeWidth = dip2px(context, 2);
        mVibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
        mResultIvList = new ArrayList<>();
        mPassWord = new StringBuilder();
        mForce = new StringBuffer();
        setOrientation(VERTICAL);
        setGravity(CENTER_HORIZONTAL);
        initView(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initView(Context context){
        //4个结果号码
        inputResultView = new LinearLayout(context);
        for(int i=0; i<4; i++){
            CircleImageView mResultItem = new CircleImageView(context);
            mResultIvList.add(mResultItem);
            LayoutParams params = new LayoutParams(mResultIvRadius, mResultIvRadius);
            params.leftMargin = dip2px(context, 4);
            params.rightMargin = dip2px(context, 4);
            mResultItem.setPadding(dip2px(context, 2),dip2px(context, 2),dip2px(context, 2),dip2px(context, 2));
            mResultItem.setLayoutParams(params);
            inputResultView.addView(mResultItem);
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = dip2px(context, 34);
        inputResultView.setLayoutParams(params);
        addView(inputResultView);

        //数字键盘
        GridLayout numContainer = new GridLayout(context);
        numContainer.setColumnCount(3);
        for(int i=0; i<numArr.length; i++){
            RelativeLayout numItem = new RelativeLayout(context);
            numItem.setPadding(mPaddingLeftRight,mPaddingTopBottom,mPaddingLeftRight,mPaddingTopBottom);
            RelativeLayout.LayoutParams gridItemParams = new RelativeLayout.LayoutParams(mNumRadius, mNumRadius);
            gridItemParams.addRule(CENTER_IN_PARENT);
            final TextView numTv = new TextView(context);
            numTv.setText(numArr[i]);
            numTv.setTextColor(mPanelColor);
            numTv.setTextSize(30);
            numTv.setGravity(Gravity.CENTER);
            numTv.setLayoutParams(gridItemParams);
            final CircleImageView numBgIv = new CircleImageView(context);
            numBgIv.setLayoutParams(gridItemParams);
            numTv.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            numBgIv.setFillCircle();
                            numTv.setTextColor(Color.WHITE);
//                            float pressure = event.getPressure();
//                            float size = event.getSize();
//                            System.out.println("Pressure: " + pressure);
//                            System.out.println("Size: " + size);
                            if(mPassWord.length() < 4){
                                mPassWord.append(numTv.getText());
                                mResultIvList.get(mPassWord.length()-1).setFillCircle();
//                                if(mInputListener!=null && mPassWord.length() == 4){
//                                    mInputListener.inputFinish(mPassWord.toString());
//                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            numBgIv.setStrokeCircle();
                            numTv.setTextColor(mPanelColor);
                            Long mTime = event.getEventTime() - event.getDownTime();
                            System.out.println("Each Duration: " + mTime);
                            mDuration.add(mTime);
                            System.out.println("Duration List: " + mDuration);
                            System.out.println("mPassword Size: " + mPassWord.length());

                            if (mPassWord.length() == 4) {
                                long sumDuration = 0;
                                for (long duration : mDuration) {
                                    sumDuration += duration;
                                }
                                long avgDuration = sumDuration / mDuration.size();
                                System.out.println("Avg Duration: " + avgDuration);


                                for (long duration : mDuration) {
                                    if (duration >= avgDuration) {
                                        mForce.append("1");
                                    }
                                    else {
                                        mForce.append("0");
                                    }
                                    if (mInputListener != null && mForce.length() == 4) {
                                        mInputListener.inputFinish(mPassWord.toString(), mForce.toString());
                                    }
                                }
                            }

                            break;
                    }
                    return true;
                }
            });

            numItem.addView(numBgIv);
            numItem.addView(numTv);
            numContainer.addView(numItem);
            if(i == 9){
                numItem.setVisibility(INVISIBLE);
            }
        }

        //删除按钮
        RelativeLayout deleteItem = new RelativeLayout(context);
        deleteItem.setPadding(mPaddingLeftRight,mPaddingTopBottom,mPaddingLeftRight,mPaddingTopBottom);
        RelativeLayout.LayoutParams gridItemParams = new RelativeLayout.LayoutParams(mNumRadius, mNumRadius);
        gridItemParams.addRule(CENTER_IN_PARENT);
        //假如删除按钮是设置自定义图片资源的话，可用注释这段
        //ImageView deleteIv = new ImageView(context);
        //deleteIv.setImageResource(R.drawable.icn_delete_pw);
        //deleteIv.setLayoutParams(gridItemParams);
        //deleteItem.addView(deleteIv);
        TextView deleteTv = new TextView(context);
        deleteTv.setText(R.string.delete);
        deleteTv.setTextColor(mPanelColor);
        deleteTv.setTextSize(dip2px(context, 8));
        deleteTv.setLayoutParams(gridItemParams);
        deleteTv.setGravity(Gravity.CENTER);
        deleteItem.addView(deleteTv);
        numContainer.addView(deleteItem);
        deleteTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        LayoutParams gridParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gridParams.gravity = Gravity.CENTER_HORIZONTAL;
        numContainer.setLayoutParams(gridParams);
        addView(numContainer);
    }

    /**
     * 输入错误的状态显示(包括震动，密码位左右摇摆效果，重置密码位)
     */
    public void showErrorStatus(){
        mVibrator.vibrate(new long[]{100,100,100,100},-1);
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(inputResultView, "translationX", -50.0f,50.0f,-50.0f,0.0f);
        translationXAnim.setDuration(400);
        animators.add(translationXAnim);
        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.start();
        btnSexAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetResult();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 输入正确的显示（重置密码位）
     */
    public void showCorrectStatus() {
        List<Animator> animators = new ArrayList<>();
        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.start();
        btnSexAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetResult();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 删除
     */
    public void delete(){
//        if((mPassWord.length() == 0) || (mForce.length() == 0)){
//            return;
//        }
        mResultIvList.get(mPassWord.length()-1).setStrokeCircle();
        if (mPassWord.length() != 0) {
            mPassWord.deleteCharAt(mPassWord.length()-1);
        }
        // TODO mForce删除逻辑要改 mDuration删除逻辑待添加
        if (mForce.length() != 0) {
            mForce.deleteCharAt(mForce.length() - 1);
        }
        if (mDuration != null && !mDuration.isEmpty()) {
            mDuration.remove(mDuration.size() - 1);
        }

    }

    /**
     * 重置密码输入
     */
    public void resetResult(){
        for(int i=0; i<mResultIvList.size(); i++){
            mResultIvList.get(i).setStrokeCircle();
        }
        mPassWord.delete(0, 4);
        mForce.delete(0, 4);
        mDuration.clear();
    }

    /**
     * 监听输入完毕的接口
     */
    private InputListener mInputListener;

    public void setInputListener(InputListener mInputListener) {
        this.mInputListener = mInputListener;
    }

    public interface InputListener{
        void inputFinish(String resultPwd, String resultForce);
//        void inputFinishForce(String forceResult);
    }


    /**
     * dip/dp转像素
     *
     * @param dipValue
     *            dip或 dp大小
     * @return 像素值
     */
    public static int dip2px(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dipValue * (metrics.density) + 0.5f);
    }

    /**
     * 圆形背景ImageView（设置实心或空心）
     */
    public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {

        private Paint mPaint;
        private int mWidth;
        private int mHeight;

        public CircleImageView(Context context) {
            this(context, null);
        }

        public CircleImageView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView(context);
        }

        public void initView(Context context){
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mPanelColor);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setAntiAlias(true);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mWidth = w;
            mHeight = h;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2 - 6, mPaint);
            super.draw(canvas);
        }

        /**
         * 设置圆为实心状态
         */
        public void setFillCircle(){
            mPaint.setStyle(Paint.Style.FILL);
            invalidate();
        }

        /**
         * 设置圆为空心状态
         */
        public void setStrokeCircle(){
            mPaint.setStyle(Paint.Style.STROKE);
            invalidate();
        }
    }

}
