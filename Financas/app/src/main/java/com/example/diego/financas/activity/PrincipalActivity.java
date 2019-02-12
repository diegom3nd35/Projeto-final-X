package com.example.diego.financas.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;
    private DatabaseReference movimentacaoReferencia;
    private String mesAnoSelecionado;


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
        swipe();

        // Configurando adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);

        // configurando RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFalgs = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFalgs = ItemTouchHelper.START|ItemTouchHelper.END;
                return makeMovementFlags(dragFalgs,swipeFalgs);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                excluirmovimetacao(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirmovimetacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Movimentacao da Conta");
        alertDialog.setMessage("Você tem certeza?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = CriptografiaBase64.codificarBase64(emailUsuario);
                movimentacaoReferencia = firebaseReferencia.child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                
                movimentacaoReferencia.child(movimentacao.getChave()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);

            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void recuperaMovimentacoes() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = CriptografiaBase64.codificarBase64(emailUsuario);
        movimentacaoReferencia = firebaseReferencia.child("movimentacao")
                                                    .child(idUsuario)
                                                    .child(mesAnoSelecionado);
            valueEventListenerMovimentacoes = movimentacaoReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear();
                for(DataSnapshot dados:dataSnapshot.getChildren()){

                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setChave(dados.getKey());
                    movimentacoes.add(movimentacao);
                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d",(dataAtual.getMonth()+1)); // salvando o mes no formato de dois digito pois no firebase acabei salvando assim.
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d",(date.getMonth()+1)); // salvando o mes no formato de dois digito pois no firebase acabei salvando assim.
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                movimentacaoReferencia.removeEventListener(valueEventListenerMovimentacoes);
                recuperaMovimentacoes();
            }
        });

    }

    protected void onStart(){
        super.onStart();
        recuperarResumo();
        recuperaMovimentacoes();
    }
    protected  void onStop(){
        super.onStop();
        Log.i("Evento","Evento foi removido!");
        usuarioReferencia.removeEventListener(valueEventListenerUsuario);
        movimentacaoReferencia.removeEventListener(valueEventListenerMovimentacoes);
    }
}
