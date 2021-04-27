package com.neostra.electronicproject;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author njmsir
 * Created by njmsir on 2019/3/8.
 */
public class ElectronicMenuAdapter extends BaseRecyclerAdapter<MenuBean> {
    private int parentWidth;

    public ElectronicMenuAdapter(List<MenuBean> data, int parentWidth) {
        super(data);
        this.parentWidth = parentWidth;
    }

    @Override
    protected void bindContent(BaseViewHolder holder, final int i) {
        final View itemView = holder.itemView.getRootView();
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.height = parentWidth >> 1;

        MenuBean menuBean = data.get(i);
        ImageView icon = (ImageView) itemView.findViewById(R.id.iv_ic);
        TextView price = (TextView) itemView.findViewById(R.id.tv_menu_name);
        icon.setImageResource(menuBean.getResId());
        price.setText(String.format(itemView.getContext().getString(R.string.price),
                String.valueOf(menuBean.getPrice())));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v, i, null);
                }
            }
        });
    }

    @Override
    protected int getLayoutResId(int position) {
        return R.layout.item_ele_menu;
    }
}
