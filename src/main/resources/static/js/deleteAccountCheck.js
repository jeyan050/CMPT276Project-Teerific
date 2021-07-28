
function checkDelete(event){
    return confirm("Are you sure you want to delete your account?");        //alert (If ok then true, and if cancel then false)
}

// insert golf sound
var golfSwingSound = new Audio("http://freesoundeffect.net/sites/default/files/golf-driver-3-sound-effect-62710572.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 350; // in milliseconds (perfect timing for above mp3)
    

function submitForm(target){
    if(target.id == "deleteAccount"){ //does check if its for submit button
        if(checkDelete()){
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