package application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class NameTree  {
	public Person top;
	public String treeMode;
	int count;
	
	public NameTree(Person top) {
		super();
		this.top = top;
	}
	
	public NameTree autoRoot() {
		NameTree newTree=null;
		String root=null;
		int left = countChildren(top.getBefore())+1;
		int right = countChildren(top.getAfter())+1;
		StringBuilder strBuilder = new StringBuilder();
		char c;
		
		while (Math.abs(left-right)>1) {
			if (treeMode.equals("Sort by Name")) {
				char l = top.getBefore().getName().charAt(0);
				char r = top.getAfter().getName().charAt(0);
				if (l==r) {
					c = top.getName().charAt(0);
				}else {
					if (left>right) {
						c = top.getName().charAt(0);
						c=(char) (c-1);
					}else {
						c = top.getName().charAt(0);
						
					}
				}
			
			}else {//"Sort by Surname"
				c = top.getSurName().charAt(0);
				if (left>right) {
					c = (char)(c-1);
				}else {
					c = (char) (c+1);
				}
				
			}
			strBuilder.append(c);
			root = strBuilder.toString();
			newTree = changeRoot(root);
			left = countChildren(top.getBefore())+1;
			right = countChildren(top.getAfter())+1;
		}
		
		return newTree;
	}

	public NameTree changeRoot(String letters) {
		List<Person> stack = new ArrayList<>();
		stack = this.findChildren(this.top);
		NameTree newTree = new NameTree(new Person (letters, letters));
		newTree.setTreeMode(this.getTreeMode());
		for (Person p:stack) {
			p.setBefore(null);
			p.setAfter(null);
			newTree.addPerson(p);
		}
		stack.clear();
		return newTree;
	}
	
	public NameTree switchTreeMode(String switcher) {
		
		List<Person> stack = new ArrayList<>();
		stack = this.findChildren(this.top);
		
		Person newTop = this.getTop();
		newTop.setBefore(null);
		newTop.setAfter(null);
		NameTree surNameTree = new NameTree(newTop);
		
		surNameTree.setTop(newTop);
		surNameTree.setTreeMode(switcher);
		
		for (Person p:stack) {
			p.setBefore(null);
			p.setAfter(null);
			surNameTree.addPerson(p);
//			this.setTreeMode(switcher);
		}
		stack.clear();
		System.out.println("_________change to surname___________");		
		surNameTree.printAll(); //test
		return surNameTree;
	}
	
	
	public void addPerson(Person newP) {
//		System.out.println("enter addPerson nameTree..." + treeMode);
		
		if (treeMode.equals("Sort by Name")) {
			this.addByName(top, newP);
		}else {
			this.addBySurname(top, newP);
		}
	}
	public void addByName(Person currentP, Person newP) {
		if(currentP.getName().compareTo(newP.getName())>0) {
			if (currentP.getBefore()==null) {
				currentP.setBefore(newP);
				count++;
			}else {
				addByName(currentP.getBefore(), newP);
			}
		}else {
			if (currentP.getAfter()==null) {
				currentP.setAfter(newP);
				count++;
			}else {
				addByName(currentP.getAfter(), newP);
			}
		}
	}
	
	public void addBySurname(Person currentP, Person newP) {
		if(currentP.getSurName().compareTo(newP.getSurName())>0) {
			if (currentP.getBefore()==null) {
				currentP.setBefore(newP);
				count++;
			}else {
				addBySurname(currentP.getBefore(), newP);
			}
		}else {
			if (currentP.getAfter()==null) {
				currentP.setAfter(newP);
				count++;
			}else {
				addBySurname(currentP.getAfter(), newP);
			}
		}
	}
	
	public void handleNameChange(String oldName, String newName) {
		String tm = this.getTreeMode();
		System.out.println("tm in NameTree handleNameChange " + tm);
		this.deletePerson(oldName, this.getTreeMode());
		Scanner sc = new Scanner(newName);
        while(sc.hasNext()) {
			String name = sc.next();
			String surName = sc.next();
			if (sc.hasNext()) { //check names longer than 2 strings
				name = name + " " + surName;
				surName = sc.next();
			}
			Person p = new Person(name, surName);
			this.addPerson(p);
		}	
        sc.close();
	}
	
	public Person findParent(Person child) {	
		return parentFinder(top, child);
	}
	public Person parentFinder(Person currentP, Person child) {
		if (currentP.getBefore()==child || currentP.getAfter() ==child) {
			return currentP;
		}else {
			if(currentP.getName().compareTo(child.getName())>0) {
				return parentFinder(currentP.getBefore(), child);
			}else {
				return parentFinder(currentP.getAfter(), child);
			}
		}				
	}
	
	public void deletePerson(String name, String treeMode) {
		Person f = this.findPersonByFullName(name);
		if (f==null) {
			return;
		}else {
			count--;
			System.out.print("findPersonByName finished--move to findParent");
		}
		
		Person p = this.findParent(f);
	
		System.out.print("delete person: ");
		f.print();//test
		System.out.print("parent: ");
		p.print();//test
		
		//find all f's children and add them in re-list
		List<Person> relist = this.findChildren(f);
		//delete parent's pointers
		if (p.getBefore()==f) {
			System.out.println("delete the before pointer");//test
			p.setBefore(null);	
		}else if (p.getAfter()==f){
			System.out.println("delete the after pointer");//test
			p.setAfter(null);
		}
		
		//put these children back to the tree
		for (Person c : relist) {
			c.print();//test
			c.setBefore(null);//clear the pointer before re-list
			c.setAfter(null);
			this.addPerson(c);
		}		
		this.printAll();	
	}
	
	private Person findPersonByFullName(String fullname) {	
		return findByFullName(fullname, top);
	}
	private Person findByFullName(String fullname, Person f) {
		if (f!=null) {
			if(f.getFullName().equalsIgnoreCase(fullname)) {
				return f;
			}
			Person before = findByFullName(fullname, f.getBefore());
			if(before!=null) {
				return before;
			}
			return findByFullName(fullname, f.getAfter());
		}else {
//			System.out.println("Name not found- in NameTree findByFullName");//test
			return null;
		}
	}

	public Person findPersonByName(String name) {
		return findByName(name, top);
	}
	public Person findByName(String name, Person f) {
		if (f!=null) {
			if(f.getName().equalsIgnoreCase(name)) {
				return f;
			}
			Person before = findByName(name, f.getBefore());
			if(before!=null) {
				return before;
			}
			return findByName(name, f.getAfter());
		}else {
//			System.out.println("Name not found- in NameTree findByName");//test
			return null;
		}
	}
	
	public Person findPersonBySurame(String surname) {
		return findBySurname(surname, top);
	}
	public Person findBySurname(String surname, Person f) {
		if (f!=null) {
			if(f.getSurName().equalsIgnoreCase(surname)) {
				return f;
			}
			Person before = findBySurname(surname, f.getBefore());
			if(before!=null) {
				return before;
			}
			return findBySurname(surname, f.getAfter());
		}else {
//			System.out.println("Surname not found- in name tree findPersonBySurname");//test
			return null;
		}
	}
	

	public void findPersonByLength(int length) {
		findByLength(length, top);
	}
	
	public void findByLength(int length, Person p) {
		if(p!=null) {
			this.findByLength(length, p.getBefore());
			if(p!=top && length==p.getName().length()) {
				p.print();
			}
			this.findByLength(length, p.getAfter());
		}
	}
	
	public List<Person> findChildren(Person f){
		List<Person> childList = new ArrayList<Person>();
		childList = listAllChilds(f, childList);
		return childList;
	}	
	public List<Person> listAllChilds(Person f, List<Person> childList) {	
		if(f!=null) {
			if (f.getBefore()!=null) {
				childList.add(f.getBefore());
				childList = listAllChilds(f.getBefore(), childList);
			}
			if(f.getAfter()!=null) {
				childList.add(f.getAfter());
				childList = listAllChilds(f.getAfter(), childList);
			}	
		}		
		return childList;
	}
	
	public int countChildren(Person c) {
		int countNr = 0;
		List<Person>list = this.findChildren(c);
		for (Person x : list) {
			countNr++;
		}
		return countNr;
	}
	
	public void printAll() {		
		printNames(top);
//		printNamesPreOrder(top);
//		printNamesBreathFirst(top);
	}
	
//in-order depth sorted		
	public void printNames(Person p) {
		if (p!=null) {
			this.printNames(p.getBefore());
			if (p!=top) {
				p.print();
			}
			this.printNames(p.getAfter());
		}
	}
	
//in-order depth pre-order
	public void printNamesPreOrder(Person p) {
		if(p!=null) {
			if (p!=top) {
				p.print();	
			}else {
//				System.out.println("Test root");
			}
			this.printNamesPreOrder(p.getBefore());
			this.printNamesPreOrder(p.getAfter());	
		}
	}
	
	public void printNamesBreathFirst(Person p) {
		Queue<Person>todo = new ArrayDeque<Person>();
		todo.offer(p);
		while(!todo.isEmpty()) {
			Person x = todo.poll();
			if(x!=top) {
				x.print();
			}
			if (x.getBefore()!=null) {
				todo.offer(x.getBefore());
			}
			if(x.getAfter()!=null) {
				todo.offer(x.getAfter());
			}
		}
	}
	
	public Person getTop() {
		return top;
	}


	public void setTop(Person top) {
		this.top = top;
	}


	public String getTreeMode() {
		return treeMode;
	}


	public void setTreeMode(String treeMode) {
		this.treeMode = treeMode;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

	
	
	
}
