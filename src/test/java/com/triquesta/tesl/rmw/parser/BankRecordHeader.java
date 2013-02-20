package com.triquesta.tesl.rmw.parser;

public class BankRecordHeader {

	private String cisKey;
	private String name;
	private String parentCISKey;
	private String parentName;
	private String crg;
	private String rm;
	private String country;

	public String getCisKey() {
		return cisKey;
	}

	public void setCisKey(String cisKey) {
		this.cisKey = cisKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentCISKey() {
		return parentCISKey;
	}

	public void setParentCISKey(String parentCISKey) {
		this.parentCISKey = parentCISKey;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getCrg() {
		return crg;
	}

	public void setCrg(String crg) {
		this.crg = crg;
	}

	public String getRm() {
		return rm;
	}

	public void setRm(String rm) {
		this.rm = rm;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BankRecordHeader [cisKey=" + cisKey + ", name=" + name
				+ ", parentCISKey=" + parentCISKey + ", parentName="
				+ parentName + ", crg=" + crg + ", rm=" + rm + ", country="
				+ country + "]";
	}
	
	
	
}
