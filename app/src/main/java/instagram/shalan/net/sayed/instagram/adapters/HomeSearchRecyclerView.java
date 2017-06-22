package instagram.shalan.net.sayed.instagram.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import instagram.shalan.net.sayed.instagram.R;
import instagram.shalan.net.sayed.instagram.model.FeedModel;
import instagram.shalan.net.sayed.instagram.model.User;

public class HomeSearchRecyclerView extends RecyclerView.Adapter<HomeSearchRecyclerView.MyViewHolder>{

    private Context context ;
    private ArrayList<User> data ;
    private LayoutInflater layoutInflater ;
    private String feedImageURl ="http://192.168.43.188/instagram/instaimage/";

    public HomeSearchRecyclerView(Context context, ArrayList<User> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.home_search_single_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User currentElement =  data.get(position);
        Glide.with(context)
                .load(feedImageURl +currentElement.getPp())
                .fitCenter()
                .into(holder.img);
        holder.userName.setText(currentElement.getUsername());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public User getItem(int postion){
        return  data.get(postion);
    }
    public void refresh(ArrayList<User> data) {
        this.data = data ;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView userName;
        public MyViewHolder(View itemView) {
            super(itemView);
            img =(ImageView) itemView.findViewById(R.id.homeSearchImg);
            userName=(TextView) itemView.findViewById(R.id.homesearchTxt);
        }
    }
}
