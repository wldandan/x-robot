package lbs.xrobot.handler;

import android.app.Activity;
import android.widget.Toast;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.EventCallback;

import org.json.JSONArray;
import org.json.JSONException;

public class ArduinoCommandCallback implements EventCallback {
    private Activity activity;
    private ArduinoController arduinoController;

    public ArduinoCommandCallback(Activity activity, ArduinoController arduinoController) {
        this.activity = activity;
        this.arduinoController = arduinoController;
    }

    @Override
    public void onEvent(String event, JSONArray argument, Acknowledge acknowledge) {
        if(!arduinoController.isConnected()){
            Toast.makeText(activity.getBaseContext(), "Arduino is not connected!", Toast.LENGTH_SHORT);
            return;
        }

        try {
            String command = argument.getJSONObject(0).getJSONObject("action").getString("action");
            if("w".equals(command)){
                arduinoController.forward();
            }else if("s".equals(command)){
                arduinoController.backward();
            }else if("a".equals(command)){
                arduinoController.turnLeft();
            }else if("d".equals(command)){
                arduinoController.turnRight();
            }else{
                arduinoController.stop();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
