#include <TimerOne.h>

int servePin=9;

void setup() 
{ 
    //  pinMode(servePin,OUTPUT);
    Timer1.initialize(1000); // set a timer of length 100000 microseconds (or 0.1 sec - or 10Hz => the led will blink 5 times, 5 cycles of on-and-off, per second)
} 

void loop() 
{ 
    int  plusMinus=1;  

    digitalWrite(servePin,LOW);    
    int delaytime=getPwmTime(-plusMinus);
    Timer1.pwm(9,256);
    delay(1520);

    digitalWrite(servePin,HIGH);    
    delaytime=getPwmTime(plusMinus);
    Timer1.pwm(9,256);
    delay(1520);
} 

float getPwmTime(float plusMinus){
    float hz=plusMinus*50;
    float hr=1000000/hz;//1Mhz=1048576 Mhz to M
    Timer1.setPeriod(hr);
}


