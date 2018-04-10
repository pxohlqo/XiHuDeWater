package com.cracky_axe.pxohlqo.xihudewater.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cracky_axe.pxohlqo.xihudewater.App;
import com.cracky_axe.pxohlqo.xihudewater.R;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidResultBeanObjectBox;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class MainActivity extends AppCompatActivity implements HomeAdapter.HomeAdapterOnItemClickHandler {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private HomePresenter mPresenter;

    static BoxStore boxStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boxStore = ((App) getApplication()).getBoxStore();
        Box<GankAndroidResultBeanObjectBox> box = boxStore.boxFor(GankAndroidResultBeanObjectBox.class);
        mAdapter = new HomeAdapter(box, this, this);
        mPresenter = new HomePresenter(boxStore, mAdapter, this);

        recyclerView = findViewById(R.id.rv_list);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getAdapter());

        RefreshLayout refreshLayout = findViewById(R.id.home_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.clearObjectBox();
                mPresenter.requestDataFromApi();

                refreshLayout.finishRefresh(2000, true);
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPresenter.requestMoreDataFromApi();

                refreshLayout.finishLoadMore(2000, true, false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.requestDataFromApi();

                refreshLayout.finishRefresh(2000, true);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_loadFromDb:
                Log.d(TAG, "onOptionsItemSelected: load from db");
                mPresenter.getAllEntitiesFromObjectBox();
                Toast toast = Toast.makeText(this, String.valueOf(mPresenter.getmBox().count()), Toast.LENGTH_SHORT);
                toast.show();
                break;

            case R.id.action_requestFromApi:
                Log.d(TAG, "onOptionsItemSelected: request from api");
                mPresenter.requestDataFromApi();
                break;

            case R.id.action_clearDb:
                mPresenter.clearObjectBox();
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.action_requestMore:
                mPresenter.requestMoreDataFromApi();
                mAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: click");
    }
}
