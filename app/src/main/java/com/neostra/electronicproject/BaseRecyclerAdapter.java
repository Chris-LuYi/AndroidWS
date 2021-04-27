package com.neostra.electronicproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author njmsir
 * Created by Administrator on 2018/11/24.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> data;
    protected OnClickListener onClickListener;

    protected BaseRecyclerAdapter(List<T> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(i), viewGroup, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int i) {
        bindContent(holder, i);
    }

    protected abstract void bindContent(BaseViewHolder holder, int i);

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected abstract int getLayoutResId(int position);

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
