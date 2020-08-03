var username = null;

function connect() {
    username = document.getElementById("username").value;

    if(validateInput(username)) {

        console.log("Inside connect")
        ws = new WebSocket('ws://localhost:8000/chat');
        console.log("Connected");
        console.log(ws);

        ws.onopen = function(event) {
            onOpen(event.data)
        };

        ws.onmessage = function(event){
            console.log("onMessage")
            onMessage(event.data);
        //  return false;
        }

        document.getElementById("myName").innerHTML = username;

        setConnected(true);
    }


}

function disconnect() {
    console.log("Inside disconnect")
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
    //ws.send("Computer: " + username + "is now Online!!");


}


function setConnected(x) {
    console.log("Inside setConnected")
    document.getElementById("login").disabled = x;
    document.getElementById("logout").disabled = !x

    flip();


}

function send() {
    console.log("Inside send")
    var userInput = document.getElementById("textMsg").value;

    if(userInput.length>0) {

        ws.send(username + ":" +userInput);
        document.getElementById("textMsg").value = null;
    }

}

function onOpen(x) {
    console.log(x);
    ws.send("%^&*"+username)
    ws.send(username + " is now Online!!");
}

function onMessage(message) {

    console.log(message)

    if(message.includes("@#$")) {

        var userList = message.substring( message.indexOf(":")+1);
        var userArray = userList.split(',');
        console.log(userList);

        var parent = document.getElementById("parentUserDiv");
        parent.innerHTML = "";

        var user;
        for(user of userArray) {

         var childDiv = document.createElement('div');
         var nameDiv = document.createElement('p');

           $(childDiv).addClass("chatbox__user&#45;&#45;active");
           $(nameDiv).text(user);
           childDiv.appendChild(nameDiv);

           parent.appendChild(childDiv);
        }

    }else {

        var index =  message.indexOf(":");
        var sender = message.substring(0,index);
        var msg = message.substring(index+1);

        var parent = document.getElementById("parentChatBoxDiv");
        var childDiv = document.createElement('div');
        var nameDiv = document.createElement('p');
        var msgDiv = document.createElement('p');

        $(childDiv).addClass("chatbox__messages__user-message--ind-message");
        $(nameDiv).addClass("name");
        $(msgDiv).addClass("message");

        $(nameDiv).text(sender);
        $(msgDiv).text(msg);


        childDiv.appendChild(nameDiv);
        childDiv.appendChild(msgDiv);


        if(sender==username) {
             childDiv.style.float="right";
        }


        parent.appendChild(childDiv);

        var elem = document.getElementById('chatbox');
        elem.scrollTop = elem.scrollHeight;
    }

}

function flip() {
    $('.card').toggleClass('flipped');
}


function validateInput(username) {
    username = username.trim();
    if(username.length > 15 || username.length<3) {
        alert("Username should be between 3 to 8 characters");
        return false;
    }

    return true;
}


 function stoppedTyping(){
        if(this.value.length > 0) {
            document.getElementById('sendBtn').disabled = false;
        } else {
            document.getElementById('sendBtn').disabled = true;
        }
}