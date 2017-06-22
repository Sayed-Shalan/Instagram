package instagram.shalan.net.sayed.instagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import instagram.shalan.net.sayed.instagram.FriendActivity;
import instagram.shalan.net.sayed.instagram.PhotoDetailsActivity;
import instagram.shalan.net.sayed.instagram.R;
import instagram.shalan.net.sayed.instagram.adapters.HomeRecyclerViewAdapter;
import instagram.shalan.net.sayed.instagram.adapters.HomeSearchRecyclerView;
import instagram.shalan.net.sayed.instagram.adapters.RecyclerViewTouchHelper;
import instagram.shalan.net.sayed.instagram.model.FeedModel;
import instagram.shalan.net.sayed.instagram.model.User;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;
import instagram.shalan.net.sayed.instagram.volley.AppController;

public class SearchFragment extends Fragment {
    private ArrayList<User> feedItems;
    RecyclerView recyclerView ;
    HomeSearchRecyclerView adapter ;
    private String  FeedURL = "http://192.168.43.188/instagram/homeuser.php";
    private  View view ;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView =(RecyclerView)view.findViewById(R.id.recyclerView3);
        feedItems = new ArrayList<>();
        getFeed();
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchHelper(getActivity(), recyclerView, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {
                User user = new User();
                user=adapter.getItem(postion);
                Intent i = new Intent(getActivity(), FriendActivity.class);
                i.putExtra("id",user.getId());
                i.putExtra("username",user.getUsername());
                i.putExtra("email",user.getEmail());
                i.putExtra("pp",user.getPp());
                i.putExtra("status",user.getStatus());
                startActivity(i);
            }

            @Override
            public void onLongClick(View child, int postion) {

            }
        }));

        return view;
    }
///////////////////////////////////////////////////////////////////////
    public void getFeed()
    {
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
    ////////////////////////////////////////////////////
    private void parseJsonFeed(String response) {
        try {
            JSONObject resObj = new JSONObject(response);
            JSONArray feedArr = resObj.getJSONArray("user");
            for (int i=0 ; i<feedArr.length();i++){
                JSONObject currentElemnt =  feedArr.getJSONObject(i);
                if(currentElemnt.getInt("id")!=new MysharedPreferences(getActivity()).getUser().getId()) {
                    User currentFeed = new User();
                    currentFeed.setId(currentElemnt.getInt("id"));
                    currentFeed.setPp(currentElemnt.getString("profilepic"));
                    currentFeed.setStatus(currentElemnt.getString("status"));
                    currentFeed.setUsername(currentElemnt.getString("username"));
                    currentFeed.setPassword(currentElemnt.getString("userpassword"));
                    currentFeed.setEmail(currentElemnt.getString("useremail"));
                    feedItems.add(currentFeed);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {

            adapter = new HomeSearchRecyclerView(getActivity(),feedItems );
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.notifyDataSetChanged();
        }

    }
    }

