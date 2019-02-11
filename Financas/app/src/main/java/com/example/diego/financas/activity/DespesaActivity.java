package com.example.diego.financas.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.diego.financas.R;
import com.example.diego.financas.auxiliar.ConfigurandoData;
import com.example.diego.financas.modelo.Movimentacao;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoValor = findViewById(R.id.editText_campo_valor);
        campoData = findViewById(R.id.edit_campo_data);
        campoCategoria = findViewById(R.id.edit_campo_categoria);
        campoDescricao = findViewById(R.id.edit_campo_descricao);

        campoData.setText(ConfigurandoData.dataAtual());
    }

    public void salvarDespesa(View view){
        movimentacao = new Movimentacao();
        String data = campoData.getText().toString();
        movimentacao.setValor(Double.parseDouble(campoValor.getText().toString()));
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setData(data);
        movimentacao.setTipo("despesa");

        movimentacao.salvar(data);

    }
}
