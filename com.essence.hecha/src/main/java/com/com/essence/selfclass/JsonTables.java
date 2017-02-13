package com.com.essence.selfclass;

import java.util.List;

public class JsonTables {
private int ID;
private String Name;
private String ReportingFrequency;
private List<JsonRows>Rows;

public int getID() {
	return ID;
}
public void setID(int iD) {
	ID = iD;
}
public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
public List<JsonRows> getRows() {
	return Rows;
}
public void setRows(List<JsonRows> rows) {
	Rows = rows;
}
public String getReportingFrequency() {
	return ReportingFrequency;
}
public void setReportingFrequency(String reportingFrequency) {
	ReportingFrequency = reportingFrequency;
}


}
