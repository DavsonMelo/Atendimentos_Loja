package com.example.atendimentosloja.formatters;

import com.github.mikephil.charting.formatter.ValueFormatter;


public class PercentFormatter extends ValueFormatter{
    @Override
    public String getFormattedValue(float value) {
        // Formata o valor sem casas decimais e adiciona o símbolo de porcentagem
        return String.format("%.0f%%", value);
    }
}
