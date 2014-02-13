package lbs.xrobot.handler;

import android.app.Activity;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.LinkedList;

public class RTCClient implements EventCallback {
    private Activity activity;

    public void setClient(SocketIOClient client) {
        this.client = client;
    }

    private SocketIOClient client;
    private String target;
    private LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<PeerConnection.IceServer>();
    private PeerConnectionFactory factory;
    private PeerConnection pc;
    private MediaConstraints pcConstraints;
    private MediaConstraints videoConstraints;
    private final PCObserver pcObserver = new PCObserver();
    private final SDPObserver sdpObserver = new SDPObserver();


    public RTCClient(Activity activity) {
        this.activity = activity;
    }

    public void initialize(SocketIOClient client){
        this.client = client;
        configVideo();
        setupIceServer();
        onIceServers(iceServers);
    }

    private void configVideo() {
        videoConstraints = new MediaConstraints();
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxHeight", "240"));
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxWidth", "320"));
    }

    private void setupIceServer() {
        iceServers.add(new PeerConnection.IceServer("stun:23.21.150.121"));
        iceServers.add(new PeerConnection.IceServer("stun:176.58.114.68:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));

        // factory cannot be initialized before AndroidGlobals
        PeerConnectionFactory.initializeAndroidGlobals(activity);
        factory = new PeerConnectionFactory();

        pcConstraints = new MediaConstraints();
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
    }

    // Cycle through likely device names for the camera and return the first
    // capturer that works, or crash if none do.
    private VideoCapturer getVideoCapturer() {
        String[] cameraFacing = {"front", "back"};
        int[] cameraIndex = {0, 1};
        int[] cameraOrientation = {0, 90, 180, 270};
        for (String facing : cameraFacing) {
            for (int index : cameraIndex) {
                for (int orientation : cameraOrientation) {
                    String name = "Camera " + index + ", Facing " + facing +
                            ", Orientation " + orientation;
                    VideoCapturer capturer = VideoCapturer.create(name);
                    if (capturer != null) {
                        return capturer;
                    }
                }
            }
        }
        throw new RuntimeException("Failed to open capturer");
    }

    private void sendMessage(String type, JSONObject payload) throws JSONException {
        JSONObject message = new JSONObject();
        message.put("to", target);
        message.put("type", type);
        message.put("payload", payload);
        client.emit("message", new JSONArray().put(message));
    }

    @Override
    public void onEvent(String event, JSONArray argument, Acknowledge acknowledge) {
        try {
            handleMessage(argument.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(final JSONObject json) {
        try {
            String type = json.getString("type");
            target = json.getString("from");
            JSONObject payload = json.getJSONObject("payload");
            if (type.equals("offer")) {
                SessionDescription sdp = new SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(type),
                        (String) payload.get("sdp"));
                pc.setRemoteDescription(sdpObserver, sdp);
            } else if (type.equals("answer")) {
                SessionDescription sdp = new SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(type),
                        (String) payload.get("sdp"));
                pc.setRemoteDescription(sdpObserver, sdp);
            } else if (type.equals("stop")) {
                client.emit("closed");
            } else if (type.equals("closed")) {
                // reset pc
            } else if (type.equals("candidate")) {
                if (pc.getRemoteDescription() != null) {
                    IceCandidate candidate = new IceCandidate(
                            (String) payload.get("id"),
                            payload.getInt("label"),
                            (String) payload.get("candidate"));
                    pc.addIceCandidate(candidate);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private class SDPObserver implements SdpObserver {

        @Override
        public void onCreateSuccess(final SessionDescription sdp) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONObject payload = new JSONObject();
                        payload.put("type", sdp.type.canonicalForm());
                        payload.put("sdp", sdp.description);
                        sendMessage("answer", payload);
                        pc.setLocalDescription(sdpObserver, sdp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onSetSuccess() {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    pc.createAnswer(SDPObserver.this, pcConstraints);
                }
            });
        }

        @Override
        public void onCreateFailure(String s) {

        }

        @Override
        public void onSetFailure(String s) {

        }
    }

    private class PCObserver implements PeerConnection.Observer {

        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {

        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

        }

        @Override
        public void onIceCandidate(final IceCandidate candidate) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONObject payload = new JSONObject();
                        payload.put("label", candidate.sdpMLineIndex);
                        payload.put("id", candidate.sdpMid);
                        payload.put("candidate", candidate.sdp);
                        sendMessage("candidate", payload);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError() {

        }

        @Override
        public void onAddStream(MediaStream mediaStream) {

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }
    }

    public void onIceServers(LinkedList<PeerConnection.IceServer> iceServers) {

        pc = factory.createPeerConnection(iceServers, pcConstraints, pcObserver);

        VideoCapturer capturer = getVideoCapturer();
        VideoSource videoSource = factory.createVideoSource(capturer, videoConstraints);
        MediaStream lMS = factory.createLocalMediaStream("ARDAMS");
        VideoTrack videoTrack = factory.createVideoTrack("ARDAMSv0", videoSource);
        lMS.addTrack(videoTrack);
        lMS.addTrack(factory.createAudioTrack("ARDAMSa0"));
        pc.addStream(lMS, new MediaConstraints());
    }

}
