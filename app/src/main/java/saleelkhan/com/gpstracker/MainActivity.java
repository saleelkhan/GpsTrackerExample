package saleelkhan.com.gpstracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button user, admin;
    Database database;
    Cursor cursor;
    TextView newsigin;
    Address address;

    double latitude;
    double longitude;
    GPSTracker gpsTracker;
    ArrayList<Address> addresses;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getLocaion();
        gpsTracker = new GPSTracker(MainActivity.this);
        String resunlt =null;
        // check if GPS enabled
        if(gpsTracker.canGetLocation()){

             latitude = gpsTracker.getLatitude();
             longitude = gpsTracker.getLongitude();
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            addresses = new ArrayList<Address>();
            try {
                addresses = (ArrayList<Address>) geocoder.getFromLocation(latitude,longitude,3);
                Log.d("location",""+addresses.size());
                System.out.println("address sixe="+addresses.size());
                if(addresses.size()>=1) {
                    Log.d("location",""+addresses.size());
                    address = addresses.get(0);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");

                    }

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName()).append("\n");
                    resunlt = sb.toString();
                  //  Toast.makeText(getApplicationContext(), "Your Location is -\n Locaton"+resunlt, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"Wait..for the location",Toast.LENGTH_LONG).show();
                   // startActivity(new Intent(this,MainActivity.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude+"\n Locaton"+resunlt, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        username.requestFocus();
        user = (Button) findViewById(R.id.user);
        admin = (Button) findViewById(R.id.admin);
        newsigin = (TextView) findViewById(R.id.register);

        database = new Database(this);
        database.run();
        final String finalResunlt = resunlt;
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if(!(name.equals(""))&& !(pass.equals("")))
                {

                   // database.insertUserDetails();
                    cursor = database.getUserDetails(name);
                    if(cursor.getCount()>0) {
                        if (cursor.moveToNext()) {

                            String passwordfromDb = cursor.getString(2);
                            String username1 = cursor.getString(1);

                           // Toast.makeText(MainActivity.this, "password"+username1, Toast.LENGTH_SHORT).show();
                            if (pass.equals(passwordfromDb)) {
                               //
                                //
                                database.updateAddress(latitude,longitude,username1,passwordfromDb, finalResunlt);
                                showMap(latitude,longitude);
                                username.setText("");
                                password.setText("");

                               // Toast.makeText(MainActivity.this, "Corret password", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Worng password", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else
                    {
                        Toast.makeText(MainActivity.this,"You dont have account please sigin ",Toast.LENGTH_SHORT).show();
                    }

                }else
                {
                    Toast.makeText(MainActivity.this,"Please Enter user name passord",Toast.LENGTH_SHORT).show();
                }


            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });

        newsigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,Signin.class));
                finish();
            }
        });

    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        new AlertDialog.Builder(this)
                .setTitle("CLose App")
                .setMessage("Are you sure you want to close the App?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                                   *//* Login login = new Login();
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.activity_main,login);
                                    //ft.addToBackStack(null);
                                    ft.commit();*//*
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
*/
    protected void showMap(double latitude, double longitude) {

        boolean installedMaps = false;

        // CHECK IF GOOGLE MAPS IS INSTALLED
        PackageManager pkManager = getPackageManager();
        try {
            @SuppressWarnings("unused")
            PackageInfo pkInfo = pkManager.getPackageInfo("com.google.android.apps.maps", 0);
            installedMaps = true;
        } catch (Exception e) {
            e.printStackTrace();
            installedMaps = false;
        }

        // SHOW THE MAP USING CO-ORDINATES FROM THE CHECKIN
        if (installedMaps == true) {
            String geoCode = "geo:0,0?q=" + latitude + ","
                    + longitude ;
            Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(geoCode));
            //sendLocationToMap.setPackage("com.google.android.apps.maps");
            startActivity(sendLocationToMap);
        } else if (installedMaps == false) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);

            // SET THE ICON
            alertDialogBuilder.setIcon(R.drawable.ic_stat_name);

            // SET THE TITLE
            alertDialogBuilder.setTitle("Google Maps Not Found");

            // SET THE MESSAGE
            alertDialogBuilder
                    .setMessage(R.string.noMapsInstalled)
                    .setCancelable(false)
                    .setNeutralButton("Got It",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                }
                            });

            // CREATE THE ALERT DIALOG
            AlertDialog alertDialog = alertDialogBuilder.create();

            // SHOW THE ALERT DIALOG
            alertDialog.show();
        }
    }
   // boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("CLose App")
                .setMessage("Are you sure you want to close the App?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                      /*  *//**//* Login login = new Login();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.activity_main,login);
                        //ft.addToBackStack(null);
                        ft.commit();*//**//**/
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
