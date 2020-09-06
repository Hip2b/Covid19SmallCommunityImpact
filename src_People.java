import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class People implements Serializable {
    private double movementFreedom = 0.8;
    private double moveChance;
    private static double ageRandPercent;
    private Location newLocation;
    private double infecRand;
    private static int age;
    private boolean alive;
    private static double infecFactorAge;
    private boolean maskOn = true;
    private static double deathRate;
    private double infecFactorDistance;
    private static double infecChance;
    private Location location;
    private static boolean infecFactorMask;
    private double maskEffect = (Math.random() * .26) + .49;


    public People(boolean startWithRandomAge)
    {
        ageGenerator();
        alive = true;

    }
    public static boolean mask(boolean mask) {
        if(mask = true) {
            infecFactorMask = true;
        } else {
            infecFactorMask = false;
        }
        return infecFactorMask;
    }


    public void run(Field updatedField, List<People> babyPeopleStorage, int hour) {

        if(alive) {

            if (hour < 17) {
                moveChance = (double) (Math.random());
                if(moveChance > movementFreedom) {
                    newLocation = updatedField.freeAdjacentLocation(location);
                } else {
                    newLocation = location;
                }
            } else {
                newLocation = location;
            }
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.put(this, newLocation);
            }
            else {
                alive = false;
            }
        }
    }

    public static void ageGenerator() {
        ageRandPercent = (double) (Math.random());

        if (ageRandPercent < 0.058) {
            age = (int) (1 + Math.random() * 4);
            infecFactorAge = 0.022;
            deathRate = 0;
        } else if (ageRandPercent < 0.225) {
            age = (int) (5 + Math.random() * 12);
            infecFactorAge = 0.08;
        } else if (ageRandPercent < 0.468) {
            age = (int) (18 + Math.random() * 16);
            infecFactorAge = 0.352;
        } else if (ageRandPercent < 0.661) {
            age = (int) (35 + Math.random() * 14);
            infecFactorAge = 0.249;
            deathRate = 0.058;
        } else if (ageRandPercent < 0.785) {
            age = (int) (50 + Math.random() * 9);
            infecFactorAge = 0.139;
            deathRate = 0.104;
        } else if (ageRandPercent < 0.844) {
            age = (int) (60 + Math.random() * 4);
            infecFactorAge = 0.05;
            deathRate = 0.082;
        } else if (ageRandPercent < 0.894) {
            age = (int) (65 + Math.random() * 4);
            infecFactorAge = 0.035;
            deathRate = 0.102;
        } else if (ageRandPercent < 0.961) {
            age = (int) (70 + Math.random() * 9);
            infecFactorAge = 0.041;
            deathRate = 0.114;
        } else if (ageRandPercent < 1) {
            age = (int) (80 + Math.random() * 19);
            infecFactorAge = 0.034;
            deathRate = 0.413;
        }
    }

    public double getDeathRate() {
        return deathRate;
    }

    public void infectionChance() {
        if(infecFactorMask) {
            infecChance = infecFactorAge*maskEffect;
        } else {
            infecChance = infecFactorAge;
        }

    }


    public boolean isAlive()
    {
        return alive;
    }

    public int getAge() {
        return age;
    }


    public boolean setInfected() {
        infecRand = (double) (Math.random() * 1);

        infectionChance();
        if (infecChance > infecRand) {
            alive = false;

            return true;
        } else {
            return false;
        }
    }

    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }


    public void setLocation(Location location)
    {
        this.location = location;
    }
}
