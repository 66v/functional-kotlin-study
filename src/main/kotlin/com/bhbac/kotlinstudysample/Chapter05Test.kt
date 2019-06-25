/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudy


import java.lang.Exception

import com.bhbac.kotlinstudy.Material.*
import com.bhbac.kotlinstudy.BarType.*
import com.bhbac.kotlinstudy.Brake.*


fun main() {
    println(">>>>> 5장 테스트 시작 <<<<<")

    println("sum_0 : ${sum_0(5, 9)}")
    println("sum_1 : ${sum_1(5, 9)}")
    println("sum_2 : ${sum_2(5, 9)}")

    println("mul_0 : ${mul_0(0, 9)}")
    println("mul_0 : ${mul_0(5, 9)}")
    println("mul_0 : ${mul_0(-2, 9)}")
    println("mul_1 : ${mul_1(5, 9)}")

    aVarargFun()
    aVarargFun("가나다", "라마바", "사아자")

    unless(false) {
        println("으아닛챠~!")
    }

    val strs = transform(1, 2, 3, 4) { it.toString() + "_tr" }
    println("transform(1, 2, 3, 4) = $strs")

    // Compile error. vararg 람다는 소괄호 밖에 선언하면 안된다.
//        emit(2) { println(it) }
    emit(2, ::println, { i -> println(i * 2) }, { println(it * it * it) })

    high { i, s ->
        println("$i : $s")
    }

    val programmer1 = Programmer("김", "땡땡")
    // 언더바는 data class로부터 변수를 초기화 할 때 사용할 수 있다.
    val (p2LN, _, _, p2Year) = programmer1

    "우하하! 확장함수닷!".printMe()
    "우하하! 확장함수닷!".printMe(9)
    printSpeak(Canine())
    printSpeak(Dog())
    printSpeak(Feline())
    printSpeak(Cat())
    printSpeak(Primate("코코"))
    printSpeak(GiantApe("콩"))

    val adam = Caregiver("애덤")
    val fulgencio = Cat()
    val koko = Primate("코코")
    adam.takeCare(fulgencio)
    adam.takeCare(koko)

    val brenda = Vet("브렌디~")
    listOf(adam, brenda).forEach { caregiver ->
        println("${caregiver.javaClass.simpleName} : [${caregiver.name}]")
        caregiver.takeCare(fulgencio)
        caregiver.takeCare(koko)
    }

    val worker = Worker()
    println(worker.work())
    println(worker.work("이름 짓기"))
    println(worker.rest())

    println(Builder.buildBridge())
    println(Designer.fastProtytype())
    Designer.Desk.portfolio().forEach(::println)

    println(1 superOperation 2)
    println(1.superOperation(2))
    println("Kotlin" shouldStartWith "Ko")
    println("따봉" `(╯°□°）╯︵ ┻━┻` "안따봉")

    val pair1: Pair<String, Int> = Pair("f", 1)
    val pair2 = "s" to 2
    println("$pair1")
    println("$pair2")
    All your (Base are Belong to Us)

    val talbot = Wolf("탤벗")
    val northPack: Pack = talbot + Wolf("빅 버사")
    println(northPack.members)

    val map = mapOf(pair1)
    println(map + pair2)
    val biggerPack = northPack + Wolf("배드 울프~!")
    println(biggerPack.members)
    println(northPack.members)

    println(talbot(WolfActions.SLEEP))
    println("${talbot.invoke(WolfActions.BITE)}")

    try {
        val crashCode = biggerPack["배드 울프"]
    } catch (e: Exception) {
        println("Exception : ${e.javaClass.simpleName}")
    }
    val badWolf = biggerPack["배드 울프~!"]
    talbot[WolfRelationships.ENEMY] = badWolf
    println(!talbot)

    val joinWithPipe = with(listOf("one", "two", "three")) {
        joinToString(separator = "|")
    }
    println(joinWithPipe)
    val html = buildString {
        append("<html>\n")
        append("\t<body>\n")
        append("\t\t<ul>\n")
        listOf(1, 2, 3).forEach { i ->
            append("\t\t\t<li>$i</li>\n")
        }
        append("\t\t</ul>\n")
        append("\t</body>\n")
        append("</html>")
    }
    println(html)

    val commuter = bicycle {
        description("Fast carbon commuter")
        bar {
            barType = FLAT
            material = ALUMINIUM
        }
        frame {
            material = CARBON
            backWheel {
                material = ALUMINIUM
                brake = DISK
            }
        }
        fork {
            material = CARBON
            frontWheel {
                material = ALUMINIUM
                brake = DISK
            }
        }
    }
    println(commuter)

    val (_, time) = time { Thread.sleep(200) }
    println("time = $time")
    val pairVal = time { Thread.sleep(100) }
    println("pairVal.first = ${pairVal.first}")
    println("pairVal.second = ${pairVal.second}")
    val (_, inTime) = inNanoTime { Thread.sleep(200) }
    println("inNanoTime = $inTime")

    val userService = UserService()
    userService.transformName(String::toLowerCase)

    val seq = unfold(6) { s ->
        if (s > 0)
            "아하하" to s - 1
        else
            null
    }
    println("unfold() : $seq")
    seq.forEach(::println)
    val strings = elements("코끼리", 3)
    strings.forEach (::println)

    println("myFactorial(5) -> ${myFactorial(5).forEach(::println)}")
    println("factorial(5) -> ${factorial(5).forEach(::println)}")
    println("myFib(1) -> ${myFib(1).forEach(::println)}")
    println("myFib(2) -> ${myFib(2).forEach(::println)}")
    println("myFib(6) -> ${myFib(6).forEach(::println)}")
    println("fib(6) -> ${fib(6).forEach(::println)}")

    println(">>>>> 5장 테스트 종료 <<<<<")
}