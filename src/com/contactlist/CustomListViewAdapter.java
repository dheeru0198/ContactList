package com.contactlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.contactlist.R;
import com.contactlist.RowItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

//An adapter to display the data in list view.
public class CustomListViewAdapter extends ArrayAdapter<RowItem> implements SectionIndexer {
 
    Context context;
    HashMap<String, Integer> alphaIndexer;
    String[] sections;
 
    public CustomListViewAdapter(Context context, int resourceId,
            List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
        
        alphaIndexer = new HashMap<String, Integer>();
        int size = items.size();
        
        for (int i=0; i < size; i++) {
        	String s = items.get(i).getName();
        	
        	String ch = s.substring(0, 1);
        	
        	ch = ch.toUpperCase();
        	
        	if (alphaIndexer.get(ch) == null) {
                	alphaIndexer.put(ch, i);
            	}
        }
        
        Set<String> sectionLetters = alphaIndexer.keySet();
        
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        
        Collections.sort(sectionList);
        
        sections = new String[sectionList.size()];  
        
        sectionList.toArray(sections);
    }
 
    //private view holder class
    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
        TextView txtNumber;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.txtNumber = (TextView) convertView.findViewById(R.id.number);
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.txtNumber.setText(rowItem.getNumber());
        holder.txtName.setText(rowItem.getName());
        holder.imageView.setImageBitmap(rowItem.getImage());
 
        return convertView;
    }

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return alphaIndexer.get(sections[section]);
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return sections;
	}

}
