package com.example.atendimentosloja;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.atendimentosloja.adapters.ViewPageAdapter;
import com.example.atendimentosloja.databinding.ActivityMainBinding;
import com.example.atendimentosloja.fragments.FilaFragment;
import com.example.atendimentosloja.fragments.IndicadoresFragment;
import com.example.atendimentosloja.fragments.VendedorasFragment;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // A linha abaixo modifica a cor do Status bar.
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.black));

        configTablayout();
    }
    private void configTablayout() {
        ViewPageAdapter adapter = new ViewPageAdapter(this);
        binding.viewPager.setAdapter(adapter);

        adapter.addFragment(new FilaFragment(), "Fila");
        adapter.addFragment(new VendedorasFragment(), "Vendedoras");
        adapter.addFragment(new IndicadoresFragment(), "Indicadores");

        binding.viewPager.setOffscreenPageLimit(adapter.getItemCount());

        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabs, binding.viewPager, (tab, position) -> {
            tab.setText(adapter.getTitle(position));
        });

        mediator.attach();

    }

}