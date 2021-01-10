package application;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TreeBoard extends GridPane{
	
	public TreeBoard(int width, int height) {
		super();
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public StackPane drawNameStack(Person n) {
		Rectangle rect = new Rectangle(200, 200, 80, 20);
		rect.setStroke(Color.BLUE);
		rect.setFill(Color.AQUA);
		rect.setArcHeight(5);
		rect.setArcWidth(5);
		StackPane nameStack = new StackPane();
		Text nT = new Text();
		nameStack.getChildren().addAll(rect, nT);
		nT.setText(n.getName());
		return nameStack;
	}
	
	public void drawTree(NameTree myTree) {
		Person J = new Person ("Joni");
		
		this.getChildren().add(this.drawNameStack(J));
		
	}
	
	
}
