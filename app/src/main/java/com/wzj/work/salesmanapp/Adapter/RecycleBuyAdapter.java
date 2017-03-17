package com.wzj.work.salesmanapp.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzj.work.salesmanapp.OtherClass.Food;
import com.wzj.work.salesmanapp.OtherClass.GroupBuy;
import com.wzj.work.salesmanapp.R;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/2.
 */
public class RecycleBuyAdapter extends RecyclerView.Adapter<RecycleBuyAdapter.ViewHolder> {
    Context context;
    ArrayList<GroupBuy> foodList = new ArrayList<>();
    Drawable drawable1;
    Drawable drawable2;
    public RecycleBuyAdapter(Context context, ArrayList<GroupBuy> foodList) {
        this.context = context;
        this.foodList = foodList;
        drawable1 = context.getResources().getDrawable(R.drawable.delete);
        drawable1.setBounds(0, 0, 40, 40);
        drawable2 = context.getResources().getDrawable(R.drawable.revise);
        drawable2.setBounds(0, 0, 40, 40);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_name;
        TextView tv_price;
        TextView tv_disprice;
        TextView tv_re;
        TextView tv_delete;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_dishesM);
            tv_price = (TextView) view.findViewById(R.id.tv_dishesM_price);
            tv_disprice = (TextView) view.findViewById(R.id.tv_dishesM_discountPrice);
            tv_re = (TextView) view.findViewById(R.id.tv_dishesM_revise);
            tv_delete = (TextView) view.findViewById(R.id.tv_dishesM_delete);
            tv_name = (TextView) view.findViewById(R.id.tv_dishesM_name);

            tv_delete.setCompoundDrawables(drawable1, null, null, null);
            tv_re.setCompoundDrawables(drawable2, null, null, null);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dishesmanager_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        x.image().bind(holder.imageView, "http://118.89.31.123:80"+foodList.get(position).ImageUrl);
        holder.tv_name.setText(foodList.get(position).Title_CN);
        holder.tv_price.setText(foodList.get(position).Price);
        holder.tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tv_disprice.setText("€"+foodList.get(position).Discountprice);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
