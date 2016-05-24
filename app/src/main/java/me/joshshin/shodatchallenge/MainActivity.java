package me.joshshin.shodatchallenge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.joshshin.shodatchallenge.Services.PhotoService;
import me.joshshin.shodatchallenge.Services.PhotoServiceGenerator;
import me.joshshin.shodatchallenge.models.Photo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    PhotoService photoService;
    Button callAPIButton;
    List<Photo> photos;
    PhotoAdapter adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listview);
        callAPIButton = (Button) findViewById(R.id.call_api_button);

        callAPIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onclick", "On Click called");
                photoService = PhotoServiceGenerator.createService(PhotoService.class);
                Call<List<Photo>> getCall = photoService.getPhotos(1, 0);
                getCall.enqueue(new Callback<List<Photo>>(){

                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        Log.i("getPages onResponse", "Pages: onSuccess: " + response.body() + "; onError: " + response.errorBody());
                        photos = response.body();
                        adapter = new PhotoAdapter(getBaseContext(), R.layout.listview_activity, photos);
                        listView.setAdapter(adapter);
                        Log.i("ListView called", "ListView instantiated with response data... hopefully");
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {
                        Log.i("getPages onFailure", t.toString());
                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
