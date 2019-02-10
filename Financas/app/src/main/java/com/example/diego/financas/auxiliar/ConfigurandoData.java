package com.example.diego.financas.auxiliar;

import java.text.SimpleDateFormat;

public class ConfigurandoData {

    public static String dataAtual(){

       long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy");
        String dataString = simpleDateFormat.format(data);

        return dataString;
    }
}
