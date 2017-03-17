package com.wzj.work.salesmanapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzj.work.salesmanapp.Activity.AddDishesActivity;
import com.wzj.work.salesmanapp.OtherClass.Food;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/2.
 */
public class RecyclerDishesAdapter extends RecyclerView.Adapter<RecyclerDishesAdapter.ViewHolder> {
    Context context;
    ArrayList<Food> foodList = new ArrayList<>();
    Drawable drawable1;
    Drawable drawable2;

    public RecyclerDishesAdapter(Context context, ArrayList<Food> foodList) {
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String result=msg.obj.toString();
                if(funNet.isOk(result,context)){
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dishesmanager_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String imageUrl = foodList.get(position).ImageUrl;
        if (imageUrl != null) {
            imageUrl = (imageUrl.split(","))[0];
        }
        x.image().bind(holder.imageView, context.getResources().getString(R.string.http_tu) + imageUrl);
        holder.tv_name.setText(foodList.get(position).Title_CN);
        holder.tv_price.setText(foodList.get(position).Price);
        holder.tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_disprice.setText("€" + foodList.get(position).Discountprice);
        holder.tv_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("food", foodList.get(position));
                Intent intent = new Intent(context, AddDishesActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("state", "1");
                context.startActivity(intent);
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);  //先得到构造器
                builder.setMessage("是否删除该菜品?"); //设置内容
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams params = new RequestParams(context.getResources().getString(R.string.http_msg) + "/Food/DeleteFood");
                        params.addQueryStringParameter("ShopId", foodList.get(position).ShopId);
                        params.addQueryStringParameter("FoodId", foodList.get(position).Id);
                        funNet.toolNet(params, handler, 1);
                        foodList.remove(position);
                        notifyItemRemoved(position);
                        dialog.dismiss(); //关闭dialog
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}

