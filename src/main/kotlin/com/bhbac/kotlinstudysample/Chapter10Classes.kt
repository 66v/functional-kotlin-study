package com.bhbac.kotlinstudysample

import kotlin.system.measureNanoTime

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

fun <A, B, C> ((A) -> B).flatMap(lambdaTransform: (B) -> (A) -> C): (A) -> C = { t ->
    lambdaTransform(this(t))(t)
}

fun <A, B, C> ((A) -> B).applicative(functions: (A) -> (B) -> C): (A) -> C = functions.flatMap { function ->
    map(function)
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

fun <T> Option.Companion.pure(t: T): Option<T> = Option.Some(t)

fun <T, R> Option<T>.applicative(functions: Option<(T) -> R>): Option<R> = functions.flatMap { function ->
    map(function)
}

infix fun <T, R> Option<(T) -> R>.funcAdd(option: Option<T>): Option<R> = flatMap { function: (T) -> R ->
    option.map(function)
}

object Function2 {
    fun <A, B> pure(b: B) = { _: A -> b }
}

fun recursiveFib(n: Long): Long = if (n < 2) {
    n
} else {
    recursiveFib(n - 1) + recursiveFib(n - 2)
}

fun imperativeFib(n: Long): Long {
    return when (n) {
        0L -> 0
        1L -> 1
        else -> {
            var a = 0L
            var b = 1L
            var c = 0L
            for (i in 2 .. n) {
                c = a + b
                a = b
                b = c
            }
            c
        }
    }
}

inline fun milliseconds(description: String, body: () -> Unit): String {
    return "$description:${measureNanoTime(body) / 1_000_000.00} ms"
}