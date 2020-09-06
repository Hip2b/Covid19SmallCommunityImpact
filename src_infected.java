import java.io.Serializable;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class infected implements Serializable {
	private int age;
	private int totalHour;
	private double movementFreedom = 0.95;
	private double moveChance;
	private boolean personInfected = false;
	private int infectedAge;
	private double deathChance,deathRand;
	private boolean alive;
	private Location location;

	public infected(boolean startWithRandomAge) {
		totalHour = 0;
		alive = true;

	}

	public void hunt(Field currentField, Field updatedField, List<infected> babyinfectedStorage, List<People> babyPeopleStorage, int hour) {
		totalHour ++;

		if (totalHour > 336) {
			deathRand = (double) (Math.random());

			if (deathRand > deathChance) {
				People newPeople = new People(false);
				babyPeopleStorage.add(newPeople);
				Location loc = updatedField.randomAdjacentLocation(location);
				newPeople.setLocation(loc);
				updatedField.put(newPeople, loc);
				alive = false;
			} else {
				alive = false;
			}

		}

		if (alive) {

			if(personInfected) {
				infected newinfected = new infected(false);
				babyinfectedStorage.add(newinfected);
				Location loc = updatedField.randomAdjacentLocation(location);
				newinfected.setLocation(loc);
				newinfected.setAge(infectedAge);
				updatedField.put(newinfected, loc);
				personInfected = false;
			}
			Location newLocation = findFood(currentField, location);
			if (hour < 17) {
				if (newLocation == null) {
					moveChance = (double) (Math.random());
					if(moveChance > movementFreedom) {
						newLocation = updatedField.freeAdjacentLocation(location);
					} else {
						newLocation = location;
					}
				}
			} else {
				newLocation = location;
			}
			if (newLocation != null) {
				setLocation(newLocation);
				updatedField.put(this, newLocation);
			} else {
				alive = false;
			}
		}
	}


	private Location findFood(Field field, Location location) {
		List<Location> adjacentLocations = field.adjacentLocations(location);

		for (Location where : adjacentLocations) {
			Object organism = field.getObjectAt(where);
			if (organism instanceof People) {
				People People1 = (People) organism;
				if (People1.isAlive()) {
					personInfected = People1.setInfected();
					deathChance = People1.getDeathRate();
					infectedAge = People1.getAge();
				}
			}
		}

		return null;
	}

	public void setAge(int A) {
		age = A;
	}

	public int getAge() {
		return age;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setLocation(int row, int col) {
		this.location = new Location(row, col);
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}

