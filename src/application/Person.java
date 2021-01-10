package application;

public class Person {
	String name;
	String surName;
	String fullName;
	
	Person before;
	Person after;
	
	public Person(String name) {
		this.name = name;
	}

	public Person(String name, String surName) {
		this.name = name;
		this.surName = surName;
		this.fullName = name+" "+surName;
	}
		
	public void print() {
		System.out.println("Name: "+ name + " SurName: "+ surName);
		if (this.getBefore()!=null) {
			System.out.println("      "+"Before: "+ this.getBefore().toString());
		}else {
			System.out.println("      "+"No Before");
		}
		if(this.getAfter()!=null) {
			System.out.println("      "+"After: "+this.getAfter().toString());
		}else {
			System.out.println("      "+"No After");
		}		
	}
	
	public String toString() {
		return name + " " + surName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getBefore() {
		return before;
	}

	public void setBefore(Person before) {
		this.before = before;
	}

	public Person getAfter() {
		return after;
	}

	public void setAfter(Person after) {
		this.after = after;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
}
