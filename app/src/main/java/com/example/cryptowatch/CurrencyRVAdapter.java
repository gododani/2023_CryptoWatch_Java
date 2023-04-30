package com.example.cryptowatch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder> {

    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CurrencyRVAdapter(ArrayList<CurrencyRVModal> currencyRVModalArrayList, Context context) {
        this.currencyRVModalArrayList = currencyRVModalArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<CurrencyRVModal> filteredList){
        currencyRVModalArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item, parent,false);
        return new CurrencyRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.ViewHolder holder, int position) {
        CurrencyRVModal currencyRVModal = currencyRVModalArrayList.get(position);
        holder.currencyNameTV.setText(currencyRVModal.getName());
        holder.symbolTV.setText(currencyRVModal.getSymbol());
        holder.rateTV.setText("$" + df2.format(currencyRVModal.getPrice()));
        holder.hourTV.setText(df2.format(currencyRVModal.getHourChange()) + "%");
        holder.dayTV.setText(df2.format(currencyRVModal.getDayChange()) + "%");
        holder.weekTV.setText(df2.format(currencyRVModal.getWeekChange()) + "%");

        // get the color based on data
        int hourColor = getColorForChange(currencyRVModal.getHourChange());
        int dayColor = getColorForChange(currencyRVModal.getDayChange());
        int weekColor = getColorForChange(currencyRVModal.getWeekChange());

        // Set the color on elements
        holder.hourTV.setTextColor(hourColor);
        holder.dayTV.setTextColor(dayColor);
        holder.weekTV.setTextColor(weekColor);
    }

    private int getColorForChange(double change) {
        if (change > 0) {
            return Color.GREEN; // positive change, use green color
        } else if (change < 0) {
            return Color.RED; // negative change, use red color
        } else {
            return Color.WHITE; // no change, use white color
        }
    }

    @Override
    public int getItemCount() {
        return currencyRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView currencyNameTV, symbolTV, rateTV, hourTV, dayTV, weekTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyNameTV = itemView.findViewById(R.id.idTvCurrencyName);
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            rateTV = itemView.findViewById(R.id.idTVCurrencyRate);
            hourTV = itemView.findViewById(R.id.idTvHourChangeValue);
            dayTV = itemView.findViewById(R.id.idTvDayChangeValue);
            weekTV = itemView.findViewById(R.id.idTvWeekChangeValue);
        }
    }
}
