package br.com.geekstorm.sussemfila;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AgendamentoActivity extends AppCompatActivity {

    private Spinner especialidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        especialidade = (Spinner) findViewById(R.id.Agendamento_especialidade);

        Intent a = getIntent();
        Bundle bundle = a.getBundleExtra("bundle");
        ArrayList especialidadeArray = (ArrayList) (ArrayList) bundle.getSerializable("especialidade");

        for(int i = 0; i <= especialidadeArray.size(); i++){
            Log.d("Especialidade", (String) especialidadeArray.get(i));
        }


        ArrayAdapter<Especialidade> adapter = new ArrayAdapter<Especialidade>(this,android.R.layout.simple_spinner_dropdown_item, especialidadeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        especialidade.setAdapter(adapter);

        especialidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
             int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }
}