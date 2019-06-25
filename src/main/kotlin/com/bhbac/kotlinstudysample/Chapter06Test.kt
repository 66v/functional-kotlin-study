/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudy


import java.text.SimpleDateFormat


fun main() {
    println(">>>>> 6장 테스트 시작 <<<<<")

    // 예외 발생
//    println(notNullStr)
//    println(notNullStr2)
    notNullStr = "초기 값"
    notNullStr2 = "초기 값2"
    println(notNullStr)
    println(notNullStr2)

    println("myLazyVal 사용 전")
    println(myLazyVal)

    myStr = "값 변경"
    myStr = "Value changed!"
    myStr = "값 또 변경!"

    myIntEven = 6
    myIntEven = 3
    println("myIntEven : $myIntEven")

    myCounter = 2
    myCounter = 5
    myCounter = 4
    myCounter++
    myCounter--
    println("myCounter : $myCounter")

    val map1 = mapOf(
        Pair("name", "Reactive Programming in Kotlin"),
        Pair("authors", "Rivu Chakraborty"),
        Pair("pageCount", 400),
        Pair("publicationData", SimpleDateFormat("yyyy/mm/dd")
            .parse("2017/12/05")),
        Pair("publisher", "Packt")
    )
    val map2 = mapOf(
        "name" to "Kotlin Blueprints",
        "authors" to "Ashish Belagali, Hardik Trivedi, Akshay Chordiya",
//        "pageCount" to 250,
        "publicationData" to SimpleDateFormat("yyyy/mm/dd")
            .parse("2017/12/05"),
        "publisher" to "Packt"
    )
    val book1 = Book(map1)
    val book2 = Book(map2)
    println("Book1 $book1\nBook2 $book2")
    println("${book2.authors}")
    // 없는 속성에 접근시 예외 발생
//    println("${book2.pageCount}")

    myEven = 6
    myEven = 3
    myEven = 5
    myEven = 8
    println("myEven : $myEven")

    val person = PersonImpl("마이콜", 37)
    val user = User2(person)
    person.printName()
    person.printAge()
    user.printName()
    user.printAge()

    println(">>>>> 6장 테스트 종료 <<<<<")
}