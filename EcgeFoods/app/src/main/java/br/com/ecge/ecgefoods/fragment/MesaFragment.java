package br.com.ecge.ecgefoods.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.activity.ECGEFoodsActivity;
import br.com.ecge.ecgefoods.activity.PedidoActivity;
import br.com.ecge.ecgefoods.adapter.MesaAdapter;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.service.MesaService;
import br.com.ecge.ecgefoods.utils.InfoUtils;

public class MesaFragment extends BaseFragment {

    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private List<Mesa> mesas;
    private Mesa mesa;
    private int idxMesaCancelada;
    private final int CODE_CANCELAR = 1;
    private static MesaFragment fragment;
    public final static String STATUS_CANCELADO = "CANCELADA";
    public final static String STATUS_OCUPADA = "OCUPADA";
    public final static String TAG = "MesaFragment";


    public static MesaFragment newInstance() {
        if (fragment == null) {
            fragment = new MesaFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mesa, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        swipeRefresh = view.findViewById(R.id.swipeToRefresh);
        swipeRefresh.setOnRefreshListener(onRefreshListener());
        swipeRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        taskMesas();
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    private void taskMesa() { startTask("mesa", new GetMesaTask(), R.id.swipeToRefresh); }

    private void taskMesas() {
        startTask("mesas", new GetMesasTask(), R.id.swipeToRefresh);
    }

    private void taskCancelarMesa() { startTask("cancelar", new CancelarMesaTask(), R.id.swipeToRefresh); }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> taskMesas();
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private MesaOnClickListener clickListener;
        private GestureDetector gestureDetector;

        private RecyclerTouchListener(Context contexto, final RecyclerView recyclerView, final MesaOnClickListener clickListener) {
            this.clickListener = clickListener;
            this.gestureDetector = new GestureDetector(contexto, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        int idx = recyclerView.getChildAdapterPosition(child);
                        clickListener.onLongClickMesa(child, idx);
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                int idx = recyclerView.getChildAdapterPosition(child);
                try {
                    clickListener.onClickMesa(child, idx);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private RecyclerTouchListener getTouchListener() {
        return new RecyclerTouchListener(getContext(), recyclerView, new MesaOnClickListener() {
            @Override
            public void onClickMesa(View view, int idx) throws IOException {
                mesa = mesas.get(idx);
                taskMesa();
            }

            @Override
            public void onLongClickMesa(View view, int idx) {
                mesa = mesas.get(idx);
                InfoUtils.snack(view, getContext().getString(R.string.cancelar_mesa)).addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == CODE_CANCELAR) {
                            MesaFragment.this.idxMesaCancelada = idx;
                            apagarMesa();
                        }
                    }
                });
            }

            private void apagarMesa() {
                taskCancelarMesa();
            }
        });
    }

    private void atualizarTela() {
        recyclerView.getAdapter().notifyItemRemoved(idxMesaCancelada);
        Mesa mesaCancelada = Stream.of(mesas).filter(m -> m.getNumeroMesa().equalsIgnoreCase(mesa.getNumeroMesa())).single();
        mesas.remove(mesaCancelada);
        swipeRefresh.setRefreshing(false);
        InfoUtils.toast(getContext(), getString(R.string.mesa_cancelada));
    }

    public interface MesaOnClickListener {
        void onClickMesa(View view, int idx) throws IOException;
        void onLongClickMesa(View view, int idx);
    }

    private class GetMesaTask implements TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            return MesaService.getMesa(getContext(), Integer.parseInt(mesa.getNumeroMesa()));
        }

        @Override
        public void updateView(Mesa mesa) {
            Intent intent = new Intent(getContext(), PedidoActivity.class);
            intent.putExtra("mesa", Parcels.wrap(mesa));
            ActivityCompat.startActivity(getContext(), intent, null);
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private class GetMesasTask implements TaskListener<List<Mesa>> {

        @Override
        public List<Mesa> execute() throws Exception {
            List<Mesa> mesas = MesaService.getMesas(getContext());
            return mesas;
        }

        @Override
        public void updateView(List<Mesa> mesas) {
            if (mesas != null && !mesas.isEmpty()) {
                MesaFragment.this.mesas = mesas;
                recyclerView.setAdapter(new MesaAdapter(getContext(), mesas));
                recyclerView.addOnItemTouchListener(getTouchListener());
                swipeRefresh.setRefreshing(false);
            } else {
                recyclerView.setAdapter(new MesaAdapter(getContext(), new ArrayList<>()));
            }
        }

        @Override
        public void onError(Exception exception) {
            InfoUtils.longToast(getContext(), getContext().getString(R.string.lista_mesas_vazia));
            ((ECGEFoodsActivity)getActivity()).getFabNovaMesa().setVisibility(View.GONE);
            ((ECGEFoodsActivity)getActivity()).getPesquisa().setVisibility(View.GONE);
            swipeRefresh.setRefreshing(false);
            //((ECGEFoodsActivity)getActivity()).sair();
        }

        @Override
        public void onCancelled(String cod) {
            swipeRefresh.setRefreshing(false);
        }

    }

    private class CancelarMesaTask implements TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            Mesa mesaCancelada = MesaService.getMesa(getContext(), Integer.parseInt(mesa.getNumeroMesa()));
            mesaCancelada = setMesaCancelada(mesaCancelada);
            if (MesaService.cancelarMesaProduto(getContext(), mesaCancelada)) {
                return mesaCancelada;
            } else {
                return new Mesa();
            }
        }

        @Override
        public void updateView(Mesa mesa) {
            atualizarTela();
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }

        private Mesa setMesaCancelada(Mesa mesaCancelada) {
            mesaCancelada.setStatus(STATUS_CANCELADO);
            mesaCancelada.getReceita().getListaReceitaProdutoCancelado().clear();
            if (mesaCancelada.getReceita() != null) {
                mesaCancelada.getReceita().setStatus(STATUS_CANCELADO);
                mesaCancelada.getReceita().getListaReceitaProdutoCancelado().addAll(mesaCancelada.getReceita().getReceitaVenda().getListaReceitaVendaProduto());
                mesaCancelada.getReceita().getReceitaVenda().getListaReceitaVendaProduto().clear();
            }
            return mesaCancelada;
        }
    }

}
