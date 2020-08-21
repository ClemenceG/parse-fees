package com.personal.parse_transactions_portfolio;

import java.util.Date;

public class PortfolioFee {
	
	private Date date;
	private double unitGrossVal;
	private double totalGrossVal;
	private double totalNetVal;
	
	public PortfolioFee() {
	}
	public PortfolioFee(Date date, double unitGrossFee, double totalGrossFee, double totalNetFee) {
		this.date = date;
		this.unitGrossVal = unitGrossFee;
		this.totalGrossVal = totalGrossFee;
		this.totalNetVal = totalNetFee;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getUnitGrossFee() {
		return unitGrossVal;
	}
	public void setUnitGrossFee(double unitGrossFees) {
		this.unitGrossVal = unitGrossFees;
	}
	public double getTotalGrossFee() {
		return totalGrossVal;
	}
	public void setTotalGrossFee(double totalGrossFees) {
		this.totalGrossVal = totalGrossFees;
	}
	public double getTotalNetFee() {
		return totalNetVal;
	}
	public void setTotalNetFee(double totalNetFees) {
		this.totalNetVal = totalNetFees;
	}
	
	public void addTotalGrossFee(double grossFee) {
		this.totalGrossVal += grossFee;
	}
	public void addUnitFee(double Fee) {
		this.unitGrossVal += Fee;
	}
	public void addTotalNetFee(double Fee) {
		this.totalNetVal += Fee;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(totalGrossVal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalNetVal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(unitGrossVal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		PortfolioFee other = (PortfolioFee) obj;
		if (Double.doubleToLongBits(totalGrossVal) != Double.doubleToLongBits(other.totalGrossVal))
			return false;
		if (Double.doubleToLongBits(totalNetVal) != Double.doubleToLongBits(other.totalNetVal))
			return false;
		if (Double.doubleToLongBits(unitGrossVal) != Double.doubleToLongBits(other.unitGrossVal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Fee [unitGrossFees=" + unitGrossVal + ", totalGrossFees=" + totalGrossVal + ", totalNetFees="
				+ totalNetVal + "]";
	}

}
