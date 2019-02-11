package com.example.diego.financas.modelo;

import com.example.diego.financas.auxiliar.ConfigurandoData;
import com.example.diego.financas.auxiliar.CriptografiaBase64;
import com.example.diego.financas.configuracao.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private  double valor;

    public Movimentacao(){
    }

    public void salvar(String dataEscolhida){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = CriptografiaBase64.codificarBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = ConfigurandoData.mesAnoDataEscolhida(dataEscolhida);
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipo() { return tipo; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getValor() { return valor; }

    public void setValor(double valor) { this.valor = valor; }
}
