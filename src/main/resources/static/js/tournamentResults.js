for(var i = 0; i < 9; i++){
    if(i < 3){
        document.querySelectorAll("td")[i].style.backgroundColor = "#FFD700";   //gold
        document.querySelectorAll("td")[i].style.color = "black";
        document.querySelectorAll("td")[i].style.backgroundImage = "url(https://www.transparenttextures.com/patterns/binding-light.png)";
    }else if(i >= 3 && i < 6){
        document.querySelectorAll("td")[i].style.backgroundColor = "#C0C0C0";   //silver
        document.querySelectorAll("td")[i].style.color = "black";
        document.querySelectorAll("td")[i].style.backgroundImage = "url(https://www.transparenttextures.com/patterns/light-aluminum.png)";
    }else{
        document.querySelectorAll("td")[i].style.backgroundColor = "#CD7F32";   //bronze
        document.querySelectorAll("td")[i].style.color = "black";
        document.querySelectorAll("td")[i].style.backgroundImage = "url(https://www.transparenttextures.com/patterns/brushed-alum.png)";
    }
}