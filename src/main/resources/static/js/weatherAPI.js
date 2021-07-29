var offset;
var city = document.getElementById("city").innerHTML;
var opening_time = parseInt(document.getElementById("open_hours").innerHTML);
var closing_time = parseInt(document.getElementById("close_hours").innerHTML);

window.addEventListener("load", function()
{
    $.getJSON("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
    {
        var first_data = new Date((data.list[0].dt)*1000);
        offset = Math.floor((first_data.getHours())/3);
        // console.log(first_data.getHours());
        // console.log(offset);
        const days = [];
        for (let i = 0; i < 40; i++)  //goes through each eentry and assigns unique days to an array
        {
            var info_date = new Date((data.list[i].dt)*1000); //it automatically adjusts for some dumb ass reason even though the documentatino says it does not, I spent 3 hours trying to fix that problem
            date = info_date.toDateString();
            if (days.indexOf(date) == -1) //checks if the day is already in the list
            {
                days.push(date);
            } 
        }
        // console.log(days);
        //assign the buttons the values of the dates
        $("#date1").html(days[0]);
        $("#date2").html(days[1]);
        $("#date3").html(days[2]);
        $("#date4").html(days[3]);
        $("#date5").html(days[4]);
        if (days.length > 5) //if there is an entry for the 6th day, display it, else, hide the button
        {
            $("#date6").html(days[5]);
        }
        else
        {
            $("#date6").hide();
        }
        
        for (let i = 1; i <= 6; i++)//for each day
        {
            for (let j = 1; j <= 8; j++) //for each 3hour block
            {
                // console.log(((i-2)*8) + j-1 + (8 - offset));
                if (((i-2)*8) + j-1 + (8 - offset) > 0 && ((i-2)*8) + j-1 + (8 - offset) < 40)
                {
                    var hour = new Date((data.list[((i-2)*8) + j-1 + (8 - offset)].dt)*1000); //it automatically adjusts for some dumb ass reason even though the documentatino says it does not, I spent 3 hours trying to fix that problem
                    
                    // console.log("date" + i.toString() + "_" + j.toString());
                    // console.log(hour.getHours() + ":00");
                    if (hour.getHours() < opening_time || hour.getHours() > closing_time) //hides the button if the time is outside hours of opperation
                    {
                        $("#date" + i.toString() + "_" + j.toString()).hide();
                    }
                    $("#date" + i.toString() + "_" + j.toString()).html(hour.getHours() + ":00");
                }
                else
                {
                    $("#date" + i.toString() + "_" + j.toString()).hide();
                }


            }
        }
    }
    );
});


document.getElementById("date1_1").onclick = function () {getWeatherData(0 - offset)};
document.getElementById("date1_2").onclick = function () {getWeatherData(1 - offset)};
document.getElementById("date1_3").onclick = function () {getWeatherData(2 - offset)};
document.getElementById("date1_4").onclick = function () {getWeatherData(3 - offset)};
document.getElementById("date1_5").onclick = function () {getWeatherData(4 - offset)};
document.getElementById("date1_6").onclick = function () {getWeatherData(5 - offset)};
document.getElementById("date1_7").onclick = function () {getWeatherData(6 - offset)};
document.getElementById("date1_8").onclick = function () {getWeatherData(7 - offset)};

document.getElementById("date2_1").onclick = function () {getWeatherData(8 - offset)};
document.getElementById("date2_2").onclick = function () {getWeatherData(9 - offset)};
document.getElementById("date2_3").onclick = function () {getWeatherData(10 - offset)};
document.getElementById("date2_4").onclick = function () {getWeatherData(11 - offset)};
document.getElementById("date2_5").onclick = function () {getWeatherData(12 - offset)};
document.getElementById("date2_6").onclick = function () {getWeatherData(13 - offset)};
document.getElementById("date2_7").onclick = function () {getWeatherData(14 - offset)};
document.getElementById("date2_8").onclick = function () {getWeatherData(15 - offset)};

document.getElementById("date3_1").onclick = function () {getWeatherData(16 - offset)};
document.getElementById("date3_2").onclick = function () {getWeatherData(17 - offset)};
document.getElementById("date3_3").onclick = function () {getWeatherData(18 - offset)};
document.getElementById("date3_4").onclick = function () {getWeatherData(19 - offset)};
document.getElementById("date3_5").onclick = function () {getWeatherData(20 - offset)};
document.getElementById("date3_6").onclick = function () {getWeatherData(21 - offset)};
document.getElementById("date3_7").onclick = function () {getWeatherData(22 - offset)};
document.getElementById("date3_8").onclick = function () {getWeatherData(23 - offset)};

document.getElementById("date4_1").onclick = function () {getWeatherData(24 - offset)};
document.getElementById("date4_2").onclick = function () {getWeatherData(25 - offset)};
document.getElementById("date4_3").onclick = function () {getWeatherData(26 - offset)};
document.getElementById("date4_4").onclick = function () {getWeatherData(27 - offset)};
document.getElementById("date4_5").onclick = function () {getWeatherData(28 - offset)};
document.getElementById("date4_6").onclick = function () {getWeatherData(29 - offset)};
document.getElementById("date4_7").onclick = function () {getWeatherData(30 - offset)};
document.getElementById("date4_8").onclick = function () {getWeatherData(31 - offset)};

document.getElementById("date5_1").onclick = function () {getWeatherData(32 - offset)};
document.getElementById("date5_2").onclick = function () {getWeatherData(33 - offset)};
document.getElementById("date5_3").onclick = function () {getWeatherData(34 - offset)};
document.getElementById("date5_4").onclick = function () {getWeatherData(35 - offset)};
document.getElementById("date5_5").onclick = function () {getWeatherData(36 - offset)};
document.getElementById("date5_6").onclick = function () {getWeatherData(37 - offset)};
document.getElementById("date5_7").onclick = function () {getWeatherData(38 - offset)};
document.getElementById("date5_8").onclick = function () {getWeatherData(39 - offset)};

document.getElementById("date6_1").onclick = function () {getWeatherData(40 - offset)};
document.getElementById("date6_2").onclick = function () {getWeatherData(41 - offset)};
document.getElementById("date6_3").onclick = function () {getWeatherData(42 - offset)};
document.getElementById("date6_4").onclick = function () {getWeatherData(43 - offset)};
document.getElementById("date6_5").onclick = function () {getWeatherData(44 - offset)};
document.getElementById("date6_6").onclick = function () {getWeatherData(45 - offset)};
document.getElementById("date6_7").onclick = function () {getWeatherData(46 - offset)};
document.getElementById("date6_8").onclick = function () {getWeatherData(47 - offset)};


function getWeatherData(i)
{
    $.getJSON("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
    {
        console.log(i);
        var info_date = new Date((data.list[i].dt)*1000); //it automatically adjusts for some dumb ass reason even though the documentatino says it does not, I spent 3 hours trying to fix that problem
        
        var icon = "http://openweathermap.org/img/w/" + data.list[i].weather[0].icon + ".png";

        var temp = Math.floor(data.list[i].main.temp);
        var temp_feel = Math.floor(data.list[i].main.feels_like);
        var weather = data.list[i].weather[0].main; 
        var weather_desc = data.list[i].weather[0].description;
        var wind = data.list[i].wind.speed;
        var gust = data.list[i].wind.gust;
        var prob_rain = data.list[i].pop;
        // var rain_vol = data.list[i].rain.3h;

        $('.forcast_title').text(info_date.toDateString() + ", " + info_date.getHours() + ":00");
        // console.log(info_date.toDateString() + ", " + info_date.getHours() + ":00");
        // console.log(data.list[i].dt_txt);
        $('.icon').attr('src',icon);
        $('.weather').text(weather);
        $('.weather_description').text(weather_desc);
        $('.temp').text("Temperature:" + temp +  '\u00B0C');
        $('.temp_feels_like').text("Feels like:" + temp_feel +  '\u00B0C');
        $('.wind').text("Wind speed: " + wind + "  m/s");
        $('.gust').text("Gusts up to: " + gust + "  m/s");
        $('.prob_rain').text(prob_rain + "% chance of Rain");
    }
    );
}