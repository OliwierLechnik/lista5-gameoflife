package org.example.gameoflife;

import java.util.ArrayList;

public class Grid {
    private static Grid instance;
    boolean started;
    private ArrayList<ArrayList<Cell>> grid;


    private Grid(){
        started = false;
        grid = new ArrayList<ArrayList<Cell>>();
        for(int i = 0; i < Settings.getSettings().getHeight(); i++){
            ArrayList<Cell> tmp = new ArrayList<Cell>();
            for(int j = 0; j < Settings.getSettings().getWidth(); j++){
                tmp.add(new Cell(i*Settings.getSettings().getWidth()+j));
            }
            grid.add(tmp);
        }
        for(int i = 0; i < Settings.getSettings().getHeight(); i++){
            for(int j = 0; j < Settings.getSettings().getWidth(); j++){
                grid.get(i).get(j).addNeighbor(grid
                        .get((Settings.getSettings().getHeight()+i-1)%Settings.getSettings().getHeight())
                        .get(j)
                );
                grid.get(i).get(j).addNeighbor(grid
                        .get((Settings.getSettings().getHeight()+i+1)%Settings.getSettings().getHeight())
                        .get(j)
                );
                grid.get(i).get(j).addNeighbor(grid
                        .get(i)
                        .get((Settings.getSettings().getWidth()+j+1)%Settings.getSettings().getWidth())
                );
                grid.get(i).get(j).addNeighbor(grid
                        .get(i)
                        .get((Settings.getSettings().getWidth()+j-1)%Settings.getSettings().getWidth())
                );
            }
        }
    }

    public void start(){
        if(!started){
            for(int i = 0; i < Settings.getSettings().getHeight(); i++){
                for(int j = 0; j < Settings.getSettings().getWidth(); j++){
                    grid.get(i).get(j).start();
                }
            }
        }
    }

    public Cell get(int row, int column){
        return grid.get(row).get(column);
    }

    public static Grid getInstance(){
        if(instance == null){
            instance = new Grid();
        }
        return instance;
    }
}
