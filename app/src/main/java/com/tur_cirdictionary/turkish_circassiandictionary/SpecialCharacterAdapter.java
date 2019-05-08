package com.tur_cirdictionary.turkish_circassiandictionary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

public class SpecialCharacterAdapter extends RecyclerView.Adapter<SpecialCharacterAdapter.MyViewHolder> {
    private String[] specialCharacters;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Button button;
        public MyViewHolder(Button button) {
            super(button);
            this.button = button;
        }
    }

    public SpecialCharacterAdapter(String[] specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    @Override
    public SpecialCharacterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button btn_specialCharacter = (Button) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.special_character_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(btn_specialCharacter);
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
}
