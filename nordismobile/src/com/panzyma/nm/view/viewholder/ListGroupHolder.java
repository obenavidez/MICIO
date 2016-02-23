package com.panzyma.nm.view.viewholder;

import com.panzyma.nm.view.adapter.ExpandListGroup;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

import android.widget.TextView;

public class ListGroupHolder {

	@InvokeView(viewId = R.id.lblListHeader)
	public TextView header;
	
	public void mappingData(Object entity) {
	
		ExpandListGroup grp=(ExpandListGroup)entity;
		header.setText("" + grp.getName());
	}
}
