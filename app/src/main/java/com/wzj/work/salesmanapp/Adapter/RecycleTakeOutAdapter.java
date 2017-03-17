package com.wzj.work.salesmanapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.wzj.work.salesmanapp.Activity.ManageTakeOutActivity;
import com.wzj.work.salesmanapp.OtherClass.TakeOutList;
import com.wzj.work.salesmanapp.R;
import com.wzj.work.salesmanapp.Tools.funNet;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 44967 on 2017/3/8.
 */
public class RecycleTakeOutAdapter extends RecyclerView.Adapter<RecycleTakeOutAdapter.ViewHolder> {
    private ArrayList<TakeOutList> takeOutList;
    private Context context;

    public RecycleTakeOutAdapter(Context context, ArrayList<TakeOutList> takeOutList) {
        this.context = context;
        this.takeOutList = takeOutList;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String str=msg.obj.toString();
                if(funNet.isOk(str,context)){
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_take_out_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.type.setText(takeOutList.get(position).Title);
        ImageView[] imageViews = {holder.ivType1, holder.ivType2, holder.ivType3, holder.ivType4};
        for (int i = 0; i < takeOutList.get(position).foods.size(); i++) {
            x.image().bind(imageViews[i], takeOutList.get(position).foods.get(i));
        }

    }

    @Override
    public int getItemCount() {
        return takeOutList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView tvManege;
        TextView tvDelete;
        ImageView ivType1;
        ImageView ivType2;
        ImageView ivType3;
        ImageView ivType4;

        public ViewHolder(View view) {
            super(view);
            type = (TextView) view.findViewById(R.id.tv_takeOut_type);
            tvManege = (TextView) view.findViewById(R.id.tv_takeOut_manege);
            tvDelete = (TextView) view.findViewById(R.id.tv_takeOut_delete);
            ivType1 = (ImageView) view.findViewById(R.id.iv_type1);
            ivType2 = (ImageView) view.findViewById(R.id.iv_type2);
            ivType3 = (ImageView) view.findViewById(R.id.iv_type3);
            ivType4 = (ImageView) view.findViewById(R.id.iv_type4);
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);  //先得到构造器
                    builder.setMessage("是否删除该分类?"); //设置内容
                    builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int t=getLayoutPosition();
                            RequestParams params = new RequestParams(context.getResources().getString(R.string.http_msg) + "/Food/DelTakeWay");
                            params.addQueryStringParameter("ShopId", takeOutList.get(t).shopId);
                            params.addQueryStringParameter("takeWayId", takeOutList.get(t).takeWayId);
                            funNet.toolNet(params, handler, 1);
                            takeOutList.remove(t);
                            notifyItemRemoved(t);
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
            tvManege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int t=getLayoutPosition();
                    Intent intent=new Intent(context, ManageTakeOutActivity.class);
                    intent.putExtra("shopId",takeOutList.get(t).shopId);
                    intent.putExtra("takeWayId",takeOutList.get(t).takeWayId);
                    intent.putExtra("type",takeOutList.get(t).Title);
                    context.startActivity(intent);
                }
            });
        }
    }
}
