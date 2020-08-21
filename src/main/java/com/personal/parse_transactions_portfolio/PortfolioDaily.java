package com.personal.parse_transactions_portfolio;

import java.util.Date;

public class PortfolioDaily {
	private Date date;
	private PortfolioFee subscriptionFees = new PortfolioFee();
	private PortfolioFee operationalFees = new PortfolioFee();
	private PortfolioFee managementFees = new PortfolioFee();

	public PortfolioDaily() {
	}
	public PortfolioDaily(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	public PortfolioFee getSubscriptionFees() {
		return subscriptionFees;
	}
	public PortfolioFee getOperationalFees() {
		return operationalFees;
	}
	public PortfolioFee getManagementFees() {
		return managementFees;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}	
	public void setOperationalFees(PortfolioFee operationalFees) {
		this.operationalFees = operationalFees;
	}
	public void setManagementFees(PortfolioFee managementFees) {
		this.managementFees = managementFees;
	}
	
	public void addSubscriptionFee(PortfolioFee newFee) {
		this.subscriptionFees.addTotalGrossFee(newFee.getTotalGrossFee());
		this.subscriptionFees.addUnitFee(newFee.getUnitGrossFee());
		this.subscriptionFees.addTotalNetFee(newFee.getTotalNetFee());	
	}
	public void addOperationalFee(PortfolioFee newFee) {
		this.operationalFees.addTotalGrossFee(newFee.getTotalGrossFee());
		this.operationalFees.addUnitFee(newFee.getUnitGrossFee());
		this.operationalFees.addTotalNetFee(newFee.getTotalNetFee());	
	}
	public void addManagementFee(PortfolioFee newFee) {
		this.managementFees.addTotalGrossFee(newFee.getTotalGrossFee());
		this.managementFees.addUnitFee(newFee.getUnitGrossFee());
		this.managementFees.addTotalNetFee(newFee.getTotalNetFee());	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((managementFees == null) ? 0 : managementFees.hashCode());
		result = prime * result + ((operationalFees == null) ? 0 : operationalFees.hashCode());
		result = prime * result + ((subscriptionFees == null) ? 0 : subscriptionFees.hashCode());
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
		PortfolioDaily other = (PortfolioDaily) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (managementFees == null) {
			if (other.managementFees != null)
				return false;
		} else if (!managementFees.equals(other.managementFees))
			return false;
		if (operationalFees == null) {
			if (other.operationalFees != null)
				return false;
		} else if (!operationalFees.equals(other.operationalFees))
			return false;
		if (subscriptionFees == null) {
			if (other.subscriptionFees != null)
				return false;
		} else if (!subscriptionFees.equals(other.subscriptionFees))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PortfolioDaily [date=" + date + ", subscriptionFees=" + subscriptionFees + ", operationalFees="
				+ operationalFees + ", managementFees=" + managementFees + "]";
	}
	
	
	
}
