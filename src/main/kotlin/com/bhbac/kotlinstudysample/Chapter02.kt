/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudysample



import com.bhbac.kotlinstudysample.Chapter02.FunList.Cons
import com.bhbac.kotlinstudysample.Chapter02.FunList.Nil

import com.bhbac.kotlinstudysample.Chapter02.MyClass.Empty
import com.bhbac.kotlinstudysample.Chapter02.MyClass.Sub01

typealias Machine2<T> = (T) -> Unit

class Chapter02 {

    fun main() {
        println(capitalize("hello world!"))
        println(capitalize2("abcc cdeff"))
        println(transform("abc", capitalize))
        println(transform("def", capitalize2))
        println(transform("hij", capitalize))
        println(transform("klm", 66, twoGeneric))
        println(transform("abcdefg", { str -> str.substring(0..1) }))
        println(transform("hijklmn", { it.substring(2..3) }))
        println(transform("abcdefg", ::reverse))

        val my = ParentClass("init")
        println("my.value = ${my.value}")
        val fn = { cl: ParentClass ->
            cl.value = "override"
        }
        fn(my)
        println("my.value = ${my.value}")

        val cond = false
        unless(cond) {
            println("oh no!")
        }

        useMachine(5, PrintMachine())
        useMachine(55, object : Machine<Int> {
            override fun process(t: Int) {
                println(t)
            }
        })

        useMachine2(6, PrintMachine2())
        useMachine2(66, ::println)
        useMachine2(666) { i ->
            println(i)
        }

        println(factorial(5))
        println(functionalFactorial(5))
        println(tailrecFactorial(5))
        println(executionTime { factorial(20) })
        println(executionTime { functionalFactorial(20) })
        println(executionTime { tailrecFactorial(20) })

        println(fib(15))
        println(factorialFib(15))
        println(tailrecFib(15))
        println(executionTime { fib(920) })
        println(executionTime { factorialFib(920) })
        println(executionTime { tailrecFib(920) })

        val i by lazy {
            println("느린 초기화")
            1
        }

        println("before i")
        println(i)

        val list = listOf({ 1 }, { 2 }, { 3 / 0 }, { 4 })
        println(list.size)
        println(list.get(1)())

        val nums = listOf(1, 2, 3, 4)
        val numsTwice: MutableList<Int> = mutableListOf()
        val numsTri = nums.map { i -> i * 3 }
        for (i in nums) {
            println("i = $i")
            numsTwice.add(i * 2)
        }
        nums.forEach { i -> println("i => $i") }
        numsTwice.forEach { i -> println("i =>> $i") }
        numsTri.forEach { i -> println("i =>>> $i") }

        println(nums.sum())
        var sum = nums.fold(-3) { acc, i ->
            println("acc($acc), i($i)")
            acc + i
        }
        println(sum)
        sum = nums.foldRight(-3) { i, acc ->
            println("acc($acc), i($i)")
            acc + i
        }
        println(sum)
        sum = nums.reduce { acc, i ->
            println("acc($acc), i($i)")
            acc + i
        }
        println(sum)
        sum = nums.reduceRight { i, acc ->
            println("acc($acc), i($i)")
            acc + i
        }
        println(sum)

        val funNums = Cons(1, Cons(2, Cons(3, Nil)))
        println(funNums)
        val funNums2 = intListOf(1, 2, 3, 4)
        println(funNums2)
        funNums2.forEach { i -> println("i = $i") }
        sum = funNums2.fold(-3) { acc, i ->
            println("acc($acc), i($i)")
            acc + i
        }
        println(sum)

        println("funList fold time : ${executionTime { funNums2.fold(0) { acc, i -> acc + i } }}")
        println("list fold time : ${executionTime { nums.fold(0) { acc, i -> acc + i } }}")

        println("reverse() : ${funNums2.reverse()}")
        println("foldRight() : ${funNums2.foldRight(-3) { acc, i -> acc + i }}")
        println("map() : ${funNums2.map() { "triple_" + it * 3 }}")
    }

    class ParentClass(var value: String)

    val capitalize = { str: String -> str.capitalize() }
    val capitalize2 = object : Function1<String, String> {
        override fun invoke(p1: String): String {
            return p1.capitalize()
        }
    }

    fun transform(str: String, fn: (String) -> String): String {
        return fn(str)
    }

    fun <T> transform(t: T, fn: (T) -> T): T {
        return fn(t)
    }

    val twoGeneric = { str: String, i: Int -> "twoGeneric($str, $i)" }
    fun <T, S> transform(t: T, s: S, fn: (T, S) -> T): T {
        return fn(t, s)
    }

    fun reverse(str: String): String {
        return str.reversed()
    }

    fun unless(condition: Boolean, fn: () -> Unit) {
        if (!condition) {
            fn()
        }
    }

    interface Machine<T> {
        fun process(t: T)
    }

    fun <T> useMachine(t: T, machine: Machine<T>) {
        machine.process(t)
    }

    class PrintMachine<T> : Machine<T> {
        override fun process(t: T) {
            println(t)
        }
    }

    fun <T> useMachine2(t: T, machine: Machine2<T>) {
        machine(t)
    }

    class PrintMachine2<T> : Machine2<T> {
        override fun invoke(p1: T) {
            println(p1)
        }
    }

    fun factorial(n: Long): Long {
        var result = 1L
        for (i in 1..n) {
            result *= i
        }
        return result
    }

    fun functionalFactorial(n: Long): Long {
        fun loop(n: Long, acc: Long): Long {
            return if (n < 1) {
                acc
            } else {
                loop(n - 1, n * acc)
            }
        }

        return loop(n, 1)
    }

    fun tailrecFactorial(n: Long): Long {
        tailrec fun loop(n: Long, acc: Long): Long {
            return if (n < 1) {
                acc
            } else {
                loop(n - 1, n * acc)
            }
        }

        return loop(n, 1)
    }

    fun executionTime(body: () -> Unit): Long {
        val startTime = System.nanoTime()
        body()
        val endTime = System.nanoTime()
        return endTime - startTime
    }

    fun fib(n: Long): Long {
        return when (n) {
            0L -> 0
            1L -> 1
            else -> {
                var a = 0L
                var b = 1L
                var c = 0L
                for (i in 2..n) {
                    c = a + b
                    a = b
                    b = c
                }
                c
            }
        }
    }

    fun factorialFib(n: Long): Long {
        fun loop(a: Long, b: Long, cnt: Long): Long {
            val c = a + b
            return if (cnt < 1) {
                a
            } else
                loop(b, c, cnt - 1)
        }

        return loop(0, 1, n)
    }

    fun tailrecFib(n: Long): Long {
        tailrec fun loop(a: Long, b: Long, cnt: Long): Long {
            val c = a + b
            return if (cnt < 1) {
                a
            } else
                loop(b, c, cnt - 1)
        }

        return loop(0, 1, n)
    }

    sealed class FunList<out T> {
        object Nil : FunList<Nothing>()
        data class Cons<out T>(val head: T, val tail: FunList<T>) : FunList<T>()

        fun forEach(f: (T) -> Unit) {
            tailrec fun go(list: FunList<T>, f: (T) -> Unit) {
                when (list) {
                    is Cons -> {
                        f(list.head)
                        go(list.tail, f)
                    }
                    is Nil -> Unit
                }
            }

            go(this, f)
        }

        fun <R> fold(init: R, f: (R, T) -> R): R {
            tailrec fun go(list: FunList<T>, init: R, f: (R, T) -> R): R = when (list) {
                is Cons -> go(list.tail, f(init, list.head), f)
                is Nil -> init
            }
            return go(this, init, f)
        }

        fun reverse(): FunList<T> = fold(Nil as FunList<T>) { acc, i ->
            println("acc($acc), i($i)")
            Cons(i, acc)
        }

        fun <R> foldRight(init: R, f: (R, T) -> R): R {
            return this.reverse().fold(init, f)
        }

        fun <R> map(f: (T) -> R): FunList<R> {
            return this.foldRight(Nil as FunList<R>) { firstNil, i ->
                Cons(f(i), firstNil)
            }
        }
    }

    fun intListOf(vararg numbers: Int): FunList<Int> {
        return if (numbers.isEmpty()) {
            Nil
        } else {
            Cons(
                numbers.first(), intListOf(
                    *numbers.drop(1)
                        .toTypedArray()
                        .toIntArray()
                )
            )
        }
    }

    sealed class MyClass<out T> {
        object Empty : MyClass<Nothing>()
        data class Sub01<out T>(val t: T) : MyClass<T>()
    }

    fun test(i: Int): MyClass<Int> {
        if (i < 0) {
            return Empty
        } else {
            return Sub01(1)
        }
    }

    fun <T> sayType(cls: MyClass<T>): String {
        when (cls) {
            is Empty -> return "empty"
            is Sub01 -> return "sub01"
        }
    }

    interface Comparable<in T> {
        operator fun compareTo(other: T): Int
    }

    fun demo(x: Comparable<Number>) {
        x.compareTo(1.0)
        val y: Comparable<Double> = x
    }
}