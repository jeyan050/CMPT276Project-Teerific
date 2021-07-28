// insert golf sound
var golfSwingSound = new Audio("http://freesoundeffect.net/sites/default/files/golf-driver-3-sound-effect-62710572.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 350; // in milliseconds (perfect timing for above mp3)


//*******************
// FUNCTIONS
//*******************
function validateChanges(){
    var allValid = true;
    for(var i = 0; i < document.querySelectorAll(".ownerTableInputCell").length; i++){
        var cell = document.querySelectorAll(".ownerTableInputCell")[i];

        if(cell.value == ""){
            //do not submit form
            //prints an error on submission, do not clear inputs
            console.log("invalid entry of NULL");
            cell.style.color = "red";               //colors value red if not valid
            allValid = false;
        }else if(isNaN(cell.value)){
            console.log("invalid entery of NaN");
            cell.style.color = "red";               //colors value red if not valid
            allValid = false;
        }else{
            cell.style.color = "green";                 //colors value green when valid
        }
    }
    return allValid;
}//validateChanges()
    

function submitForm(event){
    if(event.target.id == "validate"){
        if(!validateChanges()){
            document.querySelector("#ownerErrorMessage").style.display = "initial";
            document.querySelector("#ownerErrorMessage").innerHTML = "Invalid Entry, Please Try Again";
        }else{
            event.target.parentElement.submit();
        }
    } else {
        event.target.parentElement.submit();
    }
}//submitForm()


function sumColumns(){
    var sumYardage = 0;
    var sumPar = 0;
    var sumHandicap = 0;

    for(var i = 0; i < document.querySelectorAll(".editYardage").length; i++){
        sumYardage += Number(document.querySelectorAll(".editYardage")[i].lastElementChild.value);
    }

    for(var i = 0; i < document.querySelectorAll(".editPar").length; i++){
        sumPar += Number(document.querySelectorAll(".editPar")[i].lastElementChild.value);
    }

    for(var i = 0; i < document.querySelectorAll(".editHandicap").length; i++){
        sumHandicap += Number(document.querySelectorAll(".editHandicap")[i].lastElementChild.value);
    }

    document.querySelectorAll(".yardageSum")[0].innerText = sumYardage;
    document.querySelectorAll(".parSum")[0].innerText = sumPar;
    document.querySelectorAll(".handicapSum")[0].innerText = sumHandicap;
}//sumColumns()



//*******************
//EVENT LISTENERS 
//*******************

//add event listeners to all buttons
var numberOfButtons = document.querySelectorAll("button").length;
for(var i = 0; i < numberOfButtons; i++) {
    document.querySelectorAll("button")[i].addEventListener("click", function(event){
        currentButtonClicked = event.target;
        
        event.preventDefault();
        golfSwingSound.play();
    
        setTimeout(submitForm, timeoutLength, event);
    });
}


// edit yardage cell, changes the yardage cell to an input
for(var i = 0; i < document.querySelectorAll(".editYardage").length; i++){
    document.querySelectorAll(".editYardage")[i].addEventListener("click", function(event){
        if(event.target.classList.length !== 2){
            event.target.style.display = "none";
            event.target.parentElement.lastElementChild.style.display = "initial";
        }
    });
}


//edit par cell, changes the par cell to an input
for(var i = 0; i < document.querySelectorAll(".editPar").length; i++){
    document.querySelectorAll(".editPar")[i].addEventListener("click", function(event){
        if(event.target.classList.length !== 2){
            event.target.style.display = "none";
            event.target.parentElement.lastElementChild.style.display = "initial";
        }
    });
}


//edit handicap cell, changes the handicap cell to an input
for(var i = 0; i < document.querySelectorAll(".editHandicap").length; i++){
    document.querySelectorAll(".editHandicap")[i].addEventListener("click", function(event){
        if(event.target.classList.length !== 2){
            event.target.style.display = "none";
            event.target.parentElement.lastElementChild.style.display = "initial";
        }
    });
}


//*************
// STARTUP 
//************* 

//adds event listener at loading of page & sums the columns
document.addEventListener("click", sumColumns);
sumColumns();