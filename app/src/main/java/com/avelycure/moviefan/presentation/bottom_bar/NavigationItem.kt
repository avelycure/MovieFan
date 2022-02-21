package com.avelycure.moviefan.presentation.bottom_bar

sealed class NavigationItem(var route: String, var icon: Int, var title: String){
    object Home : NavigationItem("popular_movies", android.R.drawable.ic_menu_camera, "Movies")
    object Persons : NavigationItem("persons", android.R.drawable.ic_menu_gallery, "Persons")
}
