package application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import application.TreeList.TextFieldTreeCellImpl;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class TreeList extends TreeView<String>{
	public NameTree myTree;
	public String sortMode;
	public String treeMode;
	
	private TreeItem<String> rootNode;
	private TreeItem<String> subRootNode;
	
	public TreeList(NameTree tree, String sortMode) {
		super();	
		this.myTree = tree;
		this.treeMode = tree.getTreeMode();
		this.rootNode = new TreeItem<String> (sortMode);
		this.rootNode.setExpanded(true);
		
		this.setEditable(true);		
        this.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
        	
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCellImpl(myTree, treeMode);
            }
        });                       
        this.printAll();  
        
	}
	
	public int modeSwitch() {
		int mode = 0;
		if (sortMode!=null) {
			if (sortMode.equals("In-Order Depth")) {
				mode = 1;
			}else if (sortMode.equals("Breath-First")) {
				mode = 2;
			}else if (sortMode.equals("Pre-Order Depth")){
				mode = 3;
			}
		}
		return mode;
	}
	
	public TreeView<String> addSubList(String findMode, String x) {
		TreeView<String> sublist = new TreeView<String>();
		this.subRootNode = new TreeItem<String>(findMode);
		this.subRootNode.setExpanded(true);
		
		switch (findMode) {
		case "Find name longer than":
			int l = Integer.parseInt(x);
			this.findPersonByLength(l);
			sublist.setRoot(subRootNode);		
			break;
		case "Find by name":
			this.findAllByName(x);
			sublist.setRoot(subRootNode);
			break;
		case "Find by surname":
			this.findAllBySurname(x);
			sublist.setRoot(subRootNode);
			break;
			
		case "Find name longer than surname":
			this.findNameLongerThanSurname();
			sublist.setRoot(subRootNode);
			break;
		}
		
		return sublist;
	}
	
	public void findAllBySurname(String surname) {
		findAllBySurnameHelper(surname, myTree.top);
	}
	public void findAllBySurnameHelper(String surname, Person f) {
		if(f!=null) {
			this.findAllBySurnameHelper(surname, f.getBefore());
			if(f!=myTree.top && f.getSurName().equalsIgnoreCase(surname)) {
				String fullName = f.toString();
				TreeItem<String> nameNode = new TreeItem<String>(fullName);
				this.subRootNode.getChildren().add(nameNode);
			}
			this.findAllBySurnameHelper(surname, f.getAfter());
		}
	}
	
//	public Person findPersonByName(String name) {
//		return findByName(name, myTree.top);
//	}
//	public Person findByName(String name, Person f) {
//		if (f!=null) {
//			if(f.getName().equalsIgnoreCase(name)) {
//				return f;
//			}
//			Person before = findByName(name, f.getBefore());
//			if(before!=null) {
//				return before;
//			}
//			return findByName(name, f.getAfter());
//		}else {
//			System.out.println("Name not found");//test
//			return null;
//		}
//	}
	
	public void findAllByName(String name) {
		findAllByNameHelper(name, myTree.top);
	}
	public void findAllByNameHelper(String name, Person f) {
		if (f!=null) {
			this.findAllByNameHelper(name, f.getBefore());
			if(f!=myTree.top && f.getName().equalsIgnoreCase(name)) {
				String fullName = f.toString();
				TreeItem<String> nameNode = new TreeItem<String>(fullName);
				this.subRootNode.getChildren().add(nameNode);
			}
			this.findAllByNameHelper(name, f.getAfter());
		}
	}
	
	public void findPersonByLength(int length) {	
		findByLength(length, myTree.top);
	}
	
	public void findByLength(int length, Person p) {
		if(p!=null) {
			this.findByLength(length, p.getBefore());
			if(p!=myTree.top && length<p.getName().length()) {
				String fullName = p.toString();
				TreeItem<String> nameNode = new TreeItem<String>(fullName);
				this.subRootNode.getChildren().add(nameNode);
			}
			this.findByLength(length, p.getAfter());
		}
	}
	
	public void findNameLongerThanSurname() {
		findNameLonger(myTree.top);
	}
	public void findNameLonger(Person p) {
		if(p!=null) {
			this.findNameLonger(p.getBefore());
			if(p.getName().length()>p.getSurName().length()) {
				String fullName = p.toString();
				TreeItem<String> nameNode = new TreeItem<String>(fullName);
				this.subRootNode.getChildren().add(nameNode); 
			}
		}
		this.findNameLonger(p.getAfter());;
	}
	
	public void printAll() {		
		int mode = this.modeSwitch();		
		switch (mode) {
		case 1:
			this.printNames(myTree.top);
			break;
		case 2:
			this.printNamesBreathFirst(myTree.top);
			break;
		case 3:
			this.printNamesPreOrder(myTree.top);
			break;
		case 0:
			this.printNames(myTree.top);
		}			
		this.setRoot(rootNode);		
	}
	public void printNames(Person p) {			
		if (p!=null) {
			this.printNames(p.getBefore());
			if (p!=myTree.top) {
				p.print();
				String fullName = p.toString();
				TreeItem<String> nameNode = new TreeItem<String>(fullName);
				TreeItem<String> nBefore = new TreeItem<String>("Before: "+p.getBefore());
				TreeItem<String> nAfter = new TreeItem<String>("After: "+ p.getAfter());
				nameNode.getChildren().add(nBefore);
				nameNode.getChildren().add(nAfter);
				this.rootNode.getChildren().add(nameNode);
			}
			
			this.printNames(p.getAfter());
		}	
	}
	
	public void printNamesPreOrder(Person p) {
		if(p!=null) {
			if (p!=myTree.top) {
				p.print();
				String fullName = p.toString();
				TreeItem<String> nameNode = new TreeItem<String>(fullName);
				TreeItem<String> nBefore = new TreeItem<String>("Before: "+p.getBefore());
				TreeItem<String> nAfter = new TreeItem<String>("After: "+ p.getAfter());
				nameNode.getChildren().add(nBefore);
				nameNode.getChildren().add(nAfter);
				this.rootNode.getChildren().add(nameNode);
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
			if(x!=myTree.top) {
				x.print();
				String fullName = x.toString();
				TreeItem<String> nameNode = new TreeItem<String>(fullName);
				TreeItem<String> nBefore = new TreeItem<String>("Before: "+x.getBefore());
				TreeItem<String> nAfter = new TreeItem<String>("After: "+ x.getAfter());
				nameNode.getChildren().add(nBefore);
				nameNode.getChildren().add(nAfter);
				this.rootNode.getChildren().add(nameNode);
			}
			if (x.getBefore()!=null) {
				todo.offer(x.getBefore());
			}
			if(x.getAfter()!=null) {
				todo.offer(x.getAfter());
			}
		}
	}
	
	public final class TextFieldTreeCellImpl extends TreeCell<String> {
        private TextField textField;
        public String treeMode;
        public NameTree myTree;
        
        public TextFieldTreeCellImpl() {
        	
        }
        public TextFieldTreeCellImpl(NameTree myTree, String treeMode) {
        	this.treeMode = treeMode;
        	this.myTree = myTree;
		}
		@Override
        public void startEdit() {
            super.startEdit();
 
            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            String oldName = textField.getText();
            System.out.println(oldName);//test
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                        String newName = textField.getText();
                        myTree.handleNameChange(oldName, newName);     
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

	public void setMode(String mode) {
		this.sortMode = mode;
		System.out.println("*****************");
		System.out.println("Print new mode " + mode);
		this.rootNode = new TreeItem<String> (sortMode);
		this.rootNode.setExpanded(true);
		this.printAll();		
	}

	public NameTree getMyTree() {
		return myTree;
	}

	public void setMyTree(NameTree myTree) {
		this.myTree = myTree;
	}

	public String getTreeMode() {
		return treeMode;
	}

	public void setTreeMode(String treeMode) {
		this.treeMode = treeMode;
	}
	
	
}
