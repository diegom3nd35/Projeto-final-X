package com.example.diego.financas.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.diego.financas.R;
import com.example.diego.financas.configuracao.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    //private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        //apos catastro inicia o metodo verificaUsuarioLogado que consequentemente chama a tela principal
        super.onStart();
        verificarUsuarioLogado();
    }

    //Navegar pra tela de Login
    public void botaoEntrar(View view){
        startActivity(new Intent(this,
               LoginActivity.class));
    }

    //Navegar para a tela de Cadastro
    public void botaoCadastrar(View view){
        startActivity(new Intent(this,
                CadastroActivity.class));
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
       autenticacao.signOut();
        if (autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this,
                PrincipalActivity.class));
    }
}
