document.querySelectorAll(".screenToSmall")[0].style.display = "none";  //hides not mobile friendly message


function checkScreenSize(){
    if(screen.width < 750){
        var homeScreenButtons = document.querySelectorAll(".homePageButton").length;
        var buttonsToHide = homeScreenButtons - 1;
    
        //hides the homescreen buttons and nav links except for logout  
        for(var i = 0; (i < buttonsToHide); i++){                       
            document.querySelectorAll(".homePageButtonBorder")[i].style.display = "none";     //hide the current home screen button
            document.querySelectorAll(".nav-item")[i].style.display = "none";                   //hide the current header directory
        }
    
            document.querySelectorAll(".screenToSmall")[0].style.display = "initial";           //reveals not mobile friendly message
        
    }
}//checkScreenSize()



checkScreenSize();
