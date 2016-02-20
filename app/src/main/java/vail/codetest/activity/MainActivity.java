package vail.codetest.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import vail.codetest.R;
import vail.codetest.adapter.BaseAdapter;
import vail.codetest.adapter.ProductsAdapter;
import vail.codetest.data.DataHelper;
import vail.codetest.data.model.Product;
import vail.codetest.data.model.Rate;
import vail.codetest.data.model.Transaction;
import vail.codetest.utils.Utils;

public class MainActivity extends AppCompatActivity implements BaseAdapter.OnClickListener {

    private static final String RATE_ASSET = "rates.json";
    private static final String TRANSACTION_ASSET = "transactions.json";

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductsAdapter adapter;
    private LoadDataTask loadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();

        loadData();
    }

    private void init() {
        adapter = new ProductsAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        loadTask = new LoadDataTask();
        loadTask.execute();
    }

    @Override
    public void onItemClick(int position) {
        TransactionActivity.start(this, adapter.getItem(position));
    }

    private class LoadDataTask extends AsyncTask<Object, Object, List<Product>> {

        @Override
        protected List<Product> doInBackground(Object[] params) {
            DataHelper dataHelper = DataHelper.getInstance();
            loadRates(dataHelper);
            loadTransactions(dataHelper);

            List<Product> productList = new ArrayList<>(dataHelper.loadProducts());
            Collections.sort(productList, new Comparator<Product>() {
                @Override
                public int compare(Product lhs, Product rhs) {
                    if (lhs.getId() == null) return -1;
                    if (rhs.getId() == null) return 1;
                    return lhs.getId().compareTo(rhs.getId());
                }
            });

            return productList;
        }

        private void loadRates(DataHelper dataHelper) {
            String rateString = Utils.loadStringFromAsset(MainActivity.this, RATE_ASSET);
            List<Rate> rateList = new Gson().fromJson(rateString, new TypeToken<List<Rate>>(){}.getType());
            dataHelper.setRates(rateList);
        }

        private void loadTransactions(DataHelper dataHelper) {
            String rateString = Utils.loadStringFromAsset(MainActivity.this, TRANSACTION_ASSET);
            List<Transaction> transactions = new Gson().fromJson(rateString, new TypeToken<List<Transaction>>(){}.getType());
            dataHelper.setTransactions(transactions);
        }

        @Override
        protected void onPostExecute(List<Product> productList) {
            adapter.addItems(productList);
            progressBar.setVisibility(View.GONE);
        }
    }
}
