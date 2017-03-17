package com.wzj.work.salesmanapp.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzj.work.salesmanapp.OtherClass.CashCoupon;
import com.wzj.work.salesmanapp.OtherClass.GroupBuy;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/13.
 */
public class RecycleCashAdapter extends RecyclerView.Adapter<RecycleCashAdapter.ViewHolder> {
    private Context context;
    private ImageOptions options;
    private ArrayList<CashCoupon> cashList = new ArrayList<>();
    private String shopId;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                String str=msg.obj.toString();
                if(funNet.isOk(str,context)){
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what==100){
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
            }
        }
    };
    public RecycleCashAdapter(Context context, ArrayList<CashCoupon> cashList,String shopId) {
        this.context = context;
        this.cashList = cashList;
        this.shopId=shopId;
        options = new ImageOptions.Builder().setFadeIn(true).setCircular(true).build();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cash;
        TextView tv_title;
        TextView tv_content;
        TextView tv_use;
        Button btn_delete;
        public ViewHolder(View view) {
            super(view);
            iv_cash= (ImageView) view.findViewById(R.id.iv_cash_item);
            tv_title= (TextView) view.findViewById(R.id.tv_cash_title);
            tv_content= (TextView) view.findViewById(R.id.tv_cash_content);
            tv_use= (TextView) view.findViewById(R.id.tv_cash_use);
            btn_delete= (Button) view.findViewById(R.id.btn_cash_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int t=getLayoutPosition();
                    RequestParams params = new RequestParams(context.getResources().getString(R.string.http_msg)+"/Food/DelCoupon");
                    params.addQueryStringParameter("ShopId",shopId );
                    params.addQueryStringParameter("CouponId",cashList.get(t).Id);
                    funNet.toolNet(params, handler, 1);
                    notifyItemRemoved(t);
                    cashList.remove(t);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cash_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        x.image().bind(holder.iv_cash, context.getResources().getString(R.string.http_tu)+cashList.get(position).ImageUrl,options);
        holder.tv_title.setText(cashList.get(position).Title_CN);
        holder.tv_content.setText(cashList.get(position).Discountprice+"代"+cashList.get(position).Price+"元代金券");
        holder.tv_use.setText("满"+cashList.get(position).Price+"元可用;全场通用,不限品类");
    }

    @Override
    public int getItemCount() {
        return cashList.size();
    }
}

