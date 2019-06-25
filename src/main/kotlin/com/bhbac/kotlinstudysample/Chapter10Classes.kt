package com.bhbac.kotlinstudysample

sealed class Option<out T> {
    object None : Option<Nothing>() {
        override fun toString() = "None"
    }

    data class Some<out T>(val value: T) : Option<T>()
    companion object
}

fun <T, R> Option<T>.map(transform: (T) -> (R)): Option<R> = when (this) {
    Option.None -> Option.None
    is Option.Some -> Option.Some(transform(value))
}

fun <A, B, C> ((A) -> B).map(transform: (B) -> C): (A) -> C = { t ->
    transform(this(t))
}

fun <T, R> Option<T>.flatMap(transform: (T) -> Option<R>): Option<R> = when (this) {
    Option.None -> Option.None
    is Option.Some -> transform(value)
}

fun <T, R> Option<T>.mapWithFlatMap(transform: (T) -> R): Option<R> = flatMap {
    Option.Some(transform(it))
}

fun calculateDiscount(price: Option<Double>): Option<Double> {
    return price.flatMap {
        if (it > 50.0)
            Option.Some(5.0)
        else
            Option.None
    }
}

fun <T, R> List<T>.applicative(functions: List<(T) -> R>): List<R> = functions.flatMap { function ->
    this.map(function)
}



// TODO: Here
fun <T> Option.Companion.pure(t: T): Option<T> = Option.Some(t)