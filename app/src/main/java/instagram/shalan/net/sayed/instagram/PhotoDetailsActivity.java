package instagram.shalan.net.sayed.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import instagram.shalan.net.sayed.instagram.volley.AppController;

public class PhotoDetailsActivity extends AppCompatActivity {
    ImageView img;
    TextView postTxt;
    TextView postDate;
    Button delBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        img=(ImageView)findViewById(R.id.imgDetail);
        postTxt=(TextView)findViewById(R.id.postText);
        postDate=(TextView)findViewById(R.id.postDate);
        delBtn=(Button)findViewById(R.id.deleteBtn);
        ////////////////////////////////////////////////////
        final Bundle bundle = getIntent().getExtras();
        postTxt.setText(bundle.getString("post_text"));
        postDate.setText(bundle.getString("post_date"));

        Glide.with(getApplicationContext())
                .load("http://192.168.43.188/instagram/instaimage/"+bundle.getString("post_image"))
                .fitCenter()
                .into(img);
        ///////////////////////////////////////////////////////////
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteImage(bundle.getString("post_image"));
            }
        });
    }

    private void deleteImage(final String imgName) {

        String url= "http://192.168.43.188/instagram/delete.php";
        StringRequest loginRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Volley","Response : "+response.toString());
                Intent i = new Intent(PhotoDetailsActivity.this,ProfileActivity.class);
                ProfileActivity.fa.finish();
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(),"Image is removed",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley","ErrorResponse : "+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("imgName",imgName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(loginRequest);
    }

    }

