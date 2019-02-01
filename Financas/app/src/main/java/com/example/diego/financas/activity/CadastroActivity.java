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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

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

                //Tratamento de erros para o cadastro
                if( !txtNome.isEmpty()){
                    if (!txtEmail.isEmpty()){
                        if(!txtSenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(txtNome);
                            usuario.setEmail(txtEmail);
                            usuario.setSenha(txtSenha);

                            cadastraUsuario();

                        }else{
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha sua Senha!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,
                                "Preencha seu Email!",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Preencha seu Nome!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cadastraUsuario(){
        //recupera o objeto do Firebase que permite fazer autenticacao do usuario
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //cadastro do usuario
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish(); //finalizando tela
                }else{

                    // Capturando erro de exceçoes
                    String excecao = "";
                    try{
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Senha fraca, adicione mais caractere";
                    }catch( FirebaseAuthInvalidCredentialsException e){
                        excecao ="Por favor, digite um email valido";
                    }catch(FirebaseAuthUserCollisionException e){
                        excecao = "Este email ja foi cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar Usuario" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

