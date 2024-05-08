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
abstract class NavigationDestinationWithArgs<T, S> : NavigationDestination {
    abstract val routeBase: String
    abstract val arg1Name: String
    abstract val arg2Name: String
    override val route: String
        get() = "$routeBase/{$arg1Name}/{$arg2Name}"

    fun getDestinationWithArgs(arg1: T, arg2: S) = "$routeBase/$arg1/$arg2"
}
