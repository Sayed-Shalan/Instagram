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

public class SignupActivity extends AppCompatActivity {
    Button sigupBtn;
    EditText emailtxt;
    EditText passtxt;
    EditText usernametxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sigupBtn = (Button)findViewById(R.id.insignupBtn);
        emailtxt=(EditText)findViewById(R.id.signupemailtxt);
        passtxt =(EditText)findViewById(R.id.signuppasstxt);
        usernametxt =(EditText)findViewById(R.id.signupusertxt);
        sigupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailtxt.getText().toString().length()==0|| passtxt.getText().toString().length()==0|| usernametxt.getText().toString().length()==0) {
                    Toast.makeText(getApplicationContext(),"Please enter email and password",Toast.LENGTH_LONG).show();
                    emailtxt.setText(null);
                    passtxt.setText(null);
                }
                else {
                    signup();
                }
            }
        });
    }

    private void signup() {
        String url= "http://192.168.43.188/instagram/signup.php";
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
                String s1 = emailtxt.getText().toString();
                String s2 = passtxt.getText().toString();
                String s3 = usernametxt.getText().toString();
                Map<String,String> params = new HashMap<>();
                params.put("email",s1);
                params.put("pass",s2);
                params.put("username",s3);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(loginRequest);
    }
    public void parseResponse(String response)
    {
        try {
            JSONObject json=new JSONObject(response);
            if(json.getString("response").equals("Done"))
            {

                User user = new User();
                user.setEmail(emailtxt.getText().toString());
                user.setPassword(passtxt.getText().toString());
                new MysharedPreferences(getApplicationContext()).storeUser(user);
                Intent i = new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(),"Your account is created",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error occured",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

