package br.com.ecge.ecgefoods.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.activity.ECGEFoodsActivity;
import br.com.ecge.ecgefoods.adapter.ImpressoraAdapter;
import br.com.ecge.ecgefoods.domain.Impressora;
import br.com.ecge.ecgefoods.service.ImpressoraService;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.InfoUtils;

public class ImpressoraFragment extends BaseFragment {

    private View view;
    private List<Impressora> impressoras;
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private TextView selecioneImpressora;
    private int indexImpressoraSelecionada;
    private static ImpressoraFragment fragment;
    public static final String TAG = "ImpressoraFragment";


    public static ImpressoraFragment newInstance() {
        if (fragment == null) {
            fragment = new ImpressoraFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_impressora, container, false);
        findViewsById(view);
        setRvImpressora();
        setSrImpressora();
        return view;
    }

    private void findViewsById(View view) {
        selecioneImpressora = view.findViewById(R.id.selecioneImpressora);
        swipeRefresh = view.findViewById(R.id.swipeToRefreshImpressora);
        recyclerView = view.findViewById(R.id.recyclerViewImpressora);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskImpressoras();
    }

    private void taskImpressoras() {
        startTask("impressoras", new GetImpressorasTask());
    }

    private void setRvImpressora() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setSrImpressora() {
        swipeRefresh.setOnRefreshListener(onRefreshListener());
        swipeRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> taskImpressoras();
    }

    private class GetImpressorasTask implements TaskListener<List<Impressora>> {

        @Override
        public List<Impressora> execute() throws Exception {
            impressoras = ImpressoraService.getImpressoras(getContext());
            return impressoras;
        }

        @Override
        public void updateView(List<Impressora> impressoras) {
            if (impressoras != null && !impressoras.isEmpty()) {
                recyclerView.setAdapter(new ImpressoraAdapter(getContext(), impressoras, onClickListener()));
                swipeRefresh.setRefreshing(false);
            } else {
                InfoUtils.longToast(getContext(), getContext().getString(R.string.impressoras_nao_cadastradas));
            }
        }

        @Override
        public void onError(Exception exception) {
        }

        @Override
        public void onCancelled(String cod) {
            swipeRefresh.setRefreshing(false);
        }

    }

    private ImpressoraAdapter.ImpressoraOnClickListener onClickListener() {
        return (view, idx) -> {
            indexImpressoraSelecionada = idx;
            ECGEFoodsUtils.setImpressora(getContext(), this.impressoras.get(indexImpressoraSelecionada));
            Intent intent = new Intent(getContext(), ECGEFoodsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityOptionsCompat opts = ActivityOptionsCompat.makeCustomAnimation(getContext(), R.anim.slide_in_left, br.com.ecge.ecgefoods.R.anim.slide_out_left);
            ActivityCompat.startActivity(getContext(), intent, opts.toBundle());
            getActivity().finish();
        };
    }

}
