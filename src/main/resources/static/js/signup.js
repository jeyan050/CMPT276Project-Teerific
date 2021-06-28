function checkInput() {
    var checkUsername = document.getElementById(username).value;
    var checkPassword1 = document.getElementById(passw1).value;
    var checkPassword2 = document.getElementById(passw2).value;
    var checkFname = document.getElementById(firstN).value;
    var checkLname = document.getElementById(lastN).value;
    var checkEmail = document.getElementById(email).value;
    if (checkUsername == ""){
        alert ("Please enter Username");
        return false;
    }
    else if (checkPassword1 == ""){
        alert ("Please enter Password");
        return false;
    }
    else if (checkPassword2 == ""){
        alert ("Please enter Password Confirmation");
        return false;
    }
    else if (password1 != password2) {
        alert ("Password and Password Confirmation did not match: Please try again...")
        return false;
    }
    else if (checkFname == ""){
        alert ("Please enter First Name");
        return false;
    }
    else if (checkLname == ""){
        alert ("Please enter Last Name");
        return false;
    }
    else if (checkEmail == ""){
        alert ("Please enter Email");
        return false;
    }
    else{
        return true;
    }
}