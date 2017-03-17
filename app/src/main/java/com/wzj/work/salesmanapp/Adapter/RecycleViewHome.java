package com.wzj.work.salesmanapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzj.work.salesmanapp.OtherClass.MerchantList;
import com.wzj.work.salesmanapp.OtherClass.MyShopList;
import com.wzj.work.salesmanapp.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/2/28.
 */
public class RecycleViewHome extends RecyclerView.Adapter<RecycleViewHome.ViewHolder>{
    Context context;
    ImageOptions options;
    ArrayList<MyShopList> shopLists;
    public RecycleViewHome(Context context,ArrayList<MyShopList>  shopLists) {
        this.context=context;
        this.shopLists=shopLists;
        options = new ImageOptions.Builder().setFadeIn(true).setRadius(20).build();
        //ImageOptions.Builder().setFadeIn(true).setCircular(true).build();
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnItemClickListener mListener;
        ImageView imageView;
        TextView tv1;
        TextView tv2;
        public ViewHolder(View view,OnItemClickListener listener) {
            super(view);
            mListener=listener;
            view.setOnClickListener(this);
            imageView= (ImageView) view.findViewById(R.id.iv_home_item);
            tv1= (TextView) view.findViewById(R.id.tv_home_item1);
            tv2= (TextView) view.findViewById(R.id.tv_home_item2);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_item,parent,false);
        return new ViewHolder(v,itemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        x.image().bind(holder.imageView,context.getString(R.string.http_tu)+shopLists.get(position).LogoImageUrl,options);
        holder.tv1.setText(shopLists.get(position).ShopName_CN);
        holder.tv2.setText(shopLists.get(position).ST);
    }
    @Override
    public int getItemCount() {
        return shopLists.size();
    }
}

