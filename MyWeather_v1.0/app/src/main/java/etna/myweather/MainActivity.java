package etna.myweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
//    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    private SharedPreferences preferences;
    private ArrayList<WeatherModal> weatherModalArrayList;
    private ArrayList<DailyModal> dailyModalArrayList;
    private WRVAdapter wrvAdapter;
    private DailyAdapter dailyAdapter;
    private RecyclerView weatherRV;
    private RecyclerView dailyRV;
//    RequestQueue queue = Volley.newRequestQueue(this);

    //        TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

//    public class Global {
//
//        public String address = "";
//
//    }
//
//    public Global global = new Global();

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String longitude;
    private String latitude;
    private int PERMISSION_CODE = 1;
    public String url = "";
    public String url_forecast = "";
    public String url_hourly;
    public String direction;
    public String old_city;
    public int count = 0;
    ArrayList<String> list = new ArrayList<String>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        weatherModalArrayList = new ArrayList<>();
        wrvAdapter = new WRVAdapter(this, weatherModalArrayList);
        dailyModalArrayList=new ArrayList<>();
        dailyAdapter= new DailyAdapter(this, dailyModalArrayList);


        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        preferences = getSharedPreferences("prefs", MODE_PRIVATE);
//        Bundle extras = getIntent().getExtras();

        TextView tempTextView = (TextView) findViewById(R.id.tempTextView);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        TextView weatherDescTextView = (TextView) findViewById(R.id.weatherDescTextView);
        TextView cityTextView = (TextView) findViewById(R.id.cityTextView);
        TextView farenheitTextView = (TextView) findViewById(R.id.farenheitTextView);
        TextView favOneTextView = (TextView) findViewById(R.id.favOneTextView);
        TextView minTextView = (TextView) findViewById(R.id.MinTextView);
        TextView maxTextView = (TextView) findViewById(R.id.maxTextView);
        TextView windSpeedTextView = (TextView) findViewById(R.id.windSpeedTextView);
        TextView windDirectionTextView = (TextView) findViewById(R.id.windDirectionTextView);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button addFavButton = (Button) findViewById(R.id.addFavButton);
//        Button rmButton = (Button) findViewById(R.id.rmButton);
        Button favButton = (Button) findViewById(R.id.favButton);
        EditText searchTextView = (EditText) findViewById(R.id.searchTextView);
        ImageView weatherImageView = (ImageView) findViewById(R.id.weatherimageView);
        RadioButton celsiusRadioButton = (RadioButton) findViewById(R.id.celsiusRadioButton);
        RadioButton farenheitRadioButton = (RadioButton) findViewById(R.id.farenheitRadioButton);
        RadioButton gpsRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
        dailyRV = findViewById(R.id.dayListView);
        weatherRV = findViewById(R.id.hourListView);


        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // cityName=getCityName(location.getLongitude(), location.getLatitude());
                longitude= String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());
                Log.d("latitude", latitude);
                Log.d("longitude",longitude);
                getCityName(longitude,latitude);
            }


        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10f,locationListener);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);





        celsiusRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (celsiusRadioButton.isChecked()){
                    farenheitRadioButton.setChecked(false);
                    method(null);
                    farenheitTextView.setText("°C");
                }
            }
        });
        farenheitRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (farenheitRadioButton.isChecked()){
                    celsiusRadioButton.setChecked(false);

                    method(null);
                    farenheitTextView.setText("°F");
                }
            }
        });

        gpsRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gpsRadioButton.isChecked() && count == 0) {
                    count += 1;
                    getCityName(longitude,latitude);
                    Log.d("count", String.valueOf(count));
                } else if (count > 0){
                    gpsRadioButton.setChecked(false);
                    count -= 1;
                }
            }
        });
        
        dateTextView.setText(getCurrentDate());




        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                method(searchTextView.getText().toString());
                old_city = searchTextView.getText().toString();
                searchTextView.setText("Choose location");



// Original -------------------------------------------
//                String units = null;
//                if (celsiusRadioButton.isChecked()) {
//
//                    farenheitTextView.setText("°C");
//                    units = "metric";
//
//                } else {
//
//                    farenheitTextView.setText("°F");
//                    units = "imperial";
//
//                }
//
//                String value = "";
//                if (extras != null){
//                    value = extras.getString("url");
//                }
//                String url_search = "";
////
//                if (value != null) {
//                    url_search = value;
//                } else {
//                    url_search = searchTextView.getText().toString();
//
//                }
//                String u_units = units;
//
//
//                url = "https://api.openweathermap.org/data/2.5/weather?q=" + url_search + "&appid=1f6a24b4f5ad78f8e52f979b677f4dab&units=" + u_units;
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                        Log.v("WEATHER", "Response: " + response.toString());
//
//                                try {
//                                    JSONObject mainJSONObject = response.getJSONObject("main");
//                                    JSONObject windJSONObject = response.getJSONObject("wind");
//                                    JSONArray weatherArray = response.getJSONArray("weather");
//                                    JSONObject firstWeatherObject = weatherArray.getJSONObject(0);
//
//                                    String temp = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp")));
//                                    String tempMin = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp_min")));
//                                    String tempMax = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp_max")));
//                                    String windSpeed = Integer.toString((int) Math.round(windJSONObject.getDouble("speed")));
//                                    String windDirection = Integer.toString((int) Math.round(windJSONObject.getDouble("deg")));
//                                    String weatherDescription = firstWeatherObject.getString("description");
//                                    String city = response.getString("name");
//
//                                    tempTextView.setText(temp);
//                                    weatherDescTextView.setText(weatherDescription);
//                                    cityTextView.setText(city);
//                                    minTextView.setText(tempMin);
//                                    maxTextView.setText(tempMax);
//                                    windSpeedTextView.setText(windSpeed);
//                                    windDirectionTextView.setText(windDirection);
//
//
//                                    int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
//                                    weatherImageView.setImageResource(iconResourceId);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // TODO: Handle error
//
//                            }
//                        });
//                queue.add(jsonObjectRequest);
// --------------------------------------------------------------------OriginalEnd
            }
        });
        addFavButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String city = cityTextView.getText().toString();
                String address = url;
                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("prefs", "test, test2");
                editor.putString(city, address);
                editor.commit();
//                String ville = preferences.getString("prefs", "FFFFFF");
                favOneTextView.setText(city);
//                (preferences.getAll()).forEach((k, v) -> System.out.println((k + ":" + v)));
                for (String key : (preferences.getAll()).keySet()) {
                    System.out.println(key + ":" + (preferences.getAll()).get(key));
                }

            }

        });

//        rmButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                SharedPreferences.Editor editor = preferences.edit();//
//                editor.clear();
//                editor.commit();
//            }
//        });

        Intent intent = new Intent(this, FavActivity.class);

        favButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
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

//    public final static String FAVORITE_COLOR = "fav color";


    public void method(String cityname) {
        RequestQueue queue = Volley.newRequestQueue(this);

        TextView tempTextView = (TextView) findViewById(R.id.tempTextView);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        TextView weatherDescTextView = (TextView) findViewById(R.id.weatherDescTextView);
        TextView cityTextView = (TextView) findViewById(R.id.cityTextView);
        TextView farenheitTextView = (TextView) findViewById(R.id.farenheitTextView);
        TextView favOneTextView = (TextView) findViewById(R.id.favOneTextView);
        TextView minTextView = (TextView) findViewById(R.id.MinTextView);
        TextView maxTextView = (TextView) findViewById(R.id.maxTextView);
        TextView windSpeedTextView = (TextView) findViewById(R.id.windSpeedTextView);
        TextView windDirectionTextView = (TextView) findViewById(R.id.windDirectionTextView);
        TextView humidityTextView = (TextView) findViewById(R.id.humidityTextView);
        TextView pressionTextView = (TextView) findViewById(R.id.pressionTextView);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button addFavButton = (Button) findViewById(R.id.addFavButton);
//        Button rmButton = (Button) findViewById(R.id.rmButton);
        Button favButton = (Button) findViewById(R.id.favButton);
        EditText searchTextView = (EditText) findViewById(R.id.searchTextView);
        ImageView weatherImageView = (ImageView) findViewById(R.id.weatherimageView);
        RadioButton celsiusRadioButton = (RadioButton) findViewById(R.id.celsiusRadioButton);
        RadioButton farenheitRadioButton = (RadioButton) findViewById(R.id.farenheitRadioButton);
        RadioButton gpsRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
        dailyRV = findViewById(R.id.dayListView);
        dailyRV.setAdapter(dailyAdapter);
        weatherRV = findViewById(R.id.hourListView);
        weatherRV.setAdapter(wrvAdapter);

        Bundle extras = getIntent().getExtras();



        String units = null;
        if (celsiusRadioButton.isChecked()) {

            farenheitTextView.setText("°C");
            units = "metric";

        } else {

            farenheitTextView.setText("°F");
            units = "imperial";

        }

        String value = null;

        if (extras != null){
            value = extras.getString("url");
            old_city =extras.getString("url");

        }

        //String url_search = "";
        String other_url = "";

        if ((searchTextView.getText().toString()).equals("Choose location") && cityname != null && extras == null){
            other_url = cityname;
        } else if ((searchTextView.getText().toString()).equals("Choose location") && cityname != null && extras != null){
            other_url = value;
        } else if ((searchTextView.getText().toString()).equals("Choose location") && cityname != null && extras == null && gpsRadioButton.isChecked()){
            other_url = cityname;
        } else if ((searchTextView.getText().toString()).equals("Choose location") && celsiusRadioButton.isChecked()  || (searchTextView.getText().toString()).equals("Choose location") && farenheitRadioButton.isChecked() && !gpsRadioButton.isChecked()){
            other_url = old_city;
        } else {
            other_url = searchTextView.getText().toString();
        }


        url_hourly ="https://api.weatherapi.com/v1/forecast.json?key=41deb8f35c9a4135ade164521223009&q="+ other_url + "&days=6&aqi=no&alerts=no";
       JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, url_hourly, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherModalArrayList.clear();
                dailyModalArrayList.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    String city = response.getJSONObject("location").getString("name");
                    String weatherDescription = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String icon_url = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    String temp;
                    if (farenheitRadioButton.isChecked() && !celsiusRadioButton.isChecked()){
                        temp = Integer.toString((int) Math.round(response.getJSONObject("current").getDouble("temp_f")));
                    }else{
                        temp = Integer.toString((int) Math.round(response.getJSONObject("current").getDouble("temp_c")));
                    }
                    String windSpeed = response.getJSONObject("current").getString("wind_kph");
                    String windDirection = response.getJSONObject("current").getString("wind_dir");
                    String pression = response.getJSONObject("current").getString("pressure_mb");
                    String humidity = response.getJSONObject("current").getString("wind_kph");
                    if(isDay==1){
                      //  Picasso.get().load("https://iphonesoft.fr/images/_092018/wallpaper-fond-ecran-pixel-3-google-6.jpg").into(backIV);
                    }else{
//                        Picasso.get().load("https://i.pinimg.com/originals/bb/98/46/bb9846754c86253e6289b066dac46ca3.jpg").into(backIV);
                    }
                    Log.d("hourly_response", response.toString());
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONObject forecastD = forecastO.getJSONObject("day");
                    JSONArray hourArray = forecastO.getJSONArray("hour");
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                    String actualdate = dateFormat.format(calendar.getTime());
                    String d= hourArray.getJSONObject(0).getString("time");
                    int result = actualdate.compareTo(d);
                    String tempMin = forecastD.getString("mintemp_c");
                    String tempMax =forecastD.getString("maxtemp_c");
                    if(actualdate.contains("PM")){
                        result +=12;
                    }
                    for (int i=result; i< hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time =  hourObj.getString("time");
                        String temper;
                        if (farenheitRadioButton.isChecked() && !celsiusRadioButton.isChecked()){
                            temper = hourObj.getString("temp_f") + "°F";
                        }else{
                            temper = hourObj.getString("temp_c") + "°C";
                        }
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherModalArrayList.add(new WeatherModal(time,temper,img,wind));
                    }

                    for (int i=1; i < forecastObj.getJSONArray("forecastday").length(); i++){
                        JSONObject dObj = forecastObj.getJSONArray("forecastday").getJSONObject(i).getJSONObject("day");;
//                        String conditions = dObj.getJSONObject("condition").getString("text");

//                        String date = forecastO.getString("date");
                        String date = forecastObj.getJSONArray("forecastday").getJSONObject(i).getString("date");
                        Log.d("res",date);
                        String max_tmp;
                        if (farenheitRadioButton.isChecked() && !celsiusRadioButton.isChecked()){
                             max_tmp = dObj.getString("maxtemp_f") + "°F";
                        }else{
                            max_tmp = dObj.getString("maxtemp_c") + "°C";
                        }

                        String img = dObj.getJSONObject("condition").getString("icon");
                        dailyModalArrayList.add(new DailyModal(date,max_tmp,img));
                    }


                    dailyAdapter.notifyDataSetChanged();
                    wrvAdapter.notifyDataSetChanged();
                    tempTextView.setText(temp);
                    weatherDescTextView.setText(weatherDescription);
                    cityTextView.setText(city);
                    minTextView.setText(tempMin);
                    maxTextView.setText(tempMax);
                    String speed = windSpeed + " " + "km";
                    windSpeedTextView.setText(speed);
                    windDirectionTextView.setText(windDirection);
                    String pression_2 = pression + " " + "hPa";
                    pressionTextView.setText(pression_2);
                    String humidity_2 = humidity + " " + "%";
                    humidityTextView.setText(humidity_2);

                    String i_url = "https:" + icon_url;
                    Log.v("icon url :", "https:" + i_url);
                    new DailyAdapter.ImageDownloader(weatherImageView).execute(i_url);


//                    int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", "").toLowerCase(), "drawable", getPackageName());
//                    weatherImageView.setImageResource(iconResourceId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(MainActivity.this, "Veuillez entrer un nom de ville valide", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest3);






    }




    private String getCurrentDate ()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd, MMMM, yyyy");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    private String getCityName(String longitude, String latitude){
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(Double.valueOf(latitude),Double.valueOf(longitude), 1);
            for (Address adr : addresses){
                if(adr!=null){
                    String city = adr.getLocality();
                    if(city!= null && !city.equals("")){
                        cityName = city;

                    }else{
                        Log.d("TAG","CITY NOT FUnd");
                        Toast.makeText(this,"User City not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("cucu",cityName);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extras.clear();
        }

        method(cityName);
        return cityName;
    }
}