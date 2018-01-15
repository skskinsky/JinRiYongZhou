/*
 * Copyright (C) 2013 Lemon Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dingtai.jinriyongzhou.view;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.database.DataBaseHelper;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.ButtomMenuAdapter;
import com.dingtai.jinriyongzhou.model.NewsADs;
import com.dingtai.newslib3.activity.CommonActivity;
import com.dingtai.newslib3.activity.NewsListActivity;
import com.dingtai.newslib3.activity.NewsWebView;
import com.dingtai.newslib3.activity.TestNewsDetailActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ExpandableButtonMenu extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "ExpandableButtonMenu";

    public enum MenuButton {
        MID, LEFT, RIGHT
    }

    /**
     * DEFAULT BUTTON SIZE AND DISTANCE VALUES
     */

    private static final float DEFAULT_BOTTOM_PADDING = 0.05f;
    private static final float DEFAULT_MAIN_BUTTON_SIZE = 0.25f;
    private static final float DEFAULT_OTHER_BUTTON_SIZE = 0.2f;
    private static final float DEFAULT_BUTTON_DISTANCE_Y = 0.15f;
    private static final float DEFAULT_BUTTON_DISTANCE_X = 0.27f;
    private DataBaseHelper databaseHelper = null;


    /**
     * Screen metrics
     */
    protected int sWidth;
    protected int sHeight;

    private ExpandableMenuOverlay mParent;


    private View mMidContainer;
    private View mRightContainer;
    private View mLeftContainer;

    private ImageButton mCloseBtn;
    private ImageButton mMidBtn;
    private ImageButton mRightBtn;
    private ImageButton mLeftBtn;

    private TextView mMidText;
    private TextView mRightText;
    private TextView mLeftText;

    private ListView listView;
    private ButtomMenuAdapter adAdapter;
    private List<NewsADs> adList;

    private String LinkTo;
    private String LinkUrl;
    private String ChID;
    private String ADFor;
    private String ResType;
    private String ResUrl;
    private String AdName;

    private boolean isShow = true;


    /**
     * Flag indicating that the menu is expanded or collapsed
     */
    private boolean mExpanded;

    /**
     * Flag indicating if clicking anywhere on the screen collapses the menu
     */
    private boolean mAllowOverlayClose = true;

    /**
     * Flag indicating that menu is being animated
     */
    private boolean mAnimating;


    /**
     * Menu button position variables in % of screen width or height
     */
    protected float bottomPadding = DEFAULT_BOTTOM_PADDING;
    protected float mainButtonSize = DEFAULT_MAIN_BUTTON_SIZE;
    protected float otherButtonSize = DEFAULT_OTHER_BUTTON_SIZE;
    protected float buttonDistanceY = DEFAULT_BUTTON_DISTANCE_Y;
    protected float buttonDistanceX = DEFAULT_BUTTON_DISTANCE_X;

    /**
     * Button click interface. Use setOnMenuButtonClickListener() to
     * register callbacks
     */
    private OnMenuButtonClick mListener;

    public ExpandableButtonMenu(Context context) {
        this(context, null, 0);
    }

    public ExpandableButtonMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableButtonMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate();
        parseAttributes(attrs);
        setViewLayoutParams();
        calculateAnimationProportions();
    }


    /**
     * Set a top level overlay that acts as a proxy to this view. In a
     * a current implementation the content of the expandable menu is
     * set to a dialog. This allows the control over native screen dim
     * and keyboard key callbacks.
     *
     * @param parent
     */
    public void setButtonMenuParentOverlay(ExpandableMenuOverlay parent) {
        mParent = parent;
    }

    /**
     * Set a callback on menu button clicks
     *
     * @param listener
     */
    public void setOnMenuButtonClickListener(OnMenuButtonClick listener) {
        mListener = listener;
    }

    /**
     * Returns the menu button container. The first child of the container is
     * a TextView, the second - an ImageButton
     *
     * @param button one of {@link MenuButton#LEFT}, {@link MenuButton#MID}, {@link MenuButton#RIGHT}
     */
    public View getMenuButton(MenuButton button) {
        switch (button) {
            case MID:
                return mMidContainer;
            case LEFT:
                return mLeftContainer;
            case RIGHT:
                return mRightContainer;
        }
        return null;
    }


    public void hideLiveBtn() {
        isShow = false;
        adList.clear();
        RuntimeExceptionDao<NewsADs, String> adDao = getHelper()
                .getMode(NewsADs.class);
        adList = adDao.queryForAll();
        adAdapter.setData(adList);
        adAdapter.notifyDataSetChanged();

    }

    public void showLiveBtn() {
        isShow = true;
        adList.clear();
        RuntimeExceptionDao<NewsADs, String> adDao = getHelper()
                .getMode(NewsADs.class);
        adList = adDao.queryForAll();
        adAdapter.setData(adList);
        adAdapter.notifyDataSetChanged();
    }


    /**
     * Set text appearance for button text views
     *
     * @param appearanceResource
     */
    public void setMenuTextAppearance(int appearanceResource) {
        mLeftText.setTextAppearance(getContext(), appearanceResource);
        mMidText.setTextAppearance(getContext(), appearanceResource);
        mRightText.setTextAppearance(getContext(), appearanceResource);
    }

    /**
     * Set image resource for a menu button
     *
     * @param button
     * @param imageResource
     */
    public void setMenuButtonImage(MenuButton button, int imageResource) {
        setMenuButtonImage(button, getResources().getDrawable(imageResource));
    }

    /**
     * Set image drawable for a menu button
     *
     * @param button
     * @param drawable
     */
    public void setMenuButtonImage(MenuButton button, Drawable drawable) {
        switch (button) {
            case MID:
                mMidBtn.setImageDrawable(drawable);
                break;
            case LEFT:
                mLeftBtn.setImageDrawable(drawable);
                break;
            case RIGHT:
                mRightBtn.setImageDrawable(drawable);
                break;
        }
    }

    /**
     * Set string resource displayed under a menu button
     *
     * @param button
     * @param stringResource
     */
    public void setMenuButtonText(MenuButton button, int stringResource) {
        setMenuButtonText(button, getContext().getString(stringResource));
    }

    /**
     * Set text displayed under a menu button
     *
     * @param button
     * @param text
     */
    public void setMenuButtonText(MenuButton button, String text) {
        switch (button) {
            case MID:
                mMidText.setText(text);
                break;
            case LEFT:
                mLeftText.setText(text);
                break;
            case RIGHT:
                mRightText.setText(text);
                break;
        }
    }

    public void setAllowOverlayClose(boolean allow) {
        mAllowOverlayClose = allow;
    }

    public void setAnimating(boolean isAnimating) {
        mAnimating = isAnimating;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public boolean isAllowOverlayClose() {
        return mAllowOverlayClose;
    }

    public float getMainButtonSize() {
        return mainButtonSize;
    }

    public float getBottomPadding() {
        return bottomPadding;
    }

    public float getOtherButtonSize() {
        return otherButtonSize;
    }

    public float getTranslationY() {
        return TRANSLATION_Y;
    }

    public float getTranslationX() {
        return TRANSLATION_X;
    }


    /**
     * Toggle the expandable menu button, expanding or collapsing it
     */
    public void toggle() {
        if (!mAnimating) {
            mAnimating = true;
            if (mExpanded) {
                animateCollapse();
            } else {
                animateExpand();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ebm__menu_left_image) {
            if (mListener != null) mListener.onClick(MenuButton.LEFT);
        } else if (id == R.id.ebm__menu_middle_image) {
            if (mListener != null) mListener.onClick(MenuButton.MID);
        } else if (id == R.id.ebm__menu_right_image) {
            if (mListener != null) mListener.onClick(MenuButton.RIGHT);
        } else if (id == R.id.ebm__menu_close_image) {
            toggle();
        }
    }


    /**
     * Inflates the view
     */
    private void inflate() {
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ebm__menu, this, true);


        mMidContainer = findViewById(R.id.ebm__menu_middle_container);
        mLeftContainer = findViewById(R.id.ebm__menu_left_container);
        mRightContainer = findViewById(R.id.ebm__menu_right_container);


        mCloseBtn = (ImageButton) findViewById(R.id.ebm__menu_close_image);
        mMidBtn = (ImageButton) findViewById(R.id.ebm__menu_middle_image);
        mRightBtn = (ImageButton) findViewById(R.id.ebm__menu_right_image);
        mLeftBtn = (ImageButton) findViewById(R.id.ebm__menu_left_image);


        mMidText = (TextView) findViewById(R.id.ebm__menu_middle_text);
        mLeftText = (TextView) findViewById(R.id.ebm__menu_left_text);
        mRightText = (TextView) findViewById(R.id.ebm__menu_right_text);


        listView = (ListView) findViewById(R.id.ad_listview);
        adList = new ArrayList<NewsADs>();
        adAdapter = new ButtomMenuAdapter(getContext(), adList);
        listView.setAdapter(adAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                LinkTo = adList.get(position).getLinkTo();
                LinkUrl = adList.get(position).getLinkUrl();
                ChID = adList.get(position).getChID();
                ADFor = adList.get(position).getADFor();
                ResType = adList.get(position).getResType();
                ResUrl = adList.get(position).getResUrl();
                AdName = adList.get(position).getADName();
                adGoTo();
            }
        });


        sWidth = DisplayMetricsTool.getWidth(getContext());
        sHeight = DisplayMetricsTool.getHeight(getContext());

        mMidBtn.setEnabled(false);
        mRightBtn.setEnabled(false);
        mLeftBtn.setEnabled(false);

        mCloseBtn.setOnClickListener(this);
        mMidBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mLeftBtn.setOnClickListener(this);

    }


    private void adGoTo() {
        try {
            // ADFor 1
            // LinkTo 1 详情
            // LinkTo 2 列表
            if (ADFor.equals("1")) {
                if (LinkTo.equals("1")) {
                    if (ResType.equals("2")) {
                        Intent intent = new Intent(getContext(),
                                NewsWebView.class);
                        intent.putExtra("Title", AdName);
                        intent.putExtra("PageUrl", ResUrl);
                        getContext().startActivity(intent);
                    } else {
                        String[] LinkUrls = LinkUrl.split(",");
                        String strTempID = LinkUrls[0];
                        String strTempGUID = LinkUrls[1];
                        Intent intent = new Intent(getContext(),
                                TestNewsDetailActivity.class);
                        intent.putExtra("ID", strTempGUID);
                        intent.putExtra("ResourceType", strTempID);
                        intent.putExtra("type", 1);
                        getContext().startActivity(intent);
                    }
                } else if (LinkTo.equals("2")) {
                    Intent intent = new Intent(getContext(),
                            NewsListActivity.class);
                    intent.putExtra("lanmuChID", ChID);
                    intent.putExtra("ChannelName", "新闻列表");
                    getContext().startActivity(intent);
                }
            }
            // ADFor 2
            // LinkTo 1 商品详情
            // LinkTo 2 商城
            else if (ADFor.equals("2")) {
                if (LinkTo.equals("1")) {

                } else if (LinkTo.equals("2")) {

                }
            }
            // ADFor 3
            // LinkTo 1 活动详情
            // LinkTo 2 活动列表
            else if (ADFor.equals("3")) {
                if (LinkTo.equals("1")) {
                    Intent intent = new Intent(getContext(),
                            NewsWebView.class);
                    intent.putExtra("Title", "网页新闻");
                    intent.putExtra("PageUrl", LinkUrl);
                    getContext().startActivity(intent);
                } else if (LinkTo.equals("2")) {
                    Intent intent = new Intent(getContext(),
                            CommonActivity.class);
                    intent.putExtra("name", "活动");
                    getContext().startActivity(intent);
                }
            }
            // ADFor 4 和1一样
            else if (ADFor.equals("4")) {
                if (LinkTo.equals("1")) {
                    String[] LinkUrls = LinkUrl.split(",");
                    String strTempID = LinkUrls[0];
                    String strTempGUID = LinkUrls[1];
                    Intent intent = new Intent(getContext(),
                            TestNewsDetailActivity.class);
                    intent.putExtra("ID", strTempGUID);
                    intent.putExtra("ResourceType", strTempID);
                    intent.putExtra("type", 1);
                    getContext().startActivity(intent);
                } else if (LinkTo.equals("2")) {
                    Intent intent = new Intent(getContext(),
                            NewsListActivity.class);
                    intent.putExtra("lanmuChID", ChID);
                    intent.putExtra("ChannelName", "新闻列表");
                    getContext().startActivity(intent);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 获得数据库管理
     *
     * @return
     */

    private DataBaseHelper getHelper() {
        if (databaseHelper == null)
            databaseHelper = ((DataBaseHelper) OpenHelperManager.getHelper(
                    getContext(), DataBaseHelper.class));
        return this.databaseHelper;
    }

    /**
     * Parses custom XML attributes
     *
     * @param attrs
     */
    private void parseAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandableMenuOverlay, 0, 0);
            try {
                // button metrics
                mainButtonSize = a.getFloat(R.styleable.ExpandableMenuOverlay_mainButtonSize, DEFAULT_MAIN_BUTTON_SIZE);
                otherButtonSize = a.getFloat(R.styleable.ExpandableMenuOverlay_otherButtonSize, DEFAULT_OTHER_BUTTON_SIZE);
                bottomPadding = a.getFloat(R.styleable.ExpandableMenuOverlay_bottomPad, DEFAULT_BOTTOM_PADDING);
                buttonDistanceY = a.getFloat(R.styleable.ExpandableMenuOverlay_distanceY, DEFAULT_BUTTON_DISTANCE_Y);
                buttonDistanceX = a.getFloat(R.styleable.ExpandableMenuOverlay_distanceX, DEFAULT_BUTTON_DISTANCE_X);

                // button resources
                mCloseBtn.setBackgroundResource(a.getResourceId(R.styleable.ExpandableMenuOverlay_closeButtonSrc, 0));
                mLeftBtn.setBackgroundResource(a.getResourceId(R.styleable.ExpandableMenuOverlay_leftButtonSrc, 0));
                mRightBtn.setBackgroundResource(a.getResourceId(R.styleable.ExpandableMenuOverlay_rightButtonSrc, 0));
                mMidBtn.setBackgroundResource(a.getResourceId(R.styleable.ExpandableMenuOverlay_midButtonSrc, 0));

                // button text
                mLeftText.setText(a.getResourceId(R.styleable.ExpandableMenuOverlay_leftButtonText, R.string.empty));
                mRightText.setText(a.getResourceId(R.styleable.ExpandableMenuOverlay_rightButtonText, R.string.empty));
                mMidText.setText(a.getResourceId(R.styleable.ExpandableMenuOverlay_midButtonText, R.string.empty));

            } finally {
                a.recycle();
            }
        }
    }

    /**
     * Initialized the layout of menu buttons. Sets button sizes and distances between them
     * by a % of screen width or height accordingly.
     * Some extra padding between buttons is added by default to avoid intersections.
     */
    private void setViewLayoutParams() {
        // Some extra margin to center other buttons in the center of the main button

        RelativeLayout. LayoutParams rParams = (RelativeLayout.LayoutParams) mCloseBtn.getLayoutParams();

        rParams.setMargins(0, 0, 0, DisplayMetricsTool.dip2px(getContext(), 22));

        rParams = (RelativeLayout. LayoutParams) mMidContainer.getLayoutParams();
        rParams.setMargins(0, 0, 0, DisplayMetricsTool.dip2px(getContext(), 22));
        rParams = (RelativeLayout. LayoutParams) mRightContainer.getLayoutParams();
        rParams.setMargins(0, 0, 0, DisplayMetricsTool.dip2px(getContext(), 22));
        rParams = (RelativeLayout. LayoutParams) mLeftContainer.getLayoutParams();
        rParams.setMargins(0, 0, 0, DisplayMetricsTool.dip2px(getContext(), 22));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMidBtn.getLayoutParams();
        params.width = DisplayMetricsTool.dip2px(getContext(), 50);
        params.height = DisplayMetricsTool.dip2px(getContext(), 50);

        params = (LinearLayout.LayoutParams) mRightBtn.getLayoutParams();
        params.width = DisplayMetricsTool.dip2px(getContext(), 50);
        params.height = DisplayMetricsTool.dip2px(getContext(), 50);

        params = (LinearLayout.LayoutParams) mLeftBtn.getLayoutParams();
        params.width = DisplayMetricsTool.dip2px(getContext(), 50);
        params.height = DisplayMetricsTool.dip2px(getContext(), 50);
    }


    /**
     * ANIMATION DEFINITIONS
     */

    /**
     * We don't use AnimatorSet so we have our own counter to see whether all animations have ended
     */
    private volatile byte ANIMATION_COUNTER;

    /**
     * Collapse and expand animation duration
     */
    private static final int ANIMATION_DURATION = 300;

    /**
     * Used interpolators
     */
    private static final float INTERPOLATOR_WEIGHT = 2f;
    private AnticipateInterpolator anticipation;
    private OvershootInterpolator overshoot;
    private DecelerateInterpolator decelerateInterpolator;
    private AccelerateInterpolator accelerateInterpolator;


    /**
     * Translation in Y axis of all three menu buttons
     */
    protected float TRANSLATION_Y;

    /**
     * Translation in X axis for left and right buttons
     */
    protected float TRANSLATION_X;

    /**
     * Initialized animation properties
     */
    private void calculateAnimationProportions() {
        TRANSLATION_Y = sHeight * buttonDistanceY;
        TRANSLATION_X = sWidth * buttonDistanceX;

        anticipation = new AnticipateInterpolator(INTERPOLATOR_WEIGHT);
        overshoot = new OvershootInterpolator(INTERPOLATOR_WEIGHT);
        decelerateInterpolator = new DecelerateInterpolator(INTERPOLATOR_WEIGHT);
        accelerateInterpolator = new AccelerateInterpolator(2.0f);
    }

    /**
     * Start expand animation
     */
    private void animateExpand() {
        mCloseBtn.setVisibility(View.VISIBLE);
        if (isShow) {
            mMidContainer.setVisibility(View.VISIBLE);
        } else {
            mMidContainer.setVisibility(View.INVISIBLE);
        }

        mRightContainer.setVisibility(View.VISIBLE);
        mLeftContainer.setVisibility(View.VISIBLE);


        ANIMATION_COUNTER = 0;
        mCloseBtn.animate().setDuration(300).rotation(-45);

        mMidContainer.animate().setDuration(500).scaleX(1.5f).scaleY(1.5f).setListener(ON_EXPAND_COLLAPSE_LISTENER).translationYBy(-TRANSLATION_Y).setInterpolator(overshoot);

        mRightContainer.animate().setDuration(700).translationYBy(-TRANSLATION_Y).translationXBy(TRANSLATION_X).setDuration(700).scaleX(1.5f).scaleY(1.5f).setListener(ON_EXPAND_COLLAPSE_LISTENER).setInterpolator(overshoot);

        mLeftContainer.animate().setDuration(300).translationYBy(-TRANSLATION_Y).translationXBy(-TRANSLATION_X).scaleX(1.5f).scaleY(1.5f).setListener(ON_EXPAND_COLLAPSE_LISTENER).setInterpolator(overshoot);

    }

    /**
     * Start collapse animation
     */
    private void animateCollapse() {
        mCloseBtn.setVisibility(View.VISIBLE);

        ANIMATION_COUNTER = 0;

        mCloseBtn.animate().setDuration(300).rotation(0);

        mMidContainer.animate().setDuration(450).translationYBy(TRANSLATION_Y).scaleX(0.8f).scaleY(0.8f).setListener(ON_EXPAND_COLLAPSE_LISTENER).setInterpolator(accelerateInterpolator);

        mRightContainer.animate().setDuration(300).translationYBy(TRANSLATION_Y).scaleX(0.8f).scaleY(0.8f).setListener(ON_EXPAND_COLLAPSE_LISTENER).translationXBy(-TRANSLATION_X).setInterpolator(accelerateInterpolator);

        mLeftContainer.animate().setDuration(600).translationYBy(TRANSLATION_Y).translationXBy(TRANSLATION_X).scaleX(0.8f).scaleY(0.8f).setListener(ON_EXPAND_COLLAPSE_LISTENER).setInterpolator(accelerateInterpolator);

    }


    /**
     * Listener for expand and collapse animations
     */
    private Animator.AnimatorListener ON_EXPAND_COLLAPSE_LISTENER = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (mCloseBtn.isEnabled())
                mCloseBtn.setEnabled(false);


            mMidText.setVisibility(View.INVISIBLE);
            mLeftText.setVisibility(View.INVISIBLE);
            mRightText.setVisibility(View.INVISIBLE);


        }

        @Override
        public void onAnimationEnd(Animator animation) {


            ANIMATION_COUNTER++;

            if (ANIMATION_COUNTER == 1 && mExpanded)
                mParent.showInitButton();

            if (ANIMATION_COUNTER == 3) {

                if (mExpanded) {
                    mCloseBtn.setVisibility(View.GONE);
                    mMidContainer.setVisibility(View.GONE);
                    mRightContainer.setVisibility(View.GONE);
                    mLeftContainer.setVisibility(View.GONE);

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mParent.dismiss();
                            mParent.mDismissing = false;
                        }
                    }, 75);
                } else {
                    if (isShow) {
                        mMidText.setVisibility(View.VISIBLE);
                    } else {
                        mMidText.setVisibility(View.INVISIBLE);
                    }

                    mLeftText.setVisibility(View.VISIBLE);
                    mRightText.setVisibility(View.VISIBLE);

                }

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //invalidateViewsForPreHC();
                        mAnimating = false;
                        mExpanded = !mExpanded;
                    }
                }, 50);


                mCloseBtn.setEnabled(true);
                mMidBtn.setEnabled(true);
                mRightBtn.setEnabled(true);
                mLeftBtn.setEnabled(true);

            }

        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    /**
     * Button click callback interface
     */
    public interface OnMenuButtonClick {
        public void onClick(MenuButton action);
    }


}

