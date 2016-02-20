package vail.codetest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import vail.codetest.R;
import vail.codetest.data.model.Product;

/**
 * Created by
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public class ProductsAdapter extends BaseAdapter<Product> {

    private final String singleTransaction;
    private final String transactions;

    public ProductsAdapter(Context context, OnClickListener onClickListener) {
        super(context, onClickListener);

        this.singleTransaction = context.getString(R.string.single_transaction);
        this.transactions = context.getString(R.string.transactions);
    }

    @Override
    public ProductVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ProductVH(view);
    }

    class ProductVH extends BaseAdapter.ItemVH {

        public ProductVH(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position) {
            super.bind(position);

            Product item = getItem(position);
            int transactionCount = item.getTransactions().size();
            String transactionString = (transactionCount == 1) ? singleTransaction : transactions;
            String valueString = item.getTransactions().size() + transactionString;

            titleTv.setText(item.getId());
            valueTv.setText(valueString);
        }
    }
}
