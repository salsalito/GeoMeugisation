package com.meuge.adapter;

import java.util.List;

import com.meuge.geolocalisation.R;
import com.tools.meuge.Model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InteractiveArrayAdapter extends ArrayAdapter<Model> {
	private final List<Model> list;
	private final Activity context;

	public InteractiveArrayAdapter(Activity context, List<Model> list) {
		super(context, R.layout.parents, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected ImageView image;
		protected TextView text;
		protected CheckBox checkbox;
	}

	private void iconeLoader(String categ, ImageView tv) {
		// Depending upon the child type, set the imageTextView01
        if ("But".equalsIgnoreCase(categ)) 
        	tv.setImageResource(R.drawable.but);
        if ("Conforama".equalsIgnoreCase(categ))
        	tv.setImageResource(R.drawable.conforama);
        if (categ.startsWith("Darty"))
        	tv.setImageResource(R.drawable.darty);
        if ("Boulanger".equalsIgnoreCase(categ))
        	tv.setImageResource(R.drawable.boulanger);
        if ("Metro".equalsIgnoreCase(categ))
        	tv.setImageResource(R.drawable.metro);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.parents, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) view.findViewById(R.id.logo);
			viewHolder.text = (TextView) view.findViewById(R.id.grp_parent);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							Model element = (Model) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getName());
		holder.checkbox.setChecked(list.get(position).isSelected());
		iconeLoader(list.get(position).getName(), holder.image);
		return view;
	}
}
