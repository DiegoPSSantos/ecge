package br.com.ecge.ecgefoods.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.annimon.stream.Stream;

import org.parceler.Parcels;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.domain.Pedido;
import br.com.ecge.ecgefoods.fragment.CategoriaDialogFragment;
import br.com.ecge.ecgefoods.fragment.DialogFragment;
import br.com.ecge.ecgefoods.fragment.PedidoFragment;
import br.com.ecge.ecgefoods.utils.InfoUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class PedidoActivity extends BaseActivity {

    private DialogFragment categoriaDialog;
    private PedidoFragment pf;
    private BottomNavigationView bnv;
    private List<Pedido> pedidos;
    private Mesa mesa;
    public static final String STATUS_FECHAR_CONTA = "PEDIDO_CONTA";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retorno = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                atualizarMesaPedidos();
                if (existeItemNaoEnviado(pedidos)) {
                    InfoUtils.longToast(getContext(), getString(R.string.pedido_pendente));
                    break;
                } else {
                    Intent intent = new Intent(getBaseContext(), ECGEFoodsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ActivityCompat.startActivity(getBaseContext(), intent, null);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    retorno = false;
                    break;
                }
        }
        return retorno;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpToolbar();
        mesa = Parcels.unwrap(getIntent().getParcelableExtra("mesa"));
        getSupportActionBar().setTitle(StringUtils.VAZIO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            pf = PedidoFragment.newInstance();
            pf.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.pedidoFragment, pf, PedidoFragment.TAG).commit();
        }
        bnv = findViewById(R.id.bnvAcoesMesa);
        bnv.setOnNavigationItemSelectedListener(acoesListener());
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        categoriaDialog = CategoriaDialogFragment.newInstance();
        return super.onCreateView(parent, name, context, attrs);
    }

    public BottomNavigationView getBnv() {
        return bnv;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener acoesListener() {
        return item -> {
            switch (item.getItemId()) {
                case R.id.btn_enviar_pedido: enviarPedido();
                    break;
                case R.id.btn_fechar_conta: fecharConta();
                    break;
                case R.id.btn_produtos: exibirCategorias();
                    break;
            }
            return true;
        };
    }

    private void exibirCategorias() {
        atualizarMesaPedidos();
        mesa.setPedidos(pedidos);
        categoriaDialog.setArguments(getBundle());
        categoriaDialog.show(getSupportFragmentManager(), CategoriaDialogFragment.TAG);
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("mesa", Parcels.wrap(mesa));
        return bundle;
    }

    private void enviarPedido() {
        atualizarMesaPedidos();
        if (existeItemNaoEnviado(pedidos)) {
            pf.taskEnviarPedido();
            pf.taskMesaPedidos();
        } else {
            InfoUtils.toast(getContext(), getString(R.string.todos_pedidos_enviados));
        }
    }

    public boolean existeItemNaoEnviado(List<Pedido> pedidos) {
        return Stream.of(pedidos).anyMatch(pedido -> !pedido.getEnviadoProducao());
    }

    private void fecharConta() {
        atualizarMesaPedidos();
        if (mesa.getStatus() != null && !existeItemNaoEnviado(pedidos)) {
            if (!mesa.getStatus().equalsIgnoreCase(STATUS_FECHAR_CONTA)) {
                mesa.setStatus(STATUS_FECHAR_CONTA);
                pf.taskFecharConta();
            } else {
                InfoUtils.toast(getContext(), getString(R.string.encerramento_conta_solicitado));
            }
        } else {
            InfoUtils.longToast(getContext(), getString(R.string.enviar_mesa_backend));
        }
    }

    private void atualizarMesaPedidos() {
        pedidos = pf.getPedidos();
        mesa = pf.getMesa();
    }
}
