import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DoublePendulum extends PApplet {

//Arm lengths
int r1 = 200;
int r2 = 200;

//Masses of the Weights
float m1 = 40;
float m2 = 40;

//Angles of arms, set inital conditions here
float a1 = PI;
float a2 = PI/8;

//Angular Velocity and Acceleration
float a1_v = 0;
float a2_v = 0;
float a1_a = 0;
float a2_a = 0;

//Gravity Constant
float g = 1;

//Holds previous posiiton of end of pendulum to draw a line segment
float prevx2 = 0;
float prevy2 = 0;

//Coordinates for the origin of the pendulum.
float centerX;
float centerY;

//Used for drawing the line segments
PGraphics canvas;

//Add dampening to velocity changes
boolean useDampening = true;
float dampeningFactor = 0.999998f;

int windowWidth = 900;
int windowHeight = 900;

public void setup(){
 
 canvas = createGraphics(900, 900);
 centerX = width/2;
 centerY = height - (r1 + r2) - (height/10);
  canvas.beginDraw();
  canvas.background(0);
  canvas.endDraw();
}

public void draw(){
  
  //Formulas for changing angular accleration
  a1_a = ((g * -1)*(2*m1+m2)*sin(a1) - (m2 * g * sin(a1-a2)) - 2*sin(a1-a2)*m2*(a2_v*a2_v* r2 + a1_v*a1_v * r1 * cos(a1-a2)))/(r1 *(2 * m1 + m2 - m2 * cos(2 * a1 - 2*a2)));
  a2_a = ( 2*sin(a1-a2) * (a1_v* a1_v * r1 * (m1+m2) + g * (m1+m2) * cos(a1) + a2_v*a2_v * r2 * m2 * cos(a1-a2)))/(r2 * (2*m1 + m2 - m2*cos(2*a1 - 2*a2)));

  //Initializes canvas 
  image(canvas, 0,0);
  
  //Settings for drawing pendulum arms
  stroke(255);
  strokeWeight(2);
  
  //First Pendulum
  translate(centerX,centerY);
  float x1 = r1 * sin(a1);
  float y1 = r1 * cos(a1);
  line (0,0,x1,y1);
  fill(255);
  ellipse(x1,y1,m1,m1);
  
  //Second Pendulum
  float x2 = x1 + r2 * sin(a2);
  float y2 = y1+ r1 * cos(a2);
  line (x1,y1,x2,y2);
  fill((int)map(x2, -r2, r2, 0, 255), 255 - (int) map(y2, -centerY - r2, centerY + r2 , 0, 255), (int) map(y2, -centerY, height -centerY, 0, 255));
  ellipse(x2,y2,m2,m2);
  
  //Changing velocities and angles
  a1_v += a1_a;
  a2_v += a2_a;
  a1  += a1_v;
  a2 += a2_v;
  
  if(useDampening){
    a1_v *= dampeningFactor;
    a2_v *= dampeningFactor;
  }
 
  //Draws line segments to show path of bottom mass
  canvas.beginDraw();
  canvas.translate(centerX,centerY);
  canvas.noFill();
  canvas.stroke((int)map(x2, -r2, r2, 0, 255), 255 - (int) map(y2, -centerY - r2, centerY + r2 , 0, 255), (int) map(y2, -centerY, height -centerY, 0, 255));
  //canvas.stroke(map(a2 % 2*PI, 0, 2*PI, 0, 255), 0, 0);
  //println(a2 % 2*PI);
  canvas.strokeWeight(2);
  if(frameCount > 1)  //Prevents line from being drawn from 0,0
    canvas.line(x2, y2, prevx2, prevy2);
  canvas.endDraw();
  
  //Stores the position of the bottom mass for next iteration
  prevx2 = x2;
  prevy2 = y2;
}
  public void settings() {  size(900, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DoublePendulum" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
