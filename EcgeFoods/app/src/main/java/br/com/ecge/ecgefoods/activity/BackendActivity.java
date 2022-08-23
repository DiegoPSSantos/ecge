package br.com.ecge.ecgefoods.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.Button;
import android.widget.TextView;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Usuario;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.InfoUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class BackendActivity extends BaseActivity {

    private TextView backend;
    private Button salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewsById();
        if (ConfigUtils.getEndpoint(this).length() == 1) {
            salvar.setOnClickListener(v -> {
                if (ConfigUtils.verificarConexao(getContext())) {
                    String url = String.valueOf(backend.getText());
                    if (validarFormato(url)) {
                        String ip = StringUtils.getProtocoloIP(url);
                        String porta = StringUtils.getPorta(url);
                        ConfigUtils.setIPPortaBackend(this, ip, porta);
                        setNavegacao();
                    } else {
                        InfoUtils.toast(getBaseContext(), getString(R.string.url_invalida));
                    }
                } else {
                    InfoUtils.toast(getBaseContext(), getString(R.string.wifi_desabilitada));
                }

            });
        } else {
            setNavegacao();
        }
    }

    private void findViewsById() {
        backend = findViewById(R.id.backend);
        salvar = findViewById(R.id.btn_salvar);
    }

    private boolean validarFormato(String url) {
        if (!url.startsWith("http://")) {
            return false;
        }

        if (url.length() < 8) {
            return false;
        }

        return true;
    }

    private boolean verificarUsuarioLogado() {
        Usuario usuario = ECGEFoodsUtils.getUsuarioLogado(getContext());
        if (usuario.getId() != null && usuario.getId().intValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private void setNavegacao() {
        if (verificarUsuarioLogado()) {
            Intent intent = new Intent(getBaseContext(), ECGEFoodsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityOptionsCompat opts = ActivityOptionsCompat.makeCustomAnimation(getBaseContext(), R.anim.slide_in_left, br.com.ecge.ecgefoods.R.anim.slide_out_left);
            ActivityCompat.startActivity(getBaseContext(), intent, opts.toBundle());
        } else {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }

        finish();
    }
}
