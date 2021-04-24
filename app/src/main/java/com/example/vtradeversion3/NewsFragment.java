package com.example.vtradeversion3;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class  NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private  NewsAdapter newsAdapter;
    private List<NewsData> newsDataList = new ArrayList<>();
    private ImageView imageView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        imageView = view.findViewById(R.id.imageView);
        progressBar =  (ProgressBar) view.findViewById(R.id.progressBar2);
        NewsDataPrepare();
        return view;
    }

    private void NewsDataPrepare(){
        String url ="https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=99aaec76a0f742d8914e42a84757ab54";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                newsAdapter = new NewsAdapter(newsDataList);
                RecyclerView.LayoutManager manager = new GridLayoutManager((getContext().getApplicationContext()), 1);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(newsAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("articles");
                    NewsData newsData = new NewsData(jsonArray.getJSONObject(0).get("title").toString(), jsonArray.getJSONObject(0).get("description").toString(), jsonArray.getJSONObject(0).get("url").toString(),
                            jsonArray.getJSONObject(0).get("urlToImage").toString(), imageView);
                    for (int i = 1, size = jsonArray.length(); i < size; i++) {
                        JSONObject objectInArray = jsonArray.getJSONObject(i);
                        newsData = new NewsData(objectInArray.get("title").toString(), objectInArray.get("description").toString(), objectInArray.get("url").toString(), objectInArray.get("urlToImage").toString(), imageView);
                        newsDataList.add(newsData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String, String> headers = new HashMap<String, String>();
               headers.put("User-Agent", "Mozzila/5.0");
               return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}