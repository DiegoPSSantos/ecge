package br.com.ecge.ecgefoods.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.parceler.Parcels;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.service.MesaService;
import br.com.ecge.ecgefoods.utils.InfoUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class NovaMesaDialogFragment extends DialogFragment {

    private EditText numeroMesa;
    private Button salvar;
    private Button cancelar;
    private DialogFragment categoriaDialog;
    private static NovaMesaDialogFragment dialog;
    public static final String TAG = "NovaMesaDialogFragment";

    @SuppressLint("ValidFragment")
    private NovaMesaDialogFragment(){};

    public static NovaMesaDialogFragment newInstance() {
        if (dialog == null)
            dialog = new NovaMesaDialogFragment();
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertStyle);
        View body = getActivity().getLayoutInflater().inflate(R.layout.dialog_nova_mesa,null);
        View head = getActivity().getLayoutInflater().inflate(R.layout.head_dialog_nova_mesa,null);
        findViewsById(body);
        setAcoesListeners();
        builder.setView(body);
        builder.setCustomTitle(head);
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.borda_arredondada);
        return dialog;
//        builder.setPositiveButton(getString(R.string.salvar), (dialogInterface, i) -> {
//            if (numeroMesa.getText() != null) {
//                if (validarQuantidade()) {
//                    quantidadeListener.onSetQuantidade(editTextQuantidade.getText().toString(), nomeProduto, tietNumeroMesa.getText().toString());

//                }
//            }
//            this.dismiss();

//        });
//        builder.setNegativeButton(getString(R.string.cancelar), (dialogInterface, i) -> {
//            PedidoFragment fragment = (PedidoFragment) getActivity().getSupportFragmentManager().findFragmentByTag(PedidoFragment.TAG);
//            fragment.desfazerExclusao(pedido, index);
//            this.dismiss();
//        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoriaDialog = CategoriaDialogFragment.newInstance();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void findViewsById(View v) {
        numeroMesa = v.findViewById(R.id.edtNumeroMesa);
        salvar = v.findViewById(R.id.btnSalvarNrMesa);
        cancelar = v.findViewById(R.id.btnCancelarNrMesa);
    }

    private void setAcoesListeners() {
        salvar.setOnClickListener(salvar());
        cancelar.setOnClickListener(cancelar());
    }

    private Button.OnClickListener salvar() {
        return v -> {
            if (numeroMesa.getText() != null && !numeroMesa.getText().toString().equalsIgnoreCase(StringUtils.VAZIO)) {
                verificarMesaExiste();
            } else {
                InfoUtils.toast(getContext(), getString(R.string.numero_nova_mesa_obrigatorio));
            }
        };
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("mesa", Parcels.wrap(getNovaMesa()));
        return bundle;
    }

    private Mesa getNovaMesa() {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(numeroMesa.getText().toString());
        return mesa;
    }

    private void verificarMesaExiste() {
        startTask("mesa", new MesaTask());
    }

    private class MesaTask implements DialogFragment.TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            return MesaService.getMesa(getContext(), Integer.parseInt(numeroMesa.getText().toString()));
        }

        @Override
        public void updateView(Mesa mesa) {
            if (mesa == null || mesa.getId() == null) {
                dismiss();
                categoriaDialog.setArguments(getBundle());
                categoriaDialog.show(getActivity().getSupportFragmentManager(), CategoriaDialogFragment.TAG);
            } else {
                InfoUtils.toast(getContext(), getString(R.string.mesa_existente));
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
