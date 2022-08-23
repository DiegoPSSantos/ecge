package br.com.ecge.ecgefoods.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.activity.PedidoActivity;
import br.com.ecge.ecgefoods.domain.Mesa;

public class LancamentoAdapter extends RecyclerView.Adapter<LancamentoAdapter.LancamentoViewHolder> {

    private final List<Mesa> mesas;
    private final Context contexto;
    private final LancamentoOnClickListener lancamentoOnClickListener;

    public LancamentoAdapter(Context contexto, List<Mesa> mesas, LancamentoOnClickListener onClickListener) {
        this.contexto = contexto;
        this.mesas = mesas;
        this.lancamentoOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public LancamentoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.adapter_lancamento, viewGroup, false);
        LancamentoViewHolder holder = new LancamentoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LancamentoAdapter.LancamentoViewHolder holder, int position) {
        Mesa mesa = mesas.get(position);
        holder.lancamento.setText(mesa.getNumeroMesa());
        if (mesa.getStatus().equalsIgnoreCase(PedidoActivity.STATUS_FECHAR_CONTA)) {
            holder.cardView.setBackground(ContextCompat.getDrawable(contexto, R.drawable.gradient_conta));
        } else {
            holder.cardView.setBackground(ContextCompat.getDrawable(contexto, R.drawable.gradient_gray));
        }
        if (lancamentoOnClickListener != null) {
            holder.itemView.setOnClickListener(v -> lancamentoOnClickListener.onClickLancamento(holder.itemView, position));
        }

    }

    @Override
    public int getItemCount() {
        return this.mesas != null ? this.mesas.size() : 0;
    }

    public interface LancamentoOnClickListener {
        void onClickLancamento(View view, int idx);
    }

    public static class LancamentoViewHolder extends RecyclerView.ViewHolder {

        TextView lancamento;
        CardView cardView;

        public LancamentoViewHolder(View view) {
            super(view);
            lancamento = view.findViewById(R.id.tvNumeroLancamento);
            cardView = view.findViewById(R.id.cvLancamento);
        }
    }
}
