var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 500; // in milliseconds (perfect timing for above mp3)
    

function submitForm(target){
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


// ******************
// FUNCTIONS
// ******************

function determineTableToShow(){
    if(document.querySelectorAll(".dateInput")[0].value.localeCompare("null") === 0 && document.querySelectorAll(".timeInput")[0].value.localeCompare("null") === 0){
        document.querySelectorAll("#partySizeForm")[0].style.display = "none";      //hide the party size selector form
        document.querySelectorAll(".checkDecoy")[0].style.display = "initial";      //reveal the check decoy button
        document.querySelectorAll(".confirmDecoy")[0].style.display = "none";       //prevents access to confirming order   
    }else{
        document.querySelectorAll("#dateTimeForm")[0].style.display = "none";       //hide the dateTime selector form
        document.querySelectorAll(".confirmDecoy")[0].style.display = "initial";    //reveal the confirm decoy button
        document.querySelectorAll(".checkDecoy")[0].style.display = "none";         //hides the check button
    }
}//determineTableToShow()


function hideFormButtons(){
    document.querySelectorAll(".check")[0].style.display = "none";
    document.querySelectorAll(".confirm")[0].style.display = "none";
}


 //clicking the change button hides the partySize table, reveals the date and time input table
function changeButtonListener(){
    document.querySelectorAll(".change")[0].addEventListener("click", function(){
        document.querySelectorAll("#dateTimeForm")[0].style.display = "initial";      
        document.querySelectorAll("#partySizeForm")[0].style.display = "none";
        document.querySelectorAll(".change")[0].style.display = "none";
        document.querySelectorAll(".confirmDecoy")[0].style.display = "none";
        document.querySelectorAll(".checkDecoy")[0].style.display = "initial";
        hideFormButtons();
    })
}//changeButtonListener();


function checkButtonListener(){
    document.querySelectorAll(".checkDecoy")[0].addEventListener("click", function(){
        document.querySelectorAll(".check")[0].parentElement.submit();
    })
}//checkButtonListener()


function confirmButtonListener(){
    document.querySelectorAll(".confirmDecoy")[0].addEventListener("click", function(){
        document.querySelectorAll(".confirm")[0].parentElement.submit();
    })
}//confirmButtonListener()


// ***************
// STARTUP
// ***************

determineTableToShow();
changeButtonListener();
hideFormButtons();
checkButtonListener();
confirmButtonListener();







