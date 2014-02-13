char val; // variable to receive data from the serial port
const int ea = 9; // LED connected to pin 48 (on-board LED)
const int i1 = 12; // LED connected to pin 48 (on-board LED)
const int i2 = 11; // LED connected to pin 48 (on-board LED)

const int eb = 10; // LED connected to pin 48 (on-board LED)
const int i3 = 9; // LED connected to pin 48 (on-board LED)
const int i4 = 8; // LED connected to pin 48 (on-board LED)
const int trigPin = 7;
const int echoPin = 6;

float temp;
int tempPin = 0;

void setup() {
  pinMode(ea, OUTPUT);  // pin 48 (on-board LED) as OUTPUT
  pinMode(eb, OUTPUT);  // pin 48 (on-board LED) as OUTPUT
  pinMode(i1, OUTPUT);  // pin 48 (on-board LED) as OUTPUT
  pinMode(i2, OUTPUT);  // pin 48 (on-board LED) as OUTPUT
  pinMode(i3, OUTPUT);  // pin 48 (on-board LED) as OUTPUT
  pinMode(i4, OUTPUT);  // pin 48 (on-board LED) as OUTPUT

  Serial.begin(9600);       // start serial communication at 9600bps
}
void loop() {

  long duration, cm;
  pinMode(trigPin, OUTPUT);
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  pinMode(echoPin, INPUT);
  duration = pulseIn(echoPin, HIGH);

  cm = microsecondsToCentimeters(duration);

  temp = analogRead(tempPin);
  temp = temp * 0.48828125;

  Serial.print(cm);
  Serial.print("cm");
  Serial.println();
  Serial.println(temp);	

  if ( Serial.available() )      // if data is available to read
  {
    val = Serial.read();         // read it and store it in 'val'
  }
  if ( val == 'w' )              // if 'H' was received
  {
    forward();
  } else if( val == 's') {
    backward();
  } else if( val == 'a') {
    left();
  } else if( val == 'd') {
    right();
  }
  delay(100);                    // wait 100ms for next reading
}

void forward() {
  leftForward();
  rightForward();  
}

void backward() {
  leftBackward();
  rightBackward();  
}

void left() {
  leftBackward();
  rightForward();  
}

void right() {
  leftForward();
  rightBackward();  
}

void leftForward() {
  digitalWrite(i1, HIGH);
  digitalWrite(i2, LOW);
}

void leftBackward() {
  digitalWrite(i2, HIGH);
  digitalWrite(i1, LOW);
}

void rightForward() {
  digitalWrite(i3, HIGH);
  digitalWrite(i4, LOW);
}

void rightBackward() {
  digitalWrite(i4, HIGH);
  digitalWrite(i3, LOW);
}

void stopEngine() {
  digitalWrite(i1, LOW);
  digitalWrite(i2, LOW);
  digitalWrite(i3, LOW);
  digitalWrite(i4, LOW);
}
long microsecondsToCentimeters(long microseconds)
{
  return microseconds / 29 / 2;
}