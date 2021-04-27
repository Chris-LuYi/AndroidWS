package com.neostra.electronicproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author njmsir
 * Created by Administrator on 2018/11/24.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        navigationClick(getNavigation());
        onActivityCreate();
    }

    private void navigationClick(@Nullable View navigation) {
        if (null != navigation) {
            navigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * Activity的创建生命周期
     */
    public abstract void onActivityCreate();

    /**
     * 获取布局资源id
     *
     * @return 布局id
     */
    public abstract int getLayoutId();

    /**
     * 获取头部返回按钮
     *
     * @return
     */
    protected abstract View getNavigation();

}
