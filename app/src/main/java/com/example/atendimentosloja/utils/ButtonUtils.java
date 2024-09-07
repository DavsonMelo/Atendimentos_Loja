package com.example.atendimentosloja.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.atendimentosloja.R;

import java.util.List;

public class ButtonUtils {

    @SuppressLint("Range")
    public static Button createCustomButton(Context context, String nome, List<String> fila) {
        // Estilo padrão
        int strokeWidth = 5; // largura do stroke em pixels
        int strokeColor = ContextCompat.getColor(context, R.color.blue); // cor do stroke (pode ser qualquer cor)

        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setCornerRadius(15f);
        normalDrawable.setStroke(strokeWidth, strokeColor);
        normalDrawable.setColor(context.getResources().getColor(R.color.white));

        // Estilo quando presionado
        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
        pressedDrawable.setCornerRadius(30f);
        pressedDrawable.setColor(context.getResources().getColor(R.color.gray));
        pressedDrawable.setStroke(5, context.getResources().getColor(R.color.black));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        states.addState(new int[]{}, normalDrawable); // Estado padrão

                // Cria o botão
        Button button = new Button(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 26, 0, 26);
        button.setLayoutParams(params);

        Typeface minhaFonte = ResourcesCompat.getFont(context, R.font.roboto_flex1);


        button.setTextSize(20);
        button.setText(nome);
        button.setTypeface(minhaFonte);
        button.setBackground(states);

        PendenciaUtils.checkPendencias(context, nome, hasPendencia -> {
            if(hasPendencia){
                button.setTextColor(context.getResources().getColor(R.color.red));
            }
        });

        return button;
    }
}

