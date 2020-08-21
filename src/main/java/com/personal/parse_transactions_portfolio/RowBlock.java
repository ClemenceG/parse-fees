package com.personal.parse_transactions_portfolio;

import java.util.ArrayList;

public class RowBlock {
	private ArrayList<String> rowElements;
	
	public RowBlock() {
		this.rowElements = new ArrayList<String>();
	}
	
	public ArrayList<String> getRowElements() {
		return this.rowElements;
	}
	public String getRowElement(int i) {
		return this.rowElements.get(i);
	}
	
	public void addRowElement(String string) {
		this.rowElements.add(string);
	}

	public int getBlockSize() {
		return this.rowElements.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rowElements == null) ? 0 : rowElements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RowBlock other = (RowBlock) obj;
		if (rowElements == null) {
			if (other.rowElements != null)
				return false;
		} else if (!rowElements.equals(other.rowElements))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RowBlock [rowElements=" + rowElements + "]";
	}

	

}
