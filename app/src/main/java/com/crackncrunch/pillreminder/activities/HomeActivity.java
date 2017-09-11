package com.crackncrunch.pillreminder.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crackncrunch.pillreminder.R;
import com.crackncrunch.pillreminder.adaptres.HomeAdapter;
import com.crackncrunch.pillreminder.data.db.PillDbContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 74;

    @BindView(R.id.home_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private HomeAdapter mAdapter;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mAdapter = new HomeAdapter(null);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TASK_LOADER_ID:
                return new AsyncTaskLoader<Cursor>(this) {
                    Cursor mPillData = null;

                    @Override
                    protected void onStartLoading() {
                        if (mPillData != null) {
                            deliverResult(mPillData);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public Cursor loadInBackground() {
                        try {
                            return getContentResolver().query(
                                    PillDbContract.CONTENT_URI,
                                    null, null, null, mSortOrder
                            );
                        } catch (Exception e) {
                            Log.e(TAG, getString(R.string.failed_to_load_data));
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(Cursor data) {
                        mPillData = data;
                        super.deliverResult(data);
                    }
                };
            default:
                throw new RuntimeException(
                        getString(R.string.loader_not_implemented) + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sortBy_key))) {
            String sort = sharedPreferences.getString(key, sharedPreferences
                    .getString(key, getResources().getString(R.string
                            .pref_sortBy_date)));
            if (sort.equals(getResources().getString(R.string.pref_sortBy_name)
            )) {
                mSortOrder = PillDbContract.DATE_SORT;
            } else {
                mSortOrder = PillDbContract.NAME_SORT;
            }
        }
    }
}
