package search.android.aboutall.eu.search;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class SearchArrayAdapter extends RecyclerView.Adapter<SearchArrayAdapter.ViewHolder> {
    private List<String> mDataset;
    private String mPrefix;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
        }
    }

    public SearchArrayAdapter(List<String> data) {
        mDataset = data;
    }

    @Override
    public SearchArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ViewHolder(v);

    }

    public void addAll(List<String> data, String prefix) {
        mPrefix = prefix;
        mDataset.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        mDataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // highlight word's prefix
        String s = mDataset.get(position);
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(s);

        int len =  mPrefix.length();
        // In case of non-alphabetic sign (e.g. "bloke's"), extend selection to one character (assuming we have only one character long non alphabetics symbols)
        if ( !Utils.isAlphaBheticString(s.substring(0,len))  && s.length() >= len + 1){
            len+=1;
        }

        spanText.setSpan(new BackgroundColorSpan(0xFFFFFF00), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.mTextView.setText(spanText);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
