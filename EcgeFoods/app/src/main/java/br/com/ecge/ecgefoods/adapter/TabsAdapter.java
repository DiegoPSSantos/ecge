package br.com.ecge.ecgefoods.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.fragment.LancamentoFragment;
import br.com.ecge.ecgefoods.fragment.MesaFragment;

public class TabsAdapter extends FragmentPagerAdapter {

    private Context contexto;
    private Fragment fragment;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.contexto = context;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence titulo = "";
        switch (position) {
            case 0: titulo = contexto.getString(R.string.label_mesas).toUpperCase();
                break;
            case 1: titulo = contexto.getString(R.string.label_lancamento).toUpperCase();
                break;
        }
        return titulo;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: fragment = MesaFragment.newInstance();
                break;
            case 1: fragment = LancamentoFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}