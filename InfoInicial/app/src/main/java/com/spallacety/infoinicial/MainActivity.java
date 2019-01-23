package com.spallacety.infoinicial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

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
                .fragment(R.layout.introducao1)
                .build()
        );
        //Slide 2
        addSlide(new FragmentSlide.Builder()
                .background(R.color.mi_icon_color_light)
                .fragment(R.layout.introducao2)
                .build()
        );
    }
}
