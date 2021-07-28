
//change to http://api.openweathermap.org/data/2.5/weather?q=" + golfCourseCity + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7

window.addEventListener("load", function()
{
    $.getJSON("http://api.openweathermap.org/data/2.5/forecast?q=vancouver&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
    {
        const days = [];
        for (let i = 0; i < 40; i++)
        {
            var info_date = new Date((data.list[i].dt)*1000); //it automatically adjusts for some dumb ass reason even though the documentatino says it does not, I spent 3 hours trying to fix that problem
            date = info_date.toDateString();
            if (days.indexOf(date) == -1)
            {
                days.push(date);
            } 
        }
        console.log(days);

        $("#date1").html(days[0]);
        $("#date2").html(days[1]);
        $("#date3").html(days[2]);
        $("#date4").html(days[3]);
        $("#date5").html(days[4]);
        if (days.length > 5)
        {
            $("#date6").html(days[5]);
        }
        else
        {
            $("#date6").hide();
        }
        
        for (let i = 1; i <= 5; i++) //change the 5 to a 6?
        {
            for (let j = 1; j <= 8; j++)
            {
                var hour = new Date((data.list[((i-1)*8) + j-1].dt)*1000); //it automatically adjusts for some dumb ass reason even though the documentatino says it does not, I spent 3 hours trying to fix that problem
                //if .getHours is less then opening time or greater then opening time, .hide the button
                // console.log("date" + i.toString() + "_" + j.toString());
                // console.log(hour.getHours() + ":00");
                $("#date" + i.toString() + "_" + j.toString()).html(hour.getHours() + ":00");

            }
        }
    }
    );
});


document.getElementById("date1_1").onclick = function () {getWeatherData(0)};
document.getElementById("date1_2").onclick = function () {getWeatherData(1)};
document.getElementById("date1_3").onclick = function () {getWeatherData(2)};
document.getElementById("date1_4").onclick = function () {getWeatherData(3)};
document.getElementById("date1_5").onclick = function () {getWeatherData(4)};
document.getElementById("date1_6").onclick = function () {getWeatherData(5)};
document.getElementById("date1_7").onclick = function () {getWeatherData(6)};
document.getElementById("date1_8").onclick = function () {getWeatherData(7)};

document.getElementById("date2_1").onclick = function () {getWeatherData(8)};
document.getElementById("date2_2").onclick = function () {getWeatherData(9)};
document.getElementById("date2_3").onclick = function () {getWeatherData(10)};
document.getElementById("date2_4").onclick = function () {getWeatherData(11)};
document.getElementById("date2_5").onclick = function () {getWeatherData(12)};
document.getElementById("date2_6").onclick = function () {getWeatherData(13)};
document.getElementById("date2_7").onclick = function () {getWeatherData(14)};
document.getElementById("date2_8").onclick = function () {getWeatherData(15)};

document.getElementById("date3_1").onclick = function () {getWeatherData(16)};
document.getElementById("date3_2").onclick = function () {getWeatherData(17)};
document.getElementById("date3_3").onclick = function () {getWeatherData(18)};
document.getElementById("date3_4").onclick = function () {getWeatherData(19)};
document.getElementById("date3_5").onclick = function () {getWeatherData(20)};
document.getElementById("date3_6").onclick = function () {getWeatherData(21)};
document.getElementById("date3_7").onclick = function () {getWeatherData(22)};
document.getElementById("date3_8").onclick = function () {getWeatherData(23)};

document.getElementById("date4_1").onclick = function () {getWeatherData(24)};
document.getElementById("date4_2").onclick = function () {getWeatherData(25)};
document.getElementById("date4_3").onclick = function () {getWeatherData(26)};
document.getElementById("date4_4").onclick = function () {getWeatherData(27)};
document.getElementById("date4_5").onclick = function () {getWeatherData(28)};
document.getElementById("date4_6").onclick = function () {getWeatherData(29)};
document.getElementById("date4_7").onclick = function () {getWeatherData(30)};
document.getElementById("date4_8").onclick = function () {getWeatherData(31)};

document.getElementById("date5_1").onclick = function () {getWeatherData(32)};
document.getElementById("date5_2").onclick = function () {getWeatherData(33)};
document.getElementById("date5_3").onclick = function () {getWeatherData(34)};
document.getElementById("date5_4").onclick = function () {getWeatherData(35)};
document.getElementById("date5_5").onclick = function () {getWeatherData(36)};
document.getElementById("date5_6").onclick = function () {getWeatherData(37)};
document.getElementById("date5_7").onclick = function () {getWeatherData(38)};
document.getElementById("date5_8").onclick = function () {getWeatherData(39)};

// document.getElementById("date6_1").onclick = function () {getWeatherData(39)};
// document.getElementById("date6_2").onclick = function () {getWeatherData(1)};
// document.getElementById("date6_3").onclick = function () {getWeatherData(2)};
// document.getElementById("date6_4").onclick = function () {getWeatherData(3)};
// document.getElementById("date6_5").onclick = function () {getWeatherData(4)};
// document.getElementById("date6_6").onclick = function () {getWeatherData(5)};
// document.getElementById("date6_7").onclick = function () {getWeatherData(6)};
// document.getElementById("date6_8").onclick = function () {getWeatherData(7)};


function getWeatherData(i)
{
    //console.log(i);
    //change to http://api.openweathermap.org/data/2.5/weather?q=" + golfCourseCity + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7

    $.getJSON("http://api.openweathermap.org/data/2.5/forecast?q=vancouver&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
    {
        console.log(i);
        var info_date = new Date((data.list[i].dt)*1000); //it automatically adjusts for some dumb ass reason even though the documentatino says it does not, I spent 3 hours trying to fix that problem
        
        var icon = "http://openweathermap.org/img/w/" + data.list[i].weather[0].icon + ".png";

        var temp = Math.floor(data.list[i].main.temp);
        var weather = data.list[i].weather[0].main; 
        var weather_desc = data.list[i].weather[0].description;
        var wind = data.list[i].wind.speed;

        $('.forcast_title').text(info_date.toDateString() + ", " + info_date.getHours() + ":00");
        // console.log(info_date.toDateString() + ", " + info_date.getHours() + ":00");
        // console.log(data.list[i].dt_txt);
        $('.icon').attr('src',icon);
        $('.weather').text(weather);
        $('.temp').text("Temperature:" + temp +  '\u00B0C');
        $('.wind').text(" Wind speed: " + wind + "  m/s");
    }
    );
}


// $.getJSON("http://api.openweathermap.org/data/2.5/forecast?q=vancouver&appid=96bf787bdb96400f9a642360f1e901d7")
// {
    
// }