package instagram.shalan.net.sayed.instagram.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import instagram.shalan.net.sayed.instagram.R;
import instagram.shalan.net.sayed.instagram.jarjar.MultipartRequest;
import instagram.shalan.net.sayed.instagram.sharedpreferences.MysharedPreferences;
import instagram.shalan.net.sayed.instagram.volley.AppController;


public class CameraFragment extends Fragment {
    View view;
    Button cameraBtn;
    Button galleryBtn;
    private static int RESULT_LOAD_IMAGE = 2;
    final static int REQUEST_TAKE_PHOTO = 1;
    String URL = "http://192.168.43.188/instagram/preUpload.php";

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_camera, container, false);
        cameraBtn=(Button)view.findViewById(R.id.camBtn);
        galleryBtn=(Button)view.findViewById(R.id.galBtn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFromGallery();
            }
        });
        //////////////////////
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takefromCamera();
            }
        });
        return view;
    }
    ///////////////////////////////////////////////////////////
public void uploadFromGallery()
{
    Intent i = new Intent(
            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    startActivityForResult(i, RESULT_LOAD_IMAGE);
}
    /////////////////////////////////////////////////////////////////
public void takefromCamera()
{
    Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(photoCaptureIntent, REQUEST_TAKE_PHOTO);
}

    //////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // take from camera
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);
            String storeFilename = "photo_" + partFilename + ".jpg";
            Log.e("picPAth",Environment.getDataDirectory()+"/" + storeFilename);
            uploadimage(new File(Environment.getExternalStorageDirectory()+"/" + storeFilename));
            saveInDatabase(new File(Environment.getExternalStorageDirectory()+"/" + storeFilename));


        }

        /// select from gallery section
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.e("Uri",selectedImage + "");
            Log.e("filePathColumn",MediaStore.Images.Media.DATA + "");

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
///////////////////////////////////////////////////////////////////////////////////
    private void saveInDatabase(final File file) {
        String url= "http://192.168.43.188/instagram/insertphoto.php";
        StringRequest loginRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Volley","Response : "+response.toString());

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
                params.put("email",new MysharedPreferences(getActivity()).getUser().getEmail());
                params.put("img",file.getName());
                params.put("date",currentDateFormat());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(loginRequest);
    }

    ////////////////////////////////////////////////////////////////////
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    //////////////////////////////////////////////////////////////////////
    private void uploadimage(File file) {
        HashMap<String,String> prams = new HashMap<>();
        prams.put("FileNme",file.getName());
        prams.put("id",new MysharedPreferences(getActivity()).getUser().getEmail());
        prams.put("date",currentDateFormat());
        MultipartRequest  uploadImageRequest = new MultipartRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response.toString() + "");
                Toast.makeText(getActivity(),"Image is added",Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(getActivity()).add(uploadImageRequest);
    }
    ///////////////////////////////////////////////////////////////////////////////////////
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
}
