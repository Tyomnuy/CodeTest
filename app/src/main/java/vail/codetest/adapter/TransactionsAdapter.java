package vail.codetest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import vail.codetest.R;
import vail.codetest.data.model.Transaction;

/**
 * Created by
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public class TransactionsAdapter extends BaseAdapter<Transaction> {

    private final String gbp;

    public TransactionsAdapter(Context context, OnClickListener onClickListener) {
        super(context, onClickListener);

        this.gbp = context.getString(R.string.gbp);
    }

    @Override
    public TransactionVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new TransactionVH(view);
    }

    class TransactionVH extends ItemVH {

        public TransactionVH(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position) {
            super.bind(position);

            Transaction item = getItem(position);

            //TODO
            String currency = item.getCurrency() + " " + item.getAmount();
            String currencyGBP = gbp + " " + item.getAmmountGBP();
            titleTv.setText(currency);
            valueTv.setText(currencyGBP);
        }
    }
}
