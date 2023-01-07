package etna.myweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DailyModal> dailyModalArrayList;

    public DailyAdapter(Context context, ArrayList<DailyModal> dailyModalArrayList) {
        this.context = context;
        this.dailyModalArrayList = dailyModalArrayList;
    }

    @NonNull
    @Override
    public DailyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_daily, parent, false);
        return new DailyAdapter.ViewHolder(view);
    }

    static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyModal modal = dailyModalArrayList.get(position);
//        WeatherModal modal_2 = dailyModalArrayList.get(position);
        holder.temperatureTV.setText(modal.getTemperature());
//        String weatherDescription = modal_2.;
//        String weatherDescription = .getJSONObject("current").getJSONObject("condition").getString("text");

//        int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
        String icon_url = "https:"+modal.getIcon().toString();
        Log.v("icon url :", "https:" + icon_url);
//        holder.conditionIV.setImageURI(Uri.parse(modal.getIcon()));
        new ImageDownloader(holder.conditionIV).execute(icon_url);
//        holder.conditionIV.setImageBitmap(imageView);
        //holder.temperatureTVF.setText(modal.getTemperatureF()+ "°F");*
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd");
        try {
            Date t = input.parse(modal.getTime());
            holder.timeTV.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dailyModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView windTV, temperatureTV, timeTV, conditionTV;
        private ImageView conditionIV;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            conditionIV = itemView.findViewById(R.id.idTVCondition);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTVTime);

        }
    }
}
