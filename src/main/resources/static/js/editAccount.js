var submit = false;

function checkNewValue(event) {
    var checkValue = document.getElementById("value").value;
    console.log("Value:'" + checkValue +"'");
    if (checkValue == ""){
        document.getElementById("error").innerHTML = "Field is Empty";
        return false;
    } else
        submit = true;
        return true;
}

function displayImage(event) {
    var image = document.getElementById("image");
    image.src = document.getElementById("value").value;
}

// insert golf sound
var golfSwingSound = new Audio("http://freesoundeffect.net/sites/default/files/golf-driver-3-sound-effect-62710572.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 350; // in milliseconds (perfect timing for above mp3)
    

function submitForm(target){
    if(target.id == "submitData"){ //does check if its for submit button
        checkNewValue();
        if(submit){
            target.parentElement.submit();
        }
    } else if (target.id == "testImage")
        displayImage();
    else 
        target.parentElement.submit();
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