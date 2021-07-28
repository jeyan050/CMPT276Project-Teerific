var submit = false;

function checkInput(event) {
    var checkUsername = document.getElementById("username").value;
    var checkPassword1 = document.getElementById("passw1").value;
    var checkPassword2 = document.getElementById("passw2").value;
    var checkFname = document.getElementById("firstN").value;
    var checkLname = document.getElementById("lastN").value;
    var checkEmail = document.getElementById("email").value;

    if (checkUsername == ""){
        document.getElementById("errorU").innerHTML = "Username is empty: Please enter a Username";
        return false;
    }
    else if (checkPassword1 == ""){
        document.getElementById("errorP1").innerHTML = "Password is empty: Please enter a Password";
        return false;
    }
    else if (checkPassword2 == ""){
        document.getElementById("errorP2").innerHTML = "Password Comfirmation is empty: Please enter a Password Comfirmation";
        return false;
    }
    else if (checkPassword1 != checkPassword2) {
        alert ("Password and Password Confirmation did not match: Please try again");
        return false;
    }
    else if (checkFname == ""){
        document.getElementById("errorF").innerHTML = "First Name is empty: Please enter a First Name";
        return false;
    }
    else if (checkLname == ""){
        document.getElementById("errorL").innerHTML = "Last Name is empty: Please enter a Last Name";
        return false;
    }
    else if (checkEmail == ""){
        document.getElementById("errorE").innerHTML = "Email is empty: Please enter a Email";
        return false;
    }
    else{
        submit = true;
        return true;
    }
    
}


// insert golf sound
var golfSwingSound = new Audio("http://freesoundeffect.net/sites/default/files/golf-driver-3-sound-effect-62710572.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 350; // in milliseconds (perfect timing for above mp3)
    

function submitForm(target){
    if(target.id == "submitUser"){ //does check if its for submit button
        checkInput();
        if(submit){
            target.parentElement.submit();
        }
    } else {
        target.parentElement.submit();
    }
}

var numberOfButtons = document.querySelectorAll("button").length;
for(var i = 0; i < numberOfButtons; i++) {
    document.querySelectorAll("button")[i].addEventListener("click", function(event){
        currentButtonClicked = event.target;
        
        event.preventDefault();
        golfSwingSound.play();
    
        setTimeout(submitForm, timeoutLength, event.target);
    });
}