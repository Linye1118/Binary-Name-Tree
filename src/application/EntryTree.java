package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ecs100.UIFileChooser;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EntryTree extends Application{
	public TreeBoard treeGrid = new TreeBoard(400,400);
	public TreeList treelist;
	public VBox vboxList;
	public TreeList sublist;
	public Text system;
	public NameTree nameTree;
	
	public int count;
	public String filepath="C:\\Users\\linye\\eclipse-workspace\\NameTree\\src\\mswdev.csv";
	public String modeDefault = "In-Order Depth";
	public String treeModeDefault = "Sort by Name";
			
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Name Trees");
		try {
			this.initialize();
			
			BorderPane rootPane = new BorderPane();
			
			HBox hboxTop = addTop();
			hboxTop.setStyle("-fx-background-color: #F0F8FF;");
			rootPane.setTop(hboxTop);		
			
			this.treelist = new TreeList(nameTree, modeDefault);
			VBox vboxRight = addRight(treelist);
			vboxRight.setStyle("-fx-background-color: #DCDCDC;");
			rootPane.setRight(vboxRight);
			
			HBox hboxBottom = addBottom();
			this.system = new Text();
			hboxBottom.setStyle("-fx-background-color: #F0F8FF;");
			rootPane.setBottom(hboxBottom);
			
			this.vboxList = new VBox();
			vboxList.setStyle("-fx-background-color: #DCDCDC;");       
			vboxList.getChildren().add(treelist);
			rootPane.setLeft(vboxList);

			ScrollPane spCenter = addCenter();
			spCenter.setStyle("-fx-background-color: #FAEBD7;");
			rootPane.setCenter(spCenter);
			spCenter.setContent(treeGrid);	
			
			Scene scene = new Scene(rootPane,800,600);		
								
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}

		//following are worked functions
		System.out.println("--------------------");
//		surNameTree.printAllSurname();			//worked!
//		nameTree.findPersonByLength(5);			//worked!
//		nameTree.deletePerson("Lauren");		//worked!
//		Person a = nameTree.findPersonByName("Alex");	//worked!
//		a.print();
//		nameTree.findChildren(a);				//worked!
//		System.out.println("---------afer find child-----------");
		
//		nameTree = nameTree.changeRoot("Mad1"); //worked!
//		System.out.println("newroot: "+ nameTree.getTop().toString());
//		int n = nameTree.getCount();	//worked!
//		System.out.println("count nr: " +n);
	}
	
	private void initialize() {
		this.modeDefault = "In-Order Depth";
		this.treeModeDefault = "Sort by Name";
		Person startP = new Person("Mad-root", "Mad");
		this.nameTree = new NameTree(startP);
		nameTree.setTreeMode(treeModeDefault); //"Sort by Name"
		this.loadFile();
			
	}
	
	private VBox addRight(TreeList treelist) {
		VBox sortBox = new VBox();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));
		
		ChoiceBox<String> cbSortWay = new ChoiceBox<>();
		cbSortWay.getItems().addAll("Sort by Name", "Sort by Surname");
		cbSortWay.setValue(treeModeDefault);
		
		final ToggleGroup group = new ToggleGroup();
		RadioButton inOrder = new RadioButton("In-Order Depth");
		RadioButton breath = new RadioButton("Breath-First");
		RadioButton preOrder = new RadioButton("Pre-Order Depth");
		inOrder.setToggleGroup(group);
		breath.setToggleGroup(group);
		preOrder.setToggleGroup(group);
		
		Button btnRoot = new Button("Set Root");
		TextField newRoot = new TextField();
		newRoot.setPromptText("Key in letters");
		//newRoot.getParent().requestFocus();
		int left = nameTree.countChildren(nameTree.top.getBefore())+1;
		Label leftChild = new Label("Left: "+ String.valueOf(left));
		int right = nameTree.countChildren(nameTree.top.getAfter())+1;
		Label rightChild = new Label("Right: "+ String.valueOf(right));
//		nameTree.countChildren(nameTree.top.getAfter());
		btnRoot.setOnAction(new EventHandler<ActionEvent>() {		
			@Override
			public void handle(ActionEvent arg0) {
				String r = newRoot.getText().toUpperCase();
				nameTree = nameTree.changeRoot(r);
				System.out.println("newroot: "+ nameTree.getTop().toString());
//				int n = nameTree.getCount();	//worked!
//				System.out.println("count nr: " +n);
				treelist.setMyTree(nameTree);
				int left = nameTree.countChildren(nameTree.top.getBefore())+1;
				int right = nameTree.countChildren(nameTree.top.getAfter())+1;
				leftChild.setText("Left: "+ String.valueOf(left));
				rightChild.setText("Right: "+ String.valueOf(right));
			}	
		});
		
		Button btnAutoRoot = new Button ("Auto Root");
		Label autoRoot = new Label("Root: "+"");
		btnAutoRoot.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				nameTree = nameTree.autoRoot();
				treelist.setMyTree(nameTree);
				int left = nameTree.countChildren(nameTree.top.getBefore())+1;
				int right = nameTree.countChildren(nameTree.top.getAfter())+1;
				leftChild.setText("Left: "+ String.valueOf(left));
				rightChild.setText("Right: "+ String.valueOf(right));
				autoRoot.setText("Root: "+ nameTree.getTop());
			}
			
		});
		
		grid.add(cbSortWay, 0, 0);
		grid.add(inOrder, 0, 1);
		grid.add(breath, 0, 2);
		grid.add(preOrder, 0, 3);
		grid.add(btnRoot, 1, 6);
		grid.add(newRoot, 0, 6);
		grid.add(leftChild, 0, 7);
		grid.add(rightChild, 1, 7);
		grid.add(autoRoot, 0, 8);
		grid.add(btnAutoRoot, 1, 8);
		
		inOrder.setUserData("In-Order Depth");
		inOrder.setSelected(true);
		
		breath.setUserData("Breath-First");
		preOrder.setUserData("Pre-Order Depth");
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			
	    	public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, 
		    	    Toggle new_toggle) {
	    		if (group.getSelectedToggle() != null) {
	    	        String mode=(String) group.getSelectedToggle().getUserData();
	    	        treelist.setMode(mode);
	    	        treelist.setMyTree(nameTree);
	    		}
	    	}
	    });
		
		cbSortWay.getSelectionModel().selectedItemProperty().addListener(
				(v, oldValue, newValue)->{
					cbSortWay.setValue(newValue);
					this.nameTree = this.nameTree.switchTreeMode(newValue);
					String sortMode =(String)group.getSelectedToggle().getUserData();
					this.nameTree.setTreeMode(sortMode);
					this.treelist = new TreeList(nameTree, sortMode);});
		
		sortBox.getChildren().add(grid);
		return sortBox;
	}

	private HBox addBottom() {
		HBox hbox = new HBox();
		this.system = new Text();
		Label hint1 = new Label("Text Label: to be continue...");
		hbox.getChildren().addAll(system, hint1);
		
		return hbox;
	}
	
	private ScrollPane addCenter() {
		ScrollPane sPane = new ScrollPane();
		sPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		treeGrid.setGridLinesVisible(true);//for test
		treeGrid.setAlignment(Pos.CENTER);
		treeGrid.setStyle("-fx-background-color: #808080;"); 
        
		return sPane;
	}

	private HBox addTop() {
		HBox top = new HBox();
		top.setPadding(new Insets(10, 15, 10, 15));
		top.setSpacing(10);
		
		ChoiceBox<String> cbFind = new ChoiceBox<>();
		cbFind.getItems().addAll("Find by name", "Find name longer than",
				"Find by surname", "Find name longer than surname");
		cbFind.setValue("Find name longer than");
		cbFind.getSelectionModel().selectedItemProperty().addListener(
				(v, oldValue, newValue)->{cbFind.setValue(newValue);});
		
		TextField name = new TextField();
		Button btn = new Button("Search");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				int listNr = vboxList.getChildren().size();
				if (listNr>=2) {//remove last sublist
					vboxList.getChildren().remove(listNr-1);
				}
				String findMode = cbFind.getValue();
				String input = name.getText();
				TreeView<String> sublist = new TreeView<String>();
				
				if (findMode.equals("Find by name")) {
					Person f = nameTree.findPersonByName(input);
					//f.print();//background
					sublist = treelist.addSubList(findMode, input);
					if (sublist==null) {
						System.out.println("name not found"); //to check...test
					}else {
						vboxList.getChildren().add(sublist);
					}
					
				}else if (findMode.equals("Find name longer than")){
				//find by length
					int l = Integer.parseInt(input);
					nameTree.findPersonByLength(l);//background
					sublist = treelist.addSubList(findMode, input);
					vboxList.getChildren().add(sublist);
					
				}else if(findMode.equals("Find by surname")){	
					sublist = treelist.addSubList(findMode, input);
					if (sublist==null) {
						System.out.println("name not found"); //to check...test
					}else {
						vboxList.getChildren().add(sublist);
					}
				}else if(findMode.equals("Find name longer than surname")) {
					sublist = treelist.addSubList(findMode, input);
					if(sublist==null) {
						System.out.println("no name longer than surname");
					}else {
						vboxList.getChildren().add(sublist);
					}
				}
			}	
		});
		Button btnFile = new Button("Choose File...");
		
		Text fileName = new Text("File Name: ");
		TextField fName = new TextField("mswdev.csv");
		btnFile.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser chooser = new FileChooser();
				chooser.setTitle("Open NameList File");
				File file = chooser.showOpenDialog(null);
				if(file!=null) {
					fName.setText(file.getPath());		
				}	
			}	
		});
		this.filepath=fName.getText();
		top.getChildren().addAll(cbFind, name, btn, fileName, fName, btnFile);
		
		return top;
	}
	
	public void loadFile() {
		count = 0;
//		String fileName="C:\\Users\\linye\\eclipse-workspace\\NameTree\\src\\mswdev.csv";						
		try {		
			Scanner scanner = new Scanner(new File(filepath));				
			while (scanner.hasNext()) {
				String w = scanner.nextLine();
				Scanner sc = new Scanner(w);
				while(sc.hasNext()) {
					String name = sc.next();
					String surName = sc.next();
					if (sc.hasNext()) { //check names longer than 2 strings
						name = name + " " + surName;
						surName = sc.next();
					}
					Person p = new Person(name, surName);
					nameTree.addPerson(p);
					
				}	

				sc.close();
			}
			scanner.close();
		} 
		catch (FileNotFoundException e) {
				System.out.printf("Error loading file: %s%n", e);
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
