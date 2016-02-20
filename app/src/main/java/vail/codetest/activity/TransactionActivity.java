package vail.codetest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import vail.codetest.R;
import vail.codetest.adapter.TransactionsAdapter;
import vail.codetest.data.DataHelper;
import vail.codetest.data.model.Product;

/**
 * Created by
 *
 * @author Evgen Marinin <ievgen.marinin@alterplay.com>
 * @since 20.02.16.
 */
public class TransactionActivity extends AppCompatActivity {

    private static final String PRODUCT_ID = "product_id";

    @Bind(R.id.total)
    TextView totalTv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private TransactionsAdapter adapter;
    private LoadDataTask loadTask;
    private String productId;
    private Product productItem;

    public static void start(Context context, Product product) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra(PRODUCT_ID, product.getId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        ButterKnife.bind(this);

        setActionBar();
        init();

        loadData();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        adapter = new TransactionsAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        productId = getIntent().getStringExtra(PRODUCT_ID);

        String labelString = getString(R.string.transition_label);
        setTitle(labelString + productId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        loadTask = new LoadDataTask();
        loadTask.execute();
    }

    private class LoadDataTask extends AsyncTask<Object, Object, Product> {

        @Override
        protected Product doInBackground(Object[] params) {
            return DataHelper.getInstance().loadProduct(productId);
        }

        @Override
        protected void onPostExecute(Product product) {
            productItem = product;
            adapter.addItems(productItem.getTransactions());
            //TODO
            String totalString = getString(R.string.total) + product.getTotalAmountGBP();
            totalTv.setText(totalString);
            progressBar.setVisibility(View.GONE);
        }
    }
}
