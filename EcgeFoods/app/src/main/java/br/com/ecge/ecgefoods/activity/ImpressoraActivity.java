package br.com.ecge.ecgefoods.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.fragment.ImpressoraFragment;

public class ImpressoraActivity extends BaseActivity {

    private ImpressoraFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressora);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpToolbar();
        setFragment(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_impressora, menu);
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

    private void setFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragment = ImpressoraFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.impressoraFragment, fragment, ImpressoraFragment.TAG).commit();
        }
    }
}
