package instagram.shalan.net.sayed.instagram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import instagram.shalan.net.sayed.instagram.R;
import instagram.shalan.net.sayed.instagram.model.FeedModel;

public class FriendAdpter extends RecyclerView.Adapter<FriendAdpter.MyViewHolder>{

    private Context context ;
    private ArrayList<FeedModel> data ;
    private LayoutInflater layoutInflater ;
    private String feedImageURl ="http://192.168.43.188/instagram/instaimage/";

    public FriendAdpter(Context context, ArrayList<FeedModel> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.friend_single_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FeedModel currentElement =  data.get(position);
        Glide.with(context)
                .load(feedImageURl +currentElement.getPostImage())
                .fitCenter()
                .into(holder.img);
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
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img =(ImageView) itemView.findViewById(R.id.FrindImg);
        }
    }
}
