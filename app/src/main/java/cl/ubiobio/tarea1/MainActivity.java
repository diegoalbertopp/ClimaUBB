package cl.ubiobio.tarea1;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.nitri.gauge.Gauge;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private Float actualT;
    private Float actualR;
    private Float actualH;

    private RequestQueue queue;

    private TextView text;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView ciudad;

    private Gauge gauge;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
            Date date = new Date();
            String fecha = dateFormat.format(date);
            switch (item.getItemId()) {
                case R.id.navigation_temperatura:
                    obtenerT(fecha);
                    return true;
                case R.id.navigation_radiacion:
                    obtenerR(fecha);
                    return true;
                case R.id.navigation_humedad:
                    obtenerH(fecha);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        text = findViewById(R.id.texto);
        queue = Volley.newRequestQueue(this);
        text2 = findViewById(R.id.texto2);
        text3 = findViewById(R.id.texto3);
        text4 = findViewById(R.id.texto4);
        ciudad = findViewById(R.id.ciudad);
        actualT = 0f;
        actualR = 0f;
        actualH = 0f;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        ciudad.setText("Chillán | "+fecha);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        gauge = (Gauge) findViewById(R.id.gauge);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mTextMessage = findViewById(R.id.message);

        //Iniciamos mostrando la temperatura
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        Date date2 = new Date();
        String fecha2 = dateFormat2.format(date2);
        obtenerT(fecha2);
    }

    private void obtenerT(String fecha){
        mTextMessage.setText(R.string.title_temperatura);
        text3.setText("°C");
        text4.setText("°C");
        gauge.setMaxValue(60);
        gauge.setTotalNicks(60);
        gauge.setValuePerNick(1);
        String url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/DY6A0fMetu/E1yGxKAcrg/"+fecha;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray mJsonArray = response.getJSONArray("data");
                    JSONObject mJsonobject = mJsonArray.getJSONObject(mJsonArray.length()-1);
                    String dato = mJsonobject.getString("valor");
                    text.setText(dato);
                    int suma = 0;

                    for(int i=0; i<mJsonArray.length();i++) {
                        JSONObject mJsonobject2 = mJsonArray.getJSONObject(i);
                        String valor = mJsonobject2.getString("valor");
                        int numero = Integer.parseInt(valor);
                        suma = suma + numero;
                    }

                    int promedio = suma / mJsonArray.length();
                    String total = Integer.toString(promedio);
                    text2.setText(total);


                    Float resultado = Float.parseFloat(dato);
                    gauge.moveToValue(resultado);
                    actualT = resultado;
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(actualT>0){
                    text.setText(Math.round(actualT)+"");
                    text2.setText(Math.round(actualT)+"");
                }else{
                    text.setText("-");
                    text2.setText("-");
                }
                gauge.moveToValue(actualT);
            }
        });
        queue.add(request);
    }

    private void obtenerR(String fecha){
        mTextMessage.setText(R.string.title_radiacion);
        text3.setText("nm");
        text4.setText("nm");
        gauge.setMaxValue(1600);
        gauge.setTotalNicks(100);
        gauge.setValuePerNick(16);
        String url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/DY6A0fMetu/8IvrZCP3qa/"+fecha;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray mJsonArray = response.getJSONArray("data");
                    JSONObject mJsonobject = mJsonArray.getJSONObject(mJsonArray.length()-1);
                    String dato = mJsonobject.getString("valor");
                    text.setText(dato);
                    int suma = 0;

                    for(int i=0; i<mJsonArray.length();i++) {
                        JSONObject mJsonobject2 = mJsonArray.getJSONObject(i);
                        String valor = mJsonobject2.getString("valor");
                        int numero = Integer.parseInt(valor);
                        suma = suma + numero;
                    }

                    int promedio = suma / mJsonArray.length();
                    String total = Integer.toString(promedio);
                    text2.setText(total);


                    Float resultado = Float.parseFloat(dato);
                    gauge.moveToValue(resultado);
                    actualR = resultado;
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(actualR>0){
                    text.setText(Math.round(actualR)+"");
                    text2.setText(Math.round(actualR)+"");
                }else{
                    text.setText("-");
                    text2.setText("-");
                }
                gauge.moveToValue(actualR);
            }
        });
        queue.add(request);
    }

    private void obtenerH(String fecha){
        mTextMessage.setText(R.string.title_humedad);
        text3.setText("%RH");
        text4.setText("%RH");
        gauge.setMaxValue(100);
        gauge.setTotalNicks(100);
        gauge.setValuePerNick(1);
        String url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/DY6A0fMetu/VIbSnGKyLW/"+fecha;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray mJsonArray = response.getJSONArray("data");
                    JSONObject mJsonobject = mJsonArray.getJSONObject(mJsonArray.length()-1);
                    String dato = mJsonobject.getString("valor");
                    text.setText(dato);
                    int suma = 0;

                    for(int i=0; i<mJsonArray.length();i++) {
                        JSONObject mJsonobject2 = mJsonArray.getJSONObject(i);
                        String valor = mJsonobject2.getString("valor");
                        int numero = Integer.parseInt(valor);
                        suma = suma + numero;
                    }

                    int promedio = suma / mJsonArray.length();
                    String total = Integer.toString(promedio);
                    text2.setText(total);


                    Float resultado = Float.parseFloat(dato);
                    gauge.moveToValue(resultado);
                    actualH = resultado;
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(actualH>0){
                    text.setText(Math.round(actualH)+"");
                    text2.setText(Math.round(actualH)+"");
                }else{
                    text.setText("-");
                    text2.setText("-");
                }
                gauge.moveToValue(actualH);
            }
        });
        queue.add(request);
    }
}
