package org.example.gameoflife;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Cell {

    private int ID;
    private ArrayList<Cell> neighbors;
    private boolean frozen;
    private Thread loop;
    private Color color;

    public Cell(int ID){
        this.ID = ID;
        frozen = false;
        loop = null;
        neighbors = new ArrayList<Cell>();
        color = Color.rgb(
                RandomUtil.random.nextInt(256),
                RandomUtil.random.nextInt(256),
                RandomUtil.random.nextInt(256)
        );
    }

    void start(){
        System.out.println("Start: " + ID);
        loop = new Thread(() -> {
            while(true){
                try{
                    int t = RandomUtil.random.nextInt(
                            Settings.getSettings().getSpeed()/2,
                            Settings.getSettings().getSpeed()*3/2
                    );
//                    System.out.println("chosen to sleep t=" + t +"ms");
                    TimeUnit.MILLISECONDS.sleep(t);

                    ArrayList<Color>colors = new ArrayList<>();
                    for(int i = 0; i < neighbors.size(); i++){
                        if(neighbors.get(i).isNotFrozen()){
                            colors.add(neighbors.get(i).readWriteColor(null));
                        }
                    }

                    if(RandomUtil.random.nextDouble() < Settings.getSettings().getProbability()){
                        if (colors.size() == 0){

                        } else if (colors.size() == 1) {
                            readWriteColor(colors.getFirst());
                        } else {
                            double r = 0;
                            double g = 0;
                            double b = 0;
                            for(int i = 0; i < colors.size(); i++){
                                r += colors.get(i).getRed();
                                g += colors.get(i).getGreen();
                                b += colors.get(i).getBlue();
                            }
                            r /= colors.size();
                            g /= colors.size();
                            b /= colors.size();
                            readWriteColor(Color.color(r,g,b));
                        }
                    }else {
                        readWriteColor(Color.color(
                                RandomUtil.random.nextDouble(),
                                RandomUtil.random.nextDouble(),
                                RandomUtil.random.nextDouble()
                                ));
                    }


                }catch (Exception e){
                    System.err.println("Skill issue w Cell::start() o ID=" + ID);
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        });
        loop.start();
    }

    public void wave(){
        System.out.println("Cell::wave() ID=" + ID);
        readWriteColor(Color.RED);
        try{
            TimeUnit.MILLISECONDS.sleep(150);
        }catch (Exception e){
            System.err.println("skill issue w Cell::wave() o ID=" + ID);
            System.exit(-1);
        }
        for(Cell cell : neighbors){
            if(cell.readWriteColor(null).equals(Color.RED)){
                continue;
            }
            (new Thread(()->{
                cell.wave();
            })).start();
        }
    }

    /**
     * updates its color with given color and returns previous color.
     * if the given color is null, the color is not updated
     * @param newColor new color
     * @return current color
     */
    public synchronized Color readWriteColor(Color newColor){
        Color prev = color;
        if(newColor != null){
            color = newColor;
        }
        return prev;
    }

    public void addNeighbor(Cell cell){
        neighbors.add(cell);
    }

    public boolean isNotFrozen() {
        return !frozen;
    }

    public void switchState(){
        frozen = !frozen;
    }
}
