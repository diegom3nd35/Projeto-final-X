package com.example.diego.financas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.diego.financas.Activity.CadastroActivity;
import com.example.diego.financas.Activity.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity
{

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Navegar pra tela de Login
    public void botaoEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    //Navegar para a tela de Cadastro
    public void botaoCadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

}
