package com.bhbac.kotlinstudysample

import arrow.core.andThen
import arrow.core.compose
import arrow.syntax.function.invoke
import arrow.syntax.function.partially2
import arrow.syntax.function.partially3
import com.sun.org.apache.xpath.internal.operations.Quo
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

    val salesSystem: (Quote) -> Unit =
            ::calculatePrice andThen ::filterBills andThen ::splitter
    salesSystem(Quote(20.0, "Foo", "Shoes", 1))
    salesSystem(Quote(20.0, "Bar", "Shoes", 200))
    salesSystem(Quote(2000.0, "Foo", "Motorbike", 1))

    val newStrong: (String, String, String) -> String = { body, id, style ->
        "<strong id=\"$id\" style=\"$style\">$body</strong>"
    }
    val redStrong: (String, String) -> String = newStrong.partially3("font: red")
    val blueStrong: (String, String) -> String = newStrong(p3 = "font: blue")
    println(redStrong("Red Sonja", "movie1"))
    println(blueStrong("Deep Blue Sea", "movie2"))

    val splitter: (billAndOrder: Pair<Bill, PickingOrder>?) -> Unit =
            ::partialSplitter.partially2 { order -> println("테스트 $order") }(p2 = ::accounting)
    val salesSystem2: (quote: Quote) -> Unit = ::calculatePrice andThen ::filterBills andThen splitter
}