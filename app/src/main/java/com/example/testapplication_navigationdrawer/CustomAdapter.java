package com.example.testapplication_navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> phoneNumbers;
    int image;

    LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<String> phoneNumbers, int image) {
        this.context = context;
        this.phoneNumbers = phoneNumbers;
        this.image = image;
    }

    @Override
    public int getCount() {
        return phoneNumbers.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneNumbers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_adapter, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.countryFlagImageView);
        TextView textView = convertView.findViewById(R.id.countryNameTextView);

        imageView.setImageResource(image);
        textView.setText(phoneNumbers.get(position));

        return convertView;
    }
}
