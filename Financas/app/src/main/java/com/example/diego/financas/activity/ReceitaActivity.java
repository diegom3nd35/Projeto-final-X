package com.example.diego.financas.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diego.financas.R;
import com.example.diego.financas.auxiliar.ConfigurandoData;
import com.example.diego.financas.auxiliar.CriptografiaBase64;
import com.example.diego.financas.configuracao.ConfiguracaoFirebase;
import com.example.diego.financas.modelo.Movimentacao;
import com.example.diego.financas.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseReferencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoValor = findViewById(R.id.editText_campo_valor);
        campoData = findViewById(R.id.edit_campo_data);
        campoCategoria = findViewById(R.id.edit_campo_categoria);
        campoDescricao = findViewById(R.id.edit_campo_descricao);

        campoData.setText(ConfigurandoData.dataAtual());
        recuperarReceitaTotal();
    }
    public void salvarReceita(View view){

        if (validarCamposReceita()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("receita");

            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarCamposReceita(){
        String textoValor = campoCategoria.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if( !textoValor.isEmpty()){
            if( !textoData.isEmpty()){
                if( !textoCategoria.isEmpty()){
                    if( !textoDescricao.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(ReceitaActivity.this,
                                "Descrição não preenchida!",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else{
                    Toast.makeText(ReceitaActivity.this,
                            "Categoria não preenchida!",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(ReceitaActivity.this,
                        "Data não preenchida!",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(ReceitaActivity.this,
                    "Valor não preenchido!",
                    Toast.LENGTH_LONG).show();
            return false;
        }

    }
    public void recuperarReceitaTotal(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = CriptografiaBase64.codificarBase64(emailUsuario);
        DatabaseReference usuarioReferencia = firebaseReferencia.child("usuarios").child(idUsuario);

        usuarioReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizarReceita(Double receita){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = CriptografiaBase64.codificarBase64(emailUsuario);
        DatabaseReference usuarioReferencia = firebaseReferencia.child("usuarios").child(idUsuario);

        usuarioReferencia.child("receitaTotal").setValue(receita);
    }
}

