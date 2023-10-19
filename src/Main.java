import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GameManager gm = new GameManager();

        Group root = new Group();
        Scene scene = new Scene(root, Color.rgb(33, 33, 33));

        Image icon = new Image("images/knight.png");
        stage.getIcons().add(icon);
        stage.setTitle("Chess");
        stage.setResizable(false);

        Text text = new Text();
        text.setText("Welcome to Chess!");
        text.setX(50);
        text.setY(100);
        text.setFont(Font.font("Arial", 50));
        text.setFill(Color.WHITE);
        root.getChildren().add(text);
        root.getChildren().add(gm.getBoard());

        stage.setScene(scene);
        stage.show();
    }
}