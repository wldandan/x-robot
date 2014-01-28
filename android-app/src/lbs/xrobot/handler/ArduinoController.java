package lbs.xrobot.handler;

import android.app.Activity;

import org.shokai.firmata.ArduinoFirmata;

import java.io.IOException;

public class ArduinoController {
    private final ArduinoFirmata arduino;
    private int ea;
    private int i1;
    private int i2;

    private int eb;
    private int i3;
    private int i4;

    private boolean isConnected;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private int speed = 100;

    public ArduinoController(Activity activity) {
        this.arduino = new ArduinoFirmata(activity);
    }


    public boolean isConnected() {
        return isConnected;
    }

    public void connect(int ea, int i1, int i2, int eb, int i3, int i4) throws IOException, InterruptedException {
        this.ea = ea;
        this.i1 = i1;
        this.i2 = i2;
        this.eb = eb;
        this.i3 = i3;
        this.i4 = i4;
        arduino.connect();
        isConnected = true;
    }

    public void disconnect(){
        arduino.close();
        isConnected = false;
    }

    public void stop() {
        arduino.digitalWrite(i1, false);
        arduino.digitalWrite(i2, false);
        arduino.digitalWrite(i3, false);
        arduino.digitalWrite(i4, false);
        arduino.analogWrite(ea, 0);
        arduino.analogWrite(eb, 0);
    }

    public void forward(){
        leftSideForward();
        rightSideForward();
    }

    public void turnLeft(){
        leftSideForward();
        rightSideBackward();
    }

    public void turnRight(){
        leftSideBackward();
        rightSideForward();
    }


    public void backward(){
        leftSideBackward();
        rightSideBackward();
    }

    public void leftSideForward() {
        arduino.analogWrite(ea, speed);
        arduino.digitalWrite(i1, true);
        arduino.digitalWrite(i2, false);
    }

    public void leftSideBackward() {
        arduino.analogWrite(ea, speed);
        arduino.digitalWrite(i1, false);
        arduino.digitalWrite(i2, true);
    }

    public void rightSideForward() {
        arduino.analogWrite(eb, speed);
        arduino.digitalWrite(i3, true);
        arduino.digitalWrite(i4, false);
    }

    public void rightSideBackward() {
        arduino.analogWrite(eb, speed);
        arduino.digitalWrite(i3, false);
        arduino.digitalWrite(i4, true);
    }

}
