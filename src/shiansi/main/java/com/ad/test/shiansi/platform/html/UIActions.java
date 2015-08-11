package com.ad.test.shiansi.platform.html;


enum UIActions {
	CLICKED("Clicked "),
	CHECKED("Checked "),
	UNCHECKED("Un-checked "),
	SELECTED("Selected "),
	ENTERED("Entered "),
	CLEARED("Cleared text ");
	private UIActions(String action){
		this.action = action;
	}
	private String action;
	
	public String getAction(){
		return this.action;
	}
	
}
