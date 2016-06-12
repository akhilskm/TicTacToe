import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PlayGame extends Application{

	private static int size = 3;
	private Label nums[] = new Label[size*size];
	private static Board board;
	private Stage window;
	
	public static void main(String args[]){
		board = new Board(size);
		board.setInitial();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		window = stage;
		window.setTitle("Tic Tac Toe");
		for(int i=0;i<size*size;i++){
			nums[i]=new Label(""+board.getBoardValue(i));
			nums[i].setFont(new Font(60));
			nums[i].setMinSize(70, 70);
			nums[i].setMaxSize(70, 70);
			nums[i].setAlignment(Pos.CENTER);
			final int clickedNum = i;
			nums[i].setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event arg0) {
					// TODO Auto-generated method stub
					playerPlays(clickedNum/size,clickedNum%size,clickedNum);
				}
			});
		}
		
		VBox layout = new VBox();
		
		HBox[] rows = new HBox[size];
		for(int i=0;i<size;i++){
			rows[i]=new HBox();
			rows[i].getChildren().add(nums[i*size]);
			for(int j=1;j<size;j++){
				rows[i].getChildren().addAll(new Separator(Orientation.VERTICAL),nums[j+i*size]);
			}
			rows[i].setAlignment(Pos.CENTER);
			rows[i].setSpacing(10);
		}
		
		layout.getChildren().add(rows[0]);
		for(int i=1;i<size;i++)
			layout.getChildren().addAll(new Separator(Orientation.HORIZONTAL),rows[i]);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(10);
		
		Scene scene = new Scene(layout,100*size,100*size);
		window.setScene(scene);
		window.show();
	}

	protected void playerPlays(int row, int col, int clickedNum) {
		// TODO Auto-generated method stub
		String overtitle = "Error";
		boolean over = true;
		board.mark(row, col, 'X');
		nums[clickedNum].setText("X");
		if(board.tie())
			overtitle = ("Its a tie");
		else if(board.win(row,col))
			overtitle = ("Player Wins");
		else{
			int cp = board.computersTurn();
			if(cp==-1){
				overtitle = ("Error\ncp = "+cp);
			}
			else{
				nums[cp].setText("O");
				board.mark(cp/size, cp%size, 'O');;
				if(board.win(cp/size,cp%size))
					overtitle = ("Computer Wins");
				else if(board.tie())
					overtitle = ("Its a tie");
				else
					over=false;
			}
		}
		if(over){
			VBox layout = new VBox();
			Label label = new Label(overtitle);
			label.setFont(new Font(40));
			
			layout.getChildren().addAll(label);
			layout.setAlignment(Pos.CENTER);
			Scene gameover = new Scene(layout,100*size,100*size);
			window.setScene(gameover);
		}
		
	}

	
}
