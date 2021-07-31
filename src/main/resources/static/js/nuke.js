var nukeSound = new Audio("https://www.myinstants.com/media/sounds/tactical-nuke.mp3");
var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var timeoutLength = 500; // in milliseconds (perfect timing for golf swing mp3)
    
//submits the form associated with the button
function submitForm(target){
    target.parentElement.submit();
}

//adds event listeners to the homePageButtons
var numberOfButtons = document.querySelectorAll(".homePageButton").length;
for(var i = 0; i < numberOfButtons; i++) {
    document.querySelectorAll("button")[i].addEventListener("click", function(event){
        currentButtonClicked = event.target;
        
        event.preventDefault();
        golfSwingSound.play();
        
        setTimeout(submitForm, timeoutLength, event.target);
    });
}


//gets the next number in the countdown and changes the onscreen text
function nextNum(count){
    document.querySelectorAll("#countdown")[0].innerText = count;
    console.log(count);
}//nextNum()


//starts the countdown for the onscreen text
function startCountdown(){
    var count = 4;

    setInterval(function(){
        if(count >= 0){
            nextNum(count);
            count--;
        }
    }, 1000);
}//startCountdown()


//clear countdownt text
function clearCountdown(event){
    document.querySelectorAll("#countdown")[0].style.display = "none";
    event.target.parentElement.submit()
}

//event listener for nuke button
document.querySelectorAll("#nukeButton")[0].addEventListener("click", function(event){
    //activate countdown

    event.preventDefault();

    document.querySelectorAll(".notActive")[0].style.display = "initial";
    document.querySelectorAll(".active")[0].style.display = "none";

    document.body.classList.add("nukeBackdrop");
    document.body.classList.remove("grassBackdrop");
    document.querySelectorAll(".homePageContainer")[0].style.opacity = "0.05";
    document.querySelectorAll(".footer")[0].style.opacity = "0.10";
    document.querySelectorAll(".navbar")[0].style.opacity = "0.10";

    document.querySelectorAll("#nukeButton")[0].style.border = "5px solid green";
    document.querySelectorAll("#nukeButton")[1].style.border = "5px solid green";
    document.querySelectorAll("#countdown")[0].style.display = "initial";
    
    nukeSound.play();

    startCountdown();
    setTimeout(clearCountdown, 12000, event);
})


document.querySelectorAll(".notActive")[0].style.display = "none";