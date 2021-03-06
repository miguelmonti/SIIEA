package com.example.miguelangel.savenergy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Configuracion_f_corte extends AppCompatActivity implements View.OnClickListener{

                            //Inicio de la declaracion de variables
    Button btn_fin;
    ImageButton ib_calendar;
    EditText fecha;
    Calendar date;
    int day, month, year;
    Spinner spTarifas, spCuotas;
    String id_user_cache, tipo_usuario_cache, id_clave_cache, id_cuota_cache, id_tarifa_cache,correo_cache,password_cache,nombre_cache,clave_cache,tarifa_cache,cuota_cache;
    String clave, correo, pass, id_tarifa, id_cuota,tarifa,cuota;

                            // Se declaran las listas en donde se almacenan los datos que se enviaran a los Spinner

    ArrayList<String> lista_tarifa = new ArrayList<String>();
    ArrayList<String> lista_cuota = new ArrayList<String>();
    ArrayList<String> lista_id_tarifa = new ArrayList<String>();
    ArrayList<String> lista_id_cuota = new ArrayList<String>();

                            //Fin de la declaración de variables

    private String conectar(int r) {
        URL url = null;
        String lru="";
        if (r==1){
            lru="https://savenergy.000webhostapp.com/savenergy/get_Tarifas.php";
        }
        if(r==2){
            lru="https://savenergy.000webhostapp.com/savenergy/get_Cuotas.php";
        }
        String line = "";
        String webServiceResult="";
        try {
            url = new URL(lru);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            bufferedReader.close();
        } catch (Exception e) {}
        return webServiceResult;//Resultado del servidor (convertido en JSON)
    }

    public void llenarSpTarifa(){//metodo para llenar el arreglo lista de tarifas
        String[] t=null;
        try {
            String resultJSON="";
            JSONObject respuestaJSON = new JSONObject (conectar(1));//Se guarda el resultado obtenido del JSON
            resultJSON = respuestaJSON.getString("estado");//guarda el registro del arreglo estado
            JSONArray jsonArray = null;
            if (resultJSON.equals("1")) {      // el correo y contraseña son correctas
                jsonArray = respuestaJSON.getJSONArray("consulta");
                for (int i=0;1<respuestaJSON.length();i++){
                    lista_tarifa.add(jsonArray.getJSONObject(i).getString("tarifa"));
                    lista_id_tarifa.add(jsonArray.getJSONObject(i).getString("id_tarifa"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void llenarSpCuota(){//metodo para llenar el arreglo lista de cuotas
        String[] t=null;
        try {
            String resultJSON="";
            JSONObject respuestaJSON = new JSONObject (conectar(2));//Se guarda el resultado obtenido del JSON
            resultJSON = respuestaJSON.getString("estado");//guarda el registro del arreglo estado
            JSONArray jsonArray = null;
            if (resultJSON.equals("1")) {      // el correo y contraseña son correctas
                jsonArray = respuestaJSON.getJSONArray("consulta");
                for (int i=0;1<respuestaJSON.length();i++){
                    lista_cuota.add(jsonArray.getJSONObject(i).getString("cuota"));
                    lista_id_cuota.add(jsonArray.getJSONObject(i).getString("id_cuotas"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String registrar(String pass, String correo, String id_clave, String id_t, String id_c) {
        URL url = null;
        String line = "";
        String webServiceResult="";
        try {
            url = new URL("https://savenergy.000webhostapp.com/savenergy/insert_user.php?" +
                    "pass="+pass+"&email="+correo+"&clave="+id_clave+"&tar="+id_t+"&cuo="+id_c);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            correo_cache = correo;
            password_cache = pass;
            clave_cache = id_clave;
            nombre_cache = "Nuevo usuario";
            tarifa_cache = id_t;
            cuota_cache = id_c;
            bufferedReader.close();
        } catch (Exception e) {}
        return webServiceResult;//Resultado del servidor (convertido en JSON)
    }

    //Metodo con 2 parametros para conectar con servidor y devuelve datos
    public String validar(String contra, String email){//Metodo que devuelve dos objetos - estado y consulta, convertidos en JSON
        URL url = null;
        String line = "";
        String webServiceResult="";
        try {
            url= new URL("https://savenergy.000webhostapp.com/savenergy/Login.php?contra="+contra+"&email="+email);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();//Se abre la conexion
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {//mientras exista un resultado los ira almacenando en la variable
                webServiceResult += line;
            }
            bufferedReader.close();
        } catch (Exception e) {}
        return webServiceResult;//Resultado del servidor (convertido en JSON)
    }

    public void cargarDatos(String pass, String email){
        try {
            String resultJSON="";
            JSONObject respuestaJSON = new JSONObject  (validar(pass,email));//Se guarda el resultado obtenido del JSON
            resultJSON = respuestaJSON.getString("estado");//guarda el registro del arreglo estado
            if (resultJSON.equals("1")) {      // el correo y contraseña son correctas
                id_user_cache = respuestaJSON.getJSONObject("usuario").getString("id_usuario");
                password_cache = respuestaJSON.getJSONObject("usuario").getString("contrasenia");
                correo_cache = respuestaJSON.getJSONObject("usuario").getString("email");
                nombre_cache = respuestaJSON.getJSONObject("usuario").getString("nombre");
                tipo_usuario_cache = respuestaJSON.getJSONObject("usuario").getString("tipo_usuario");
                id_clave_cache = respuestaJSON.getJSONObject("usuario").getString("id_clave_producto");
                id_cuota_cache= respuestaJSON.getJSONObject("usuario").getString("id_uota");
                cuota_cache = respuestaJSON.getJSONObject("usuario").getString("cuota");
                id_tarifa_cache = respuestaJSON.getJSONObject("usuario").getString("id_tarifa");
                tarifa_cache = respuestaJSON.getJSONObject("usuario").getString("tarifa");
                guardarUser();
            }
            else if (resultJSON.equals("2")){//el ususario no existe
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cargarPrincipal() {
        Intent intent = new Intent(Configuracion_f_corte.this, Principal.class);
        startActivity(intent);
        finish();
    }

    public void guardarUser(){//Metodo que guarda los datos del nuevo ususario registrado
        SharedPreferences preferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id_usuario",id_user_cache);
        editor.putString("correo", correo_cache);
        editor.putString("contrasenia",password_cache);
        editor.putString("nombre",nombre_cache);
        editor.putString("id_clave",clave_cache);
        editor.putString("id_tarifa",tarifa_cache);
        editor.putString("id_cuota",cuota_cache);
        editor.putString("tarifa",tarifa);
        editor.putString("tipo_ususario",tipo_usuario_cache);
        editor.putString("fecha",fecha.getText().toString());
        editor.putString("cuota",cuota);
        editor.commit();
    }

                                        //Métodos para el usuario (Presionar "Back")
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public  void onBackPressed(){
        Intent intent = new Intent(Configuracion_f_corte.this,Registrarse.class);
        startActivity(intent);
        finish();
    }

                                        //Fin de los metodos para el usuario

    @Override
    public void onClick(View view) {
        registrar(pass,correo,clave,id_tarifa,id_cuota);
            cargarDatos(pass,correo);
            insertFecha(id_user_cache,fecha.getText().toString());
            cargarPrincipal();
    }

    private String insertFecha(String id_user_cache, String fecha) {
        URL url = null;
        String line = "";
        String webServiceResult="";
        try {
            url= new URL("https://savenergy.000webhostapp.com/savenergy/primer_recibo.php?id_user="+id_user_cache+"&fecha="+fecha);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();//Se abre la conexion
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {//mientras exista un resultado los ira almacenando en la variable
                webServiceResult += line;
            }
            bufferedReader.close();
        } catch (Exception e) {}
        return webServiceResult;//Resultado del servidor (convertido en JSON)
    }

                                        //Método onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_f_corte);

        Toast.makeText(getApplicationContext(),"Entro a este metodo 1",Toast.LENGTH_SHORT).show();

                                        //Se Asigna permiso para mantener abierta la conexion

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

                        //Asignación de variable a componente en XML

        spTarifas = (Spinner) findViewById(R.id.sp_tarifa);         //Asignación de variable de tipo Spinner
        spCuotas = (Spinner) findViewById(R.id.sp_cuota);           //Asignación de variable de tipo Spinner

        btn_fin = (Button) findViewById(R.id.b_finalizar);          //Asignación de variable de tipo Button
        btn_fin.setOnClickListener(this);                           //Asignación de evento al Botón

        fecha = (EditText) findViewById(R.id.txt_fecha);            //Asignación de variable de tipo EditText

        ib_calendar = (ImageButton) findViewById(R.id.im_calendar);    //Asignación de variable de tipo ImageButton
        ib_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                      //Asignación de evetno al ImageButton
                DatePickerDialog date = new DatePickerDialog(Configuracion_f_corte.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1+1;
                        fecha.setText(i+"-"+i1+"-"+i2);
                    }
                }, year, month, day);
                date.show();
            }
        });

        date = Calendar.getInstance();

                                        //Obtención de variables por Intent

        clave = (String) getIntent().getExtras().getSerializable("clave");
        correo = (String) getIntent().getExtras().getSerializable("correo");
        pass = (String) getIntent().getExtras().getSerializable("pass");

                                        //Fin de la obtención de variables

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        month = month+1;
        fecha.setText(year+"-"+month+"-"+day);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog date = new DatePickerDialog(Configuracion_f_corte.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1+1;
                        fecha.setText(i+"-"+i1+"-"+i2);
                    }
                }, year, month, day);
                date.show();
            }
        });

                                //Se define el tipo de contenido y la lista de arreglos... Para tarifa y cuota

                                    //Metodo para llenar el arreglo lista_de_tarifas
        llenarSpTarifa();

                        //Se llena un ArrayAdapter con el contenido de las listas
        ArrayAdapter<String> tar = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,lista_tarifa);

                        //Se llena el Spinner con el contenido del ArrayAdapter
        spTarifas.setAdapter(tar);

                        //Se le asigna un evento al Spinner
        spTarifas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {      //Implementa una función al Spinner
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_tarifa = lista_id_tarifa.get(i); // Se asigna el valor seleccionado en el Spinner, a la variable tarifa
                tarifa = lista_tarifa.get(i);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) { }});


                                    //Metodo para llenar el arreglo lista_de_cuotas
        llenarSpCuota();

                        //Se llena un ArrayAdapter con el contenido de las listas
        ArrayAdapter<String> cuo = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,lista_cuota);

                        //Se llena el Spinner con el contenido del ArrayAdapter
        spCuotas.setAdapter(cuo);

                        //Se le asigna un evento al Spinner
        spCuotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//funcion del spinner cuota
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_cuota = lista_id_cuota.get(i);
                cuota = lista_cuota.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}});
    }

                                        //Fin del método onCreate
}
