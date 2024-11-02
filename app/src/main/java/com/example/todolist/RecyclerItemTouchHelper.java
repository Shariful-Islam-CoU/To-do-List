package com.example.todolist;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final ToDoAdapter adapter;

    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position =viewHolder.getAbsoluteAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder= new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setCancelable(false);
            builder.setMessage("Are you sure to delete this task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.deleteItem(position);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                }
            });
            AlertDialog dialog= builder.create();
            dialog.show();
        }
        else{
            adapter.editItem(position);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c,recyclerView,viewHolder,dx,dy,actionState,isCurrentlyActive);
        Drawable icon;
        ColorDrawable background;

        View itemView =viewHolder.itemView;
        int backgroundCornerOffset=20;
        if(dx>0){
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.edit);
            background=new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.lightGreen));
        }
        else{
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.delete);
            background=new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.red));
        }
        assert icon != null;
        int iconMargin=(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop=itemView.getTop()+(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconBottom=iconTop+icon.getIntrinsicHeight();

        if(dx>0){
            int iconLeft=itemView.getLeft()+iconMargin;
            int iconRight=itemView.getLeft()+iconMargin+icon.getIntrinsicHeight();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getLeft(),itemView.getTop(),itemView.getLeft()+((int)dx)+backgroundCornerOffset,itemView.getBottom());
        }
        else if(dx<0){
            int iconLeft=itemView.getRight()-iconMargin-icon.getIntrinsicHeight();
            int iconRight=itemView.getRight()-iconMargin;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getRight()+((int)dx)-backgroundCornerOffset,itemView.getTop(),itemView.getRight(),itemView.getBottom());
        }
        else{
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);

    }
}
