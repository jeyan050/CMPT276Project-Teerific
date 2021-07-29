// insert golf sound
var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 500; // in milliseconds (perfect timing for above mp3)


//*******************
// FUNCTIONS
//*******************
function validateChanges(){
    var allValid = true;
    for(var i = 0; i < document.querySelectorAll(".scorecardTableInputCell").length; i++){
        var cell = document.querySelectorAll(".scorecardTableInputCell")[i];

        if(cell.value == ""){
            //do not submit form
            //prints an error on submission, do not clear inputs
            console.log("invalid entry of NULL");
            cell.style.color = "red";               //colors value red if not valid
            allValid = false;
        }else if(isNaN(cell.value)){
            console.log("invalid entry of NaN");
            cell.style.color = "red";               //colors value red if not valid
            allValid = false;
        }else if(Number(cell.value) < 0){
            console.log("invalid entry of negative strokes");
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
            document.querySelector(".errorMessage").style.display = "initial";
            document.querySelector(".errorMessage").innerHTML = "Invalid Entry, Please Try Again";
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
    var sumScore = 0;
    var sumStrokes = 0;

    for(var i = 0; i < document.querySelectorAll(".yardageCell").length; i++){
        sumYardage += Number(document.querySelectorAll(".yardageCell")[i].lastElementChild.value);
    }

    for(var i = 0; i < document.querySelectorAll(".handicapCell").length; i++){
        sumHandicap += Number(document.querySelectorAll(".handicapCell")[i].lastElementChild.value);
    }

    for(var i = 0; i < document.querySelectorAll(".parCell").length; i++){
        sumPar += Number(document.querySelectorAll(".parCell")[i].lastElementChild.value);
    }

    for(var i = 0; i < document.querySelectorAll(".scoreCell").length; i++){
        sumScore += Number(document.querySelectorAll(".scoreCell")[i].value);
    }

    for(var i = 0; i < document.querySelectorAll(".editStrokes").length; i++){
        var value = Number(document.querySelectorAll(".editStrokes")[i].lastElementChild.value);
        if(value > 0){
            sumStrokes += value;
        }
    }

    document.querySelectorAll(".sumYardage")[0].innerText = sumYardage;
    document.querySelectorAll(".sumPar")[0].innerText = sumPar;
    document.querySelectorAll(".sumHandicap")[0].innerText = sumHandicap;
    document.querySelectorAll(".sumScore")[0].innerText = sumScore;
    document.querySelectorAll(".sumStrokes")[0].innerText = sumStrokes;
}//sumColumns()


function calculateScore(){
    var handicap = document.querySelectorAll("#handicapCheckbox")[0].checked;
    var holesPlayed = 0;
    for(var i = 0; i < document.querySelectorAll(".scoreCell").length; i++){
        var scoreCell = document.querySelectorAll(".scoreCell")[i];
        var strokes = document.querySelectorAll(".editStrokes")[i].lastElementChild.value;
        var par = document.querySelectorAll(".parCell")[i].lastElementChild.value;

        if(strokes > 0){
            if(handicap){
                var handicapCell = document.querySelectorAll(".handicapCell")[i].lastElementChild.value;
                scoreCell.value = Number(strokes) - Number(par) - Number(handicapCell);
            }else{
                scoreCell.value = Number(strokes) - Number(par);
            }
            scoreCell.innerText = scoreCell.value;
            holesPlayed++;
        }else{
            scoreCell.innerText = "";
            scoreCell.value = 0;
        }       
    }
    document.querySelectorAll(".holesPlayed")[0].innerText = holesPlayed;
}

function sumRowsAndColumns(){
    calculateScore();
    sumColumns();
}//sumRowsAndColumns();


function updateScorecard(event){
    currentButtonClicked = event.target;
        
    event.preventDefault();
    golfSwingSound.play();

    setTimeout(submitForm, timeoutLength, event);
}


//*******************
//EVENT LISTENERS 
//*******************

//add event listeners to all buttons

var numberOfButtons = document.querySelectorAll("button").length;
for(var i = 0; i < numberOfButtons; i++) {
    document.querySelectorAll("button")[i].addEventListener("click", function(event){
        updateScorecard(event);
    });
}

document.querySelectorAll("#setActivity")[0].addEventListener("click", function(event){
    //sets the status
    document.querySelectorAll("#scorecardStatus")[0].value = false;
    
    //resume submitting the form
    updateScorecard(event);
})


//*************
// STARTUP 
//*************

var status = (document.querySelectorAll("#scorecardStatus")[0].value);
if(status == "false"){
    //hide update, complete, and cancel buttons
    document.querySelectorAll(".update")[0].style.display = "none";
    document.querySelectorAll(".complete")[0].style.display = "none";
    document.querySelectorAll(".cancel")[0].style.display = "none";

    //have a status message for the game
    document.querySelectorAll("#completeGame")[0].style.display = "initial";
    document.querySelectorAll("#strokeInstruction")[0].style.display = "none";
    document.querySelectorAll(".gameStatus")[0].innerText = "Complete"

}else{
    //edit strokes cell if status is active, changes the strokes cell to an input
    for(var i = 0; i < document.querySelectorAll(".editStrokes").length; i++){
        document.querySelectorAll(".editStrokes")[i].addEventListener("click", function(event){
            if(event.target.classList.length !== 2){
                event.target.style.display = "none";
                event.target.parentElement.lastElementChild.style.display = "initial";
            }
        });
    }
    document.querySelectorAll(".gameStatus")[0].innerText = "Active"
}


//adds event listener at loading of page & sums the columns
document.addEventListener("click", sumRowsAndColumns);
calculateScore();
sumColumns();
