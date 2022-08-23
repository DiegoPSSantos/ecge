package br.com.ecge.ecgefoods.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import br.com.ecge.ecgefoods.adapter.PedidoAdapter;

/**
 * Created by diegosantos on 06/12/17.
 */

public class SwipeUtils extends ItemTouchHelper.SimpleCallback {

    private SwipeUtilListener listener;
    private int direcao;

    public interface SwipeUtilListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    public SwipeUtils(int dragDirs, int swipeDirs, SwipeUtilListener sulListener) {
        super(dragDirs, swipeDirs);
        listener = sulListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            getDefaultUIUtil().onSelected(getForegroundView(viewHolder));
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(c, recyclerView, getForegroundView(viewHolder), dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(getForegroundView(viewHolder));
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dX < 0.0f) {
            direcao = ItemTouchHelper.LEFT;
        }
        getDefaultUIUtil().onDraw(c, recyclerView, getForegroundView(viewHolder), dX, dY, actionState, isCurrentlyActive);
    }

    private final View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof PedidoAdapter.PedidosViewHolder) {
            setVisibilityBackground(viewHolder);
            return ((PedidoAdapter.PedidosViewHolder) viewHolder).foreground;
        }
        return null;
    }

    private void setVisibilityBackground(RecyclerView.ViewHolder viewHolder) {
        if (direcao == ItemTouchHelper.LEFT) {
            ((PedidoAdapter.PedidosViewHolder) viewHolder).background.setVisibility(View.VISIBLE);

        }
    }
}
