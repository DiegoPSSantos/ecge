package br.com.ecge.ecgefoods.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Produto;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private List<Produto> produtos;
    private static Context contexto;
    private static ProdutoOnClickListener produtoOnClickListener;

    public ProdutoAdapter(Context context, List<Produto> produtos, ProdutoOnClickListener onClickListener) {
        this.contexto = context;
        this.produtos = produtos;
        this.produtoOnClickListener = onClickListener;
    }
    @NonNull
    @Override
    public ProdutoAdapter.ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.adapter_produto, viewGroup, false);
        ProdutoViewHolder holder = new ProdutoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoAdapter.ProdutoViewHolder holder, int position) {
        Produto produto = this.produtos.get(position);
        holder.descricao.setText(produto.getNome());
        if (produtoOnClickListener != null) {
            holder.itemView.setOnClickListener(v -> produtoOnClickListener.onClickProduto(holder.itemView, position));
        }
    }

    @Override
    public int getItemCount() {
        return this.produtos != null ? this.produtos.size() : 0;
    }

    public interface ProdutoOnClickListener {
        void onClickProduto(View view, int idx);
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView descricao;
        CardView cardView;

        public ProdutoViewHolder(View view) {
            super(view);
            descricao = view.findViewById(R.id.descricaoProduto);
            cardView = view.findViewById(R.id.cvProduto);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

}
