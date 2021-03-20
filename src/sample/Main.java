package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {

    // списки/коллекции блоков и нажатий
    public static ArrayList<Block> platforms = new ArrayList<>();
    private HashMap<KeyCode,Boolean> keys = new HashMap<>();

    // картинка на задний фон
    Image backgroundImg = new Image(getClass().getResourceAsStream("image/background.png"));
    public static final int BLOCK_SIZE = 45; // размеры блоков
    public static final int MARIO_SIZE = 40; // размер марио

    public static Pane appRoot = new Pane(); // создание 2 панелей
    public static Pane gameRoot = new Pane();

    public Character player; // создание игрока
    int levelNumber = 0; // выбор уровня
    private int levelWidth; // размер уровня

    // ----------------------------------------------------------------------------------- загрузка контента, карты и блоков -----------------------
    private void initContent(){
        // выбор картинки на задний фон (отображение)
        ImageView backgroundIV = new ImageView(backgroundImg);
        backgroundIV.setFitHeight(14*BLOCK_SIZE); // размеры
        backgroundIV.setFitWidth(212*BLOCK_SIZE);

        // подгрузка расположения блоков на карте
        levelWidth = LevelData.levels[levelNumber][0].length()*BLOCK_SIZE;
        for(int i = 0; i < LevelData.levels[levelNumber].length; i++){ // пробегаемся по ней
            String line = LevelData.levels[levelNumber][i];
            for(int j = 0; j < line.length();j++){ // до конца строчки
                switch (line.charAt(j)){
                    case '0':
                        break; // выборка значения и его инициализация
                    case '1':
                        Block platformFloor = new Block(Block.BlockType.PLATFORM, j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '2':
                        Block brick = new Block(Block.BlockType.BRICK,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case '3':
                        Block bonus = new Block(Block.BlockType.BONUS,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case '4':
                        Block stone = new Block(Block.BlockType.STONE,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '5':
                        Block PipeTopBlock = new Block(Block.BlockType.PIPE_TOP,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '6':
                        Block PipeBottomBlock = new Block(Block.BlockType.PIPE_BOTTOM,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '*':
                        Block InvisibleBlock = new Block(Block.BlockType.INVISIBLE_BLOCK,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                }
            }
        }

        // создание игрока
        player =new Character();
        player.setTranslateX(0);
        player.setTranslateY(400); // его расположение

        // камера за играком
        player.translateXProperty().addListener((obs,old,newValue)->{
            int offset = newValue.intValue();
            if(offset>640 && offset<levelWidth-640){
                gameRoot.setLayoutX(-(offset-640));
                backgroundIV.setLayoutX(-(offset-640));
            }
        });

        // добавление на панель
        gameRoot.getChildren().add(player);
        appRoot.getChildren().addAll(backgroundIV,gameRoot);
    }

    // -------------------------------------------------------------------- обработка нажатий на кнопку ------------------------------
    private void update(){
        if(isPressed(KeyCode.UP) && player.getTranslateY()>=5){ // вверх
            player.jumpPlayer(); // запуск прыжка
        }
        if(isPressed(KeyCode.LEFT) && player.getTranslateX()>=5){ // влево
            player.setScaleX(-1);
            player.animation.play();
            player.moveX(-5);
        }
        if(isPressed(KeyCode.RIGHT) && player.getTranslateX()+40 <=levelWidth-5){ // вправо
            player.setScaleX(1);
            player.animation.play();
            player.moveX(5);
        }

        // а это если вы падаете вниз
        if(player.playerVelocity.getY()<10){
            player.playerVelocity = player.playerVelocity.add(0,1);// записывает не нулевое значение
        }
        player.moveY((int)player.playerVelocity.getY()); // что бы запустилось падение
    }
    // обработка нажатия
    private boolean isPressed(KeyCode key){
        return keys.getOrDefault(key,false);
    }

    // -------------------------------------------------------------------- запуск программы----------------------------
    @Override
    public void start(Stage primaryStage) throws Exception {
        initContent(); // загрузка контента

        // создание сцены
        Scene scene = new Scene(appRoot,1200,620);

        // обработка нажатий
        scene.setOnKeyPressed(event-> keys.put(event.getCode(), true)); // нажали на кнопку
        scene.setOnKeyReleased(event -> { // отпустили =D
            keys.put(event.getCode(), false);
            player.animation.stop();
        });

        primaryStage.setTitle("Mini Mario"); // заголовок формы
        primaryStage.setScene(scene); // добавление сцены на форму
        primaryStage.show();

        // запуск таймера
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(); // обновление, это метод выше
            }
        };
        timer.start(); // запуск таймера
    }
    public static void main(String[] args) {
        launch(args);
    }
}