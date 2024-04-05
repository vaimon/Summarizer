package me.vaimon.summarizer.presentation.navigation

interface NavigationDestination {
    val route: String
}

abstract class NavigationDestinationWithArg<T> : NavigationDestination {
    abstract val routeBase: String
    abstract val argName: String
    override val route: String
        get() = "$routeBase/{$argName}"

    fun getDestinationWithArg(arg: T) = "$routeBase/$arg"
}