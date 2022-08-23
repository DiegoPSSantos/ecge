package br.com.ecge.ecgefoods.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.utils.ConfigUtils;

public class BaseActivity extends AppCompatActivity {

    protected Context getContext() {
        return this;
    }
    protected Toolbar toolbar;

    protected void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected void sair() {
        ConfigUtils.removerUsuario(getContext());
        ConfigUtils.removerImpressora(getContext());
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat opts = ActivityOptionsCompat.makeCustomAnimation(getBaseContext(), R.anim.slide_in_left, br.com.ecge.ecgefoods.R.anim.slide_out_left);
        ActivityCompat.startActivity(getBaseContext(), intent, opts.toBundle());
        finish();
    }

}
