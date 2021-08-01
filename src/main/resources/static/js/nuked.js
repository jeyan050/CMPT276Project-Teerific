document.body.classList.add("nukeBackdrop");
document.body.classList.remove("grassBackdrop");

document.querySelectorAll(".footer")[0].style.opacity = "0.1";
document.querySelectorAll(".navbar")[0].style.opacity = "0.1";
document.querySelectorAll(".homePageContainer")[0].style.opacity = "0";

document.querySelectorAll("#countdown")[0].style.position = "absolute";
document.querySelectorAll("#nukeButton")[0].style.border = "5px solid green";
document.querySelectorAll("#countdown")[0].innerText = "The Database Has Been Nuked"
document.querySelectorAll("#countdown")[0].style.display = "initial";
document.querySelectorAll("#countdown")[0].style.top = "30vh";
document.querySelectorAll("#countdown")[0].style.fontSize = "10vh";

