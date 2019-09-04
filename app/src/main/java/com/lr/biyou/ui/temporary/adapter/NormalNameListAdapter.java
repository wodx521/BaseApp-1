package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnSearchItemClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NormalNameListAdapter extends ListBaseAdapter implements Filterable {
    private List<Map<String, Object>> mSourceList = new ArrayList<>();

    public List<Map<String, Object>> getSourceList() {
        return mSourceList;
    }

    public void setSourceList(List<Map<String, Object>> sourceList) {
        mSourceList = sourceList;
    }
    private LayoutInflater mLayoutInflater;

    public OnSearchItemClickListener getOnSearchItemClickListener() {
        return mOnSearchItemClickListener;
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener onSearchItemClickListener) {
        mOnSearchItemClickListener = onSearchItemClickListener;
    }

    private OnSearchItemClickListener mOnSearchItemClickListener;

    public NormalNameListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mDataList = mSourceList;
                } else {
                    List<Map<String, Object>> filteredList = new ArrayList<>();
                    for (Map<String, Object> map : mSourceList) {
                        String str1 = map.get("name") + "";
                        //这里根据需求，添加匹配规则
                        if (str1.contains(charString) ) {
                            filteredList.add(map);
                        }
                    }

                    mDataList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataList;
                return filterResults;
            }

            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataList = (List<Map<String, Object>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_normal_code, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        String name = item.get("name")+"";
        viewHolder.mNameValueTv.setText(name);
        viewHolder.mContentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSearchItemClickListener != null){
                    mOnSearchItemClickListener.OnSearchItemClickListener(item);
                }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_value_tv)
        TextView mNameValueTv;
        @BindView(R.id.content_lay)
        LinearLayout mContentLay;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
