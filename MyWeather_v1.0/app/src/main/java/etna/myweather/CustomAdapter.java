package etna.myweather;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;



public class CustomAdapter  extends BaseAdapter {



    private Context context;
    private SharedPreferences preferences;



    public CustomAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getCount() {
        return FavActivity.modelArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return FavActivity.modelArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        preferences = context.getSharedPreferences("prefs", MODE_PRIVATE);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, null, true);
            holder.tvCity = (TextView) convertView.findViewById(R.id.city);
            holder.btn_rm = (Button) convertView.findViewById(R.id.fav_rm);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tvCity.setText(FavActivity.modelArrayList.get(position).getCity());
        holder.btn_rm.setTag(R.integer.btn_rm_view, convertView);
        holder.btn_rm.setTag(R.integer.btn_rm_pos, position);


        Intent intent = new Intent(context, MainActivity.class);
        Intent intent2 = new Intent(context, FavActivity.class);

        holder.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("thismessage:", "");
                System.out.println(holder.tvCity.getText().toString());
                String city = holder.tvCity.getText().toString();
                intent.putExtra("url", city);
                context.startActivity(intent);
            }
        });
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

        holder.btn_rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View tempview = (View) holder.btn_rm.getTag(R.integer.btn_rm_view);
//                Integer pos = (Integer) holder.btn_rm.getTag(R.integer.btn_rm_pos);


                SharedPreferences.Editor editor = preferences.edit();

                String city = holder.tvCity.getText().toString();
                editor.remove(city);
                editor.commit();
                context.startActivity(intent2);




            }
        });
        return convertView;
    }
    private class ViewHolder {
        protected Button btn_rm;
        private TextView tvCity;
    }}