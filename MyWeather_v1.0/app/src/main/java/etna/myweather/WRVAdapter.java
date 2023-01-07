package etna.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WRVAdapter extends RecyclerView.Adapter<WRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherModal> weatherModalArrayList;

    public WRVAdapter(Context context, ArrayList<WeatherModal> weatherModalArrayList) {
        this.context = context;
        this.weatherModalArrayList = weatherModalArrayList;
    }

    @NonNull
    @Override
    public WRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_previsions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WRVAdapter.ViewHolder holder, int position) {
        WeatherModal modal = weatherModalArrayList.get(position);
        holder.temperatureTV.setText(modal.getTemperature());
        //holder.temperatureTVF.setText(modal.getTemperatureF()+ "°F");
        holder.windTV.setText(modal.getWindSpeed()+"km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date t = input.parse(modal.getTime());
            holder.timeTV.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView windTV, temperatureTV, timeTV, conditionTV;
        private ImageView conditionIV;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            windTV = itemView.findViewById(R.id.idTVWindSpeed);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTVTime);

        }
    }
}
