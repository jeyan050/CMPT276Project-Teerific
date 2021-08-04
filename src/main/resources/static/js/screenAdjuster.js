var wideScreenTableRemoved = 0;
var narrowScreenTableRemoved = 0;


//checks the screen side, ensures correct table is used before other functions are run for set up
function removeTable(){
    if(window.screen.width < 1025){
        wideScreenTableRemoved = 1;
        document.querySelectorAll(".wideScreenTable")[0].remove();
    }else{
        narrowScreenTableRemoved = 1;
        document.querySelectorAll(".narrowScreenTable")[0].remove();
    }
}

//adds event listener for resizing the window and reloads so appropriate sized table is used
function addScreenEventListener(){
    window.addEventListener('resize', function(event) {
        removeTable();
        
        //will only reload if both tables have been removed
        if(wideScreenTableRemoved == 1 && narrowScreenTableRemoved == 1){
            window.location.reload();
        }
    });
}

removeTable();              //removes one of the tables on startup
addScreenEventListener();


