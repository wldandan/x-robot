var socket = io.connect('http://127.0.0.1:9081/'),
    allowed = true;

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

function startRemoteControl(){
    $("body").bind('keydown', function(event){
        if (!allowed) return;
        allowed = false;
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
            console.log("please send actions 'w','s','a','d','h' to other device.")
        }
    });

    $("body").bind('keyup', function(event){
        allowed = true;
        if (event.keyCode == 87){
            sentKeyboardMessageToServer("h");
        }
        else if (event.keyCode == 83){
            sentKeyboardMessageToServer("h");
        }
        else if (event.keyCode == 65){
            sentKeyboardMessageToServer("h");
        }
        else if (event.keyCode == 68){
            sentKeyboardMessageToServer("h");
        }
        else{
            console.log("please send actions 'w','s','a','d','h' to other device.")
        }
    });
}

function bindSwitchButton(){
    $(".main input[type='checkbox']").click(function() {
        if(this.checked){
            startRemoteControl();
        }else{
            $("body").unbind('keyup');
        }
    });
}

bindSwitchButton();
recieveKeyboardMessageFromServer();