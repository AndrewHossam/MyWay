package com.AMDevelopers.myway;

public class MenuItem {

	private int Icon;
	private String IconLabel;

	public MenuItem(int icon, String iconLabel) {
		Icon = icon;
		IconLabel = iconLabel;
	}

	public int getIcon() {
		return Icon;
	}

	public void setIcon(int icon) {
		Icon = icon;
	}

	public String getIconLabel() {
		return IconLabel;
	}

	public void setIconLabel(String iconLabel) {
		IconLabel = iconLabel;
	}
}
