package com.panzyma.nm.interfaces;

import java.util.List;

public interface DataSource {

	public List<Item> getData();

	public Item getData(int position);

}
