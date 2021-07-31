var nukeSound = new Audio("https://www.myinstants.com/media/sounds/tactical-nuke.mp3");
// var timeoutLength = 12000; // in milliseconds (perfect timing for above mp3)


function nextNum(count){
    document.querySelectorAll("#countdown")[0].innerText = count;
    console.log(count);
}

function startCountdown(){
    var count = 4;

    setInterval(function(){
        if(count >= 0){
            nextNum(count);
            count--;
        }
    }, 1000);
}

function clearCountdown(event){
    document.querySelectorAll("#countdown")[0].style.display = "none";

}

document.querySelectorAll("#nukeButton")[0].addEventListener("click", function(event){
    //activate countdown

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