package com.bhbac.kotlinstudysample

import arrow.core.andThen
import arrow.core.compose
import arrow.syntax.function.*
import kotlin.random.Random

fun main() {
    val p: (String) -> String = { body -> "<p>$body</p>" }
    val span: (String) -> String = { body -> "<span>$body</span>" }
    val div: (String) -> String = { body -> "<div>$body</div>" }
    val strong: (String) -> String = { body -> "<strong>$body</strong>" }
    val randomNames: (Unit) -> String = {
        if (Random.nextInt() % 2 == 0) {
            "foo"
        } else {
            "bar"
        }
    }
    val divStrong: (String) -> String = div compose strong
    val spanP: (String) -> String = p andThen span
    val randomStrong: (Unit) -> String = randomNames andThen strong andThen strong andThen spanP
    println(divStrong("헬로 컴포지션 월드~!"))
    println(spanP("헬로 컴포지션 월드~!"))
    println(randomStrong(Unit))
    println(randomStrong(Unit))
    println(randomStrong(Unit))

    var salesSystem: (Quote) -> Unit =
            ::calculatePrice andThen ::filterBills andThen ::splitter
    salesSystem(Quote(20.0, "Foo", "Shoes", 1))
    salesSystem(Quote(20.0, "Bar", "Shoes", 200))
    salesSystem(Quote(2000.0, "Foo", "Motorbike", 1))

    val newStrong: (String, String, String) -> String = { body, id, style ->
        "<strong id=\"$id\" style=\"$style\">$body</strong>"
    }

    val redStrong: (String, String) -> String = newStrong.partially3("font: red")
    val blueStrong: (String, String) -> String = newStrong(p3 = "font: blue")
    val customBodyStrong: (String, String) -> String = newStrong(p1 = "CUSTOM BODY TEXT")
    println(redStrong("Red Sonja", "movie1"))
    println(blueStrong("Deep Blue Sea", "movie2"))
    println(customBodyStrong("냐하하하~ : 커스텀이라네", "font: custom"))

    val splitter: (billAndOrder: Pair<Bill, PickingOrder>?) -> Unit =
            ::partialSplitter.partially2 { order -> println("테스트 $order") }(p2 = ::accounting)
    salesSystem = ::calculatePrice andThen ::filterBills andThen splitter
    salesSystem(Quote(20.0, "Foo", "Shoes", 1))
    salesSystem(Quote(20.0, "Bar", "Shoes", 200))
    salesSystem(Quote(2000.0, "Foo", "Motorbike", 1))
    // splitter 는 명시적 스타일 이후 파라메터가 2개로 줄어들었을때 암시적 스타일로 선언하여 이해하기 어렵지만
    // 아래와 같은 의미를 나타낸다.
    val sameSplitter: (billAndOrder: Pair<Bill, PickingOrder>?) -> Unit =
            ::partialSplitter.partially2{ println("sameSplitter $it") }.partially2(::accounting)
    salesSystem = ::calculatePrice andThen ::filterBills andThen sameSplitter
    salesSystem(Quote(20.0, "Foo", "Shoes", 1))
    salesSystem(Quote(20.0, "Bar", "Shoes", 200))
    salesSystem(Quote(2000.0, "Foo", "Motorbike", 1))

    val footer: (String) -> String = { content -> "<footer>$content</footer>"}
    // parameter가 하나인 경우는 암시적인 스타일을 사용할 수 없다.
    // 함수가 실행되어 버리기 때문.
    //fixFooter = footer(p1 = "Functional Kotlin - 2018")
    // 이를 위해 bind 가 존재한다.
    var fixFooter: () -> String = footer.bind("Functional Kotlin - 2018")
    println(fixFooter())
    // 물론 partially1 을 사용할 수도 있다.
    // 사실 bind는 partially1 의 alias 이기 때문이다.
    fixFooter = footer.partially1("Functional Kotlin - 2019")
    println(fixFooter())

    println(redStrong("Red Sonja", "movie1"))
    println(redStrong.reverse()("movie2", "The Hunt for Red October"))

    "From a pipe".pipe(strong).pipe(::println)
    splitter(filterBills(calculatePrice(Quote(20.0, "Foo", "Shoes", 1))))
    Quote(20.0, "Foo", "Shoes", 1).pipe(::calculatePrice).pipe(::filterBills).pipe(::splitter)
    val reverseRedStrong: (String, String) -> String = "color: red" pipe3 newStrong.reverse()
    reverseRedStrong("movie 3", "Three colors: Red") pipe ::println

    val curriedStrong: (style: String) -> (id: String) -> (body: String) -> String =
            newStrong.reverse().curried()
    val greenStrong: (id: String) -> (body: String) -> String =
            curriedStrong("color: green")
    val uncurriedGreenStrong: (id: String, body: String) -> String =
            greenStrong.uncurried()
    println(greenStrong("movie 5")("Green Inferno"))
    println(uncurriedGreenStrong("movie 6", "Green Hornet"))
    "Fried Green Tomatoes" pipe ("movie 7" pipe greenStrong) pipe ::println

}