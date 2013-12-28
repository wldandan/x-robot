/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lbs.xrobot;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.koushikdutta.async.http.socketio.SocketIOClient;

import java.io.IOException;

import lbs.xrobot.handler.ArduinoCommandCallback;
import lbs.xrobot.handler.ArduinoController;
import lbs.xrobot.handler.ServerConnectCallback;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class XRobotActivity extends Activity {
    public static String TAG = "X_ROBOT_ACTIVITY";
    private ToggleButton startBtn;
    private EditText serverAddressEditor;
    private EditText leftSpeedPinEditor;
    private EditText leftFrontPinEditor;
    private EditText leftBackPinEditor;
    private EditText rightSpeedPinEditor;
    private EditText rightFrontPinEditor;
    private EditText rightBackPinEditor;
    private String serverAddress;
    private int leftSpeedPin;
    private int leftFrontPin;
    private int leftBackPin;
    private int rightSpeedPin;
    private int rightFrontPin;
    private int rightBackPin;
    private ArduinoController arduinoController;
    private ServerConnectCallback serverConnectCallback;
    private SeekBar speedBar;

    public XRobotActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.xrobot_activity);
        initialize();
        bind();
    }

    private void initialize() {
        arduinoController = new ArduinoController(this);
        initializeControls();
    }

    private void bind() {
        startBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    updateConfiguration();
                    connect();
                }else{
                    disconnectAll();
                    Toast.makeText(getBaseContext(), "Disconnected all", Toast.LENGTH_LONG).show();
                }
            }
        });

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                arduinoController.setSpeed(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getBaseContext(), "Current Speed: " + seekBar.getProgress(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void disconnectAll() {
        serverConnectCallback.disconnect();
        arduinoController.disconnect();
    }

    private void connect() {
        try {
            connectServer(connectArduino());
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Fail to connect arduino: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private ArduinoCommandCallback connectArduino() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Arduino Connected: ").append("\n");
        sb.append("ea:").append(leftSpeedPin).append(" ");
        sb.append("i1:").append(leftFrontPin).append(" ");
        sb.append("i2:").append(leftBackPin).append(" ");
        sb.append("eb:").append(rightSpeedPin).append(" ");
        sb.append("i3:").append(rightFrontPin).append(" ");
        sb.append("i4:").append(rightBackPin).append(" ");

        try {
            arduinoController.connect(leftSpeedPin, leftFrontPin, leftBackPin, rightSpeedPin, rightFrontPin, rightBackPin);
            Toast.makeText(getBaseContext(), sb.toString(), Toast.LENGTH_SHORT).show();
            return new ArduinoCommandCallback(this, arduinoController);
        } catch (IOException e) {
            throw new Exception("Connect arduino failed", e);
        } catch (InterruptedException e) {
            throw new Exception("Connect arduino failed", e);
        }
    }

    private void connectServer(ArduinoCommandCallback arduinoCommandCallback) {
        serverConnectCallback = new ServerConnectCallback(this, arduinoCommandCallback);
        SocketIOClient.connect(serverAddress, serverConnectCallback,new Handler());
    }

    private void updateConfiguration() {
        serverAddress = serverAddressEditor.getText().toString();

        leftSpeedPin = Integer.parseInt(leftSpeedPinEditor.getText().toString());
        leftFrontPin = Integer.parseInt(leftFrontPinEditor.getText().toString());
        leftBackPin = Integer.parseInt(leftBackPinEditor.getText().toString());

        rightSpeedPin = Integer.parseInt(rightSpeedPinEditor.getText().toString());
        rightFrontPin = Integer.parseInt(rightFrontPinEditor.getText().toString());
        rightBackPin = Integer.parseInt(rightBackPinEditor.getText().toString());
    }

    private void initializeControls() {
        startBtn = (ToggleButton) findViewById(R.id.start);
        serverAddressEditor = (EditText) findViewById(R.id.serverAddress);
        leftSpeedPinEditor = (EditText) findViewById(R.id.ea);
        leftFrontPinEditor = (EditText)findViewById(R.id.i1);
        leftBackPinEditor = (EditText)findViewById(R.id.i2);
        rightSpeedPinEditor = (EditText)findViewById(R.id.eb);
        rightFrontPinEditor = (EditText)findViewById(R.id.i3);
        rightBackPinEditor = (EditText)findViewById(R.id.i4);
        speedBar = (SeekBar) findViewById(R.id.speed);
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

}
