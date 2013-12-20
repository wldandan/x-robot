var app = require('express')();
var server = require('http').createServer(app);
var webRTC = require('webrtc.io').listen(server);

var port = process.env.PORT || 9080;

server.listen(port);

app.get('/', function(req, res) {
  res.sendfile(__dirname + '/index.html');
});

app.get('/style.css', function(req, res) {
  res.sendfile(__dirname + '/css/style.css');
});

app.get('/fullscrean.png', function(req, res) {
  res.sendfile(__dirname + '/images/fullscrean.png');
});

app.get('/script.js', function(req, res) {
  res.sendfile(__dirname + '/js/script.js');
});

app.get('/remote-control.js', function(req, res) {
  res.sendfile(__dirname + '/js/remote-control.js');
});

app.get('/socket.io.js', function(req, res) {
  res.sendfile(__dirname + '/js/lib/socket.io.js');
});

app.get('/webrtc.io.js', function(req, res) {
  res.sendfile(__dirname + '/js/lib/webrtc.io.js');
});

app.get('/jquery.js', function(req, res) {
  res.sendfile(__dirname + '/js/lib/jquery-1.10.1.js');
});

app.get('/switch-style.css', function(req, res) {
  res.sendfile(__dirname + '/css/switch-style.css');
});

app.get('/demo.css', function(req, res) {
  res.sendfile(__dirname + '/css/demo.css');
});

app.get('/font-awesome.css', function(req, res) {
  res.sendfile(__dirname + '/css/font-awesome.css');
});

app.get('/font/fontawesome-webfont.woff', function(req, res) {
  res.sendfile(__dirname + '/font/fontawesome-webfont.woff');
});

app.get('/font/fontawesome-webfont.svg', function(req, res) {
  res.sendfile(__dirname + '/font/fontawesome-webfont.svg');
});

app.get('/font/fontawesome-webfont.eot', function(req, res) {
  res.sendfile(__dirname + '/font/fontawesome-webfont.eot');
});

app.get('/font/fontawesome-webfont.ttf', function(req, res) {
  res.sendfile(__dirname + '/font/fontawesome-webfont.ttf');
});

app.get('/normalize.css', function(req, res) {
  res.sendfile(__dirname + '/css/normalize.css');
});

app.get('/images/bg.jpg', function(req, res) {
  res.sendfile(__dirname + '/images/bg.jpg');
});

webRTC.rtc.on('chat_msg', function(data, socket) {
  var roomList = webRTC.rtc.rooms[data.room] || [];

  for (var i = 0; i < roomList.length; i++) {
    var socketId = roomList[i];

    if (socketId !== socket.id) {
      var soc = webRTC.rtc.getSocket(socketId);

      if (soc) {
        soc.send(JSON.stringify({
          "eventName": "receive_chat_msg",
          "data": {
            "messages": data.messages,
            "color": data.color
          }
        }), function(error) {
          if (error) {
            console.log(error);
          }
        });
      }
    }
  }
});

var io = require('socket.io').listen(9081);
io.sockets.on('connection', function (socket) {
      socket.on('keyboard message from client event', function (data) {
         socket.broadcast.emit("keyboard message to other client event", {action: data});
         console.log("keyboard message from client event");
         console.log(data);
      });
});

