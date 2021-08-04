//checks the screen side, ensures correct table is used before other functions are run for set up
if(screen.width < 1025){
    document.querySelectorAll(".wideScreenTable")[0].remove();
}else{
    document.querySelectorAll(".narrowScreenTable")[0].remove();
}

//adds event listener for resizing the window and reloads so appropriate sized table is used
var previousScreenWidth = screen.width;
window.addEventListener('resize', function(event) {
    //will only reload if the width of the screen changes
    if(this.screen.width !== previousScreenWidth){
        previousScreenWidth = this.screen.width;
        window.location.reload();
    }
});