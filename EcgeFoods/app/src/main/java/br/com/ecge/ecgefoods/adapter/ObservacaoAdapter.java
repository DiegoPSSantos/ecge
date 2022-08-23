package br.com.ecge.ecgefoods.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Observacao;

public class ObservacaoAdapter extends RecyclerView.Adapter<ObservacaoAdapter.ObservacaoViewHolder> {

    private final List<Observacao> observacoes;
    private final Context contexto;
    private final ObservacaoOnClickListener observacaoOnClickListener;

    public ObservacaoAdapter(Context contexto, List<Observacao> observacoes, ObservacaoOnClickListener onClickListener) {
        this.contexto = contexto;
        this.observacoes = observacoes;
        this.observacaoOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ObservacaoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.adapter_observacao, viewGroup, false);
        ObservacaoViewHolder holder = new ObservacaoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ObservacaoViewHolder holder, int position) {
        Observacao observacao = observacoes.get(position);
        holder.checkBox.setText(observacao.getDescricao());
        if (observacaoOnClickListener != null) {
            holder.checkBox.setOnClickListener(v -> observacaoOnClickListener.onClickObservacao(holder.checkBox, position));
        }
    }

    @Override
    public int getItemCount() {
        return this.observacoes != null ? this.observacoes.size() : 0;
    }

    public interface ObservacaoOnClickListener {
        void onClickObservacao(View view, int idx);
    }

    public static class ObservacaoViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ObservacaoViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkObservacao);
            this.setIsRecyclable(false);
        }

    }
}
