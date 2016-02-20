package vail.codetest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import vail.codetest.R;

/**
 * Created by
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ItemVH> {

    protected final LayoutInflater inflater;
    private final OnClickListener onClickListener;
    private List<T> items = new ArrayList<>();

    public BaseAdapter(Context context, OnClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    public void addItems(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public BaseAdapter.ItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new BaseAdapter.ItemVH(view);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.ItemVH holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.title)
        TextView titleTv;
        @Bind(R.id.value)
        TextView valueTv;

        private int position;

        public ItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onItemClick(position);
            }
        }
    }

    public interface OnClickListener {
        void onItemClick(int position);
    }
}
