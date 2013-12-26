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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class XRobotActivity extends Activity {

    private ToggleButton startBtn;
    private EditText serverAddressEditor;
    private EditText leftSpeedPinEditor;
    private EditText leftFrontPinEditor;
    private EditText leftBackPinEditor;
    private EditText rightSpeedPinEditor;
    private EditText rightFrontPinEditor;
    private EditText rightBackPinEditor;
    private String serverAddress;
    private String leftSpeedPin;
    private String leftFrontPin;
    private String leftBackPin;
    private String rightSpeedPin;
    private String rightFrontPin;
    private String rightBackPin;

    public XRobotActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.xrobot_activity);
        initializeControls();
        bind();
    }

    private void bind() {
        startBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    updateConfiguration();
                    connect();
                }else{
                    Toast.makeText(getBaseContext(), "Disconnected all", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void connect() {
        connectServer();
        connectArduino();
    }

    private void connectArduino() {
        StringBuilder sb = new StringBuilder();
        sb.append("Arduino Connected: ").append("\n");
        sb.append("ea:").append(leftSpeedPin).append("\n");
        sb.append("i1:").append(leftFrontPin).append("\n");
        sb.append("i2:").append(leftBackPin).append("\n");
        sb.append("eb:").append(rightSpeedPin).append("\n");
        sb.append("i3:").append(rightFrontPin).append("\n");
        sb.append("i4:").append(rightBackPin).append("\n");
        Toast.makeText(getBaseContext(), sb.toString(), Toast.LENGTH_SHORT).show();
    }

    private void connectServer() {
        Toast.makeText(getBaseContext(), "Server Connected: " + serverAddress, Toast.LENGTH_SHORT).show();
    }

    private void updateConfiguration() {
        serverAddress = serverAddressEditor.getText().toString();

        leftSpeedPin = leftSpeedPinEditor.getText().toString();
        leftFrontPin = leftFrontPinEditor.getText().toString();
        leftBackPin = leftBackPinEditor.getText().toString();

        rightSpeedPin = rightSpeedPinEditor.getText().toString();
        rightFrontPin = rightFrontPinEditor.getText().toString();
        rightBackPin = rightBackPinEditor.getText().toString();
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
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

}
