package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Collection;

public class Block extends Pane {

    // подгружаем картикни блоков
    Image blocksImg = new Image(getClass().getResourceAsStream("image/1.png"));
    ImageView block;

    // даём им названия (их будут использовать в Main)
    public enum BlockType{
        PLATFORM, BRICK, BONUS, PIPE_TOP, PIPE_BOTTOM, INVISIBLE_BLOCK, STONE
    }

    // конструктор
    public Block(BlockType blockType, int x, int y) {

        block = new ImageView(blocksImg);
        block.setFitWidth(Main.BLOCK_SIZE); //размеры и место положение
        block.setFitHeight(Main.BLOCK_SIZE);
        this.block.setTranslateX(x);
        this.block.setTranslateY(y);

        switch (blockType) { // в зависимости от enum, закрашиваем в нужную картинку
            case PLATFORM:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                break;
            case BRICK:
                block.setViewport(new Rectangle2D(16, 0, 16, 16));
                break;
            case BONUS:
                block.setViewport(new Rectangle2D(384, 0, 16, 16));
                break;
            case PIPE_TOP:
                block.setViewport(new Rectangle2D(0, 128, 32, 16));
                block.setFitWidth(Main.BLOCK_SIZE * 2);
                break;
            case PIPE_BOTTOM:
                block.setViewport(new Rectangle2D(0, 145, 32, 14));
                block.setFitWidth(Main.BLOCK_SIZE * 2);
                break;
            case INVISIBLE_BLOCK:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                block.setOpacity(0);
                break;
            case STONE:
                block.setViewport(new Rectangle2D(0, 16, 16, 16));
                break;
        }

        getChildren().add(block); // добавляем блок внутрь класса
        Main.platforms.add(this); // а так же в списки
        Main.gameRoot.getChildren().add(this.block); // добавляем блок на панель
    }

}
