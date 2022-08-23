package br.com.ecge.ecgefoods.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.adapter.MesaAdapter;
import br.com.ecge.ecgefoods.adapter.TabsAdapter;
import br.com.ecge.ecgefoods.fragment.MesaFragment;
import br.com.ecge.ecgefoods.fragment.NovaMesaDialogFragment;
import br.com.ecge.ecgefoods.utils.ConfigUtils;

public class ECGEFoodsActivity extends BaseActivity {

    private SearchView pesquisa;
    private ViewPager vp;
    private FloatingActionButton fabNovaMesa;

    public FloatingActionButton getFabNovaMesa() {
        return fabNovaMesa;
    }

    public SearchView getPesquisa() {
        return pesquisa;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecgefoods);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpToolbar();
        setupViewPagerTabs();
        setFabNovaMesa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        pesquisa = (SearchView) menu.findItem(R.id.pesquisar).getActionView();
        setSearchTextColour();
        pesquisar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sair) {
            sair();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFabNovaMesa() {
        fabNovaMesa = findViewById(R.id.fabNovaMesa);
        fabNovaMesa.setOnClickListener(exibirDialogNovaMesa());
    }

    private void setSearchTextColour() {
        EditText searchEditText = pesquisa.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.primary_text));
        searchEditText.setHintTextColor(getResources().getColor(R.color.primary_text));
    }

    private void pesquisar() {
        pesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String mesa) {
                if (fabNovaMesa.getVisibility() != View.GONE) {
                    MesaFragment mf = (MesaFragment) ((TabsAdapter) vp.getAdapter()).getItem(0);
                    ((MesaAdapter) mf.getRecyclerView().getAdapter()).filter(mesa);
                }
                return false;
            }
        });
    }

    private FloatingActionButton.OnClickListener exibirDialogNovaMesa() {
        return v -> {
            DialogFragment novaMesaDialog = NovaMesaDialogFragment.newInstance();
            novaMesaDialog.show(getSupportFragmentManager(), NovaMesaDialogFragment.TAG);
        };
    }

    private void setupViewPagerTabs() {
        vp = findViewById(R.id.viewPager);
        vp.setAdapter(new TabsAdapter(getContext(), getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp);
        int naoSelecionado = ContextCompat.getColor(getContext(), R.color.mint_cream);
        int selecionado = ContextCompat.getColor(getContext(), R.color.detalhe_mesa);
        tabLayout.setTabTextColors(naoSelecionado, selecionado);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.detalhe_mesa));
        tabLayout.setSelectedTabIndicatorHeight(10);
    }

}
