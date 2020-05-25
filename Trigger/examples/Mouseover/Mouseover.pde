import kelvinclark.Trigger;
import processing.sound.*;

Trigger mouseover, click;
SoundFile sound;
int col = 160, num, bgColor = 255;

void setup() {
  size(600, 400);
  textSize(80);
  textAlign(CENTER, CENTER);
  sound = new SoundFile(this, "bip.mp3");
  mouseover = new Trigger(this);
  click = new Trigger(this);
}

void draw() {
  background(bgColor);
  float x = width/2;
  float y = height/2;
  float r = 100;
  float d = dist(mouseX, mouseY, x, y);
  boolean isInside = d < r;

  fill(col);
  circle(x, y, r*2);
  fill(0);
  text(num, width/2, height/2-10);

  mouseover.callTrigger(isInside);
  click.callTrigger(isInside && mousePressed);
  if (mouseover.isActive() && click.getNumExecutions() == 5) System.exit(0);
}

void triggerEvent(Trigger t) {
  if (t == mouseover) {
    sound.play();
    col = color(random(255), random(255), random(255));
    num++;
  } 
  if (t == click) bgColor = color(random(255), random(255), random(255));
}
