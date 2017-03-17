package com.wzj.work.salesmanapp.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzj.work.salesmanapp.OtherClass.Dishes;
import com.wzj.work.salesmanapp.OtherClass.MtoFood;
import com.wzj.work.salesmanapp.R;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/15.
 */
public class RecycleAddMtoAdapter extends RecyclerView.Adapter<RecycleAddMtoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Dishes> dishesList = new ArrayList<>();
    private String shopId;
    int[] picFood = {R.mipmap.food11, R.mipmap.food22, R.mipmap.food33, R.mipmap.food44, R.mipmap.food55,
            R.mipmap.food66, R.mipmap.food77, R.mipmap.food88, R.mipmap.food99};
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            } else if (msg.what == 100) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public RecycleAddMtoAdapter(Context context, ArrayList<Dishes> dishesList, String shopId) {
        this.context = context;
        this.dishesList = dishesList;
        this.shopId = shopId;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_mto;
        TextView tv_title;
        LinearLayout ll_array;
        TextView tv_discountPrice;
        TextView tv_price;
        TextView tv_ok;

        public ViewHolder(View view) {
            super(view);
            iv_mto = (ImageView) view.findViewById(R.id.iv_mto);
            tv_title = (TextView) view.findViewById(R.id.tv_mto_title);
            tv_discountPrice = (TextView) view.findViewById(R.id.tv_mto_discountprice);
            tv_price = (TextView) view.findViewById(R.id.tv_mto_price);
            ll_array = (LinearLayout) view.findViewById(R.id.ll_array);
            tv_ok = (TextView) view.findViewById(R.id.tv_ok);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mtakeout_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        x.image().bind(holder.iv_mto, context.getResources().getString(R.string.http_tu) + dishesList.get(position).ImageUrl);
        holder.tv_title.setText(dishesList.get(position).Title_CN);
        holder.tv_discountPrice.setText("€" + dishesList.get(position).Discountprice);
        holder.tv_price.setText(dishesList.get(position).Price);
        holder.tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_ok.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(-1, -1);
        llp.width = 70;
        llp.height = 70;
        String str = dishesList.get(position).IngredientsIconArray;
        String[] foodArray = null;
        if (!str.equals("null")) {
            foodArray = str.split(",");
            Log.e("zhang: ", str);
            holder.ll_array.removeAllViews();
            for (int i = 0; i < foodArray.length; i++) {
                if (!foodArray[i].equals("null")) {
                    int t = Integer.parseInt(foodArray[i]);
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(llp);
                    tv.setBackgroundResource(picFood[t]);
                    holder.ll_array.addView(tv);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }
}

