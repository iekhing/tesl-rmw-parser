package com.triquesta.tesl.rmw.parser;

import java.math.BigDecimal;
import java.util.Date;

public class BankRecordDetail {

	private String cisKey;
	private String recordType;
	private Integer facilityId;
	private Integer subFacilityId;
	private String type;
	private String groupLimitFlag;
	private Integer groupLimitParentFacilityId;
	private BigDecimal limitAmount;
	private String limitCurrency;
	private BigDecimal originalLimit;
	private String originalLimitCurrency;
	private Date tempLimitExpiryDate;
	private Date originalLimitMaturity;

	public String getCisKey() {
		return cisKey;
	}

	public void setCisKey(String cisKey) {
		this.cisKey = cisKey;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getSubFacilityId() {
		return subFacilityId;
	}

	public void setSubFacilityId(Integer subFacilityId) {
		this.subFacilityId = subFacilityId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroupLimitFlag() {
		return groupLimitFlag;
	}

	public void setGroupLimitFlag(String groupLimitFlag) {
		this.groupLimitFlag = groupLimitFlag;
	}

	public Integer getGroupLimitParentFacilityId() {
		return groupLimitParentFacilityId;
	}

	public void setGroupLimitParentFacilityId(Integer groupLimitParentFacilityId) {
		this.groupLimitParentFacilityId = groupLimitParentFacilityId;
	}

	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getLimitCurrency() {
		return limitCurrency;
	}

	public void setLimitCurrency(String limitCurrency) {
		this.limitCurrency = limitCurrency;
	}

	public BigDecimal getOriginalLimit() {
		return originalLimit;
	}

	public void setOriginalLimit(BigDecimal originalLimit) {
		this.originalLimit = originalLimit;
	}

	public String getOriginalLimitCurrency() {
		return originalLimitCurrency;
	}

	public void setOriginalLimitCurrency(String originalLimitCurrency) {
		this.originalLimitCurrency = originalLimitCurrency;
	}

	public Date getTempLimitExpiryDate() {
		return tempLimitExpiryDate;
	}

	public void setTempLimitExpiryDate(Date tempLimitExpiryDate) {
		this.tempLimitExpiryDate = tempLimitExpiryDate;
	}

	public Date getOriginalLimitMaturity() {
		return originalLimitMaturity;
	}

	public void setOriginalLimitMaturity(Date originalLimitMaturity) {
		this.originalLimitMaturity = originalLimitMaturity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BankRecordDetail [cisKey=" + cisKey + ", recordType="
				+ recordType + ", facilityId=" + facilityId
				+ ", subFacilityId=" + subFacilityId + ", type=" + type
				+ ", groupLimitFlag=" + groupLimitFlag
				+ ", groupLimitParentFacilityId=" + groupLimitParentFacilityId
				+ ", limitAmount=" + limitAmount + ", limitCurrency="
				+ limitCurrency + ", originalLimit=" + originalLimit
				+ ", originalLimitCurrency=" + originalLimitCurrency
				+ ", tempLimitExpiryDate=" + tempLimitExpiryDate
				+ ", originalLimitMaturity=" + originalLimitMaturity + "]";
	}

}
