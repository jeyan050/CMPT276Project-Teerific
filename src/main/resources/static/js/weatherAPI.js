
//change to http://api.openweathermap.org/data/2.5/weather?q=" + golfCourseCity + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7

document.getElementById("today6").onclick = function () {getWeatherData()};

function getWeatherData()
{
    $.getJSON("http://api.openweathermap.org/data/2.5/forecast?q=vancouver&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
    {
        console.log(data); //take out
        var icon = "http://openweathermap.org/img/w/" + data.list[0].weather[0].icon + ".png";

        var temp = Math.floor(data.list[0].main.temp);
        var weather = data.list[0].weather[0].main; 
        var weather_desc = data.list[0].weather[0].description;
        var wind = data.list[0].wind.speed;

        $('.icon').attr('src',icon);
        $('.weather').append(weather);
        $('.temp').append(temp);
        $('.wind').append(wind);
    }
    );
}


// $.getJSON("http://api.openweathermap.org/data/2.5/forecast?q=vancouver&appid=96bf787bdb96400f9a642360f1e901d7")
// {
    
// }