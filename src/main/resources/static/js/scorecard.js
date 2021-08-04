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
            document.querySelectorAll(".update")[0].parentElement.submit();
        }
    } else {
        if(event.target.id == "setActivity"){       //if its the complete button
            document.querySelectorAll(".update")[0].parentElement.submit();
        }else{
            event.target.parentElement.submit();
        }
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
    var holesPlayed = 0;
    for(var i = 0; i < document.querySelectorAll(".scoreCell").length; i++){
        var scoreCell = document.querySelectorAll(".scoreCell")[i];
        var strokes = document.querySelectorAll(".editStrokes")[i].lastElementChild.value;
        var par = document.querySelectorAll(".parCell")[i].lastElementChild.value;

        if(strokes > 0){
            scoreCell.value = Number(strokes) - Number(par);
            scoreCell.innerText = scoreCell.value;
            holesPlayed++;
        }else{
            scoreCell.innerText = "";
            scoreCell.value = 0;
        }
        recolorCell(scoreCell);       
    }
    document.querySelectorAll(".holesPlayed")[0].innerText = holesPlayed;
}//calculateScore()


function sumRowsAndColumns(){
    calculateScore();
    sumColumns();
}//sumRowsAndColumns();


function updateScorecard(event){
    currentButtonClicked = event.target;
        
    event.preventDefault();
    golfSwingSound.play();

    setTimeout(submitForm, timeoutLength, event);
}//updateScorecard()


//initial startup procedures to set the status and hide buttons
function startUp(){
    var status = (document.querySelectorAll("#scorecardStatus")[0].value);
    document.querySelectorAll(".update")[0].style.display = "none";     //hide the update button in the form

    if(status == "false"){
        //hide update, complete, and cancel buttons
        document.querySelectorAll(".update")[0].style.display = "none";
        document.querySelectorAll(".complete")[0].style.display = "none";
        document.querySelectorAll(".cancel")[0].style.display = "none";
        document.querySelectorAll(".inviteButton")[0].style.display = "none";
        document.querySelectorAll(".decoyUpdate")[0].style.display = "none";

        //have a status message for the game
        document.querySelectorAll("#completeGame")[0].style.display = "initial";
        document.querySelectorAll("#strokeInstruction")[0].innerText = "";
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
}//startUp()


//adds button listeners for updating the scorecard
function addButtonListeners(){
    //add event listeners to all buttons
    var numberOfButtons = document.querySelectorAll("button").length;
    for(var i = 0; i < numberOfButtons; i++) {
        document.querySelectorAll("button")[i].addEventListener("click", function(event){
            updateScorecard(event);
        });
    }
}//addButtonListeners()


//set the status of scorecard to complete
function setActivity(){
    document.querySelectorAll("#setActivity")[0].addEventListener("click", function(event){
        //sets the status
        document.querySelectorAll("#scorecardStatus")[0].value = false;
        
        //resume submitting the form
        updateScorecard(event);
    })
}//setActivity()


function setUpDecoyButtons(){
    //decoy button for updating the scorecard (real button hidden in form)
    document.querySelectorAll(".decoyUpdate")[0].addEventListener("click", function(event){
        updateScorecard(event);
    })
    
    //decoy button for completing the scorecard (real button hidden in form)
    document.querySelectorAll(".complete")[0].addEventListener("click", function(event){
        document.querySelectorAll("#scorecardStatus")[0].value = false;
        updateScorecard(event);
    })
}//setUpDecoyButtons()


//set holes and score columns in friends tables
function setHolesAndColumnsInFriendTables(){
    var totalFriends = document.querySelectorAll(".friendScorecardTable").length;
    var friendsTableRow = 0;
    for(var i = 0; i < totalFriends; i++){
        var overallScore = 0;
        var overallStrokes = 0;

        for(var j = 0; j < 18; j++){
            var holeNum = Number(document.querySelectorAll(".holeCell")[j].firstElementChild.innerText);
            var par = Number(document.querySelectorAll(".parCell")[j].firstElementChild.innerText);

            document.querySelectorAll(".holeValueFriends")[friendsTableRow].innerText = holeNum;
            var strokes = Number(document.querySelectorAll(".strokeValueFriends")[friendsTableRow].innerText);
        
            var score = strokes - par;
            document.querySelectorAll(".scoreValueFriends")[friendsTableRow].innerText = score;

            if(strokes == 0){
                document.querySelectorAll(".scoreValueFriends")[friendsTableRow].innerText = "";        //will not display score if strokes is 0
            }else{
                var colorCellText = document.querySelectorAll(".scoreValueFriends")[friendsTableRow];
                recolorCell(colorCellText);

                overallScore += score;
                overallStrokes += strokes;
            }
            friendsTableRow++;
        }
        document.querySelectorAll(".scoreSumFriends")[i].innerText = overallScore;
        document.querySelectorAll(".strokeSumFriends")[i].innerText = overallStrokes;
    }
}//setHolesAndColumnsInFriendTables()


//recolors the cell's text based on the score
function recolorCell(cell){
    var score = Number(cell.innerText);
    if(score < 0){
        cell.style.color = "green";    
    }else if(score > 0){
        cell.style.color = "#AF160A";    //red
    }
}//recolorCell()


//recolors the argument cell with a tan color and image
function cellDarkTan(cell){
    cell.style.backgroundColor = "#F0EDC1";
    cell.style.backgroundImage = "url('https://www.transparenttextures.com/patterns/black-felt.png')";
    cell.style.fontWeight = "bold";
}//cellDarkTan()


function recolorHeadersAndSumColumns(){
    var totalHeaders = document.querySelectorAll("th").length;
    for(var i = 0; i < totalHeaders; i++){
        var cell = document.querySelectorAll("th")[i];
        cellDarkTan(cell);
    }

    var totalSumColumns = document.querySelectorAll(".sumColumn").length;
    for(var i = 0; i < totalSumColumns; i++){
        var cell = document.querySelectorAll(".sumColumn")[i];
        cellDarkTan(cell);
    }
}//recolorHeadersAndSumColumns()


//hide the invite button, then the input field cannot be revealed
function toggleInviteButton(){
    var numPlayers = Number(document.querySelectorAll(".currentCount")[0].innerText);
    var maxPlayersAllowed = Number(document.querySelectorAll(".playerCount")[0].innerText);
    if(numPlayers === maxPlayersAllowed){
        document.querySelectorAll(".inviteButton")[0].style.display = "none";
    }
}//toggleInviteButton()


//event listener for invite button, once clicked, it reveals the input field for adding a user
function sendInviteButton(){
    document.querySelectorAll(".inviteButton")[0].addEventListener("click", function(){
        document.querySelectorAll(".inviteButton")[0].style.display = "none";
        document.querySelectorAll(".inviteFriends")[0].style.display = "initial";
    });
}//sendInviteButton()


//adds event listener at loading of page & sums the columns
function initial(){
    document.addEventListener("click", sumRowsAndColumns);
    calculateScore();
    sumColumns();
    recolorHeadersAndSumColumns();

    //won't show the friends button if only one golfer
    var currentPlayercount = Number(document.querySelectorAll(".currentCount")[0].innerText);

    if(currentPlayercount == 1){
        document.querySelectorAll(".showFriends")[0].style.display = "none";        
    }

    document.querySelectorAll(".inviteFriends")[0].style.display = "none";  //hides the inviteFriendsButton
}//initial()


//*******************
//EVENT LISTENERS 
//*******************

addButtonListeners();
setActivity();


//*************
// STARTUP 
//*************

startUp();
setHolesAndColumnsInFriendTables();
toggleInviteButton();
sendInviteButton();
showFriends();
hideFriends();
setUpDecoyButtons();
initial();


//hide the friends on click of collapse button
document.querySelectorAll(".hideFriends")[0].addEventListener("click", function(){
    hideFriends();
})


//show friends on click of friends button
document.querySelectorAll(".showFriends")[0].addEventListener("click", function(){
    showFriends();
})


function showFriends(){
    document.querySelectorAll(".showFriends")[0].style.display = "none";
    document.querySelectorAll(".hideFriends")[0].style.display = "initial";

    var friendScorecardCount = document.querySelectorAll(".friendScorecard").length;
    for(var i = 0; i < friendScorecardCount; i++){
        document.querySelectorAll(".friendScorecard")[i].style.display = "initial";
    }
}

function hideFriends(){
    document.querySelectorAll(".showFriends")[0].style.display = "initial";
    document.querySelectorAll(".hideFriends")[0].style.display = "none";

    var friendScorecardCount = document.querySelectorAll(".friendScorecard").length;
    for(var i = 0; i < friendScorecardCount; i++){
        document.querySelectorAll(".friendScorecard")[i].style.display = "none";
    }
}