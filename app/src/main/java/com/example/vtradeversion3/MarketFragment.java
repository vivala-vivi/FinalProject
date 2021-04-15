package com.example.vtradeversion3;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class MarketFragment extends Fragment {

    private Context mContext;
    private ListView listView;
    private LinkedList<Stock> searchList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_market, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        searchList = new LinkedList<Stock>();

        listView = (ListView) view.findViewById(R.id.mListView);

        String URL = "https://finnhub.io/api/v1/stock/symbol?exchange=US&token=" + getString(R.string.APIKEY);
        fRequest(URL);
    }

    private void fRequest(String url){
        RequestQueue mQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                (response) -> {

                    setStockData(response);

                    displaySearchResponse();
                }, (error) -> {
         });
        mQueue.add(jsonObjectRequest);
    }


    private void setStockData(JSONArray response){
        try{
            for(int i = 0; i<10;i++){
                JSONObject mStockResponse = response.getJSONObject(i);

                Stock mStock = new Stock();

                mStock.setSymbol(mStockResponse.getString("symbol"));
                mStock.setLongName(mStockResponse.getString("description"));

                searchList.add(mStock);
            }
        } catch (JSONException e){
            //
        }
    }

    private void displaySearchResponse(){
        SearchAdapter mSearchAdapter = new SearchAdapter(mContext, searchList);
        listView.setAdapter((ListAdapter) mSearchAdapter);
    }

}

