package com.example.vtradeversion3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MarketFragment extends Fragment {

    private Context mContext;
    private ListView listView;
    private Button button;
    private LinkedList<Stock> searchList;
    public AutoCompleteTextView autoComplete;
    public List<String> suggest;
    public ArrayAdapter<String> aAdapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_market, container, false);
        progressBar =  (ProgressBar) view.findViewById(R.id.progressBar3);

        //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggest);
        // autoComplete.setAdapter(adapter);
        return view;

        //return inflater.inflate(R.layout.fragment_market, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        searchList = new LinkedList<Stock>();
        listView = (ListView) view.findViewById(R.id.mListView);
        button= (Button) view.findViewById(R.id.quote);

        Button button = (Button) view.findViewById(R.id.quote);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String message = autoComplete.getText().toString();
                message = message.trim();
                if(!message.equals("")) {
                    Intent intent = new Intent(MarketFragment.this.getActivity(), SendString.class);
                    intent.putExtra("data", message);
                    // intent.putExtra("favorite", favList.contains(message));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MarketFragment.this.getActivity(), "Please enter a Stock name or a symbol", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String URL = "https://finnhub.io/api/v1/stock/symbol?exchange=US&token=" + getString(R.string.APIKEY);
        fRequest(URL);


        autoComplete= (AutoCompleteTextView) view.findViewById(R.id.search);
        // aAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggest);
        autoComplete.setAdapter(aAdapter);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                selection  = selection.split("\n")[0];
                autoComplete.setText(selection);
            }
        });

        autoComplete.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("auto","auto complete called");
                String newText = s.toString();
                new getJsonAutoComplete().executethis(newText);
            }
        });
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
        progressBar.setVisibility(View.INVISIBLE);
        try{
            for(int i = 0; i<10;i++){
                JSONObject mStockResponse = response.getJSONObject(i);

                Stock mStock = new Stock();

                mStock.setSymbol(mStockResponse.getString("symbol"));
                mStock.setLongName(mStockResponse.getString("description"));


                // mStock.setCurrentPrice(mStockResponse.getDouble("c"));

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



/////////////////////////////////////////////////////////////////////////////////////

    public void sendMessage(View view){
        String message = autoComplete.getText().toString();
        message = message.trim();
        if(!message.equals("")) {
            Intent intent = new Intent(MarketFragment.this.getActivity(), SendString.class);
            intent.putExtra("data", message);
            // intent.putExtra("favorite", favList.contains(message));
            startActivity(intent);
        }
        else{
            Toast.makeText(getContext(), "Enter a Stock name or a symbol", Toast.LENGTH_SHORT).show();
        }
    }

    class getJsonAutoComplete {

        public void execute(String newText) {

            String JsonURL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + newText + "&apikey=8NEHIYGHRJY1RB3J";
            Log.d("auto", "input " + JsonURL);
            RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JsonURL, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("auto", response.toString());
                        JSONObject data = response;
                        JSONArray jArray = new JSONArray(data);

                        // Display the first 500 characters of the response string.
                        suggest = new ArrayList<String>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jsonobject = jArray.getJSONObject(i);

                            suggest.add(i, Html.fromHtml("<b>" + jsonobject.getString("1. symbol") + "</b>") + "\n" + jsonobject.getString("2. name"));  // + " (" + jsonobject.getString("Exchange") + ")");
                        }

                        aAdapter = new ArrayAdapter<String>(mContext.getApplicationContext(), android.R.layout.simple_list_item_1, suggest);
                        autoComplete.setAdapter(aAdapter);
                        aAdapter.notifyDataSetChanged();

                        Log.d("Reply", "Getting respone");
                        Log.d("TAG", "hello");
                    } catch (JSONException e) {
                        Log.d("auto", "NOT GETTING RESPONSE!! hmmm");
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Error11" + error.toString());
                        }
                    }
            );
            queue.add(stringRequest);
        }

        public void executethis(String newText) {
            String JsonURL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + newText + "&apikey=8NEHIYGHRJY1RB3J";
            Log.d("auto", "input " + JsonURL);
            RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());


            StringRequest req = new StringRequest(Request.Method.GET, JsonURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                String data = response;
                                Log.d("auto", "data" + data);
                                //processData(response);
                                //  JSONArray jArray = new JSONArray(data);

                                JSONObject jsonObject = new JSONObject(data);
                                JSONArray jArray = jsonObject.getJSONArray("bestMatches");
                                // Display tthe first 500 characters of the response string.
                                suggest = new ArrayList<String>();
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject jsonobject = jArray.getJSONObject(i);

                                    suggest.add(i, Html.fromHtml("<b>" + jsonobject.getString("1. symbol") + "</b>") + "\n" + jsonobject.getString("2. name"));// + " (" + jsonobject.getString("Exchange") + ")");
                                }

                                aAdapter = new ArrayAdapter<String>(mContext.getApplicationContext(), R.layout.autocomplete, suggest);
                                autoComplete.setAdapter(aAdapter);

                                aAdapter.notifyDataSetChanged();
                                Log.d("Reply", "Getting response");

                            } catch (JSONException e) {
                                Log.d("Reply", "Mmmm no response");
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // handle error response
                        }
                    }
            );
            queue.add(req);

        }

    }



}