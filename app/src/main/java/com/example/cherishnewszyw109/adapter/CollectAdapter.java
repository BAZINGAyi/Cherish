package com.example.cherishnewszyw109.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cherishnewszyw109.R;

import java.util.List;
import java.util.Map;

/**
 * Created by yuwei on 2016/12/19.
 */
public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.MyViewHolder> {
    Context context;
    private List<Map> datas;
    public LayoutInflater mInflater;
    public OnViewClickListener onViewClickListener;//item子view点击事件
    public OnItemClickListener onItemClickListener;//item点击事件
    public CollectAdapter(Context context ,  List<Map> datas,OnViewClickListener onViewClickListener){
        this.context = context;
        this.datas = datas;
        this.mInflater = LayoutInflater.from(context);
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public CollectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         return new MyViewHolder(mInflater.inflate(R.layout.fg_collect_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mTv_name.setText(datas.get(position).get("title").toString());
        holder.mTv_sign.setText(datas.get(position).get("time").toString());
        String pic = datas.get(position).get("pic").toString();
        Glide.with(context)
                .load(pic)
                .placeholder(R.mipmap.news_jiazai)
                .crossFade(1000)
                .centerCrop()
                .into(holder.mImg_face);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onViewClickListener != null) {
                   int position1 = holder.getLayoutPosition(); //这里是为了插入Item时获取position位置不正确的，所以从holder获取
                       onViewClickListener.onViewClick(position1,0);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTv_name;
        private ImageView mImg_face;
        private TextView mTv_sign;
        private Button deleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_name = (TextView)itemView.findViewById(R.id.person_name);
            mImg_face = (ImageView)itemView.findViewById(R.id.person_face);
            mTv_sign = (TextView)itemView.findViewById(R.id.person_sign);
            deleteButton = (Button)itemView.findViewById(R.id.delete_collect);
        }
    }


    public interface OnViewClickListener {
        void onViewClick(int position, int viewtype);
    }

    public void delete(int pos){
        datas.remove(pos);
        notifyItemRemoved(pos);
    }

    public String getid(int position){
        return datas.get(position).get("id").toString();
    }
    public Map getDetailinfo(int position){
        return datas.get(position);
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
