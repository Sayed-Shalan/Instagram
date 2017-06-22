package instagram.shalan.net.sayed.instagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import instagram.shalan.net.sayed.instagram.LoginActivity;
import instagram.shalan.net.sayed.instagram.PhotoDetailsActivity;
import instagram.shalan.net.sayed.instagram.R;
import instagram.shalan.net.sayed.instagram.adapters.RecyclerViewAdapter;
import instagram.shalan.net.sayed.instagram.adapters.RecyclerViewTouchHelper;
import instagram.shalan.net.sayed.instagram.model.FeedModel;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;
import instagram.shalan.net.sayed.instagram.volley.AppController;

public class GridViewFragment extends Fragment {
    private ArrayList<FeedModel> feedItems;
    RecyclerView recyclerView ;
    RecyclerViewAdapter adapter ;
    private String  FeedURL = "http://192.168.43.188/instagram/photo.php";
    private  View view ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if ( view == null){

            view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        }


        recyclerView =(RecyclerView)view.findViewById(R.id.recyclerView);
        feedItems = new ArrayList<>();
        getFeed();

        ///////////////////////////////////////////////////////////
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchHelper(getActivity(), recyclerView, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {
                FeedModel m= new FeedModel();
                m=adapter.getItem(postion);
                Intent i = new Intent(getActivity(), PhotoDetailsActivity.class);
                i.putExtra("post_id",m.getPost_id());
                i.putExtra("id",m.getId());
                i.putExtra("post_image",m.getPostImage());
                i.putExtra("post_text",m.getPost());
                i.putExtra("post_date",m.getDate());
                startActivity(i);

            }

            @Override
            public void onLongClick(View child, int postion) {

            }
        }));
        ///////////////////////////////////////////////////////
        return  view ;
    }

    private void getFeed() {
        StringRequest feedRequest = new StringRequest(Request.Method.POST, FeedURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJsonFeed(response);
                Log.e("volley","respnse"+response.toString() );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "error" + error.toString());
            }
        });

    AppController.getInstance().addToRequestQueue(feedRequest);
    }
    ///////////////////////////////////////////////////////////////////////////////
    private void parseJsonFeed(String response) {
        try {
            JSONObject resObj = new JSONObject(response);
            JSONArray feedArr = resObj.getJSONArray("user");
            for (int i=0 ; i<feedArr.length();i++){
                JSONObject currentElemnt =  feedArr.getJSONObject(i);
                if(currentElemnt.getInt("id")==new MysharedPreferences(getActivity()).getUser().getId()) {
                    FeedModel currentFeed = new FeedModel();
                    currentFeed.setPost_id(currentElemnt.getInt("post_id"));
                    currentFeed.setId(currentElemnt.getInt("id"));
                    currentFeed.setPost(currentElemnt.getString("post_text"));
                    currentFeed.setPostImage(currentElemnt.getString("post_image"));
                    currentFeed.setDate(currentElemnt.getString("date"));
                    feedItems.add(currentFeed);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {

            adapter = new RecyclerViewAdapter(getActivity(),feedItems );
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            adapter.notifyDataSetChanged();


        }

    }
}
