package etna.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class FavActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private ListView favListView;
    private TextView citytext;
    public static ArrayList<Model> modelArrayList;
    private CustomAdapter customAdapter;
//    public String[] cityList;
//    private String[] cityList;
//    private String[] cityList = new String[]{"Paris", "Barcelona", "Lyon", "Marseille","Madrid"};



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        favListView = (ListView) findViewById(R.id.favListView);
        citytext = (TextView) findViewById(R.id.city);
        modelArrayList = getModel();
        customAdapter = new CustomAdapter(this);
        favListView.setAdapter(customAdapter);
        Log.v("message" , favListView.toString());


//        preferences = getSharedPreferences("prefs", MODE_PRIVATE);
//        ArrayList<String> city_list = new ArrayList<String>();

//        try {
//            for (String key : (preferences.getAll()).keySet()) {
//                city_list.add(key.toString());
//
//            }
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

//        cityList = city_list.toArray(new String[0]);

        Intent intent = new Intent(this, MainActivity.class);

//
//        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                for (String key : (preferences.getAll()).keySet()) {
//                    System.out.println(key + ":" + (preferences.getAll()).get(key));
//                }
//                String city = adapterView.getItemAtPosition(i).toString();
//                System.out.println(adapterView.getItemAtPosition(i));
//                intent.putExtra("url", city);
//                startActivity(intent);
//            }
//        });









    }
//    private ArrayList<Model> getModel(){
//        ArrayList<Model> list = new ArrayList<>();
//        for(int i = 0; i < 5; i++){
//            Model model = new Model();
//            model.setCity(cityList[i]);
//            list.add(model);
//        }
//        return list;
//    }
    private ArrayList<Model> getModel(){
        ArrayList<Model> list = new ArrayList<>();
        preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        try {
            for (String key : (preferences.getAll()).keySet()) {
                Model model = new Model();
                model.setCity(key.toString());
//                city_list.add(key.toString());
                list.add(model);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

//        for(int i = 0; i < 1; i++){
//            Model model = new Model();
//            model.setCity(cityList[i]);
//            list.add(model);
//        }
        return list;
    }


}

    //--------------------------------------------------------------------------------------------
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

//        preferences = getSharedPreferences("prefs", MODE_PRIVATE);
//
//        setContentView(R.layout.activity_fav);
//        ListView favListView = (ListView) findViewById(R.id.favListView);
//        ArrayList<String> list = new ArrayList<String>();
//
//        try {
//            for (String key : (preferences.getAll()).keySet()) {
//                list.add(key.toString());
//            }
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//
//
//        String[] strings = list.toArray(new String[0]);
//
//
//
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
//        favListView.setAdapter(arrayAdapter);
//
//
//        Intent intent = new Intent(this, MainActivity.class);
//
//        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                for (String key : (preferences.getAll()).keySet()) {
//                    System.out.println(key + ":" + (preferences.getAll()).get(key));
//                }
//                String city = adapterView.getItemAtPosition(i).toString();
//                System.out.println(adapterView.getItemAtPosition(i));
//                intent.putExtra("url", city);
//                startActivity(intent);
//            }
//        });
//     }

        // ---------------------------------------------------------------------------------------






