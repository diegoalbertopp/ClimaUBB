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
    private RequestQueue queue;
    private TextView text;
    private TextView text2;
    private TextView ciudad;
    private Gauge gauge;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_temperatura:
                    mTextMessage.setText(R.string.title_temperatura);
                    gauge.setMaxValue(60);
                    gauge.setTotalNicks(60);
                    gauge.setValuePerNick(1);
                    obtener("E1yGxKAcrg");
                    return true;
                case R.id.navigation_radiacion:
                    mTextMessage.setText(R.string.title_radiacion);
                    gauge.setMaxValue(1600);
                    gauge.setTotalNicks(100);
                    gauge.setValuePerNick(16);
                    obtener("8IvrZCP3qa");
                    return true;
                case R.id.navigation_humedad:
                    mTextMessage.setText(R.string.title_humedad);
                    gauge.setMaxValue(100);
                    gauge.setTotalNicks(100);
                    gauge.setValuePerNick(1);
                    obtener("VIbSnGKyLW");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.texto);
        queue = Volley.newRequestQueue(this);
        text2 = findViewById(R.id.texto2);
        ciudad = findViewById(R.id.ciudad);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        ciudad.setText("Chillán | "+fecha);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        gauge = (Gauge) findViewById(R.id.gauge);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Iniciamos de esta manera
        mTextMessage = findViewById(R.id.message);
        mTextMessage.setText("Temperatura");
        obtener("E1yGxKAcrg");
        gauge.setMaxValue(60);
        gauge.setTotalNicks(60);
        gauge.setValuePerNick(1);
    }

    private void obtener(final String sensor){
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        String url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/DY6A0fMetu/"+sensor+"/"+fecha;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray mJsonArray = response.getJSONArray("data");
                    JSONObject mJsonobject = mJsonArray.getJSONObject(mJsonArray.length()-1);
                    String dato = mJsonobject.getString("valor");
                    if(sensor == "E1yGxKAcrg"){
                        text.setText(dato+"°C");
                    }else{
                        if(sensor == "8IvrZCP3qa"){
                            text.setText(dato+"nm");
                        }else{
                            text.setText(dato+"%RH");
                        }
                    }

                    int suma = 0;

                    for(int i=0; i<mJsonArray.length();i++) {
                        JSONObject mJsonobject2 = mJsonArray.getJSONObject(i);
                        String valor = mJsonobject2.getString("valor");
                        int numero = Integer.parseInt(valor);
                        suma = suma + numero;
                    }

                    int promedio = suma / mJsonArray.length();
                    String total = Integer.toString(promedio);

                    if(sensor == "E1yGxKAcrg"){
                        text2.setText(total+"°C");
                    }else{
                        if(sensor == "8IvrZCP3qa"){
                            text2.setText(total+"nm");
                        }else{
                            text2.setText(total+"%RH");
                        }
                    }

                    Float resultado = Float.parseFloat(dato);
                    gauge.moveToValue(resultado);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}
