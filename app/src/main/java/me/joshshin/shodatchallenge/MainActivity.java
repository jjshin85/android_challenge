package me.joshshin.shodatchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import me.joshshin.shodatchallenge.Services.PhotoService;
import me.joshshin.shodatchallenge.Services.PhotoServiceGenerator;
import me.joshshin.shodatchallenge.models.Photo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    PhotoService photoService;
    PhotoAdapter adapter;
    ListView listView;
    FloatingActionButton forward_fab;
    FloatingActionButton backward_fab;
    int album = 1;
    List<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listview);
        photoService = PhotoServiceGenerator.createService(PhotoService.class);
        forward_fab = (FloatingActionButton) findViewById(R.id.forward_fab);
        backward_fab = (FloatingActionButton) findViewById(R.id.backward_fab);

        getPhotos(photoService, album);

        forward_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempVal = album;
                album = tempVal + 2;
                getPhotos(photoService, album);
            }
        });

        backward_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempVal = album;
                album = tempVal - 2;
                getPhotos(photoService, album);
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

    public List<Photo> getPhotos(PhotoService ps, final int albumNum) {
        Call<List<Photo>> getCall = ps.getPhotos(albumNum, albumNum + 1);
        getCall.enqueue(new Callback<List<Photo>>(){
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                Log.i("getPhotos onResponse", "Pages: onSuccess: " + response.body() + "; onError: " + response.errorBody());
                photos = response.body();
                Log.i("Photos list size: ", "" + photos.size());
                Log.i("getPhotos albums: ", "" + albumNum + ", " + (albumNum + 1));
                adapter = new PhotoAdapter(getBaseContext(), R.layout.activity_listview, photos);
                listView.setAdapter(adapter);
                setUpItemClickListener(listView, adapter);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.i("getPhotos onFailure", t.toString());
            }
        });
        //Set visibility of back navigation
        if(albumNum == 1) {
            backward_fab.setVisibility(View.INVISIBLE);
        } else {
            if(backward_fab.getVisibility() == View.INVISIBLE) {
                backward_fab.setVisibility(View.VISIBLE);
            }
        }
        return photos;
    }

    public void setUpItemClickListener(ListView lv, final PhotoAdapter pa){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = pa.getItem(position);
                //get url for image and send it to PhotoActivity
                String url = pa.changeToHTTPS(photo.getUrl());
                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("photo_url", url);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }



}
