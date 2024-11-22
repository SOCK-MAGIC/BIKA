package com.shizq.bika.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;

/**
 * https://github.com/youlookwhat/ByRecyclerView
 */
public abstract class BaseBindingHolder<T, B extends ViewBinding> extends BaseByViewHolder<T> {

    public B binding;

    public BaseBindingHolder(ViewGroup viewGroup, int layoutId) {
        super(viewGroup, layoutId);
    }

    @Override
    protected void onBaseBindView(BaseByViewHolder<T> holder, T bean, int position) {

    }

    @Override
    protected void onBaseBindViewPayloads(BaseByViewHolder<T> holder, T bean, int position, @NonNull List<Object> payloads) {
        onBindingViewPayloads(this, bean, position, payloads);
    }

    protected abstract void onBindingView(BaseBindingHolder holder, T bean, int position);

    protected void onBindingViewPayloads(BaseBindingHolder holder, T bean, int position, @NonNull List<Object> payloads) {
        /*
         * fallback to onBindingViewPayloads(holder, bean,position) if app does not override this method.
         * 如果不覆盖 bindViewPayloads() 方法，就走 onBindingView()
         */
        onBindingView(holder, bean, position);
    }
}
