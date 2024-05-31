package org.example.gameoflife;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Cell {

    private final int ID;
    private final ArrayList<Cell> neighbors;
    private boolean frozen;
    private Thread loop;
    private Color color;

    /**
     * Constructs a new Cell instance with a unique ID, initial state, and random color.
     * The constructor initializes the cell with the following properties:
     * - The unique identifier of the cell is set to the provided ID.
     * - The cell's state is set to active (not frozen).
     * - The loop thread is initialized as null.
     * - An empty list of neighboring cells is created.
     * - The cell's color is set to a random RGB value.
     *
     * @param ID The unique identifier for the new cell.
     */
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

    /**
     * Initiates the cell's activity process, which includes random sleep intervals and color calculations.
     * The method creates a new thread that runs indefinitely, performing the following actions:
     * 1. Calculates a random sleep time based on the cell's speed settings.
     * 2. Sleeps for the calculated time, or for a short interval if the cell is frozen.
     * 3. Gathers colors from non-frozen neighboring cells.
     * 4. Determines the new color of the cell based on the gathered colors and a probability check:
     *    - If no colors are gathered, no action is taken.
     *    - If one color is gathered, the cell adopts this color.
     *    - If multiple colors are gathered, the cell adopts the average color.
     *    - With a certain probability, the cell may also choose a random color.
     * 5. Handles exceptions by logging the error and terminating the program.
     * The thread is named after the cell's ID and is started immediately.
     *
     * @throws Exception if an error occurs during the thread's waiting, which logs the error and exits the program.
     */
    void start(){
        System.out.println("Start: " + ID);
        loop = new Thread(() -> {
            while(true){
                try{

                    int t = RandomUtil.random.nextInt(
                            Settings.getSettings().getSpeed()/2,
                            Settings.getSettings().getSpeed()*3/2
                    );

                    TimeUnit.MILLISECONDS.sleep(t);

                    if(frozen){
                        TimeUnit.MILLISECONDS.sleep(10);
                        continue;
                    }

                }catch (Exception e){
                    System.err.println("Skill issue w Cell::start() o ID=" + ID);
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    System.exit(-1);
                }

                ArrayList<Color>colors = new ArrayList<>();
                for(int i = 0; i < neighbors.size(); i++){
                    if(neighbors.get(i).isNotFrozen()){
                        colors.add(neighbors.get(i).readWriteColor(null));
                    }
                }

                if(RandomUtil.random.nextDouble() > Settings.getSettings().getProbability()){
                    calcNewColor(colors);
                }else {
                    readWriteColor(Color.color(
                            RandomUtil.random.nextDouble(),
                            RandomUtil.random.nextDouble(),
                            RandomUtil.random.nextDouble()
                            ));
                }


            }
        },"Cell " + ID);
        loop.start();
    }

    private void calcNewColor(ArrayList<Color> colors) {
        if (colors.isEmpty()){
            /* do nothing */
            return;
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
    }

    /**
     * Performs a read and/or write operation on the cell's color.
     * If a new color is provided, it updates the cell's color with the new value.
     * Regardless of the update, it returns the previous color of the cell.
     *
     * @param newColor The new color to be set for the cell; if null, no new color is set.
     * @return The previous color of the cell before any update.
     */
    public synchronized Color readWriteColor(Color newColor){
        Color prev = color;
        if(newColor != null){
            color = newColor;
        }
        return prev;
    }

    /**
     * Adds a neighboring cell to the list of neighbors.
     * This method is used to maintain a list of adjacent cells for color calculation purposes.
     *
     * @param cell The neighboring cell to be added.
     */
    public void addNeighbor(Cell cell){
        neighbors.add(cell);
    }

    /**
     * Checks if the cell is currently not frozen.
     * This method is typically used to determine if the cell can participate in color updates.
     *
     * @return true if the cell is not frozen, false otherwise.
     */
    public boolean isNotFrozen() {
        return !frozen;
    }

    /**
     * Toggles the frozen state of the cell and prints the state change to the console.
     * If the cell becomes frozen, it prints "Stop: [ID]".
     * If the cell becomes active, it prints "Start: [ID]".
     */
    public void switchState(){
        frozen = !frozen;
        if(frozen){
            System.out.println("Stop: " + ID);
        }else{
            System.out.println("Start: " + ID);
        }
    }
}
