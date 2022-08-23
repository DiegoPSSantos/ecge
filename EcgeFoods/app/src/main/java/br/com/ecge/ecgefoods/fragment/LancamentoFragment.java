package br.com.ecge.ecgefoods.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.adapter.LancamentoAdapter;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.service.MesaService;
import br.com.ecge.ecgefoods.utils.InfoUtils;

public class LancamentoFragment extends BaseFragment {

    private DialogFragment categoriaDialog;
    private TextView selecioneMesa;
    private List<Mesa> mesas;
    private int indexMesaSelecionada;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private static LancamentoFragment fragment;
    public static final String TAG = "LancamentoFragment";

    @SuppressLint("ValidFragment")
    private LancamentoFragment() {}

    public static LancamentoFragment newInstance() {
        if (fragment == null)
            fragment = new LancamentoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoriaDialog = CategoriaDialogFragment.newInstance();
        View view = inflater.inflate(R.layout.fragment_lancamento, container, false);
        findViewsById(view);
        setRvMesa();
        setSrMesa();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskMesas();
    }

    private void findViewsById(View view) {
        selecioneMesa = view.findViewById(R.id.selecioneMesa);
        swipeRefresh = view.findViewById(R.id.swipeToRefreshMesa);
        recyclerView = view.findViewById(R.id.recyclerViewMesa);
    }

    private void setRvMesa() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setSrMesa() {
        swipeRefresh.setOnRefreshListener(onRefreshListener());
        swipeRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> taskMesas();
    }

    private void taskMesas() {
        startTask("lancamentos", new GetLancamentosTask());
    }

    private void taskMesa() { startTask("mesa", new GetMesaTask()); }

    private LancamentoAdapter.LancamentoOnClickListener onClickListener() {
        return (view, idx) -> {
            indexMesaSelecionada = idx;
            taskMesa();
        };
    }

    private Bundle getBundle(Mesa selecionada) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("mesa", Parcels.wrap(selecionada));
        return bundle;
    }

    private class GetLancamentosTask implements TaskListener<List<Mesa>> {

        @Override
        public List<Mesa> execute() throws Exception {
            mesas = MesaService.getMesas(getContext());
            return mesas;
        }

        @Override
        public void updateView(List<Mesa> mesas) {
            if (mesas != null && !mesas.isEmpty()) {
                recyclerView.setAdapter(new LancamentoAdapter(getContext(), mesas, onClickListener()));
                swipeRefresh.setRefreshing(false);
            } else {
                InfoUtils.longToast(getContext(), getContext().getString(R.string.mesas_vazias));
            }
        }

        @Override
        public void onError(Exception exception) {
            selecioneMesa.setVisibility(View.GONE);
            swipeRefresh.setRefreshing(false);
        }

        @Override
        public void onCancelled(String cod) {
            swipeRefresh.setRefreshing(false);
        }

    }

    private class GetMesaTask implements TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            Mesa selecionada = mesas.get(indexMesaSelecionada);
            return MesaService.getMesa(getContext(), Integer.parseInt(selecionada.getNumeroMesa()));
        }

        @Override
        public void updateView(Mesa mesa) {
            categoriaDialog.setArguments(getBundle(mesa));
            categoriaDialog.show(getActivity().getSupportFragmentManager(), CategoriaDialogFragment.TAG);
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }
}
