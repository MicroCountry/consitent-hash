package com.consistent.hash;

import java.math.BigDecimal;

public class Account {
	private String accountId;
	private BigDecimal amt;
	public Account(){
	}
	public Account(String accountId,BigDecimal amt){
		this.accountId = accountId;
		this.amt = amt;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	
}
