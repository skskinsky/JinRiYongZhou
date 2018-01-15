package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
@DatabaseTable(tableName = "ModelZhiboComments")
public class ModelZhiboComments {
	@DatabaseField()
	ArrayList<ModelZhiboHuifu> comments;

	public ArrayList<ModelZhiboHuifu> getComments() {
		return comments;
	}

	public void setComments(ArrayList<ModelZhiboHuifu> comments) {
		this.comments = comments;
	}

}
