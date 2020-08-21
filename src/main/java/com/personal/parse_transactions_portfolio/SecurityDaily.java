package com.personal.parse_transactions_portfolio;

import java.util.Date;

public class SecurityDaily {
	private Date date;
	private SecurityFee incomeFee = null;
	private SecurityFee coverageFee = null;
	private SecurityFee transaction = null;
	
	public SecurityDaily() {
	}
	public SecurityDaily(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return this.date;
	}
	public SecurityFee getIncomeFee() {
		return incomeFee;
	}
	public SecurityFee getCoverageFee() {
		return coverageFee;
	}
	public SecurityFee getTransaction() {
		return transaction;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	public void setIncomeFee(SecurityFee incomeFee) {
		this.incomeFee = incomeFee;
	}
	public void setCoverageFee(SecurityFee coverageFee) {
		this.coverageFee = coverageFee;
	}
	public void setTransaction(SecurityFee transaction) {
		this.transaction = transaction;
	}
	
	public void addIncomeFee(SecurityFee newFee) {
		if(incomeFee == null)
			this.incomeFee = newFee;
		else {
			this.incomeFee.addTotalGrossVal(newFee.getTotalGrossVal());
			this.incomeFee.addUnitVal(newFee.getUnitGrossVal());
			this.incomeFee.addTotalNetVal(newFee.getTotalNetVal());	
		}
	}
	public void addCoverageFee(SecurityFee newFee) {
		if(coverageFee == null) {
			this.coverageFee = newFee;
		} else {
			this.coverageFee.addTotalGrossVal(newFee.getTotalGrossVal());
			this.coverageFee.addUnitVal(newFee.getUnitGrossVal());
			this.coverageFee.addTotalNetVal(newFee.getTotalNetVal());	
		}
	}
	public void addTransaction(SecurityFee newFee) {
		if(transaction == null) {
			this.transaction = newFee;
		} else {
			this.transaction.addTotalGrossVal(newFee.getTotalGrossVal());
			this.transaction.addUnitVal(newFee.getUnitGrossVal());
			this.transaction.addTotalNetVal(newFee.getTotalNetVal());	
		}
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coverageFee == null) ? 0 : coverageFee.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((incomeFee == null) ? 0 : incomeFee.hashCode());
		result = prime * result + ((transaction == null) ? 0 : transaction.hashCode());
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
		SecurityDaily other = (SecurityDaily) obj;
		if (coverageFee == null) {
			if (other.coverageFee != null)
				return false;
		} else if (!coverageFee.equals(other.coverageFee))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (incomeFee == null) {
			if (other.incomeFee != null)
				return false;
		} else if (!incomeFee.equals(other.incomeFee))
			return false;
		if (transaction == null) {
			if (other.transaction != null)
				return false;
		} else if (!transaction.equals(other.transaction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SecurityDaily [date=" + date + ", incomeFee=" + incomeFee + ", coverageFee=" + coverageFee
				+ ", transaction=" + transaction + "]";
	}
	
	
}
