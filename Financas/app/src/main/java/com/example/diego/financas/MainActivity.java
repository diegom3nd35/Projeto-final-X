package com.example.diego.financas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {


    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); --> Desativando a tela principal

        //Deixando os botões invisiveis
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        //Slide 1
        addSlide(new FragmentSlide.Builder()
                .background(R.color.mi_icon_color_light)
                .fragment(R.layout.slide_1)
                .build()
        );
        //Slide 2
        addSlide(new FragmentSlide.Builder()
                .background(R.color.mi_icon_color_light)
                .fragment(R.layout.slide_2)
                .build()

        );
        
    }
}
