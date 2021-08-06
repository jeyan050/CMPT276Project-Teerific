var submit = false;

function checkNewValue(event) {
    var checkValue = document.getElementById("value").value;
    if (checkValue == ""){
        document.getElementById("error").innerHTML = "Field is Empty";
        return false;
    } else {
        var checkSize = document.getElementById("value").tagName;
        switch (checkSize){
            case "TEXTAREA":
                if (checkValue.length > 1000){
                    var error = "Value is too long, the character limit is 1000 and your new value is of length: " + checkValue.length;
                    document.getElementById("error").innerHTML = error;
                    return false;
                }
                break;
            case "INPUT":
                if (document.getElementById("value").className == "inputBox newLogo"){
                    if (checkValue.length > 800){   // For logo URL
                        var error = "Value is too long, the character limit is 800 and your new value is of length: " + checkValue.length;
                        document.getElementById("error").innerHTML = error;
                        return false;
                    }
                } else {
                    if (checkValue.length > 150){   // For weekday/weekend Rates, and website (everything else should be less than 100 already)
                        var error = "Value is too long, the character limit is 150 and your new value is of length: " + checkValue.length;
                        document.getElementById("error").innerHTML = error;
                        return false;
                    }
                }
            default:
                break;
        }
    }
    submit = true;
    return true;
}

function displayImage(event) {
    var image = document.getElementById("imageCourseLogo");
    image.src = document.getElementById("value").value;
}

// insert golf sound
var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 500; // in milliseconds (perfect timing for above mp3)
    

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
