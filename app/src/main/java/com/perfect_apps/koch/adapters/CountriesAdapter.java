package com.perfect_apps.koch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.perfect_apps.koch.R;
import com.perfect_apps.koch.models.Countries;

import java.util.List;


/**
 * Created by mostafa on 20/06/16.
 */
public class CountriesAdapter extends ArrayAdapter {

    private Context mContext;
    private List<Countries> mDataset;
    LayoutInflater inflater;

    /*************  TeachersListAdapter Constructor *****************/
    public CountriesAdapter(
            Context mContext,
            int textViewResourceId,
            List<Countries> mDataset
    )
    {
        super(mContext, textViewResourceId, mDataset);

        /********** Take passed values **********/
        this.mContext = mContext;
        this.mDataset = mDataset;

        // for add fake item at first to make as title
        this.mDataset.add(0, null);
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView label = (TextView)row.findViewById(R.id.label);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");
        label.setTypeface(font);


        if(position==0){

            // Default selected Spinner item
            label.setText(mContext.getString(R.string.country1));
            label.setTextColor(Color.DKGRAY);
        }
        else
        {
            label.setText(mDataset.get(position).getName());
            label.setTextColor(Color.BLACK);
        }

        return row;
    }
}