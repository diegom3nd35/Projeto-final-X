package com.example.diego.financas.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.diego.financas.R;
import com.example.diego.financas.auxiliar.ConfigurandoData;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoValor = findViewById(R.id.editText_campo_valor);
        campoData = findViewById(R.id.edit_campo_data);
        campoCategoria = findViewById(R.id.edit_campo_categoria);
        campoDescricao = findViewById(R.id.edit_campo_descricao);

        //atualizando campo da data atual
        campoData.setText(ConfigurandoData.dataAtual());

    }
}
