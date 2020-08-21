package com.personal.parse_transactions_portfolio;

import java.util.ArrayList;

public class Portfolio {
	private ArrayList<Security> securities = new ArrayList<Security>();
	private ArrayList<PortfolioDaily> portfolioDays = new ArrayList<PortfolioDaily>();
	private ArrayList<SecurityFee> securityFees = new ArrayList<SecurityFee>();
	private int index;

	public Portfolio(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
	public ArrayList<Security> getSecurities() {
		return this.securities;
	}
	public ArrayList<PortfolioDaily> getPortfolioDays() {
		return this.portfolioDays;
	}
	public ArrayList<SecurityFee> getTransactions() {
		return this.securityFees;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	public void addSecurity(Security sec) {
		this.securities.add(sec);
	}
	public void addPortfolioDay(PortfolioDaily day) {
		this.portfolioDays.add(day);
	}
	public void addTransaction(SecurityFee securityFee) {
		this.securityFees.add(securityFee);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + ((portfolioDays == null) ? 0 : portfolioDays.hashCode());
		result = prime * result + ((securities == null) ? 0 : securities.hashCode());
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
		Portfolio other = (Portfolio) obj;
		if (index != other.index)
			return false;
		if (portfolioDays == null) {
			if (other.portfolioDays != null)
				return false;
		} else if (!portfolioDays.equals(other.portfolioDays))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Portfolio [securityDays=" + securities + ", portfolioDays=" + portfolioDays + ", index=" + index
				+ "]";
	}
	
}
