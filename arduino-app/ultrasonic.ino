const int trigPin = 13;
const int echoPin = 12;

float temp;
int tempPin = 0;

void setup() {
  Serial.begin(9600);
}
 
void loop()
{
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

  Serial.print(cm);
  Serial.print("cm");
  Serial.println();
  temp = analogRead(tempPin);
  temp = temp * 0.48828125;
  Serial.println(temp);
  delay(1000);  
}
 
long microsecondsToCentimeters(long microseconds)
{
  return microseconds / 29 / 2;
}