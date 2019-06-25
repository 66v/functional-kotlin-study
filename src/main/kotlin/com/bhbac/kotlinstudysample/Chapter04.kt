/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudysample


import java.util.regex.Pattern

class Chapter04 {


    fun main() {
        val (userId, userName) = getUser()
        println("유저ID : $userId, 유저명 : $userName")

        println("단어 갯수 : ${"공백을 기준으로 한 이 단어의 갯수".countWords()}")

        invokeSomeStuff { println("람다 실행") }

        val sum = { x: Int, y: Int -> x + y }
        println("Sum : ${sum(5, 10)}")
        println("Sum : ${sum(12, 46)}")

        val reverse: (Int) -> Int = {
            var n = it
            var sum = 0
            while (n > 0) {
                val digit = n % 10
                sum = sum * 10 + digit
                n = n / 10
            }
            sum
        }
        println("reverse 12345 : ${reverse(12345)}")
        println("reverse 67890 : ${reverse(67890)}")

        println("${performOperationOnEven(4, { it * 2 })}")
        println("${performOperationOnEven(5, { it * 2 })}")

        getAnotherFunction(4)("abcd")
        getAnotherFunction(5)("Fxx")
    }

    fun getUser(): Pair<Int, String> {
        return Pair(1, "아하")
    }

    fun String.countWords(): Int {
        return trim()
            .split(Pattern.compile("\\s+"))
            .size
    }

    fun invokeSomeStuff(doSomething: () -> Unit) {
        doSomething()
    }

    fun performOperationOnEven(number: Int, operation: (Int) -> Int): Int {
        if (number % 2 == 0) {
            return operation(number)
        } else {
            return number
        }
    }

    fun getAnotherFunction(n: Int): (String) -> Unit {
        return { println("n : $n, it : $it") }
    }
}