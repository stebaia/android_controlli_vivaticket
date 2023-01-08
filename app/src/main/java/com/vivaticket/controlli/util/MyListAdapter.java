package com.vivaticket.controlli.util;

import android.app.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vivaticket.controlli.R;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<SearchElement> {

    private Context context;
    private List<SearchElement> mySearchList;
    public MyListAdapter(Activity context, List<SearchElement> searchList) {
        super(context, R.layout.mylist, searchList);
        this.context = context;
        this.mySearchList = searchList;
    }

    public void swapData(List<SearchElement> newElement){
        this.mySearchList = newElement;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.mylist, null);
        }
        TextView ragioneSocialeAll = convertView.findViewById(R.id.ragioneSocialeAll);
        TextView ragioneSocialeEsp = convertView.findViewById(R.id.ragioneSocialeEsp);
        TextView padiglioneEsp = convertView.findViewById(R.id.padiglioneEsp);
        TextView standEsp = convertView.findViewById(R.id.standEsp);

        SearchElement searchElement = getItem(position);

        ragioneSocialeAll.setText(searchElement.getRagioneSocialeAll());
        ragioneSocialeEsp.setText(searchElement.getRagioneSocialeEsp());
        padiglioneEsp.setText(searchElement.getPadiglioneEsp());
        standEsp.setText(searchElement.getStandEsp());

        return convertView;

    };
}
