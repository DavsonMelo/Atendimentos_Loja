package com.example.atendimentosloja.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.atendimentosloja.R;

public class Utils {

    // Método estático para mostrar o Toast personalizado usando um Context
    public static void showToast(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        ImageView toastImage = layout.findViewById(R.id.toast_image);
        toastImage.setImageResource(R.drawable.ic_alert); // Substitua pelo seu ícone desejado

        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    // Método estático sobrecarregado para mostrar o Toast personalizado usando um Fragment
    public static void showToast(Fragment fragment, String message) {
        if (fragment != null && fragment.isAdded()) {
            showToast(fragment.requireContext(), message);
        }
    }
}
