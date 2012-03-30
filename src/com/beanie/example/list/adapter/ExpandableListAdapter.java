
package com.beanie.example.list.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.meuge.geolocalisation.KmsCalcules;
import com.meuge.geolocalisation.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    private Context context;

    private ArrayList<String> groups;

    private ArrayList<ArrayList<KmsCalcules>> children;

    public ExpandableListAdapter(Context context, ArrayList<String> groups,
            ArrayList<ArrayList<KmsCalcules>> children) {
        this.context = context;
        this.groups = groups;
        this.children = children;
    }

    /**
     * A general add method, that allows you to add a Vehicle to this list
     * 
     * Depending on if the category opf the vehicle is present or not,
     * the corresponding item will either be added to an existing group if it 
     * exists, else the group will be created and then the item will be added
     * @param vehicle
     */
    public void addItem(KmsCalcules vehicle) {
        if (!groups.contains(vehicle.getCategorie())) {
            groups.add(vehicle.getCategorie());
        }
        int index = groups.indexOf(vehicle.getCategorie());
        if (children.size() < index + 1) {
            children.add(new ArrayList<KmsCalcules>());
        }
        children.get(index).add(vehicle);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    // Return a child view. You can load your custom layout here.
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
    	KmsCalcules vehicle = (KmsCalcules) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.enfants, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.grp_enfant);
        tv.setText("   " + vehicle.getInformations());

        // Depending upon the child type, set the imageTextView01
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        if (vehicle instanceof Car) {
//            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.car, 0, 0, 0);
//        } else if (vehicle instanceof Bus) {
//            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bus, 0, 0, 0);
//        } else if (vehicle instanceof Bike) {
//            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bike, 0, 0, 0);
//        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Return a group view. You can load your custom layout here.
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parents, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.grp_parent);
        tv.setText(group);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}
