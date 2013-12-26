package lbs.xrobot.handler;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.ErrorCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.JSONCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

import lbs.xrobot.XRobotActivity;

public class ServerConnectCallback implements ConnectCallback, DisconnectCallback, ErrorCallback, JSONCallback, StringCallback, EventCallback {
    private Activity activity;
    private SocketIOClient client;

    public ServerConnectCallback(Activity activity) {
        this.activity = activity;
    }

    public SocketIOClient getClient() {
        return client;
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
        Toast.makeText(activity.getBaseContext(), "Server connected", Toast.LENGTH_SHORT).show();

        client.setDisconnectCallback(this);
        client.setErrorCallback(this);
        client.setJSONCallback(this);
        client.setStringCallback(this);

        client.addListener("keyboard message to other client event", this);

    }

    @Override
    public void onDisconnect(Exception e) {
        Toast.makeText(activity.getBaseContext(), "disconnected: " + e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(activity.getBaseContext(), "Error: " + error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onJSON(JSONObject json, Acknowledge acknowledge) {
        Toast.makeText(activity.getBaseContext(), "json: " + json, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onString(String string, Acknowledge acknowledge) {
        Toast.makeText(activity.getBaseContext(), "string: " + string, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onEvent(String event, JSONArray argument, Acknowledge acknowledge) {
        Toast.makeText(activity.getBaseContext(), "event: " + event, Toast.LENGTH_SHORT).show();
    }
}
