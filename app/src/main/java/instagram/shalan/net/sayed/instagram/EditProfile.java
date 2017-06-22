package instagram.shalan.net.sayed.instagram;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import instagram.shalan.net.sayed.instagram.jarjar.MultipartRequest;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;
import instagram.shalan.net.sayed.instagram.volley.AppController;

public class EditProfile extends AppCompatActivity {
    Button editPicCam;
    Button editPicGal;
    Button editState;
    EditText stateTxt;
    private static int RESULT_LOAD_IMAGE = 2;
    final static int REQUEST_TAKE_PHOTO = 1;
    String URL = "http://192.168.43.188/instagram/preUpload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editPicCam=(Button)findViewById(R.id.editProfileCamBTN);
        editPicGal=(Button)findViewById(R.id.editProfilepicturegalBTN);
        editState=(Button)findViewById(R.id.editPersonstateBTN);
        stateTxt=(EditText)findViewById(R.id.newPersonStateTxt);
        /////////////////////////////////////////////////////////////////////
        editPicCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, REQUEST_TAKE_PHOTO);
            }
        });
        /////////////////////////////////////////////////////////////////////////////
        editPicGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////
        editState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stateTxt.getText().toString().length()<=0)
                {
                    Toast.makeText(getApplicationContext(),"Please enter new state",Toast.LENGTH_LONG).show();
                }
                else
                {
                    updateState();
                }
            }
        });
    }
/////////////////////////////////////////////////////////////////
    private void updateState() {

        String url= "http://192.168.43.188/instagram/updatestate.php";
        StringRequest loginRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Volley","Response : "+response.toString());
                new MysharedPreferences(getApplicationContext()).update_state(stateTxt.getText().toString());
                Toast.makeText(getApplicationContext(),"State is Changed",Toast.LENGTH_LONG).show();
                Intent i = new Intent(EditProfile.this,ProfileActivity.class);
                ProfileActivity.fa.finish();
                startActivity(i);
                finish();
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
                params.put("state",stateTxt.getText().toString());
                params.put("email",new MysharedPreferences(getApplicationContext()).getUser().getEmail());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(loginRequest);
    }

    //////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // take from camera
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode ==RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);
            String storeFilename = "photo_" + partFilename + ".jpg";
            Log.e("picPAth", Environment.getDataDirectory()+"/" + storeFilename);
            uploadimage(new File(Environment.getExternalStorageDirectory()+"/" + storeFilename));
            saveInDatabase(new File(Environment.getExternalStorageDirectory()+"/" + storeFilename));


        }

        /// select from gallery section
        if (requestCode == RESULT_LOAD_IMAGE && resultCode ==RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.e("Uri",selectedImage + "");
            Log.e("filePathColumn",MediaStore.Images.Media.DATA + "");

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.e("picturePath",picturePath + "");
            uploadimage(new File(picturePath));
            saveInDatabase(new File(picturePath));

        }
    }
    //////////////////////////////////////////////////////////////////////
    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    ////////////////////////////////////////////////////////////////////////////
    private void uploadimage(File file) {
        HashMap<String,String> prams = new HashMap<>();
        prams.put("FileNme",file.getName());
        prams.put("id",new MysharedPreferences(getApplicationContext()).getUser().getEmail());
        prams.put("date",currentDateFormat());
        MultipartRequest uploadImageRequest = new MultipartRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response.toString() + "");

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString() + "");
                    }
                }
                ,
                file,
                prams
        )
                ;
        Volley.newRequestQueue(getApplicationContext()).add(uploadImageRequest);
    }
    ///////////////////////////////////////////////////////////////////////////////
    private void saveInDatabase(final File file) {

        String url= "http://192.168.43.188/instagram/updatepic.php";
        StringRequest loginRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Volley","Response : "+response.toString());
                new MysharedPreferences(getApplicationContext()).update_photo(file.getName()+".jpg");
                Toast.makeText(getApplicationContext(),"Picture is Changed",Toast.LENGTH_LONG).show();
                Intent i = new Intent(EditProfile.this,ProfileActivity.class);
                ProfileActivity.fa.finish();
                startActivity(i);
                finish();
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
                params.put("img",file.getName()+".jpg");
                params.put("email",new MysharedPreferences(getApplicationContext()).getUser().getEmail());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(loginRequest);
    }
}
