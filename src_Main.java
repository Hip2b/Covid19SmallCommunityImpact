import processing.core.*;
import processing.core.PFont;
public class Main extends PApplet {
	float width = 1920;
	float height = 1040;
	Simulator simulator;
	String saveFilePath = "infectedesAndPeoplesSaved.txt";
	boolean paused = true;
	int mask = 0;

	PFont myFont;


	@Override
	public void setup() {

		int intWidth = (int)width;
		int intHeight = (int)height;

		size(intWidth, intHeight);

		this.simulator = new Simulator(170, 120);
		this.simulator.setGUI(this);
	}

	@Override
	public void draw() {
		background(80,80,80);
		fill(100,50,50);
		rect(width/14,height/12,(width/7)*6,(height/6)*5);

		fill(210,210,10);
		myFont = createFont("Georgia", 60);
		textFont(myFont);

		text("COVID Small Community Simulation",(width/2) - 470,(height/6));

		fill(0,0,0);
		myFont = createFont("Times New Roman", 30);
		textFont(myFont);
		text("This a simulation model displaying how fast COVID-19 spreads in a theoretical small to medium sized community, we used \n several pieces of official research to dictate the infection rates of the people in our modelled and took into account several factors \n such as age, position and their mask status ",width/10 - 20,height/3);
		fill(170,170,0);


		rect(1200,600,250,175);
		rect(400,600,250,175);

		fill(0,0,0);
		myFont = createFont("Arial", 60);
		textFont(myFont);

		text("Mask On", 1205,710);

		text("Mask Off", 405,710);


		if (!paused) {
			background(250);
			simulator.simulateOneStep();
			simulator.drawField();
			simulator.drawGraph();

			myFont = createFont("Georgia",52);
			textFont(myFont);
			fill(0,0,0);
			text("Legend",230,250);
			text("---------------------------",250,305);
			textSize(30);
			fill(255,0,0);
			text("Infected Population = Red",250,340);
			fill(0,200,0);

			text("Healthy Population = Green",255,410);
			fill(0,0,0);
			textSize(52);
			text("---------------------------",250,445);
			mouseClicked();

			textSize(60);
			fill(150,150,150);
			rect(97,47,300,130);
			fill(0,0,0);
			if(mask == 1){
				text("With Mask", 250,130);
			} else {
				text("No Mask", 255,130);
			}
		}

	}


	public void keyReleased () {
		if (key == 's') {
			// 's' saves the current state to a file
			simulator.writeToFile(saveFilePath);
		}

		if (key == 'l') {           // 'l' loads a saved state
			simulator.readFile(saveFilePath);
		}

		if (key == 'p') {           // 'p' toggles paused and unpaused
			paused = !paused;
		}

		if (key == 'r') {           // 'r' resets the simulator
			simulator.reset();
		}
	}

	// if mouse clicked, let the simulator handle the mouse click
	public void mouseClicked () {
		if(mouseX > 400 && mouseX < 850) {
			if(mouseY > 600 && mouseY < 775) {
				paused = false;
				People.mask(false);
				mask = 0;
			}
		}
		if(mouseX > 1200 && mouseX < 1650) {
			if(mouseY > 600 && mouseY < 775) {
				paused = false;
				People.mask(true);
				mask = 1;
			}
		}

	}


	// if mouse is dragged, let the simulator handle the mouse drag
	public void mouseDragged () {
		simulator.handleMouseDrag(mouseX, mouseY);
	}

	public static void main (String[]args){
		PApplet.main(new String[]{"Main"});
	}
}

