package com.bhbac.kotlinstudysample

import java.lang.Exception

fun main() {
    println(">>>>> 10장 테스트 시작 <<<<<")

    println("Option.Some(\"Kotlin\").toUpperCase = ${Option.Some("Kotlin").map(String::toUpperCase)}")
    println("Option.None.toUpperCase = ${Option.None.map(String::toUpperCase)}")

    val add3AndMultiplyBy2: (Int) -> Int = { i: Int -> i + 3 }.map { i: Int -> i * 2 }
    println("add3AndMultiplyBy2(0) = ${add3AndMultiplyBy2(0)}")
    println("add3AndMultiplyBy2(1) = ${add3AndMultiplyBy2(1)}")
    println("add3AndMultiplyBy2(2) = ${add3AndMultiplyBy2(2)}")
    val intToException: (Int) -> Exception = { i: Int -> "Num :$i" }.map { s: String -> Exception("$s Occurred!") }
    println("intToException(0) = ${intToException(0)}")
    println("intToException(1) = ${intToException(1)}")
    println("intToException(2) = ${intToException(2)}")

    println("calculateDiscount(80) = ${calculateDiscount(Option.Some(80.0))}")
    println("calculateDiscount(30) = ${calculateDiscount(Option.Some(30.0))}")
    println("calculateDiscount(None) = ${calculateDiscount(Option.None)}")

    val maybeFive = Option.Some(5)
    val maybeTwo = Option.Some(2)
    println(maybeFive.flatMap { f ->
        maybeTwo.flatMap { t ->
            Option.Some(f + t)
        }
    })
    println(maybeFive.flatMap { f ->
        maybeTwo.map { t -> f + t }
    })
    println("Option.Some(\"Kotlin\").toLowerCase = ${Option.Some("Kotlin").mapWithFlatMap(String::toLowerCase)}")
    println("Option.None.toLowerCase = ${Option.None.mapWithFlatMap(String::toLowerCase)}")

    val numbers = listOf(1, 2, 3)
    val functions = listOf<(Int) -> Int>({i -> i * 2}, {i -> i + 3})
    var result = numbers.flatMap { number ->
        functions.map { function ->
            function(number)
        }
    }
    println(result)

    result = numbers.applicative(functions)
    println(result)

    println(">>>>> 10장 테스트 종료 <<<<<")
}