package com.AMDevelopers.myway;

import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Adapter extends BaseAdapter{
	
	ArrayList<MenuItem> Items;
	Activity Activity;
	LayoutInflater Inflater;

	public Adapter(ArrayList<MenuItem> items, Activity activity) {
		Items = items;
		Activity = activity;
		Inflater = activity.getLayoutInflater();
	}
	
	@Override
	public int getCount() {
		return Items.size();
	}

	@Override
	public Object getItem(int position) {
		return Items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout Item = (LinearLayout) Inflater.inflate(R.layout.activity_menuitem, parent, false);
		ImageView Icon = (ImageView) Item.findViewById(R.id.Icon);
		TextView IconLabel = (TextView) Item.findViewById(R.id.IconLabel);
		Icon.setBackgroundResource(Items.get(position).getIcon());
		IconLabel.setText(Items.get(position).getIconLabel());
		return Item;
	}

}
