package com.example.project.ui.screen.container

sealed class NavGraph(val route: String ){
    data object Main : NavGraph(route = "main_screen")
    data object Welcome : NavGraph(route = "welcome_screen")
    data object Login: NavGraph(route = "login_screen")
    data object Register: NavGraph(route = "register_screen")
   data object ForgotPassword : NavGraph(route = "forgot_password")
    data  object Home : NavGraph(route = "home_screen")
    data object Search : NavGraph(route = "search_screen")
    data object Saved : NavGraph(route = "saved_screen")
    data object Profile : NavGraph(route = "profile_screen")
    data object Notifications : NavGraph(route = "notifications_screen")
    data object About : NavGraph(route = "about_screen")
    data object HowItWorks : NavGraph(route = "how_it_works_screen")
    data object Contact : NavGraph(route = "contact_screen")
    data object Messages : NavGraph(route = "messages_screen")
    data object ChatDetail : NavGraph(route = "chat_detail_screen")

    data object PropertyDetail : NavGraph(route = "property_detail_screen/{propertyId}") {
        fun createRoute(propertyId: String) = "property_detail_screen/$propertyId"
    }

}
