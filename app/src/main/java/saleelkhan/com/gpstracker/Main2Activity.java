package saleelkhan.com.gpstracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    Spinner spinner;
    Button track;
    ArrayList<Spinnerdata> arrayList;
    Database database;
    Cursor cursor,getUser;
    String userId;
    Adapter adapter;
    String lat,lang;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        spinner = (Spinner) findViewById(R.id.spinner);
        track = (Button) findViewById(R.id.track);
        adapter = new Adapter();
        arrayList = new ArrayList<>();
        database = new Database(this);

        database.run();
        cursor = database.getUserDetails();
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                Spinnerdata s = new Spinnerdata();
                s.setUserName(cursor.getString(1));
                s.setUserId(cursor.getString(3));
                arrayList.add(s);
            }
            adapter.notifyDataSetChanged();

        }
        spinner.setAdapter(adapter);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String geoCode = "geo:0,0?q=" + lat + ","
                        + lang ;
                Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(geoCode));
                sendLocationToMap.setPackage("com.google.android.apps.maps");
                startActivity(sendLocationToMap);*/
                /*String geoCode = "geo:0,0?q=" + lat + ","
                        + lang;*/
                String uri = "geo:"+lat+","+lang+"0?q=10"+address;



                Intent mapIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinnerdata s = arrayList.get(position);

                    String uId = s.getUserId();
                    String name = s.getUserName();
                getUser = database.getUserDetails(name);
                if(getUser != null)
                {
                    while (getUser.moveToNext())
                    {
                        lat = getUser.getString(4);
                        lang = getUser.getString(5);
                        address = getUser.getString(6);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.customspinner,null);
            Spinnerdata s = arrayList.get(position);
            TextView tuId = (TextView) view.findViewById(R.id.spineertext);
            tuId.setText(""+s.getUserId());
            return view;
        }
    }
}
