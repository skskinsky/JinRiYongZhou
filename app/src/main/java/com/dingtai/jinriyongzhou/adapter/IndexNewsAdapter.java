package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.other.IndexType;
import com.dingtai.base.utils.CommonSubscriptMethod;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.HHIndexNewsListModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Dangelo on 2016/9/27.
 */
public class IndexNewsAdapter extends BaseAdapter {

    private final int BIGIMG_VIEW = 1;
    private final int SMALL_VIEW = 2;
    private final int PIC_VIEW = 3;
    private final int TEXT_VIEW = 4;

    private Context context;
    private List<HHIndexNewsListModel> list;
    private OnItemClickListener clickListener;

    public IndexNewsAdapter(Context context, List<HHIndexNewsListModel> list) {
        this.context = context;
        this.list = list;

    }

    public void addOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 12;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int viewType = SMALL_VIEW;
        if (convertView == null) {
            if (viewType == BIGIMG_VIEW) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news1, parent, false);
                holder = new BIGImgViewHolder(convertView);
            } else if (viewType == PIC_VIEW) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news3, parent, false);
                holder = new PICViewHolder(convertView);
            } else if (viewType == SMALL_VIEW) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news2, parent, false);
                holder = new SAMLLImgViewHolder(convertView);
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news4, parent, false);
                holder = new TextViewHolder(convertView);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        onBindViewHolder(holder, position);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getResourceCSS().equals("3")) {
            return BIGIMG_VIEW;
        } else if (list.get(position).getResourceCSS().equals("2")) {
            return PIC_VIEW;
        } else if (list.get(position).getResourceCSS().equals("1") && list.get(position).getSmallPicUrl().equals("")) {
            return TEXT_VIEW;
        } else {
            return SMALL_VIEW;
        }
    }

    public View onCreateView(ViewGroup parent, int viewType, ViewHolder holder) {
        View view;
        if (viewType == BIGIMG_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news1, parent, false);
            holder = new BIGImgViewHolder(view);
        } else if (viewType == PIC_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news3, parent, false);
            holder = new PICViewHolder(view);
        } else if (viewType == SMALL_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news2, parent, false);
            holder = new SAMLLImgViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_news4, parent, false);
            holder = new TextViewHolder(view);
        }
        return view;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof BIGImgViewHolder) {
            BIGImgViewHolder viewHolder = (BIGImgViewHolder) holder;
            RelativeLayout.LayoutParams imgpar = (RelativeLayout.LayoutParams) viewHolder.news_img.getLayoutParams();
            imgpar.width = DisplayMetricsTool.getWidth(context);
            imgpar.height = (int) (imgpar.width * 9 / 16);// 控件的宽强制设成2:3

            viewHolder.news_img.setLayoutParams(imgpar); //使设置好的布局参数应用到控件

            ImageLoader.getInstance().displayImage(list.get(position).getSmallPicUrl(), viewHolder.news_img);


            if (list.get(position).getIsTop().equals("True")) {
                viewHolder.news_flag.setVisibility(View.VISIBLE);
            } else {
                viewHolder.news_flag.setVisibility(View.GONE);
            }

            viewHolder.news_title.setText(list.get(position).getTitle());
            viewHolder.news_summary.setText(list.get(position).getSummary());

        } else if (holder instanceof TextViewHolder) {
            TextViewHolder viewHolder = (TextViewHolder) holder;

            viewHolder.news_title.setText(list.get(position).getTitle());

            // 设置角标
            IndexType strSub = CommonSubscriptMethod.setNewsSubscript(list.get(position).getResourceFlag(), context);
            if (strSub != null) {
                viewHolder.news_plnum.setText(strSub.getType());
                viewHolder.news_plnum.setTextColor(
                        strSub.getColor());
                viewHolder.news_plnum.setBackgroundDrawable(
                        strSub.getDraw());

            } else {
                viewHolder.news_plnum.setText("");
                viewHolder.news_plnum.setBackgroundColor(0);// R.drawable.news_zhuangti
            }
        } else if (holder instanceof SAMLLImgViewHolder) {
            SAMLLImgViewHolder viewHolder = (SAMLLImgViewHolder) holder;

            ImageLoader.getInstance().displayImage(list.get(position).getSmallPicUrl(), viewHolder.news_img);
            viewHolder.news_title.setText(list.get(position).getTitle());
            //viewHolder.news_plnum.setText(list.get(position).getFakeReadNo());


            // 设置角标
            IndexType strSub = CommonSubscriptMethod.setNewsSubscript(list.get(position).getResourceFlag(), context);
            if (strSub != null) {
                viewHolder.news_plnum.setText(strSub.getType());
                viewHolder.news_plnum.setTextColor(
                        strSub.getColor());
                viewHolder.news_plnum.setBackgroundDrawable(
                        strSub.getDraw());

            } else {
                viewHolder.news_plnum.setText("");
                viewHolder.news_plnum.setBackgroundColor(0);// R.drawable.news_zhuangti
            }
        } else if (holder instanceof PICViewHolder) {
            PICViewHolder viewHolder = (PICViewHolder) holder;


            viewHolder.news_title.setText(list.get(position).getTitle());

            // 设置角标
            IndexType strSub = CommonSubscriptMethod.setNewsSubscript(list.get(position).getResourceFlag(), context);
            if (strSub != null) {
                viewHolder.news_plnum.setText(strSub.getType());
                viewHolder.news_plnum.setTextColor(
                        strSub.getColor());
                viewHolder.news_plnum.setBackgroundDrawable(
                        strSub.getDraw());

            } else {
                viewHolder.news_plnum.setText("");
                viewHolder.news_plnum.setBackgroundColor(0);// R.drawable.news_zhuangti
            }

            String imgURL[] = list.get(position).getUploadPicNames().split(",");
            int width = DisplayMetricsTool.getWidth(context)
                    - DisplayMetricsTool.dip2px(context, 30);

            LinearLayout.LayoutParams layout1 = new LinearLayout.LayoutParams(
                    width / 3, width / 3 * 9 / 16);
            layout1.leftMargin = DisplayMetricsTool.dip2px(context, 10);


            viewHolder.news_img1.setLayoutParams(
                    layout1);

            LinearLayout.LayoutParams layout2 = new LinearLayout.LayoutParams(
                    width / 3, width / 3 * 9 / 16);

            layout2.leftMargin = DisplayMetricsTool.dip2px(context, 5);
            layout2.rightMargin = DisplayMetricsTool.dip2px(context, 5);

            viewHolder.news_img2.setLayoutParams(
                    layout2);


            LinearLayout.LayoutParams layout3 = new LinearLayout.LayoutParams(
                    width / 3, width / 3 * 9 / 16);

            layout3.rightMargin = DisplayMetricsTool.dip2px(context, 10);
            viewHolder.news_img3.setLayoutParams(
                    layout3);


            viewHolder.news_img1.setImageBitmap(null);
            viewHolder.news_img2.setImageBitmap(null);
            viewHolder.news_img3.setImageBitmap(null);

            // 获取图片
            try {
                if (imgURL[0].length() > 0) {
                    ImageLoader.getInstance().displayImage(imgURL[0],
                            viewHolder.news_img1);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                if (imgURL[1].length() > 0) {
                    ImageLoader.getInstance().displayImage(imgURL[1],
                            viewHolder.news_img2);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                if (imgURL[2].length() > 0) {
                    ImageLoader.getInstance().displayImage(imgURL[2],
                            viewHolder.news_img3);
                }
            } catch (Exception e) {
            }


        }
    }


    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    class PICViewHolder extends ViewHolder {

        ImageView news_img1;
        ImageView news_img2;
        ImageView news_img3;
        TextView news_title;

        TextView news_plnum;


        public PICViewHolder(View itemView) {
            news_img1 = (ImageView) itemView.findViewById(R.id.news_img1);
            news_img2 = (ImageView) itemView.findViewById(R.id.news_img2);
            news_img3 = (ImageView) itemView.findViewById(R.id.news_img3);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_plnum = (TextView) itemView.findViewById(R.id.news_plnum);
            itemView.setTag(this);
        }
    }

    class BIGImgViewHolder extends ViewHolder {


        ImageView news_img;
        ImageView news_flag;
        TextView news_title;
        TextView news_summary;


        public BIGImgViewHolder(View itemView) {
            news_img = (ImageView) itemView.findViewById(R.id.news_img);
            news_flag = (ImageView) itemView.findViewById(R.id.news_flag);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_summary = (TextView) itemView.findViewById(R.id.news_summary);
            itemView.setTag(this);
        }
    }


    class TextViewHolder extends ViewHolder {

        TextView news_title;
        TextView news_plnum;


        public TextViewHolder(View itemView) {
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_plnum = (TextView) itemView.findViewById(R.id.news_plnum);
            itemView.setTag(this);
        }
    }


    class SAMLLImgViewHolder extends ViewHolder {

        ImageView news_img;
        TextView news_title;
        TextView news_plnum;


        public SAMLLImgViewHolder(View itemView) {
            news_img = (ImageView) itemView.findViewById(R.id.news_img);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_plnum = (TextView) itemView.findViewById(R.id.news_plnum);
            itemView.setTag(this);
        }
    }

    public void setData(List<HHIndexNewsListModel> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

}