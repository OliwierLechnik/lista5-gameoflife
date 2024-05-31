package org.example.gameoflife;

import java.util.ArrayList;

public class Grid {
    private static Grid instance;
    boolean started;
    private ArrayList<ArrayList<Cell>> grid;

    /**
     * Private constructor for creating a singleton Grid instance.
     * It initializes the grid with a specified number of rows and columns from Settings,
     * and populates it with Cell instances. Each cell is then assigned neighbors
     * wrapping around the grid to form a toroidal shape (edges connected like a donut).
     */
    private Grid(){
        started = false;
        grid = new ArrayList<ArrayList<Cell>>();
        for(int i = 0; i < Settings.getSettings().getRows(); i++){
            ArrayList<Cell> tmp = new ArrayList<Cell>();
            for(int j = 0; j < Settings.getSettings().getColumns(); j++){
                tmp.add(new Cell(i*Settings.getSettings().getColumns()+j));
            }
            grid.add(tmp);
        }
        for(int i = 0; i < Settings.getSettings().getRows(); i++){
            for(int j = 0; j < Settings.getSettings().getColumns(); j++){
                grid.get(i).get(j).addNeighbor(grid
                        .get((Settings.getSettings().getRows()+i-1)%Settings.getSettings().getRows())
                        .get(j)
                );
                grid.get(i).get(j).addNeighbor(grid
                        .get((Settings.getSettings().getRows()+i+1)%Settings.getSettings().getRows())
                        .get(j)
                );
                grid.get(i).get(j).addNeighbor(grid
                        .get(i)
                        .get((Settings.getSettings().getColumns()+j+1)%Settings.getSettings().getColumns())
                );
                grid.get(i).get(j).addNeighbor(grid
                        .get(i)
                        .get((Settings.getSettings().getColumns()+j-1)%Settings.getSettings().getColumns())
                );
            }
        }
    }

    /**
     * Starts the activity of each cell in the grid if the grid has not already been started.
     */
    public void start(){
        if(!started){
            for(int i = 0; i < Settings.getSettings().getRows(); i++){
                for(int j = 0; j < Settings.getSettings().getColumns(); j++){
                    grid.get(i).get(j).start();
                }
            }
        }
        started = true;
    }

    /**
     * Retrieves a specific cell from the grid based on its row and column indices.
     *
     * @param row The row index of the cell to retrieve.
     * @param column The column index of the cell to retrieve.
     * @return The Cell object at the specified row and column.
     */
    public Cell get(int row, int column){
        return grid.get(row).get(column);
    }

    /**
     * Provides access to the singleton instance of the Grid class.
     * If no instance exists, it creates one. This ensures that only one instance of the grid is used throughout the application.
     *
     * @return The single instance of the Grid class.
     */
    public static Grid getInstance(){
        if(instance == null){
            instance = new Grid();
        }
        return instance;
    }
}
