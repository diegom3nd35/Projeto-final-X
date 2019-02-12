package com.example.diego.financas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.diego.financas.R;
import com.example.diego.financas.adapter.AdapterMovimentacao;
import com.example.diego.financas.auxiliar.CriptografiaBase64;
import com.example.diego.financas.configuracao.ConfiguracaoFirebase;
import com.example.diego.financas.modelo.Movimentacao;
import com.example.diego.financas.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaldacao, textoSaldo;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseReferencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioReferencia;
    private ValueEventListener valueEventListenerUsuario;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textoSaldo = findViewById(R.id.textView_saldo);
        textoSaldacao = findViewById(R.id.textView1_saldacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recycler_movimentacao);
        configuraCalendarView();

        // Configurando adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);

        // configurando RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);



    }

    protected void onStart(){
        super.onStart();
        recuperarResumo();
    }

    public void recuperarResumo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = CriptografiaBase64.codificarBase64(emailUsuario);
        usuarioReferencia = firebaseReferencia.child("usuarios").child(idUsuario);
        Log.i("Evento", "Evento foi adicionado");
        valueEventListenerUsuario = usuarioReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue( Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String resultadoFormatado = decimalFormat.format(resumoUsuario);
                textoSaldacao.setText("Oi, " + usuario.getNome());
                textoSaldo.setText("R$ " + resultadoFormatado);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sair :
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(this,
                DespesaActivity.class));
    }

    public void adicionarReceita(View view){
    startActivity(new Intent(this,
            ReceitaActivity.class));
    }

    public void configuraCalendarView() {

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });

    }

    protected  void onStop(){
        super.onStop();
        Log.i("Evento","Evento foi removido!");
        usuarioReferencia.removeEventListener(valueEventListenerUsuario);
    }
}
