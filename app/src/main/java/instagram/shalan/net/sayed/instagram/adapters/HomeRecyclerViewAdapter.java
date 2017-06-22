package instagram.shalan.net.sayed.instagram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import instagram.shalan.net.sayed.instagram.R;
import instagram.shalan.net.sayed.instagram.model.FeedModel;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;
import instagram.shalan.net.sayed.instagram.volley.AppController;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder>{
    String posetuserPp;
    String postUserName;
    private Context context ;
    private ArrayList<FeedModel> data ;
    private LayoutInflater layoutInflater ;
    private String feedImageURl ="http://192.168.43.188/instagram/instaimage/";

    public HomeRecyclerViewAdapter(Context context, ArrayList<FeedModel> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.home_single_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FeedModel currentElement =  data.get(position);
        getUserInfo(currentElement.getId(),holder);
        Glide.with(context)
                .load(feedImageURl +currentElement.getPostImage())
                .fitCenter()
                .into(holder.postImg);
        holder.postDate.setText(currentElement.getDate());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public FeedModel getItem(int postion){
        return  data.get(postion);
    }
    public void refresh(ArrayList<FeedModel> data) {
        this.data = data ;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView postImg;
        ImageView ppImg;
        TextView personName;
        TextView postDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            postImg =(ImageView) itemView.findViewById(R.id.homeImg);
            ppImg =(ImageView) itemView.findViewById(R.id.homepp);
            personName=(TextView) itemView.findViewById(R.id.homePersonName);
            postDate =(TextView) itemView.findViewById(R.id.homePostDate);
        }
    }
public void getUserInfo(final int userpostId, final MyViewHolder holder)
{

    String url= "http://192.168.43.188/instagram/homeuser.php";
    StringRequest loginRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Log.e("Volley","Response : "+response.toString());
            parseResponse(response,userpostId,holder);

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley","ErrorResponse : "+error.toString());
        }
    });
    AppController.getInstance().addToRequestQueue(loginRequest);
}
    public void parseResponse(String response,int userpostId,MyViewHolder holder)
    {
        try {
            JSONObject resObj = new JSONObject(response);
            JSONArray feedArr = resObj.getJSONArray("user");
            for (int i=0 ; i<feedArr.length();i++){
                JSONObject currentElemnt =  feedArr.getJSONObject(i);
                if(currentElemnt.getInt("id")==userpostId) {
                    postUserName=currentElemnt.getString("username");
                    posetuserPp=currentElemnt.getString("profilepic");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            Glide.with(context)
                    .load(feedImageURl +posetuserPp)
                    .fitCenter()
                    .into(holder.ppImg);
            holder.personName.setText(postUserName);
        }
    }
}
