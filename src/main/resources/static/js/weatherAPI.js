var offset;
var city = document.getElementById("city").innerHTML;
var opening_time = parseInt(document.getElementById("open_hours").innerHTML);
var closing_time = parseInt(document.getElementById("close_hours").innerHTML);
var selectedDate;
var selectedTime;
var currentSelectorIndex = 0;
var dates = document.querySelectorAll(".chooseDate").length;
var timeSelectors = document.querySelectorAll(".chooseTime").length;
var totalTimes = document.querySelectorAll(".validTime").length;



// ***************
// FUNCTIONS
// ***************

function windowEventListener(){
    window.addEventListener("load", function(){
        $.getJSON("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "," + country + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data){
            var first_data = new Date((data.list[0].dt)*1000);
            offset = Math.floor((first_data.getHours())/3);
            const days = [];
            for (let i = 0; i < 40; i++)                            //goes through each entry and assigns unique days to an array
            {
                var info_date = new Date((data.list[i].dt)*1000);   //it automatically adjusts 
                date = info_date.toDateString();
                if (days.indexOf(date) == -1){                      //checks if the day is already in the list
                    days.push(date);
                } 
            }
    
            //assign the buttons the values of the dates
            $("#date1").html(days[0]);
            $("#date2").html(days[1]);
            $("#date3").html(days[2]);
            $("#date4").html(days[3]);
            $("#date5").html(days[4]);
            if (days.length > 5){                                    //if there is an entry for the 6th day, display it, else, hide the button
                $("#date6").html(days[5]);
            }else{
                $("#date6").hide();
            }
            
            var timeIndex = 0;
            for (let i = 1; i <= 6; i++){               //for each day
                for (let j = 1; j <= 8; j++){           //for each 3hour block
                    if (((i - 2) * 8) + j - 1 + (8 - offset) > 0 && ((i - 2) * 8) + j - 1 + (8 - offset) < 40){
                        var hour = new Date((data.list[((i-2)*8) + j-1 + (8 - offset)].dt)*1000);   //it automatically adjusts even though the documentation says it does not
                        if (hour.getHours() < opening_time || hour.getHours() > closing_time){       //hides the button if the time is outside hours of opperation
                            $("#time" + timeIndex.toString()).hide();
                        }
                        $("#time" + timeIndex.toString()).html(hour.getHours() + ":00");
                    }else{
                        $("#time" + timeIndex.toString()).hide();
                    }
                    timeIndex++;
                }
            }
        }
        );
    });
}////windowEventListener()


function hideAllTimeSelectors(){
    for(var i = 0; i < document.querySelectorAll(".chooseTime").length; i++){
        document.querySelectorAll(".chooseTime")[i].style.display = "none";
    }
}//hideAllTimeSelectors()


function setupDateEventListeners(){
    for(var i = 0; i < dates; i++){
        document.querySelectorAll(".chooseDate")[i].addEventListener("change", function(event){
            selectedDate = event.target.value;
            console.log("date changed");
            console.log("selectedDate = " + selectedDate);
    
            //hide the old selector
            document.querySelectorAll(".chooseTime")[currentSelectorIndex].style.display = "none";
            console.log("TimeSelectorID = " + currentSelectorIndex + " now hidden");
            
            currentSelectorIndex = event.target.selectedIndex - 1;
            console.log("TimeSelectorID = " + currentSelectorIndex + " searched for...");
           
            //reveal the new one
            document.querySelectorAll(".chooseTime")[currentSelectorIndex].style.display = "initial";
            console.log("TimeSelectorID = " + currentSelectorIndex + " is found and being revealed...");
        })
    }
}//setupDateEventListeners()


function setupTimeEventListeners(){
    for(var i = 0; i < timeSelectors; i++){
        document.querySelectorAll(".chooseTime")[i].addEventListener("change", function(event){
            selectedTime = event.target.value;
            console.log("time changed");
            console.log("selectedTime = " + selectedTime);
    
            var currentSelector = event.target;
    
            var id = "";
            if(selectedDate.length !== 0 && selectedTime.length !== 0){
                console.log("entered comparison loop...");
                var index = 0;
    
                //search through the current selectors children
                for(var j = 0; j < currentSelector.children.length; j++){
                    if(selectedTime.localeCompare(currentSelector.children[j].value) === 0){
                        //the id of the current option
                        id = currentSelector.children[j].id;
                        console.log("id = " + id);
    
                        //loop through all the options, the 'k' will be the index that gets passed into the weather API
                        for(var k = 0; k < totalTimes; k++){
                            if(document.querySelectorAll(".validTime")[k].id.localeCompare(id) === 0){
                                index = k;
                                console.log("time index found: " + k);
                                getWeatherData(k - offset);
                                break;
                            }
                        }
                    }
                }
            }
        })//eventListener()
    }//loopForSettingUpEventListeners
}//setupTimeEventListeners()


function hideAllWeatherIcons(){
    hideWeatherIcon(".sunny");
    hideWeatherIcon(".cloudy");
    hideWeatherIcon(".rainy");
    hideWeatherIcon(".stormy");
    hideWeatherIcon(".snowy");
}//hideAllWeatherIcons()


function hideWeatherIcon(name){
    document.querySelectorAll(name)[0].hidden = true;
}//hideWeatherIcon()


function showWeatherIcon(name){
    document.querySelectorAll(name)[0].hidden = false;
}//showWeatherIcon()


//gets the type from the weather api, returns the string for the class so that it can be revealed
function getNameForIconReveal(type){
    hideAllWeatherIcons();

    if(type.localeCompare("Clear") === 0){
        return ".sunny";
    }else if(type.localeCompare("Clouds") === 0){
        return ".cloudy";
    }else if(type.localeCompare("Rain") === 0){
        return ".rainy";
    }else if(type.localeCompare("Thunderstorm") === 0){
        return ".stormy";
    }else if(type.localeCompare("Snow") === 0){
        return ".snowy";
    }
}//gteNameForIconReveal()


function getWeatherData(i){
    $.getJSON("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "," + country + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data){
        var info_date = new Date((data.list[i].dt)*1000);           //it automatically adjusts even though the documentation says otherwise
    
        var type = data.list[i].weather[0].main;
        var name = getNameForIconReveal(type);
        showWeatherIcon(name);

        var temp = Math.floor(data.list[i].main.temp);
        var temp_feel = Math.floor(data.list[i].main.feels_like);
        var weather = data.list[i].weather[0].main; 
        var weather_desc = data.list[i].weather[0].description;
        var wind = data.list[i].wind.speed;
        var gust = data.list[i].wind.gust;
        var prob_rain = (data.list[i].pop)*100;

        $('.forcast_title').text(info_date.toDateString() + ", " + info_date.getHours() + ":00");
        $('.weather').text(weather);
        $('.weather_description').text(weather_desc);
        $('.temp').text("Temperature: " + temp +  '\u00B0C');
        $('.temp_feels_like').text("Feels like: " + temp_feel +  '\u00B0C');
        $('.wind').text("Wind speed: " + wind + "  m/s");
        $('.gust').text("Gusts up to: " + gust + "  m/s");
        $('.prob_rain').text(prob_rain + "% chance of Rain");
    }
    );
}//getWeatherData()


// *****************
// STARTUP
// *****************

windowEventListener();
hideAllTimeSelectors();         //hide the time selectors initially
setupDateEventListeners();
setupTimeEventListeners();
hideAllWeatherIcons();          //hide all the icons initially


