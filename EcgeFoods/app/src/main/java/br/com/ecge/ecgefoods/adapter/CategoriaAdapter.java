package br.com.ecge.ecgefoods.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Categoria;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private final List<Categoria> categorias;
    private final Context contexto;
    private final CategoriaOnClickListener categoriaOnClickListener;

    public CategoriaAdapter(Context contexto, List<Categoria> categorias, CategoriaOnClickListener onClickListener) {
        this.contexto = contexto;
        this.categorias = categorias;
        this.categoriaOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.adapter_categoria, viewGroup, false);
        CategoriaViewHolder holder = new CategoriaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.descricao.setText(categoria.getNome());
        if (categoriaOnClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                categoriaOnClickListener.onClickCategoria(holder.itemView, position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.categorias != null ? this.categorias.size() : 0;
    }

    public interface CategoriaOnClickListener {
        void onClickCategoria(View view, int idx);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {

        TextView descricao;

        public CategoriaViewHolder(View view) {
            super(view);
            descricao = view.findViewById(R.id.descricaoCategoria);
        }
    }
}
