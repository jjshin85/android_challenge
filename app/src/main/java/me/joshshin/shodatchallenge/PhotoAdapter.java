package me.joshshin.shodatchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.joshshin.shodatchallenge.models.Photo;

/**
 * Created by jjshin on 5/23/16.
 */
public class PhotoAdapter extends BaseAdapter {

    List<Photo> photos;

    public PhotoAdapter(Context context, int resource, List<Photo> array) {
        photos = array;
    }
    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Photo getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return photos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.listview_activity, parent, false);

            TextView textView = (TextView) layout.findViewById(R.id.list_text);
            ImageView imageView = (ImageView) layout.findViewById(R.id.list_image);

            Photo photo = (Photo) getItem(position);
            String text = photo.getTitle();
            textView.setText(text);
            String url = photo.getThumbnailUrl();
            Picasso.with(parent.getContext())
                    .load(url)
                    .error(R.drawable.img_not_found)
                    .resize(50, 50)
                    .into(imageView);

            return layout;
        }
        else
        {
            return convertView;
        }
    }
}
