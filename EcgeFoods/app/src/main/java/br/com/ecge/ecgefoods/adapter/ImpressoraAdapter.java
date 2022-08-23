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
import br.com.ecge.ecgefoods.domain.Impressora;

public class ImpressoraAdapter extends RecyclerView.Adapter<ImpressoraAdapter.ImpressoraViewHolder> {

    private List<Impressora> impressoras;
    private final Context contexto;
    private final ImpressoraOnClickListener impressoraOnClickListener;

    public ImpressoraAdapter(Context context, List<Impressora> impressoras, ImpressoraOnClickListener onClickListener) {
        this.impressoras = impressoras;
        this.contexto = context;
        this.impressoraOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ImpressoraAdapter.ImpressoraViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.adapter_impressora, viewGroup, false);
        ImpressoraViewHolder holder = new ImpressoraViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImpressoraAdapter.ImpressoraViewHolder holder, final int position) {
        Impressora impressora = impressoras.get(position);
        holder.nome.setText(impressora.getDescricao());
        holder.cardView.setBackground(ContextCompat.getDrawable(contexto, R.drawable.gradient_gray));
        if (impressoraOnClickListener != null) {
            holder.itemView.setOnClickListener(v -> impressoraOnClickListener.onClickImpressora(holder.itemView, position));
        }
    }

    @Override
    public int getItemCount() {
        return this.impressoras != null ? this.impressoras.size() : 0;
    }

    public interface ImpressoraOnClickListener {
        void onClickImpressora(View view, int idx);
    }

    public static class ImpressoraViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        CardView cardView;
        View view;

        private ImpressoraViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.tvNomeImpressora);
            cardView = view.findViewById(R.id.cvImpressora);
            this.view = view;
        }
    }


}
