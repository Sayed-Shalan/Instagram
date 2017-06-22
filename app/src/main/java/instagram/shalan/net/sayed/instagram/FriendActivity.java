package instagram.shalan.net.sayed.instagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import instagram.shalan.net.sayed.instagram.adapters.FriendAdpter;
import instagram.shalan.net.sayed.instagram.adapters.RecyclerViewAdapter;
import instagram.shalan.net.sayed.instagram.model.FeedModel;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;
import instagram.shalan.net.sayed.instagram.volley.AppController;

public class FriendActivity extends AppCompatActivity {
ImageView imgpp;
    TextView state;
    TextView userName;
    Bundle bundle;
    private ArrayList<FeedModel> feedItems;
    RecyclerView recyclerView ;
    FriendAdpter adapter ;
    private String  FeedURL = "http://192.168.43.188/instagram/photo.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        //////////////////////////////////////////////////
        imgpp=(ImageView)findViewById(R.id.FriendPp);
        state=(TextView)findViewById(R.id.Friendstate);
        userName=(TextView)findViewById(R.id.Friendname);
        bundle=getIntent().getExtras();
        state.setText(bundle.getString("status"));
        userName.setText(bundle.getString("username"));
        Glide.with(getApplicationContext())
                .load("http://192.168.43.188/instagram/instaimage/"+bundle.getString("pp"))
                .fitCenter()
                .into(imgpp);
        ///////////////////////////////////////////////////////////

        recyclerView =(RecyclerView)findViewById(R.id.recyclerView4);
        feedItems = new ArrayList<>();
        getFeed();
    }
    /////////////////////////////////////////////////////////////////
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
                if(currentElemnt.getInt("id")==bundle.getInt("id")) {
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
           // Toast.makeText(getApplicationContext(),feedItems.get(0).getPostImage(),Toast.LENGTH_LONG).show();
            adapter = new FriendAdpter(this,feedItems );
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
//            adapter.notifyDataSetChanged();


        }

    }
}
