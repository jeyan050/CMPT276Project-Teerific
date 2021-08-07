document.querySelectorAll(".tournamentsDisabled")[0].style.display = "none";  //hides tournaments not optimized for screen size


function checkScreenSize(){
    if(screen.width < 750){
        document.querySelectorAll("#tournamentsButton")[0].parentElement.style.display = "none";        //hides the tournament button 
        document.querySelectorAll(".tournamentsDisabled")[0].style.display = "initial";                 //reveals tournaments not optimized for screen size
        document.querySelectorAll(".nav-item")[3].style.display = "none";                               //hides the nav bar link
    }
}//checkScreenSize()

checkScreenSize();