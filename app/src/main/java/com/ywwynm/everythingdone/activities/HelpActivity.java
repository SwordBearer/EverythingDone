package com.ywwynm.everythingdone.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywwynm.everythingdone.R;
import com.ywwynm.everythingdone.adapters.BaseViewHolder;
import com.ywwynm.everythingdone.fragments.HelpDetailFragment;
import com.ywwynm.everythingdone.helpers.SendInfoHelper;
import com.ywwynm.everythingdone.utils.DisplayUtil;
import com.ywwynm.everythingdone.utils.EdgeEffectUtil;

public class HelpActivity extends EverythingDoneBaseActivity {

    private String[] mTitles;
    private String[] mContents;

    private HelpDetailFragment mHelpDetailFragment;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_help;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.act_feedback) {
            SendInfoHelper.sendFeedback(this, false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initMembers() {
        mTitles   = getResources().getStringArray(R.array.help_titles);
        mContents = getResources().getStringArray(R.array.help_contents);
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initUI() {
        DisplayUtil.expandStatusBarAboveKitkat(f(R.id.view_status_bar));
        DisplayUtil.darkStatusBarForMIUI(this);

        final RecyclerView rv = f(R.id.rv_help);
        rv.setAdapter(new HelpAdapter());
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final int color = ContextCompat.getColor(HelpActivity.this, R.color.blue_deep);
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                EdgeEffectUtil.forRecyclerView(rv, color);
            }
        });
    }

    @Override
    protected void setActionbar() {
        final Toolbar toolbar = f(R.id.actionbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHelpDetailFragment != null && mHelpDetailFragment.isVisible()) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void setEvents() {

    }

    public void updateActionBarTitle(boolean toDetail) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(toDetail ? R.string.help_detail : R.string.help);
        }
    }

    class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelperHolder> {

        private LayoutInflater mInflater = LayoutInflater.from(HelpActivity.this);

        @Override
        public HelperHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HelperHolder(mInflater.inflate(R.layout.rv_help, parent, false));
        }

        @Override
        public void onBindViewHolder(HelperHolder holder, int position) {
            holder.tv.setText(mTitles[position]);
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }

        class HelperHolder extends BaseViewHolder {

            TextView tv;

            public HelperHolder(View itemView) {
                super(itemView);

                tv = f(R.id.tv_help_rv);

                f(R.id.ll_help_rv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int pos = getAdapterPosition();
                        mHelpDetailFragment = HelpDetailFragment.newInstance(
                                mTitles, mContents, pos);
                        final String tag = HelpDetailFragment.TAG;
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.fl_fragment_container_help,
                                        mHelpDetailFragment, tag)
                                .addToBackStack(tag)
                                .commit();
                    }
                });
            }
        }

    }

}