var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 500; // in milliseconds (perfect timing for above mp3)
    


var numberOfButtons = document.querySelectorAll("button").length;
for(var i = 0; i < numberOfButtons; i++) {
    document.querySelectorAll("button")[i].addEventListener("click", function(event){
        currentButtonClicked = event.target;
        
        event.preventDefault();
        golfSwingSound.play();
        
        setTimeout(submitForm, timeoutLength, event.target);
    });
}

// FUNCTIONS

function checkInput() {
    var inputs = document.getElementsByClassName("rentalInput");
    var errors = document.getElementsByClassName("error");
    var stocks = document.getElementsByClassName("stockValues");
    var ret = true;
    
    for (var i = 0; i < inputs.length; i++) {
        errors[i].innerHTML = "";
        // CASE 1: Negative input
        if (parseInt(inputs[i].value, 10) < 0) {
            errors[i].innerHTML = "Negative value entered";
            ret = false;
        }
        
        if (parseInt(stocks[i].innerText, 10) < (parseInt(inputs[i].value, 10))) {
            // CASE 2: Num in cart > Stock
            errors[i].innerHTML = "Not enough in stock, please enter smaller value"
            ret = false;
        }
    }
    return ret;
} 

function submitForm(target) {
    if (checkInput()) { 
        target.parentElement.submit();
    }
}

function checkStock() {
    // Show customer the stock only when it's low
    var stockText = document.getElementsByClassName("stocks");
    var stockValues = document.getElementsByClassName("stockValues");
    
    for (var i = 0; i < stockValues.length; i++) {
        if ((parseInt(stockValues[i].innerText, 10) <= 10) && (parseInt(stockValues[i].innerText, 10) > 0)) { 
            // Show
            stockText[i].style.display = "block";
            stockValues[i].style.display = "block";
        } else {
            // Hide
            stockText[i].style.display = "none";
            stockValues[i].style.display = "none";
        }
    }
}

function checkOOS() {
    // Out of stock check
    // If certain item out of stock, hide the input and display
    // OUT OF STOCK instead
    var stocks = document.getElementsByClassName("stockValues");
    var inputs = document.getElementsByClassName("rentalInput");
    var errors = document.getElementsByClassName("error");
    
    for (var i = 0; i < stocks.length; i++) {
        if (parseInt(stocks[i].innerText, 10) <= 0) {
            inputs[i].style.display = "none";
            errors[i].innerHTML = "- OUT OF STOCK -";
        } else {
            inputs[i].style.display = "block";
            errors[i].innerHTML = "";
        }
    }
}

// MAIN
document.getElementById("formSub").addEventListener('click', function() {
    if (checkInput()) { 
        target.parentElement.submit();
    }
})

checkInput();
checkStock();
checkOOS();