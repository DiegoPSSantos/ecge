package br.com.ecge.ecgefoods.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.activity.PedidoActivity;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class MesaAdapter extends RecyclerView.Adapter<MesaAdapter.MesaViewHolder> {

    private List<Mesa> mesas;
    private List<Mesa> mesasFiltradas;
    private final Context contexto;

    public MesaAdapter(Context context, List<Mesa>mesas) {
        this.mesas = mesas;
        this.contexto = context;
    }

    @NonNull
    @Override
    public MesaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.adapter_mesa, viewGroup, false);
        MesaViewHolder holder = new MesaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MesaViewHolder holder, final int position) {
        Mesa mesa = mesas.get(position);
        holder.numero.setText(mesa.getNumeroMesa());
        if (mesa.getStatus().equalsIgnoreCase(PedidoActivity.STATUS_FECHAR_CONTA)) {
           holder.img.setImageResource(R.drawable.conta);
        } else {
            holder.img.setImageResource(R.drawable.mesa);
        }
        holder.cardView.setBackground(ContextCompat.getDrawable(contexto, R.drawable.gradient_gray));
    }

    @Override
    public int getItemCount() {
        return this.mesas != null ? this.mesas.size() : 0;
    }

    public static class MesaViewHolder extends RecyclerView.ViewHolder {
        public TextView numero;
        ImageView img;
        CardView cardView;


        public MesaViewHolder(View view) {
            super(view);
            numero = view.findViewById(R.id.numero);
            img = view.findViewById(R.id.imgMesa);
            cardView = view.findViewById(R.id.cvMesa);
        }
    }

    public void filter(String numeroMesa) {
        if (!numeroMesa.equalsIgnoreCase(StringUtils.VAZIO) && this.mesasFiltradas == null) {
            List<Mesa> temp = Stream.of(this.mesas).filter(mesa -> mesa.getNumeroMesa().equalsIgnoreCase(numeroMesa)).toList();
            if (temp.size() > 0) {
                this.mesasFiltradas = this.mesas;
                this.mesas = temp;
            } else {
                this.mesas = this.mesasFiltradas;
            }
        } else if (numeroMesa.equalsIgnoreCase(StringUtils.VAZIO) && this.mesasFiltradas != null){
            this.mesas = this.mesasFiltradas;
        } else if (this.mesasFiltradas != null) {
            this.mesas = Stream.of(this.mesasFiltradas).filter(mesa -> mesa.getNumeroMesa().equalsIgnoreCase(numeroMesa)).toList();
        }

        notifyDataSetChanged();
    }

}
