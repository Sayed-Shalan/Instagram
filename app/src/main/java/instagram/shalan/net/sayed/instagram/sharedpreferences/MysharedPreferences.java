package instagram.shalan.net.sayed.instagram.sharedpreferences;


import android.content.Context;
import android.content.SharedPreferences;

import instagram.shalan.net.sayed.instagram.model.User;

public class MysharedPreferences {
    private int PRIVATE_MODE=0;
    private String PREF_NAME="user";
    private String KEY_UER_EMAIL="user_email";
    private String KEY_UER_PASSWORD="user_pass";
    private String KEY_USER_ID="user_id";
    private String KEY_USER_NAME="user_name";
    private String KEY_USER_PP="user_pp";
    private String KEY_USER_STATUS="user_status";
    private Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public MysharedPreferences(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedpreferences.edit();
    }
    public void storeUser(User user)
    {
        editor.putString(KEY_UER_EMAIL,user.getEmail());
        editor.putString(KEY_UER_PASSWORD,user.getPassword());
        editor.putString(KEY_USER_NAME,user.getUsername());
        editor.putString(KEY_USER_STATUS,user.getStatus());
        editor.putString(KEY_USER_PP,user.getPp());
        editor.putInt(KEY_USER_ID,user.getId());
        editor.commit();
    }
    public  void update_photo(String photo)
    {
        editor.putString(KEY_USER_PP,photo);
        editor.commit();
    }
    public void update_state(String state)
    {
        editor.putString(KEY_USER_STATUS,state);
        editor.commit();

    }
    public User getUser()
    {
        User user = new User();

        user.setEmail(sharedpreferences.getString(KEY_UER_EMAIL,""));
        user.setPassword(sharedpreferences.getString(KEY_UER_PASSWORD,""));
        user.setUsername(sharedpreferences.getString(KEY_USER_NAME,""));
        user.setStatus(sharedpreferences.getString(KEY_USER_STATUS,""));
        user.setPp(sharedpreferences.getString(KEY_USER_PP,""));
        user.setId(sharedpreferences.getInt(KEY_USER_ID,0));
        return user;

    }
    public void deleteUser()
    {
        editor.remove(KEY_UER_EMAIL);
        editor.remove(KEY_UER_PASSWORD);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_PP);
        editor.remove(KEY_USER_STATUS);
        editor.remove(KEY_USER_NAME);

        editor.commit();
    }
}
