package com.example.asus.freingo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.freingo.R;
import com.example.asus.freingo.activities.LoginActivity;
import com.example.asus.freingo.activities.LogoutActivity;
import com.example.asus.freingo.activities.RegisterActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import dmax.dialog.SpotsDialog;


public class AccountFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Uri imageUri;
    private ImageView userPic,addpic,logout;
    private EditText lastname,firstname,phone,email,password;
    private Button update;
    String url,host;
    @Override
    public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_account, container, false);
        host=this.getString(R.string.host);

        userPic= view.findViewById(R.id.userPic);
        addpic= view.findViewById(R.id.add);
        logout= view.findViewById(R.id.logout);
        update= view.findViewById(R.id.update);

        update.setOnClickListener(this);
        addpic.setOnClickListener(this) ;
        logout.setOnClickListener(this) ;

        lastname= view.findViewById(R.id.lastname);
        lastname.setText(LoginActivity.getDefaultsData("lastname",getContext()));
        firstname= view.findViewById(R.id.firstname);
        firstname.setText(LoginActivity.getDefaultsData("firstname",getContext()));
        email=view.findViewById(R.id.email);
        email.setText(LoginActivity.getDefaultsData("email",getContext()));
        phone=view.findViewById(R.id.phone);
        phone.setText(LoginActivity.getDefaultsData("phone",getContext()));
        password=view.findViewById(R.id.password);

        Log.d("User_id",LoginActivity.getDefaultsData("id",getContext()));
        if(password.getText().equals("") || password.getText()==null ){
            password.setVisibility(View.GONE);
        }
        if(email.getText().equals("") || email.getText() == null  && phone.getText().equals("") || phone.getText()==null){
            email.setText("Enter your E-mail");
            phone.setText("Enter your phone number");
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.setDefaults("session",true,getContext());
                LoginActivity.disconnectFromFacebook();
                Intent i=new Intent(getActivity(), LogoutActivity.class);
                i.putExtra("full_name",lastname.getText()+" "+firstname.getText());
                i.putExtra("picture",LoginActivity.getDefaultsData("picture",getContext()));
                startActivity(i);
                getActivity().finish();
                }
            });

        URL profilePic;
        if(LoginActivity.getDefaultsData("picture",getContext())!= null && LoginActivity.getDefaultsData("picture",getContext()).length() > 0) {
            try {
                Log.d("Urlpic",LoginActivity.getDefaultsData("picture",getContext()).trim());
                profilePic = new URL(LoginActivity.getDefaultsData("picture",getContext()).trim());
                Picasso.with(getActivity()).load(profilePic.toString()).placeholder(R.drawable.empty_img).into(userPic);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else {
            userPic.setImageResource(R.drawable.empty_img);
        }
        return view;
    }

    public void openGallery(){

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK && requestCode == 100){
             imageUri = data.getData();
            Log.d("imaaaaaaaaaage",imageUri.toString());
            setDefaultPic("picture",imageUri.toString(),getContext());
            userPic.setImageURI(imageUri);
            updateUserData();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==addpic.getId()){
            openGallery();
        }else if(v.getId()==logout.getId()){
            LoginActivity.setDefaults("session",true,getContext());
            Intent i=new Intent(getActivity(), LogoutActivity.class);
            i.putExtra("full_name",lastname.getText()+" "+firstname.getText());
            startActivity(i);
            getActivity().finish();
        }else if(v.getId()==update.getId()){
            SpotsDialog dialog = new SpotsDialog(getContext(), R.style.CustomCastTheme);
            dialog.show();
            updateUserData();
            setUserVisibleHint(true);
            dialog.dismiss();

        }
    }

    public void updateUserData(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        url = host+"/users/updateUser";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Update_Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("firstname",firstname.getText().toString());
                params.put("lastname",lastname.getText().toString());
                params.put("email", email.getText().toString());
                params.put("phone", phone.getText().toString());
                if(password.getText().toString()==null&& password.getText().toString().equals("")){
                    params.put("password",LoginActivity.getDefaultsData("password",getContext()));
                }
                params.put("password",password.getText().toString());
                params.put("picture",getDefaultPic("picture",getContext()));
                params.put("id",LoginActivity.getDefaultsData("id",getContext()));
                return params;
            }

        };

        queue.add(putRequest);

        LoginActivity.setDefaultsData("firstname",firstname.getText().toString(),getContext());
        LoginActivity.setDefaultsData("lastname",lastname.getText().toString(),getContext());
        LoginActivity.setDefaultsData("email", email.getText().toString(),getContext());
        LoginActivity.setDefaultsData("phone", phone.getText().toString(),getContext());
        LoginActivity.setDefaultsData("password",password.getText().toString(),getContext());
        LoginActivity.setDefaultsData("picture",getDefaultPic("picture",getContext()),getContext());


    }

    //fragment refresh
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        // Refresh tab data:

        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    public static void setDefaultPic(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public synchronized static String getDefaultPic(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
