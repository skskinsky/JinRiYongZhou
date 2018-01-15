package com.dingtai.jinriyongzhou.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dingtai.base.model.BaoliaoFileModel;
import com.dingtai.dtpolitics.R;
import com.dingtai.jinriyongzhou.activity.ContributeActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

public class WenzhengImageAdapter extends BaseAdapter {
    private Activity context;
    private List<BaoliaoFileModel> list;
    private int screenWidth;
    private int itemWidth;
    private int itemHeight;
    private int isDel = 0;		//判断是否删除
    private ContributeActivity activity;
    private DisplayImageOptions options;

    public WenzhengImageAdapter(Activity context, List<BaoliaoFileModel> list) {
        this.context = context;
        screenWidth = context.getWindow().getWindowManager()
                .getDefaultDisplay().getWidth();
        itemWidth = (screenWidth - dip2px(context, 3)) / 3-dip2px(context, 6);
        //itemWidth = 125;
        itemHeight = itemWidth;
        this.list = list;
        this.activity = (ContributeActivity) context;

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.dt_standard_index_news_bg).showImageForEmptyUri(R.drawable.dt_standard_index_news_bg)
                .showImageOnFail(R.drawable.dt_standard_index_news_bg).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size()>0?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position)==null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.edit_photo_item, null);
            holder.iv_showImage = (ImageView) convertView
                    .findViewById(R.id.iv_showimage);
            holder.iv_item_flag = (ImageView) convertView
                    .findViewById(R.id.iv_item_flag);
            holder.iv_delimage = (ImageView) convertView
                    .findViewById(R.id.iv_delimage);
            holder.iv_showImage.setLayoutParams(new RelativeLayout.LayoutParams(
                    itemWidth, itemHeight));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //ImageLoader.getInstance().displayImage("file://"+list.get(position).getFileImagUrl(), holder.iv_showImage, options);
        holder.iv_showImage.setImageDrawable(scaleImage(list.get(position).getFileImagUrl()));
        if(list.get(position).getFileType().equalsIgnoreCase("video"))
            holder.iv_item_flag.setVisibility(View.VISIBLE);
        else
            holder.iv_item_flag.setVisibility(View.GONE);
        if(getIsDel()!=0){
            holder.iv_delimage.setVisibility(View.VISIBLE);
            holder.iv_delimage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO 自动生成的方法存根
                    list.remove(position);
                    activity.setFileList(position);
                    //notifyDataSetChanged();

                }
            });
        }
        return convertView;
    }

    /**
     * 图片压缩
     *
     * @param
     * @return
     */
    private Drawable scaleImage(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        int scale = (int) (options.outHeight / (float) itemHeight);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return new BitmapDrawable(BitmapFactory.decodeFile(pathName, options));

    }

    private class ViewHolder {
        private ImageView iv_showImage;
        private ImageView iv_item_flag;
        private ImageView iv_delimage;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }




}
