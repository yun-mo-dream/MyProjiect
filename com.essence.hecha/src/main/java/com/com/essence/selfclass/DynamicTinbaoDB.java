package com.com.essence.selfclass;

public class DynamicTinbaoDB {
	private int UserId;
	private int StoreId;
	private int tableId;
	private int ProejctId;

	private String CreateDate;
	private String JsonString;
	
	private String ControlID;
	private String ControlValue;
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public int getStoreId() {
		return StoreId;
	}
	public void setStoreId(int storeId) {
		StoreId = storeId;
	}
	public int getProejctId() {
		return ProejctId;
	}
	public void setProejctId(int proejctId) {
		ProejctId = proejctId;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getJsonString() {
		return JsonString;
	}
	public void setJsonString(String jsonString) {
		JsonString = jsonString;
	}
	public String getControlID() {
		return ControlID;
	}
	public void setControlID(String controlID) {
		ControlID = controlID;
	}
	public String getControlValue() {
		return ControlValue;
	}
	public void setControlValue(String controlValue) {
		ControlValue = controlValue;
	}


	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
}
