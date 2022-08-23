package br.com.ecge.ecgefoods.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Pedido;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidosViewHolder> {

    private final List<Pedido> pedidos;
    private final Context contexto;
    private PedidoOnClickListener pedidoOnClickListener;

    public PedidoAdapter(Context context, List<Pedido> pedidos, PedidoOnClickListener onClickListener) {
        this.contexto = context;
        this.pedidos = pedidos;
        this.pedidoOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public PedidoAdapter.PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.adapter_item_pedido, vg, false);
        PedidosViewHolder holder = new PedidosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.PedidosViewHolder holder, int position) {
        Pedido pedido = this.pedidos.get(position);
        holder.produto.setText(pedido.getProduto());
        if (pedido.getEnviadoProducao()) {
            holder.check.setImageResource(R.drawable.baseline_check_circle_black_18dp);
            holder.produto.setTypeface(holder.produto.getTypeface(), Typeface.BOLD_ITALIC);
            holder.adiciona.setVisibility(View.GONE);
            holder.diminui.setVisibility(View.GONE);
        } else {
            holder.produto.setTypeface(holder.produto.getTypeface(), Typeface.BOLD);
            holder.check.setImageResource(R.drawable.baseline_error_black_24dp);
        }
        holder.unidade.setText(pedido.getUnidadeMedida());
        holder.quantidade.setText(String.valueOf(pedido.getQuantidade()));
        holder.preco.setText(String.valueOf(pedido.getPreco().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
        pedido.setTotal(pedido.getTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        holder.totalProduto.setText(String.valueOf(pedido.getTotal()));
        if (pedidoOnClickListener != null) {
            holder.itemView.setOnClickListener(v -> pedidoOnClickListener.onClickPedido(holder.itemView, position));
//            holder.observacoes.setOnClickListener(v -> pedidoOnClickListener.exibirObservacoes(holder.observacoes, position));
            holder.adiciona.setOnClickListener(v -> pedidoOnClickListener.adicionarQuantidade(holder.quantidade, position));
            holder.diminui.setOnClickListener(v -> pedidoOnClickListener.diminuirQuantidade(holder.quantidade, position));
        }
    }

    @Override
    public int getItemCount() {
        return this.pedidos != null ? this.pedidos.size() : 0;
    }

    public interface PedidoOnClickListener {
        void onClickPedido(View view, int idx);
        void adicionarQuantidade(TextView view, int idx);
        void diminuirQuantidade(TextView view, int idx);
//        void exibirObservacoes(View view, int idx);
    }

    public static class PedidosViewHolder extends RecyclerView.ViewHolder {
        ImageView check;
        TextView produto;
        TextView unidade;
        TextView quantidade;
        TextView preco;
        TextView totalProduto;
        ImageButton adiciona;
        ImageButton diminui;
//        ImageButton observacoes;
        View view;
        public final RelativeLayout foreground, background;
        private PedidosViewHolder(View view) {
            super(view);
            check = view.findViewById(R.id.check);
            produto = view.findViewById(R.id.txtProduto);
            unidade = view.findViewById(R.id.txtUnidade);
            quantidade = view.findViewById(R.id.txtQuantidade);
            preco = view.findViewById(R.id.txtPreco);
            totalProduto = view.findViewById(R.id.txtTotalProduto);
//            observacoes = view.findViewById(R.id.btn_observacoes);
            adiciona = view.findViewById(R.id.btn_adiciona);
            diminui = view.findViewById(R.id.btn_diminui);
            foreground = view.findViewById(R.id.foreground);
            background = view.findViewById(R.id.background);
            this.view = view;
        }

    }

    public void restaurarPedido(Pedido pedido, int position) {
        pedidos.add(position, pedido);
        notifyItemInserted(position);
    }
}
