
//change to http://api.openweathermap.org/data/2.5/weather?q=" + golfCourseCity + "&units=metric&appid=96bf787bdb96400f9a642360f1e901d7

$.getJSON("http://api.openweathermap.org/data/2.5/weather?q=vancouver&units=metric&appid=96bf787bdb96400f9a642360f1e901d7", function(data)
{
    console.log(data);
    var icon = "http://openweathermap.org/img/w/" + data.weather[0].icon + ".png";

    var temp = Math.floor(data.main.temp);
    var weather = data.weather[0].main;
    var wind = data.wind.speed;

    $('.icon').attr('src',icon);
    $('.weather').append(weather);
    $('.temp').append(temp);
    $('.wind').append(wind);
}
);
