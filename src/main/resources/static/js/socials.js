document.getElementById("facebookButton").style.display = "none";
document.getElementById("twitterButton").style.display = "none";
document.getElementById("instagramButton").style.display = "none";
document.getElementById("emailAnchor").style.display = "none";

function getFacebook() {
    document.getElementById("facebookButton").submit();
}

function getTwitter() {
    document.getElementById("twitterButton").submit();
}

function getInstagram() {
    document.getElementById("instagramButton").submit();
}

function getEmail() {
    document.getElementById("emailAnchor").click();
}


