var socket = io.connect('http://127.0.0.1:9081/');

function sentKeyboardMessageToServer(KeyboardMessage){
	socket.emit('keyboard message from client event',{action: KeyboardMessage})
	console.log("already send Keyboard Message To Server" );
}

function recieveKeyboardMessageFromServer(){
	socket.on("keyboard message to other client event", function (data) {
		console.log("recieved Keyboard Message From Server");
		console.log(data.valueOf());
	});
}

document.addEventListener("keyup", function(event){
    if (event.keyCode == 87){
        sentKeyboardMessageToServer("w");
    }
    else if (event.keyCode == 83){
        sentKeyboardMessageToServer("s");
    }
    else if (event.keyCode == 65){
        sentKeyboardMessageToServer("a");
    }
    else if (event.keyCode == 68){
        sentKeyboardMessageToServer("d");
    }
    else{
        console.log("please send actions 'w','s','a','d' to other device.")
    }
}, false);
    
recieveKeyboardMessageFromServer();