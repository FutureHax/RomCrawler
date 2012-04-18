package com.t3hh4xx0r.romcrawler.adapters;

public class TitleResults {
	 private String itemName = "";
	 private String authorDate = "";
	 private String url = "";
	 private String ident = "";
	 private String summary = "";
	 private boolean isForum = false;
	 
	 public void setIsForum(boolean isForum) {
		 this.isForum = isForum;
	 }
	 
	 public boolean getIsForum() {
		 return isForum;
	 }

	 public void setIdent(String ident) {
		  this.ident = ident;
		 }

		 public String getIdent() {
		  return ident;
		 }

	 
	 public void setItemName(String itemName) {
	  this.itemName = itemName;
	 }

	 public String getItemName() {
	  return itemName;
	 }

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return summary;
	}
	 
	public void setAuthorDate(String authorDate) {
	  this.authorDate = authorDate;
	 }

	 public String getAuthorDate() {
	  return authorDate;
	 }
}
