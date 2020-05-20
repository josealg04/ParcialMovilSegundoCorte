package com.example.parcial2corte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.parcial2corte.AddTrack.EXTRA_ARTIST;
import static com.example.parcial2corte.AddTrack.EXTRA_DURATION;
import static com.example.parcial2corte.AddTrack.EXTRA_NAME;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TrackAdapter mTrackAdapter;
    public static ArrayList<Track> mTrackList = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private FloatingActionButton mFloatingActionButtonAdd;
    private FloatingActionButton mFloatingActionButtonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFloatingActionButtonAdd = findViewById(R.id.button_add);
        mFloatingActionButtonUpdate = findViewById(R.id.button_update);
        mFloatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTrack.class);
                startActivity(intent);
            }
        });

        mFloatingActionButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = 0;
                insertTrack(position);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

        if(savedInstanceState != null){
            boolean isVisible = savedInstanceState.getBoolean("key");
            if(isVisible){
            }
        }

    }

    private void parseJSON(){
        String url = "https://api.deezer.com/playlist/93489551";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("tracks");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject mtrack = jsonArray.getJSONObject(i);
                                JSONObject martist = mtrack.getJSONObject("artist");

                                String name = mtrack.getString("title");
                                String artist = martist.getString("name");
                                int duration1 = mtrack.getInt("duration");
                                String duration = calcularDuracion(duration1);

                                mTrackList.add(new Track(name, artist, duration));
                            }

                            mTrackAdapter = new TrackAdapter(MainActivity.this, mTrackList);
                            mRecyclerView.setAdapter(mTrackAdapter);

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", mTrackList);
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

    public void insertTrack(int position){
        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);
        String artist = intent.getStringExtra(EXTRA_ARTIST);
        String duration = intent.getStringExtra(EXTRA_DURATION);
        mTrackList.add(position, new Track(name, artist, duration));
        mTrackAdapter.notifyDataSetChanged();
    }
}
