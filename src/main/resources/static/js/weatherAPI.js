
//change to http://api.openweathermap.org/data/2.5/weather?q=" + golfCourseCity + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7

window.addEventListener("load", function()
{
    $.getJSON("http://api.openweathermap.org/data/2.5/forecast?q=vancouver&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
    {
        var info_date = new Date(((data.list[0].dt) +  data.city.timezone)*1000);
        document.getElementById("date1").value = info_date.toDateString();
        document.getElementById("date2").value = info_date.toDateString();
        document.getElementById("date3").value = info_date.toDateString();
        document.getElementById("date4").value = info_date.toDateString();
        document.getElementById("date5").value = info_date.toDateString();
        
        info_date.getHours();
    }
    );
});


document.getElementById("today6").onclick = function () {getWeatherData(0)};
document.getElementById("today9").onclick = function () {getWeatherData(1)};
document.getElementById("today12").onclick = function () {getWeatherData(2)};
document.getElementById("today15").onclick = function () {getWeatherData(3)};

function getWeatherData(i)
{
    $.getJSON("http://api.openweathermap.org/data/2.5/forecast?q=vancouver&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
    {
        var info_date = new Date(((data.list[i].dt) +  data.city.timezone)*1000);
        info_date.getHours();


        var icon = "http://openweathermap.org/img/w/" + data.list[i].weather[i].icon + ".png";

        var temp = Math.floor(data.list[i].main.temp);
        var weather = data.list[i].weather[i].main; 
        var weather_desc = data.list[i].weather[i].description;
        var wind = data.list[i].wind.speed;

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