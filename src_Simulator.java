import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import processing.core.PApplet;

public class Simulator {
	private static final int DEFAULT_WIDTH = 170;

	private static final int DEFAULT_HEIGHT = 170;


	private static final double infected_CREATION_PROBABILITY = 0.0017;

	private static final double People_CREATION_PROBABILITY = 0.08;


	private ArrayList<People> PeopleList;
	private ArrayList<infected> infectedList;

	private Field field;

	private Field updatedField;

	private int step;

	private int hour;

	private FieldDisplay view;

	private Graph graph;

	private PApplet graphicsWindow;

	private FieldStats stats;


	public Simulator() {
		this(DEFAULT_HEIGHT, DEFAULT_WIDTH);
	}


	public Simulator(int width, int height) {
		if (width <= 0 || height <= 0) {
			System.out.println("The dimensions must be greater than zero.");
			System.out.println("Using default values.");
			height = DEFAULT_HEIGHT;
			width = DEFAULT_WIDTH;
		}

		PeopleList = new ArrayList<People>();
		infectedList = new ArrayList<infected>();
		field = new Field(width, height);
		updatedField = new Field(width, height);
		stats = new FieldStats();

		// Setup a valid starting point.
		reset();
	}

	public void setGUI(PApplet p, int x, int y, int display_width, int display_height) {
		this.graphicsWindow = p;

		// Create a view of the state of each location in the field.
		view = new FieldDisplay(p, this.field, x + 500, y + 20, (display_width * 2) + 600, display_height + 230);
		view.setColor(People.class, p.color(0, 255, 0));
		view.setColor(infected.class, p.color(255, 0, 0));

		graph = new Graph(p, 100, p.height - 80, p.width , p.height - 160, 0,
				0, 500, 2000);

		graph.title = "Infected People vs. Uninfected People Populations";
		graph.xlabel = "Time (in hours)";
		graph.ylabel = "Pop.     ";
		graph.setColor(People.class, p.color(0, 255, 0));
		graph.setColor(infected.class, p.color(255, 0, 0));
	}

	public void setGUI(PApplet p) {
		setGUI(p, 10, 10, p.width - 10, 400);
	}

	public void runLongSimulation() {
		simulate(500);
	}

	public void simulate(int numSteps) {
		for (int step = 1; step <= numSteps && isViable(); step++) {
			simulateOneStep();
		}
	}

	public void simulateOneStep() {
		step++;
		hour ++;

		if (hour >= 24) {
			hour = 0;
		}
		// New List to hold newborn PeopleList.
		ArrayList<People> babyPeopleStorage = new ArrayList<People>();

		ArrayList<infected> babyinfectedStorage = new ArrayList<infected>();

		// Loop through all Peoples. Let each run around.
		for (int i = 0; i < PeopleList.size(); i++) {
			People People = PeopleList.get(i);
			People.run(updatedField, babyPeopleStorage,hour);
			if (!People.isAlive()) {
				PeopleList.remove(i);
				i--;
			}
		}


		for (int i = 0; i < infectedList.size(); i++) {
			infected infected = infectedList.get(i);
			infected.hunt(field, updatedField, babyinfectedStorage,babyPeopleStorage, hour);
			if (!infected.isAlive()) {
				infectedList.remove(i);
				i--;
			}
		}

		PeopleList.addAll(babyPeopleStorage);
		infectedList.addAll(babyinfectedStorage);

		Field temp = field;
		field = updatedField;
		updatedField = temp;
		updatedField.clear();

		stats.generateCounts(field);
		updateGraph();
	}

	public void updateGraph() {
		Counter count;
		for (Counter c : stats.getCounts()) {
			graph.plotPoint(step, c.getCount(), c.getClassName());
		}
	}


	public void reset() {
		step = 0;
		PeopleList.clear();
		infectedList.clear();
		field.clear();
		updatedField.clear();
		initializeBoard(field);

		if (graph != null) {
			graph.clear();
			graph.setDataRanges(0, 500, 0, field.getHeight() * field.getWidth());
		}


	}


	private void initializeBoard(Field field) {
		Random rand = new Random();
		field.clear();
		for (int row = 0; row < field.getHeight(); row++) {
			for (int col = 0; col < field.getWidth(); col++) {
				if (rand.nextDouble() <= infected_CREATION_PROBABILITY) {
					infected infected = new infected(true);
					infected.setLocation(col, row);
					infectedList.add(infected);
					field.put(infected, col, row);
				} else if (rand.nextDouble() <= People_CREATION_PROBABILITY) {
					People People = new People(true);
					People.setLocation(col, row);
					PeopleList.add(People);

					field.put(People, col, row);
				}
			}
		}
		Collections.shuffle(PeopleList);
		Collections.shuffle(infectedList);
	}

	private boolean isViable() {
		return stats.isViable(field);
	}

	public Field getField() {
		return this.field;
	}

	public void drawField() {
		if ((graphicsWindow != null) && (view != null)) {
			view.drawField(this.field);
		}
	}

	public void drawGraph() {
		graph.draw();
	}

	public void writeToFile(String writefile) {
		try {
			Record r = new Record(PeopleList, infectedList, this.field, this.step);
			FileOutputStream outStream = new FileOutputStream(writefile);
			ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
			objectOutputFile.writeObject(r);
			objectOutputFile.close();
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e.getMessage());
		}
	}

	public void readFile(String readfile) {
		try {
			FileInputStream inputStream = new FileInputStream(readfile);
			ObjectInputStream objectInputFile = new ObjectInputStream(inputStream);
			Record r = (Record) objectInputFile.readObject();
			setinfectedList(r.getinfectedes());
			setPeopleList(r.getPeoples());
			setField(r.getField());
			setStep(r.getSteps());
			objectInputFile.close();
			// clear field
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e.getMessage());
		}
	}


	private void setStep(int steps) {
		step = steps;
	}


	private void setField(Field newField) {
		field = newField;
	}


	private void setPeopleList(ArrayList<People> newPeopleList) {
		PeopleList = newPeopleList;
	}


	private void setinfectedList(ArrayList<infected> newinfectedesList) {
		infectedList = newinfectedesList;
	}


	public void handleMouseClick(float mouseX, float mouseY) {
		Location loc = view.gridLocationAt(mouseX, mouseY);
		if (loc == null) return;

		for (int x = loc.getCol() - 8; x < loc.getCol() + 8; x++) {
			for (int y = loc.getRow() - 8; y < loc.getRow() + 8; y++) {
				Location locToCheck = new Location(x, y);
				if (field.isInGrid(locToCheck)) {
					Object organism = field.getObjectAt(locToCheck);
					if (organism instanceof People)
						PeopleList.remove((People) organism);
					if (organism instanceof infected)
						infectedList.remove((infected) organism);
					field.put(null, locToCheck);
					updatedField.put(null, locToCheck);
				}
			}
		}
	}

	private void handleMouseClick(Location l) {
		System.out.println("Change handleMouseClick in Simulator.java to do something!");
	}

	public void handleMouseDrag(int mouseX, int mouseY) {
		Location loc = this.view.gridLocationAt(mouseX, mouseY);
		if (loc == null)
			return;
		handleMouseDrag(loc);
	}

	private void handleMouseDrag(Location l) {
		System.out.println("Change handleMouseDrag in Simulator.java to do something!");
	}
}
