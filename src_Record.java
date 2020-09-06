
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Record implements Serializable{
	private ArrayList<People> Peoples;
	private ArrayList<infected> infectedes;
	private int steps;

	private Field field;

	public Record(ArrayList<People> r, ArrayList<infected> f, Field field, int step){
		setPeoples(r);
		setinfectedes(f);
		setField(field);
		setSteps(step);
	}
	public ArrayList<People> getPeoples() {
		return Peoples;
	}
	public void setPeoples(ArrayList<People> Peoples) {
		this.Peoples = Peoples;
	}
	public ArrayList<infected> getinfectedes() {
		return infectedes;
	}
	public void setinfectedes(ArrayList<infected> infectedes) {
		this.infectedes = infectedes;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
}
