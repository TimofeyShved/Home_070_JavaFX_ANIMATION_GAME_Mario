package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Character extends Pane{

    // загружаем картинку игрока
    Image marioImg = new Image(getClass().getResourceAsStream("image/mario.png"));
    ImageView imageView = new ImageView(marioImg);

    int count = 3; // количество спрайтов
    int columns = 3; // сколько вообще колонок
    int offsetX = 96; // откуда по осям
    int offsetY = 33;
    int width = 16; // размеры
    int height = 16;

    public SpriteAnimated animation; // ссылка для будущей анимации
    public Point2D playerVelocity = new Point2D(0,0); // точка игрока (место по оси у)
    private boolean canJump = true;

    public Character(){
        imageView.setFitHeight(40); // размеры картинки игрока
        imageView.setFitWidth(40);
        imageView.setViewport(new Rectangle2D(offsetX,offsetY,width,height)); // отрисовываем

        // добавляем анимацию
        animation = new SpriteAnimated(this.imageView,Duration.millis(200),count,columns,offsetX,offsetY,width,height);
        getChildren().addAll(this.imageView);// добавляем на панель
    }

    // -------------------------------------------------- передвижение по оси Х--------------
    public void moveX(int value){
        boolean movingRight = value > 0; // приходящее значение > 0, true - вправо, иначе влево
        for(int i = 0; i<Math.abs(value); i++) {
            for (Block platform : Main.platforms) { // проходимся по всеб блокам на панели
                if(this.getBoundsInParent().intersects(platform.block.getBoundsInParent())) { // если игрок и они пересикаются?
                    if (movingRight) { // вправо?
                        if (this.getTranslateX() + Main.MARIO_SIZE == platform.block.getTranslateX()){ // перед нами проходимы блок?
                            this.setTranslateX(this.getTranslateX() - 1);
                            return;
                        }
                    } else { // иначе влево
                        if (this.getTranslateX() == platform.block.getTranslateX() + Main.BLOCK_SIZE) {
                            this.setTranslateX(this.getTranslateX() + 1);
                            return;
                        }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1)); // движение
        }
    }

    // -------------------------------------------------- передвижение по оси У --------------
    public void moveY(int value){
        boolean movingDown = value > 0; // приходящее значение > 0, true - вниз, иначе вверх
        for(int i = 0; i < Math.abs(value); i++){
            for(Block platform : Main.platforms){

                // если наша картинка соприкасается размерами с квадратом, то...
                if(this.getBoundsInParent().intersects(platform.block.getBoundsInParent())){
                    if(movingDown){ // если движение вниз
                        //если наши координаты + размер  == координатом твёрдого блока, то...
                        if(this.getTranslateY()+ Main.MARIO_SIZE == platform.block.getTranslateY()){
                            this.setTranslateY(this.getTranslateY()-1); // сдвинуть вверх на 1
                            canJump = true; // разрешить прыгать
                            return; // завершить действие в этом методе
                        }
                    }
                    else{ // иначе, если бъём камни головой
                        // если наши координаты + размер  == координатом твёрдого блока, то...
                        if(this.getTranslateY() == platform.block.getTranslateY() + Main.BLOCK_SIZE){
                            this.setTranslateY(this.getTranslateY()+1); // сдвинуть вниз на 1
                            playerVelocity = new Point2D(0,10);
                            return; // завершить действие в этом методе
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown?1:-1)); // небыло блока? ну тогда действуй! =D
            if(this.getTranslateY()>640){  // если под нами пусто было долго, упали в пропость (Т.Т)
                this.setTranslateX(0);
                this.setTranslateY(400);
                Main.gameRoot.setLayoutX(0);
            }
        }
    }
    public void jumpPlayer(){
        if(canJump){ // он прыгнул?
            this.playerVelocity = playerVelocity.add(0,-30); // поднять по оси У - вверх на 30
            this.canJump = false;
        }
    }
}