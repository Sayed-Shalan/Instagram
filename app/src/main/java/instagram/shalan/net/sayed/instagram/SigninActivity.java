package instagram.shalan.net.sayed.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import instagram.shalan.net.sayed.instagram.model.User;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;
import instagram.shalan.net.sayed.instagram.volley.AppController;

public class SigninActivity extends AppCompatActivity {
    Button loginBtn;
    EditText emailTxt;
    EditText passText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        loginBtn=(Button)findViewById(R.id.insigninBtn1);
        emailTxt=(EditText)findViewById(R.id.txt1);
        passText=(EditText)findViewById(R.id.txt2);

        //////////////////////////////////////////////////////////////////////////
       loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailTxt.getText().toString().length()==0|| passText.getText().toString().length()==0) {
                    Toast.makeText(getApplicationContext(),"Please enter email and password",Toast.LENGTH_LONG).show();
                    emailTxt.setText(null);
                    passText.setText(null);
                }
                else {
                    login();
                }
            }
        });
    }
    public void login()
    {

        String url= "http://192.168.43.188/instagram/insta.php";
        StringRequest loginRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Volley","Response : "+response.toString());
                parseResponse(response);

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
                String s1 = emailTxt.getText().toString();
                String s2 = passText.getText().toString();
                Map<String,String> params = new HashMap<>();
                params.put("email",s1);
                params.put("pass",s2);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(loginRequest);
    }
public void parseResponse(String response)
{
               try {
                    JSONObject json=new JSONObject(response);
                    if(json.getString("response").equals("success"))
                    {
                        JSONObject userObject=json.getJSONObject("user");
                        User user = new User();
                        user.setEmail(userObject.getString("useremail"));
                        user.setId(userObject.getInt("id"));
                        user.setPassword(userObject.getString("userpassword"));
                        user.setUsername(userObject.getString("username"));
                        user.setPp(userObject.getString("profilepic"));
                        user.setStatus(userObject.getString("status"));
                        new MysharedPreferences(getApplicationContext()).storeUser(user);
                        Intent i = new Intent(SigninActivity.this,ProfileActivity.class);
                        startActivity(i);
                        LoginActivity.fa.finish();
                        finish();
                        Toast.makeText(getApplicationContext(),"Correct account",Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
}
}
