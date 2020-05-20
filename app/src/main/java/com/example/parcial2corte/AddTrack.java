package com.example.parcial2corte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddTrack extends AppCompatActivity{

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ARTIST = "artist";
    public static final String EXTRA_DURATION = "duration";

    //private ArrayList<Track> mTrackList;
    private RequestQueue mRequestQueue;
    EditText editText;
    EditText nameTrack;
    EditText artistTrack;
    EditText durationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        editText = findViewById(R.id.searchText);
        nameTrack = findViewById(R.id.nameTrack);
        artistTrack = findViewById(R.id.artistTrack);
        durationTrack = findViewById(R.id.durationTrack);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertTrack();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertTrack(){
        String name = nameTrack.getText().toString();
        String artist = artistTrack.getText().toString();
        String duration = durationTrack.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("artist", artist);
        intent.putExtra("duration", duration);
        startActivity(intent);
    }

    private void buscar(){
        String aux = editText.getText().toString();
        String url = "https://api.deezer.com/search?q="+aux;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject mtrack = jsonArray.getJSONObject(0);
                                JSONObject martist = mtrack.getJSONObject("artist");

                                String name = mtrack.getString("title");
                                String artist = martist.getString("name");
                                int duration1 = mtrack.getInt("duration");
                                String duration = calcularDuracion(duration1);

                                nameTrack.setText(name);
                                artistTrack.setText(artist);
                                durationTrack.setText(String.valueOf(duration));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    public String calcularDuracion(int duracion){
        int horas = (duracion / 3600);
        int minutos = ((duracion-horas*3600)/60);
        int segundos = duracion-(horas*3600+minutos*60);
        return twoDigitString(minutos) + ":" + twoDigitString(segundos);
    }

    private String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }
}
