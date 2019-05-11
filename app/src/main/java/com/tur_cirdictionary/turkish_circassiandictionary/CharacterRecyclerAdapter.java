package com.tur_cirdictionary.turkish_circassiandictionary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CharacterRecyclerAdapter extends RecyclerView.Adapter<CharacterRecyclerAdapter.MyViewHolder> {
    private String[] specialCharacters;
    private OnCharacterListener onCharacterListener;

    public CharacterRecyclerAdapter(String[] specialCharacters, OnCharacterListener onCharacterListener) {
        this.specialCharacters = specialCharacters;
        this.onCharacterListener = onCharacterListener;
    }

    @Override
    public CharacterRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button btn_specialCharacter = (Button) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.special_character_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(btn_specialCharacter, onCharacterListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.button.setText(specialCharacters[position]);
    }

    @Override
    public int getItemCount() {
        return specialCharacters.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button button;
        private OnCharacterListener onCharacterListener;

        public MyViewHolder(Button button, OnCharacterListener onCharacterListener) {
            super(button);
            this.button = button;
            this.onCharacterListener = onCharacterListener;
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCharacterListener.onCharacterClick(getAdapterPosition());
        }
    }

    public interface OnCharacterListener {
        void onCharacterClick(int position);
    }

}
