package lbs.xrobot.handler;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.ErrorCallback;
import com.koushikdutta.async.http.socketio.JSONCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

import lbs.xrobot.XRobotActivity;

public class ServerConnectCallback implements ConnectCallback, DisconnectCallback{
    private Activity activity;
    private ArduinoCommandCallback arduinoCommandCallback;
    private RTCClient rtcClient;
    private SocketIOClient client;

    public ServerConnectCallback(Activity activity, ArduinoCommandCallback arduinoCommandCallback, RTCClient rtcClient) {
        this.activity = activity;
        this.arduinoCommandCallback = arduinoCommandCallback;
        this.rtcClient = rtcClient;
    }

    public void disconnect(){
        client.disconnect();
    }

    @Override
    public void onConnectCompleted(Exception ex, SocketIOClient client) {
        this.client = client;
        if (ex != null) {
            Toast.makeText(activity.getBaseContext(), "Connect server failed: \n" + ex, Toast.LENGTH_SHORT).show();
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            Log.e(XRobotActivity.TAG, stack.toString());
            ex.printStackTrace();
            return;
        }

        client.setDisconnectCallback(this);

        client.addListener("keyboard message to other client event", arduinoCommandCallback);

        rtcClient.initialize(client);
        JSONArray arguments = new JSONArray();
        arguments.put("x-robot");
        client.emit("readyToStream", arguments);
        client.addListener("message", rtcClient);

    }

    @Override
    public void onDisconnect(Exception e) {
        Toast.makeText(activity.getBaseContext(), "disconnected: " + e, Toast.LENGTH_SHORT).show();
    }
}
