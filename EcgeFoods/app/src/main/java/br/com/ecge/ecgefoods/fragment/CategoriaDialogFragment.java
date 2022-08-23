package br.com.ecge.ecgefoods.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.parceler.Parcels;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.adapter.CategoriaAdapter;
import br.com.ecge.ecgefoods.domain.Categoria;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.service.CategoriaService;
import br.com.ecge.ecgefoods.utils.InfoUtils;

public class CategoriaDialogFragment extends DialogFragment {

    private static CategoriaDialogFragment dialog;
    private DialogFragment produtoDiaglog;
    private List<Categoria> categorias;
    private Mesa mesa;
    private RecyclerView rvCategorias;
    private SwipeRefreshLayout srCategorias;
    private Button cancelar;
    public static final String TAG = "CategoriaDialogFragment";

    @SuppressLint("ValidFragment")
    private CategoriaDialogFragment() {
    }

    public static CategoriaDialogFragment newInstance() {
        if (dialog == null)
            dialog = new CategoriaDialogFragment();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mesa = Parcels.unwrap(getArguments().getParcelable("mesa"));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertStyle);
        View body = getActivity().getLayoutInflater().inflate(R.layout.dialog_categoria, null);
        View head = getActivity().getLayoutInflater().inflate(R.layout.head_dialog_categoria, null);
        findViewsById(body);
        setRvCategorias();
        setSrCategorias();
        setAcoesListeners();
        builder.setView(body);
        builder.setCustomTitle(head);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.borda_arredondada);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        taskCategorias();
    }

    private void findViewsById(View view) {
        rvCategorias = view.findViewById(R.id.recyclerViewCategoria);
        srCategorias = view.findViewById(R.id.swipeToRefreshCategoria);
        cancelar = view.findViewById(R.id.btnCancelarCategoria);
    }

    private void setRvCategorias() {
        rvCategorias.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCategorias.setItemAnimator(new DefaultItemAnimator());
        rvCategorias.setHasFixedSize(true);
        rvCategorias.setNestedScrollingEnabled(false);
    }

    private void setSrCategorias() {
        srCategorias.setOnRefreshListener(onRefreshListener());
        srCategorias.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> taskCategorias();
    }

    public void taskCategorias() {
        startTask("categorias", new GetCategoriasTask(), R.id.swipeToRefreshCategoria);
    }

    private void setAcoesListeners() {
        cancelar.setOnClickListener(cancelar());
    }

    private CategoriaAdapter.CategoriaOnClickListener onClickListener() {
        return (view, idx) -> {
          this.dismiss();
          Categoria categoria = categorias.get(idx);
          produtoDiaglog = new ProdutoDialogFragment();
          if (produtoDiaglog.getArguments() == null) {
              produtoDiaglog.setArguments(getBundle(categoria));
          } else {
              produtoDiaglog.getArguments().putAll(getBundle(categoria));
          }
          produtoDiaglog.show(getActivity().getSupportFragmentManager(), ProdutoDialogFragment.TAG);
        };
    }

    private Bundle getBundle(Categoria categoria) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("categoria", Parcels.wrap(categoria));
        bundle.putParcelable("mesa", Parcels.wrap(mesa));
        return bundle;
    }

    private class GetCategoriasTask implements TaskListener<List<Categoria>> {

        @Override
        public List<Categoria> execute() throws Exception {
            categorias = CategoriaService.getCategorias(getContext());
            return categorias;
        }

        @Override
        public void updateView(List<Categoria> categorias) {
            if (categorias != null && !categorias.isEmpty()) {
                rvCategorias.setAdapter(new CategoriaAdapter(getContext(), categorias, onClickListener()));
                srCategorias.setRefreshing(false);
            } else {
                InfoUtils.toast(getContext(), getContext().getString(R.string.mesas_sem_lancamento));
            }
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }
}
