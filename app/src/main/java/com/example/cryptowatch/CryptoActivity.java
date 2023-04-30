package com.example.cryptowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CryptoActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 20000908;

    private EditText searchEditText;
    private RecyclerView currencyRV;
    private ProgressBar loadingPB;
    private SharedPreferences preferences;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private CurrencyRVAdapter currencyRVAdapter;

    private NotificationHandler notificationHandler;
    private final int DELAY = 60000; // delay in milliseconds
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getCurrencyData();
            handler.postDelayed(this, DELAY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notificationHandler.send("Prices have been updated. Check them out!");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto);

        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);
        if (secret_key != 20000908){finish();}

        searchEditText = findViewById(R.id.EditTextSearchCrypto);
        currencyRV = findViewById(R.id.idRVCurrencies);
        loadingPB = findViewById(R.id.idPBLoading);

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) finish();

        currencyRVModalArrayList = new ArrayList<>();
        currencyRVAdapter = new CurrencyRVAdapter(currencyRVModalArrayList, this);
        currencyRV.setLayoutManager(new LinearLayoutManager(this));
        currencyRV.setAdapter(currencyRVAdapter);

        getCurrencyData();

        notificationHandler = new NotificationHandler(this);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrencies(s.toString());
            }
        });
    }

    private void filterCurrencies(String currency){
        ArrayList<CurrencyRVModal> filteredList = new ArrayList<>();
        for (CurrencyRVModal item : currencyRVModalArrayList){
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        currencyRVAdapter.filterList(filteredList);
    }

    private void getCurrencyData(){
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            loadingPB.setVisibility(View.GONE);
            try {
                ArrayList<CurrencyRVModal> newCurrencyRVModalArrayList = new ArrayList<>();
                JSONArray dataArray = response.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++){
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    String name = dataObj.getString("name");
                    String symbol = dataObj.getString("symbol");
                    JSONObject quote = dataObj.getJSONObject("quote");
                    JSONObject usd = quote.getJSONObject("USD");
                    double price = usd.getDouble("price");
                    double hour = usd.getDouble("percent_change_1h");
                    double day = usd.getDouble("percent_change_24h");
                    double week = usd.getDouble("percent_change_7d");
                    newCurrencyRVModalArrayList.add(new CurrencyRVModal(name,symbol,price,hour,day,week));
                }
                currencyRVModalArrayList.clear();
                currencyRVModalArrayList.addAll(newCurrencyRVModalArrayList);
                currencyRVAdapter.notifyDataSetChanged();
            } catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(CryptoActivity.this,"Failed to extract JSON data", Toast.LENGTH_LONG).show();
            }
        }, error -> {
            loadingPB.setVisibility(View.GONE);
            Toast.makeText(CryptoActivity.this,"Failed to get the data", Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String>  headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","960da4bc-0f69-4374-807b-2c665f4a4385");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, DELAY);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public void gotoProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

}