package com.example.diego.financas.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diego.financas.R;
import com.example.diego.financas.configuracao.ConfiguracaoFirebase;
import com.example.diego.financas.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editText_nome);
        campoEmail = findViewById(R.id.editText_email);
        campoSenha = findViewById(R.id.editText_senha);
        botaoCadastrar = findViewById(R.id.button_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recuperar o que o usuario digitou
                String txtNome = campoNome.getText().toString();
                String txtEmail = campoEmail.getText().toString();
                String txtSenha = campoSenha.getText().toString();

                //validar se os campos foram preenchidos
                if( !txtNome.isEmpty()){
                    if (!txtEmail.isEmpty()){
                        if(!txtSenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(txtNome);
                            usuario.setEmail(txtEmail);
                            usuario.setSenha(txtSenha);

                            cadastraUsuario();

                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha sua Senha!",Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,"Preencha seu Email!",Toast.LENGTH_LONG);
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha seu Nome!",Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void cadastraUsuario(){
        //recupera o objeto/instancia do Firebase que permite fazer autenticacao do usuario
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //cadastro do usuario
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar usuario! ",
                            Toast.LENGTH_LONG);
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Erro cadastrar usuario! ",
                            Toast.LENGTH_LONG);

                }
            }
        });
    }
}
