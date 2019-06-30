package com.bhbac.kotlinstudysample

import java.util.stream.Collectors
import java.util.stream.DoubleStream
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.streams.asStream

fun main() {
    println(">>>>> 10장 테스트 시작 <<<<<")

    var stream = 1.rangeTo(10).asSequence().asStream()
    println("${stream.skip(5).collect(Collectors.toList())}")
    stream = 1.rangeTo(10).asSequence().asStream()
    println("${stream.filter { it % 2 == 0 }.collect(Collectors.toList())}")

    println("Primitive Stream : ${IntStream.range(1, 1).sum()}")
    println("Primitive Stream : ${IntStream.range(1, 2).sum()}")
    println("Primitive Stream : ${IntStream.range(1, 3).sum()}")
    println("Primitive Stream : ${IntStream.range(1, 10).sum()}")
    println("Primitive Stream : ${IntStream.range(1, 11).sum()}")

    var doubleStream = DoubleStream.iterate(1.5) { it * 1.3 }
    var avg = doubleStream
            .limit(10)
            .peek {
                println("아이템 : $it")
            }
            .average()
    println("Primitive Stream : $avg")

    var stringStream = Stream.builder<String>()
            .add("아이렘~ 1")
            .add("아이렘~ 2")
            .add("아이렘~ 3")
            .add("아이렘~ 4")
            .add("아이렘~ 5")
            .add("아이렘~ 6")
            .add("아이렘~ 7")
            .add("아이렘~ 8")
            .add("아이렘~ 9")
            .add("아이렘~ 10")
            .build()
    println("스트림 ${stringStream.collect(Collectors.toList())}")

    println("아이템 ${Stream.empty<String>().findAny()}")
    println("아이템 ${Stream.of("아이템1", 2, "아이템 3", 4, 5.0, "아이템6").collect(Collectors.toList())}")

    var randomList = Stream.generate {
        (1..20).random()
    }
            .limit(10)
            .collect(Collectors.toList())
    println("resultantList = $randomList")

    println("resultantSet ${(0..10).asSequence().asStream().collect(Collectors.toCollection { LinkedHashSet<Int>() })}")

    val resultantMap = (0..10).asSequence().asStream()
            .collect(Collectors.toMap<Int, Int, Int>({
                it
            }, {
                it * it
            }))
    println("resultantMap = $resultantMap")
    val resultantMap2 = (0..10).asSequence().asStream()
            .collect(Collectors.toMap<Int, Int, String>({
                it
            }, {
                "오호라 ${it * it}"
            }))
    println("resultantMap2 = $resultantMap2")

    stringStream = Stream.builder<String>()
            .add("아이렘~ 1")
            .add("아이렘~ 2")
            .add("아이렘~ 3")
            .add("아이렘~ 4")
            .add("아이렘~ 5")
            .add("아이렘~ 6")
            .build()
    val joinResult = stringStream.collect(Collectors.joining(" === ", "started =>", "<= ended"))
    println("joining : $joinResult")

    println("groupingBy : ${(1..20).asSequence().asStream().collect(Collectors.groupingBy<Int, Int> { it % 5 })}")
    val groupingResult = (1..20).asSequence().asStream().collect(Collectors.groupingBy<Int, String> { "result ${it % 5} group =" })
    println("groupingBy : $groupingResult")

    println(">>>>> 10장 테스트 종료 <<<<<")
}