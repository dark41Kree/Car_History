package com.car.car_history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class CarHistoryAdapter extends ArrayAdapter<CarHistory> {

    public CarHistoryAdapter(Context context, List<CarHistory> carHistoryList) {
        super(context, 0, carHistoryList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarHistory carHistory = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_element_history_view, parent, false);
        }

        TextView textType = convertView.findViewById(R.id.textType);
        TextView textDate = convertView.findViewById(R.id.textDate);
        TextView textDescription = convertView.findViewById(R.id.textDescription);

        textType.setText("Type: " + carHistory.getType());
        textDate.setText("Date: " + carHistory.getDate());
        textDescription.setText("Description: " + carHistory.getDescription());

        return convertView;
    }
}
