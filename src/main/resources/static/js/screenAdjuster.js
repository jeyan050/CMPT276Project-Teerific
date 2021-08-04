//checks the screen side, ensures correct table is used before other functions are run for set up
if(screen.width < 1025){
    document.querySelectorAll(".wideScreenTable")[0].remove();
}else{
    document.querySelectorAll(".narrowScreenTable")[0].remove();
}

//adds event listener for resizing the window and reloads so appropriate sized table is used
window.addEventListener('resize', function(event) {
    window.location.reload();
});