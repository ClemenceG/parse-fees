package com.personal.parse_transactions_portfolio;

import java.util.Date;

public class SecurityFee {
	
	private Date date;
	private String type;
	private String orderId;
	private String side;
	private String status;
	private double quantity;
	private String secCurr;
	private double pricesInCurr;
	private double unitGrossVal;
	private double totalGrossVal;
	private double totalNetVal;
	private String isin;
	private String bbGlobal;
	
	public SecurityFee() {
	}
	public SecurityFee(Date date) { 
		this.date = date;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getSecCurr() {
		return secCurr;
	}
	public void setSecCurr(String secCurr) {
		this.secCurr = secCurr;
	}
	public double getPricesInCurr() {
		return pricesInCurr;
	}
	public void setPricesInCurr(double feesInCurr) {
		this.pricesInCurr = feesInCurr;
	}
	public double getUnitGrossVal() {
		return unitGrossVal;
	}
	public void setUnitGrossVal(double unitPriceGrossFeesCurr) {
		this.unitGrossVal = unitPriceGrossFeesCurr;
	}
	public double getTotalGrossVal() {
		return totalGrossVal;
	}
	public void setTotalGrossVal(double totalValGrossFeesEur) {
		this.totalGrossVal = totalValGrossFeesEur;
	}
	public double getTotalNetVal() {
		return totalNetVal;
	}
	public void setTotalNetVal(double totalValNetEur) {
		this.totalNetVal = totalValNetEur;
	}
	public String getBbGlobal() {
		return bbGlobal;
	}
	public void setBbGlobal(String bbGlobal) {
		this.bbGlobal = bbGlobal;
	}
	
	public void addTotalGrossVal(double price) {
		this.totalGrossVal += price;
	}
	public void addUnitVal(double price) {
		this.unitGrossVal += price;
	}
	public void addTotalNetVal(double price) {
		this.totalNetVal += price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bbGlobal == null) ? 0 : bbGlobal.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		long temp;
		temp = Double.doubleToLongBits(quantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((secCurr == null) ? 0 : secCurr.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SecurityFee other = (SecurityFee) obj;
		if (bbGlobal == null) {
			if (other.bbGlobal != null)
				return false;
		} else if (!bbGlobal.equals(other.bbGlobal))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(quantity) != Double.doubleToLongBits(other.quantity))
			return false;
		if (secCurr == null) {
			if (other.secCurr != null)
				return false;
		} else if (!secCurr.equals(other.secCurr))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (Double.doubleToLongBits(unitGrossVal) != Double.doubleToLongBits(other.unitGrossVal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SecurityFee [date=" + date + ", type=" + type + ", side=" + side + ", quantity=" + quantity
				+ ", pricesInCurr=" + pricesInCurr + ", totalNetVal=" + totalNetVal + ", bbGlobal=" + bbGlobal + "]";
	}
	
	

}
