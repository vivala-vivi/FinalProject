package com.example.vtradeversion3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class SearchAdapter extends BaseAdapter {

    Context context;
    private LinkedList<Stock> mList;
    LayoutInflater inflater;

    public SearchAdapter(Context appContext, LinkedList<Stock> list) {

        this.context = appContext;
        this.mList = list;
        inflater = (LayoutInflater.from(appContext));
    }

    @Override
    public int getCount(){
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.list_item, null);

        TextView symbolTextView = (TextView) view.findViewById(R.id.symbolTextView);
        TextView longNameTextView = (TextView) view.findViewById(R.id.longNameTextView);

        symbolTextView.setText(mList.get(i).getSymbol());
        longNameTextView.setText(mList.get(i).getLongName());

        return view;
    }


}
