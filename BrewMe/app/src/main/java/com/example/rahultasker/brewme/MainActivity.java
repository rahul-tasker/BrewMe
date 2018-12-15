package com.example.rahultasker.brewme;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<BrewRecord> items;
    public static Context context;
    public Button filter_button;
    public EditText filter_input;
    public AppDatabase db;
    public String base_url = "https://api.openbrewerydb.org/breweries?per_page=500";
    public String current_URL;
    public String currentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        setup();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(items == null) {
            new HttpGetTask().execute(current_URL);
            System.out.println("get Task executed");
        } else {
            new displayAll(db, recyclerView, adapter, context).execute();
            System.out.println("displayAll executed");
        }
    }

    private void setup() {
        currentFilter = "State";
        filter_button = (Button) findViewById(R.id.filter_button);
        filter_button.setOnClickListener(this);
        filter_input = (EditText) findViewById(R.id.filter_input);
        filter_input.setHint("Filter by State");
        filter_input.setFocusable(true);
        filter_input.setClickable(true);
        filter_input.setFocusableInTouchMode(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, db.NAME)
                .fallbackToDestructiveMigration().build();
        current_URL = base_url;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.filter_button) {
            String queryString = filter_input.getText().toString();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            applyFilter(queryString);
            Toast.makeText(context, "filter Applied: " + queryString, Toast.LENGTH_LONG).show();
        }
    }

    public void applyFilter(String queryString) {
        if(queryString.equals("")) {
            Toast.makeText(context, "No text to filter", Toast.LENGTH_LONG).show();
        }
        else if(currentFilter.equals("City")) {
            current_URL += "&by_city=" + queryString;
        }
        else if(currentFilter.equals("Type")) {
            if(queryString.toLowerCase().equals("regional") || queryString.toLowerCase().equals("micro")
                    || queryString.toLowerCase().equals("brewpub") || queryString.toLowerCase().equals("large")
                    || queryString.toLowerCase().equals("bar") || queryString.toLowerCase().equals("contract")
                    || queryString.toLowerCase().equals("planning") || queryString.toLowerCase().equals("proprietor")) {
                current_URL += "&by_type=" + queryString;

            }
            else {
                Toast.makeText(context, "Invalid Type input", Toast.LENGTH_LONG).show();
            }
        }
        else if(currentFilter.equals("Name")) {
            current_URL += "&by_name=" + queryString;
        }
        else if(currentFilter.equals("State")) {
            current_URL += "&by_state=" + queryString;

        }
        else {
            Toast.makeText(context, "Error in search: " + currentFilter + " || " + current_URL, Toast.LENGTH_LONG).show();
        }
        new HttpGetTask().execute(current_URL);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        new HttpGetTask().execute(current_URL);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //3 dot menu created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //3 Dot menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cityFilter) {
            filter_input.setHint("Filter by City");
            filter_input.setText("");
            currentFilter = "City";
        } else if (id == R.id.nameFilter) {
            filter_input.setHint("Filter by Name");
            filter_input.setText("");
            currentFilter = "Name";
        } else if (id == R.id.stateFilter) {
            filter_input.setHint("Filter by State");
            filter_input.setText("");
            currentFilter = "State";
        } else if (id == R.id.typeFilter) {
            filter_input.setHint("Filter by Type");
            filter_input.setText("");
            currentFilter = "Type";
        } else if (id == R.id.clearFilter) {
            current_URL = base_url;
            new HttpGetTask().execute(current_URL);
            filter_input.setHint("Filter by State");
            filter_input.setText("");
            currentFilter = "State";
        }
        return super.onOptionsItemSelected(item);
    }


    //Actions done in slide bar
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_instructions) {
            Intent map = new Intent(this, InstructionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("instructions",null);
            map.putExtras(bundle);
            startActivity(map);

        } else if (id == R.id.nav_saved) {
            Intent map = new Intent(this, SavedActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("saved",null);
            map.putExtras(bundle);
            startActivity(map);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class HttpGetTask extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String queryString = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String brewJSONString = null;
            try {
                URL requestURL = new URL(queryString);
                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                    publishProgress();
                }

                if (builder.length() == 0) {
                    return null;
                }
                brewJSONString = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return brewJSONString;
        }

        @Override
        protected void onPostExecute(String s) {
            items = new ArrayList<BrewRecord>();
            String name;
            String brewery_type;
            String street;
            String city;
            String state;
            String postal_code;
            String country;
            String longitude;
            String latitude;
            String phone;
            String website_url;
            int brewId;

            try {
                JSONArray itemsArray = new JSONArray(s);
                int i = 0;
                while (i < itemsArray.length()) {
                    JSONObject brewObject = itemsArray.getJSONObject(i);
                    name = brewObject.getString("name");
                    brewery_type = brewObject.getString("brewery_type");
                    street = brewObject.getString("street");
                    city = brewObject.getString("city");
                    state = brewObject.getString("state");
                    postal_code = brewObject.getString("postal_code");
                    country = brewObject.getString("country");
                    if ((brewObject.getString("longitude") != null) && (brewObject.getString("latitude") != null)) {
                        longitude = brewObject.getString("longitude");
                        latitude = brewObject.getString("latitude");
                    }
                    else {
                        longitude = getLongitudeFromAddress(street + ", " + city + ", "+ state + " " + postal_code + ", " + country);
                        latitude = getLattitudeFromAddress(street + ", " + city + ", "+ state + " " + postal_code + ", " + country);
                    }
                    phone = brewObject.getString("phone");
                    website_url = brewObject.getString("website_url");
                    brewId = brewObject.getInt("id");
                    BrewRecord brewRecord = new BrewRecord();
                    brewRecord.setName(name);
                    brewRecord.setBrewery_type(brewery_type);
                    brewRecord.setStreet(street);
                    brewRecord.setCity(city);
                    brewRecord.setState(state);
                    brewRecord.setLatitude(latitude);
                    brewRecord.setLongitude(longitude);
                    brewRecord.setPostal_code(postal_code);
                    brewRecord.setCountry(country);
                    brewRecord.setPhone(phone);
                    brewRecord.setWebsite_url(website_url);
                    brewRecord.setBrewId(brewId);
                    items.add(brewRecord);
                    i ++;
                }
                new loadDataBase( db, recyclerView,  adapter, context).execute(items);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        public String getLattitudeFromAddress(String strAddress) {
            Geocoder coder = new Geocoder(getBaseContext());
            List<Address> address;
            String p1 = null;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                return Double.toString(location.getLatitude());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getLongitudeFromAddress(String strAddress) {
            Geocoder coder = new Geocoder(getBaseContext());
            List<Address> address;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                return Double.toString(location.getLongitude());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
