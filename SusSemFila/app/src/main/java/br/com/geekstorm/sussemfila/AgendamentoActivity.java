package br.com.geekstorm.sussemfila;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgendamentoActivity extends AppCompatActivity {

    //Widgets do Layout
    private Spinner especialidade, medico;
    Button sair;
    //Objetos de Requisição do servidor
    private RequestQueue requestQueue;
    private StringRequest request;
    //Url para onde a requisição será enviada
    private static final String URLespecialidade = "http://sussemfila.000webhostapp.com/especialidades.php ";
    //Array e objeto de Especialidade
    ArrayList<Especialidade> especialidadesArray = new ArrayList<>();
    Especialidade especialyt;
    //Objetos medico
    private static final String URLMedicos = "http://sussemfila.000webhostapp.com/medicos.php ";
    long idMedico;
    ArrayList<Medico> medicoArray = new ArrayList<>();
    Medico medicolyt;
    //Sistema de Sessão
    private UsuarioSessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        //Efetuando ligação do Objeto Sessão ao Contexto
        this.sessao = new UsuarioSessao(getApplicationContext());
        //Checando se o usuario esta logado
        if(sessao.checkLogin())
            finish();

        //Ligando requisição do Sistema pelo metodo Volley
        requestQueue = Volley.newRequestQueue(this);
        //Executando função para popular Spinner Especialidade
        this.SpinnerEspecialidade();

        //Efetuando ligação dos objetos do layout com objetos da classe
        especialidade = (Spinner) findViewById(R.id.Agendamento_especialidade);
        sair = (Button) findViewById(R.id.Agendamento_sair);
        medico = (Spinner) findViewById(R.id.Agendamento_medico);

        //Spinner de Medico



        //Botão Sair
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });


    }

    //Função para popular Spinner de Especialidade
    private void SpinnerEspecialidade(){
        request = new StringRequest(Request.Method.POST, URLespecialidade, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objeto = jsonArray.getJSONObject(i);
                        especialyt = new Especialidade(objeto.getInt("id"), objeto.getString("descricao"));
                        especialidadesArray.add(especialyt);
                    }

                    //Spinner de Especialidade
                    ArrayAdapter<Especialidade> adapter = new ArrayAdapter<Especialidade>(getApplicationContext(),R.layout.spinner_item, especialidadesArray);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    especialidade.setAdapter(adapter);

                    //Onclick do Spinner
                    especialidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                            Especialidade a = (Especialidade) especialidade.getSelectedItem();

                            Log.i("TESTE SELECAO", a.toString());
                            SpinnerMedico(a.getId()+"");

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapter) {  }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                return null;
            }
        };
        requestQueue.add(request);
    }

    //Spinner medico
    private void SpinnerMedico(final String v){

        request = new StringRequest(Request.Method.POST, URLMedicos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("TESTE JSON MEDICO",response);
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject objeto = jsonArray.getJSONObject(i);
                        medicolyt = new Medico(objeto.getInt("id"),objeto.getString("nome"));
                        medicoArray.add(medicolyt);
                    }
                    Log.i("TESTE SPINNER MEDICO", medicoArray.toString());

                    ArrayAdapter<Medico> adapter2 = new ArrayAdapter<Medico>(AgendamentoActivity.this,R.layout.spinner_item, medicoArray);
                    adapter2.setDropDownViewResource(R.layout.spinner_item);
                    medico.setAdapter(adapter2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("especialidade", v);
                return null;
            }
        };
        requestQueue.add(request);
    }
}
