package com.personal.parse_transactions_portfolio;

import java.util.ArrayList;

public class Security {
	private String bbGlobal;
	private String isin;
	private	String curr;
	private ArrayList<SecurityDaily> securityDays = new ArrayList<SecurityDaily>();
	
	
	public Security(String bbGlobal, String isin, String curr) {
		this.bbGlobal = bbGlobal;
		this.isin = isin;
		this.curr = curr;
	}
	
	public String getIsin() {
		return this.isin;
	}
	public String getBbGlobal() {
		return this.bbGlobal;
	}
	public String getCurr() {
		return this.curr;
	}
	public ArrayList<SecurityDaily> getSecurityDays() {
		return this.securityDays;
	}
	
	public void addSecurityDaily(SecurityDaily day) {
		this.securityDays.add(day);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bbGlobal == null) ? 0 : bbGlobal.hashCode());
		result = prime * result + ((curr == null) ? 0 : curr.hashCode());
		result = prime * result + ((isin == null) ? 0 : isin.hashCode());
		result = prime * result + ((securityDays == null) ? 0 : securityDays.hashCode());
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
		Security other = (Security) obj;
		if (bbGlobal == null) {
			if (other.bbGlobal != null)
				return false;
		} else if (!bbGlobal.equals(other.bbGlobal))
			return false;
		if (curr == null) {
			if (other.curr != null)
				return false;
		} else if (!curr.equals(other.curr))
			return false;
		if (isin == null) {
			if (other.isin != null)
				return false;
		} else if (!isin.equals(other.isin))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Security [bbGlobal=" + bbGlobal + ", isin=" + isin + ", curr=" + curr + ", securityDays=" + securityDays
				+ "]";
	}
	
	

}
