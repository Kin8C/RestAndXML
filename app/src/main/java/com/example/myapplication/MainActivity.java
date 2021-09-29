package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private static final String XML_URL = "https://www3.interrapidisimo.com/ApiservInter/api/Mensajeria/ObtenerRastreoGuias?guias=750001570809";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "Main", Toast.LENGTH_SHORT).show();

        //loaddata();
        Peticion();
    }

    private void loaddata() {
        Toast.makeText(getApplicationContext(), "Loaddata", Toast.LENGTH_SHORT).show();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, XML_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Data",response+"");
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        try {

                            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                            Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(response.getBytes()));

                            for (int i=0;i< parse.getElementsByTagName("ArrayOfADRastreoGuiaDC").getLength();i++){
                                String user_id = parse.getElementsByTagName("ADRastreoGuiaDC").item(i).getTextContent();
                                String name = parse.getElementsByTagName("EstadosGuia").item(i).getTextContent();
                                String phone = parse.getElementsByTagName("Guia").item(i).getTextContent();

                                Log.d("Test", user_id);
                                Log.d("Test", name);
                                Log.d("Test", phone);

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Error", error.getMessage());
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    public void Peticion(){
        String url = "https://www3.interrapidisimo.com/ApiservInter/api/Mensajeria/ObtenerRastreoGuias?guias=750001570809";
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Result", response.toString());
                        try {
                            JSONObject guia = response.getJSONObject(0);
                            Log.d("Print", String.valueOf(guia.names()));
                            Log.d("Print",  guia.getString("Guia"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                        Toast.makeText(getApplicationContext(), error.toString(),  Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

}