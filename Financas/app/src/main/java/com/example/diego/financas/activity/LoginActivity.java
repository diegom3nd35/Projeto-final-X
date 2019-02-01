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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editText_email);
        campoSenha = findViewById(R.id.editText_senha);
        botaoEntrar = findViewById(R.id.button_entrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtEmail = campoEmail.getText().toString();
                String txtSenha = campoSenha.getText().toString();

                // Tratamento de erro no login
                if (!txtEmail.isEmpty()){
                    if(!txtSenha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(txtEmail);
                        usuario.setSenha(txtSenha);
                        validarLogin();

                    }else{
                        Toast.makeText(LoginActivity.this,
                                "Preencha sua Senha!",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Preencha seu Email!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void validarLogin(){

        autenticao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        // login do usuario
        autenticao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() ){
                    
                    //Toast.makeText(LoginActivity.this,
                      //      "Sucesso ao fazer login",
                        //    Toast.LENGTH_LONG).show();
                }else{

                    //Capturando erro de exceções
                    String excecao = "";
                    try{
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e){
                        excecao = "Usuario não esta cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email ou senha esta errado";
                    } catch (Exception e){
                        excecao = "Erro ao cadastrar Usuario" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
