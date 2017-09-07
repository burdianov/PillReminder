package com.crackncrunch.pillreminder.adaptres;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crackncrunch.pillreminder.R;
import com.crackncrunch.pillreminder.data.dto.Pill;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public HomeAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout
                .item_pill_details, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.HomeViewHolder holder, int position) {
        Pill pill = getItem(position);

        holder.name.setText(pill.getName());
        float floatQty = pill.getQty();
        float qty = floatQty - (int) floatQty;
        String stringQty = String.valueOf(floatQty);
        if (qty == 0) {
            stringQty = stringQty.substring(0, 1);
        }
        holder.qty.setText(stringQty);
        CharSequence timeSpan = DateUtils.getRelativeTimeSpanString(
                mContext, pill.getDate());
        holder.date.setText(timeSpan);
    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    public Pill getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }
        return new Pill(mCursor);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_text_view)
        TextView name;
        @BindView(R.id.qty_text_view)
        TextView qty;
        @BindView(R.id.date_text_view)
        TextView date;

        public HomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
