var links = document.querySelectorAll(".ownerEmail").length;

for(var i = 0; i < links; i++){
    var link = document.querySelectorAll(".ownerEmail")[i].children[0].href;
    var length = link.length;
    if(link[length - 4] == "n"  && link[length - 3] == "u" && link[length - 2] == "l" && link[length - 1] == "l"){
        console.log("null link");
        document.querySelectorAll(".ownerEmail")[i].innerHTML = "<span>N/A</span>";
    }
}