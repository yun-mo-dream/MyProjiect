package com.com.essence.selfclass;

import java.util.List;

public class JsonRows {
	private int ID;
	private String Name;
	private String Img;
	private List<JsonWidget>Contorls;
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
	public List<JsonWidget> getContorls() {
		return Contorls;
	}

	public void setContorls(List<JsonWidget> contorls) {
		Contorls = contorls;
	}

	public String getImg() {
		return Img;
	}

	public void setImg(String img) {
		Img = img;
	}
}
