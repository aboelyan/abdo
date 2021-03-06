package com.aboelyan.sha.mshaweerak.RecyclerVeiwShofiers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfBookings extends AppCompatActivity {

    RecyclerView RS_BookingSh;
    private ShAdapter adapter;
    private RequestQueue mRequestQueue;
    private List<BookModels> bookModelses1 ;
    private ProgressDialog pd;
    SharedPreferences prefComment,pref,prefsh;
    SharedPreferences.Editor editorsh,editor;
    private JsonArrayRequest jsonArrayRequest;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter  recyclerViewadapter;
    private RequestQueue requestQueue;
    String urlListBokings = "http://devsinai.com/mashaweer/show.php";
    String car_id;
    ProgressBar progressBar;

    final String TAAG=this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_bookings);
        bookModelses1=new ArrayList<>();
        RS_BookingSh = (RecyclerView) findViewById(R.id.show_BookingSh);
        RS_BookingSh.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        RS_BookingSh.setLayoutManager(recyclerViewlayoutManager);
        recyclerViewadapter = new ShAdapter(bookModelses1,this);
        RS_BookingSh.setAdapter(recyclerViewadapter);


        prefsh = getSharedPreferences("Loginsh.shofier", Context.MODE_PRIVATE);
        car_id = prefsh.getString("car_id","");

        editorsh = prefsh.edit();


        Toast.makeText(this, car_id, Toast.LENGTH_SHORT).show();

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);


        JSON_DATA_WEB_CALL();

    }



  public void JSON_DATA_WEB_CALL() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlListBokings,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            JSON_PARSE_DATA_AFTER_WEBCALL(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String, String>();
                params.put("car_id", car_id);


                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
      progressBar.setVisibility(View.GONE);

  }


    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            BookModels bookModels2 = new BookModels();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                progressBar.setVisibility(View.VISIBLE);

                bookModels2.setFace(json.getString("face"));

                bookModels2.setTraveTime(json.getString("traveTime"));

               // bookModels2.setUser_id(json.getString("user_id"));

              //  bookModels2.setCar_id(json.getString("car_id"));
                Log.v(String.valueOf(json),"oioiiooo");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            bookModelses1.add(bookModels2);
        }

        recyclerViewadapter = new ShAdapter(bookModelses1,this);

        RS_BookingSh.setAdapter(recyclerViewadapter);




    }
}