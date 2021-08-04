
//source: https://thoughtcatalog.com/january-nelson/2018/08/golf-puns/
var puns = ["It Takes Alot Of Golf Balls To Golf The Way I Do", 
            "May The 'Course' Be With You", 
            "I Once Golfed On A Course So Hard, I Lost Three Golf Balls In The Ball-Washer...",
            "Young, Wild, And Free Golf!", 
            "Asking 'FORE' A Friend...", 
            "Un-'FORE'-gettable, In Every Way", 
            "As 'Par' As I Can See!", 
            "You Are The Best By Par!", 
            "Green There, Done That!",
            "I Am Iron-Man", 
            "Not All People Are Created Eagle", 
            "A Chip Off The Old Block!", 
            "A Game So Good, You Will Never FORE-get!", 
            "A Golfer's Worst Nightmare Is The Bogeyman...",
            "Golf Is A Lot Like Taxes, You Go For The Green And End Up In The Hole...",
            "Golf Balls Are Like Eggs, They Are White, Sold By The Dozen, And A Week Later You Have To Buy More!",
            "The Only Problem With Golf Is The Slow Groups Are Always Infront Of You And The Fast Groups Are Always Behind You",
            "Whats The Easiest Shot In Golf? Your Fourth Putt..."
            ]

//choose 5 random numbers, no duplicates chosen
var randomNums = [];

while(randomNums.length < 5){
    var num = Number(Math.floor((Math.random() * 18) + 1));     //gets random number between 1 and 18
    var insertIndex = randomNums.length;
    console.log(num);

    if(!randomNums.includes(num)){
        randomNums.push(num);
    }
}

//assign the web text to contain the pun
for(var i = 0; i < document.querySelectorAll(".bannerText").length; i++){
    var indexForPun = randomNums[i] - 1;
    document.querySelectorAll(".bannerText")[i].innerText = puns[indexForPun];
}
