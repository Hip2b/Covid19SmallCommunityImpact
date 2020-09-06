import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Field implements Serializable {

	private static final Random rand = new Random();

	private int height, width;

	private Object[][] board;

	private HashMap<Class, ArrayList<Location>> animals;

	private int numberOfRows;
	private int numberOfColumns;

	static final int N = 0;
	static final int NE = 1;
	static final int E = 2;
	static final int SE = 3;
	static final int S = 4;
	static final int SW = 5;
	static final int W = 6;
	static final int NW = 7;
	static final int STAY = 8;
	static final int MIN_DIRECTION = 0;
	static final int MAX_DIRECTION = 7;

	public Field(int width, int height) {
		this.height = height;
		this.width = width;
		this.numberOfColumns = width;
		this.numberOfRows = height;
		board = new Object[width][height];
		animals = new HashMap<Class, ArrayList<Location>>();
	}

	public void clear() {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				board[col][row] = null;
			}
		}
	}

	public void put(Object obj, int col, int row) {
		put(obj, new Location(col, row));
	}

	public boolean isInGrid(Location loc) {
		return isInGrid(loc.getRow(), loc.getCol());
	}

	public boolean isInGrid(int row, int col) {
		return ((row >= 0) && (row < this.width) &&
				(col >= 0) && (col < this.height));
	}

	public void put(Object obj, Location location) {
		board[location.getCol()][location.getRow()] = obj;
	}

	public Object getObjectAt(Location location) {
		return getObjectAt(location.getCol(), location.getRow());
	}

	public Object getObjectAt(int col, int row) {
		return board[col][row];
	}

	public Location randomAdjacentLocation(Location location) {
		int row = location.getRow();
		int col = location.getCol();
		int nextRow = row + rand.nextInt(3) - 1;
		int nextCol = col + rand.nextInt(3) - 1;
		if (nextRow < 0 || nextRow >= height || nextCol < 0 || nextCol >= width) {
			return location;
		} else if (nextRow != row || nextCol != col) {
			return new Location(nextCol, nextRow);
		} else {
			return location;
		}
	}



	public Location freeAdjacentLocation(Location location) {
		List<Location> adjacent = adjacentLocations(location);
		for (Location next : adjacent) {
			if (board[next.getCol()][next.getRow()] == null) {
				return next;
			}
		}
		if (board[location.getCol()][location.getRow()] == null) {
			return location;
		} else {
			return null;
		}
	}

	public Location freeAdjacentLocation(int x, int y) {
		return freeAdjacentLocation(new Location(x, y));
	}

	public List<Location> adjacentLocations(Location location) {
		int row = location.getRow();
		int col = location.getCol();
		List<Location> locations = new LinkedList<Location>();
		for (int roffset = -1; roffset <= 1; roffset++) {
			int nextRow = row + roffset;
			if (nextRow >= 0 && nextRow < height) {
				for (int coffset = -1; coffset <= 1; coffset++) {
					int nextCol = col + coffset;
					// Exclude invalid locations and the original location.
					if (nextCol >= 0 && nextCol < width
							&& (roffset != 0 || coffset != 0)) {
						locations.add(new Location(nextCol, nextRow));
					}
				}
			}
		}
		Collections.shuffle(locations, rand);
		return locations;
	}

	public List<Location> adjacentLocations(int x, int y) {
		return adjacentLocations(new Location(x, y));
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	static int rowChange(int direction) {
		int change = 0;
		switch (direction) {
			case N:
			case NE:
			case NW:
				change = -1;
				break;
			case S:
			case SE:
			case SW:
				change = +1;
				break;
		}
		return change;
	}

	static int columnChange(int direction) {
		int change = 0;
		switch (direction) {
			case W:
			case NW:
			case SW:
				change = -1;
				break;
			case E:
			case NE:
			case SE:
				change = +1;
		}
		return change;
	}

	boolean isLegalLocation(int column, int row) {
		return row >= 0 && row < getHeight() && column >= 0
				&& column < getWidth();
	}

	boolean isLegalLocation(Location l) {
		return isLegalLocation(l.getCol(), l.getRow());
	}

	boolean isEmpty(int col, int row) {
		return this.board[col][row] == null;
	}

	boolean isEmpty(Location l) {
		return isEmpty(l.getRow(), l.getCol());
	}

	private Object look(int column, int row, int direction) {
		int rowDelta = rowChange(direction);
		int columnDelta = columnChange(direction);

		while (true) {
			row = row + rowDelta;
			column = column + columnDelta;
			if (!isLegalLocation(row, column))
				return null;
			if (board[column][row] != null)
				return board[column][row];
		}
	}

	public Object getObjectInDirection(Location l, int d) {
		return look(l.getCol(), l.getRow(), d);
	}

	private int distance(int row, int column, int direction) {
		int rowDelta = rowChange(direction);
		int columnDelta = columnChange(direction);

		int steps = 0;
		while (true) {
			row = row + rowDelta;
			column = column + columnDelta;
			steps++;
			if (!isLegalLocation(row, column) || board[column][row] != null) {
				return steps;
			}
		}
	}

	public int distanceToObject(Location l, int d) {
		return distance(l.getRow(), l.getCol(), d);
	}

	static int calculateNewDirection(int direction, int number) {
		int mod = (direction + number) % (MAX_DIRECTION - MIN_DIRECTION + 1);
		if (mod >= MIN_DIRECTION)
			return mod;
		else
			return 8 + mod;
	}
}