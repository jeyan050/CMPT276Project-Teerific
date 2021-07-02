function checkInput(event) {
    var checkUsername = document.getElementById("username").value;
    var checkPassword1 = document.getElementById("passw1").value;
    var checkPassword2 = document.getElementById("passw2").value;
    var checkFname = document.getElementById("firstN").value;
    var checkLname = document.getElementById("lastN").value;
    var checkEmail = document.getElementById("email").value;
    if (checkUsername == ""){
        document.getElementById("errorU").innerHTML = "Username is empty: Please enter a Username";
        return false;
    }
    else if (checkPassword1 == ""){
        document.getElementById("errorP1").innerHTML = "Password is empty: Please enter a Password";
        return false;
    }
    else if (checkPassword2 == ""){
        document.getElementById("errorP2").innerHTML = "Password Comfirmation is empty: Please enter a Password Comfirmation";
        return false;
    }
    else if (checkPassword1 != checkPassword2) {
        alert ("Password and Password Confirmation did not match: Please try again");
        return false;
    }
    else if (checkFname == ""){
        document.getElementById("errorF").innerHTML = "First Name is empty: Please enter a First Name";
        return false;
    }
    else if (checkLname == ""){
        document.getElementById("errorL").innerHTML = "Last Name is empty: Please enter a Last Name";
        return false;
    }
    else if (checkEmail == ""){
        document.getElementById("errorE").innerHTML = "Email is empty: Please enter a Email";
        return false;
    }
    else{
        return true;
    }
}

// checkInput();