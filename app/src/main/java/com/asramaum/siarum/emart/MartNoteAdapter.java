package com.asramaum.siarum.emart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asramaum.siarum.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by austi on 9/26/2018.
 */

public class MartNoteAdapter extends FirestoreRecyclerAdapter<MartNote, MartNoteAdapter.MartNoteHolder> {

    private OnItemClickListener listener;
    private String priceValue;

    public MartNoteAdapter(@NonNull FirestoreRecyclerOptions<MartNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MartNoteHolder holder, int position, @NonNull MartNote model) {
        priceValue = "Rp. " + String.valueOf(model.getPrice());
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewPrice.setText(priceValue);
        holder.textViewStock.setText(String.valueOf(model.getStock()));
    }

    @Override
    public MartNoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent,false);
        return new MartNoteHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    //TODO decrease stock!
    /*public void decreaseStock(int position){
        int price = Integer.parseInt(editProductPrice.getText().toString().trim());
        int stock = Integer.parseInt(editProductStock.getText().toString().trim());
        getSnapshots().getSnapshot(position).getReference().update(new MartNote(name, price, stock));
    }*/

    class MartNoteHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewStock;

        public MartNoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewStock = itemView.findViewById(R.id.text_view_stock);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
