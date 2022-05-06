# MovieFan
TMDB does not work in all countries, you may need to enable VPN for the application to work. In addition, you must specify your API key. If you have problems opening the application, write to me in telegram: @ivan_emelin

Multi-module project to get information from TMDB.

## Features
* Kotlin
* Hilt
* MVVM
* Multi-module
* CustomView: Roulette for choosing film
* Custom image loader
* Custom navigation
* ANR tracking
* Widgets on main screen

# [[CustomImageLoader]](https://github.com/avelycure/MovieFan/tree/master/image-loader)
To load images, a module was written that uses a pool of threads to quickly load a lot of pictures

# [[CustomView]]
Here I used LaRoulette, custom view from one of [[my repositories]](https://github.com/avelycure/LaRoulette)

# [[Custom navigation]](https://github.com/avelycure/MovieFan/tree/master/navigation)
Here I created a class that is responsible for navigation in the application. Implemented a stack stack structure and saving the state of fragments when the screen is rotated

# [[ANR tracking]](https://github.com/avelycure/MovieFan/tree/master/crash-report)
The way to check if ANR occurred is to ping main thread using handler

# [[Widgets]](https://github.com/avelycure/MovieFan/tree/master/widgets)
Small signs where you can quickly see the most popular actors and the most interesting films

# Screenshots
<p>
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/1.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/2.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/3.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/4.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/5.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/6.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/7.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/8.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/9.jpg" width="256" />
  <img src="https://github.com/avelycure/avelycure/blob/master/assets/movieFan/new/10.jpg" width="256" />
</p>
