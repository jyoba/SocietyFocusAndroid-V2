package com.zircon.app.utils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public abstract class AbsSearchListAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private ArrayList<T> displayItems = new ArrayList<>();
    protected ArrayList<T> masterItems = new ArrayList<>();

    protected String query;

    public final void filter(String query) {
        this.query = query;
        animateTo(getFilteredList(query));
    }

    protected abstract List<T> getFilteredList(String query);

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    protected T getItem(int position) {
        return displayItems.get(position);
    }

    public void animateTo(List<T> newList) {
        applyAndAnimateRemovals(newList);
        applyAndAnimateAdditions(newList);
        applyAndAnimateMovedItems(newList);
        for (int i = 0; i < displayItems.size(); i++) {
            notifyItemChanged(i);
        }
    }

    private void applyAndAnimateRemovals(List<T> newList) {
        for (int i = displayItems.size() - 1; i >= 0; i--) {
            final T t = displayItems.get(i);
            if (!newList.contains(t)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> newList) {
        for (int i = 0, count = newList.size(); i < count; i++) {
            final T t = newList.get(i);
            if (!displayItems.contains(t)) {
                addItem(i, t);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<T> newList) {
        for (int toPosition = newList.size() - 1; toPosition >= 0; toPosition--) {
            final T t = newList.get(toPosition);
            final int fromPosition = displayItems.indexOf(t);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private T removeItem(int position) {
        final T t = displayItems.remove(position);
        notifyItemRemoved(position);
        return t;
    }

    public void addAllItems(ArrayList<T> models) {
        int i = displayItems.size();
        masterItems.addAll(models);
        displayItems.addAll(models);
        notifyItemRangeInserted(i, models.size());
    }

    public void addItemAtTop(T model) {
        displayItems.add(0, model);
        masterItems.add(0, model);
        notifyItemInserted(0);
    }

    private void addItem(int position, T model) {
        displayItems.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final T t = displayItems.remove(fromPosition);
        displayItems.add(toPosition, t);
        notifyItemMoved(fromPosition, toPosition);
    }

}