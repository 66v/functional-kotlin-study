/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudy

import java.lang.StringBuilder

fun sum_0(a: Int, b: Int): Int {
    return a + b
}

fun sum_1(a: Int, b: Int): Int = a + b
fun sum_2(a: Int, b: Int) = a + b

fun mul_0(a: Int, b: Int): Int = when (a) {
    0 -> b - 1
    5 -> b
    else -> b + 1
}

// 단일 표현이 아니라면 리턴타입은 람다(함수)가 된다.
fun mul_1(a: Int, b: Int) = {
    val c = a + 1
    val d = b + 1
    c + d
}

// vararg.forEach 는 람다를 인자로 받고 각 요소에 대해 해당 함수를 실행한다.
fun aVarargFun(vararg names: String) {
    names.forEach(::println)
}
// Compile error. 다중 vararg는 금지.
//    fun aVarargFun(vararg names: String, vararg sizes: Int) {}

fun unless(condition: Boolean, block: () -> Unit) {
    if (!condition) block()
}

fun <T, R> transform(vararg ts: T, f: (T) -> R): List<R> = ts.map(f)

fun <T> emit(t: T, vararg listeners: (T) -> Unit) =
    listeners.forEach { listener ->
        listener(t)
    }

fun high(f: (age: Int, name: String) -> Unit) {
    // Compile error. 람다에서는 파라메터 이름을 지정하더라도 실제로 호출할 수는 없다.
//    f(age = 6, name = "앗싸라뵹")
    f(6, "앗싸라뵹")
}

data class Programmer(
    val lastName: String,
    val firstName: String,
    val favouriteLanguage: String = "C++",
    val yearsOfExperience: Int = 1
)

fun String.printMe() = println(this)
fun String.printMe(i: Int) = println("$this($i)")

class Human(private val name: String)
// Compile error. 확장함수는 private 멤버에 접근할 수 없음.
//fun Human.speak(): String = "${this.name}"

open class Canine {
    open fun speak() = "<일반적인 개과 소리>"
}

class Dog : Canine() {
    override fun speak() = "멍멍!!"
}

fun printSpeak(canine: Canine) {
    println(canine.speak())
}

open class Feline

fun Feline.speak() = "<일반적인 고양잇과 소리>"
class Cat : Feline()

fun Cat.speak() = "야옹!!"
fun printSpeak(feline: Feline) {
    println(feline.speak())
}

open class Primate(val name: String)

fun Primate.speak() = "$name: <일반적인 영장류 소리>"
open class GiantApe(name: String) : Primate(name)

fun GiantApe.speak() = "${this.name}: <뭇서운 100db 포효"
fun printSpeak(primate: Primate) {
    println(primate.speak())
}

class Dispatcher {
    val dispatcher: Dispatcher = this
    fun Int.extension() {
        val receiver: Int = this
        val dispatcher: Dispatcher = this@Dispatcher
    }
}

open class Caregiver(val name: String) {
    open fun Feline.react() = "크엉!!!"
    fun Primate.react() = "** $name (은)는 ${this@Caregiver.name} (와)과 함께 논다 **"
    fun takeCare(feline: Feline) {
        println("고양잇과 반응: ${feline.react()}")
    }

    fun takeCare(primate: Primate) {
        println("영장류 반응: ${primate.react()}")
    }
}

open class Vet(name: String) : Caregiver(name) {
    override fun Feline.react() = "** $name (으)로부터 도망친다 **"
}

class Worker {
    fun work() = "** 열심히 일한다 **"
    private fun rest() = "** 쉰다 **"
}

fun Worker.work() = "* 그까이꺼 대~충~ *"
fun <T> Worker.work(t: T) = "* $t 작업중 *"
fun Worker.rest() = "* 철권하며 휴식중~ *"

object Builder

fun Builder.buildBridge() = "삐까뻔쩍 피카츄~"
class Designer {
    companion object
    object Desk
}

fun Designer.Companion.fastProtytype() = "프로토타입"
fun Designer.Desk.portfolio() = listOf("프로젝트1", "프로젝트2")

infix fun Int.superOperation(i: Int) = this + i
infix fun String.shouldStartWith(s: String) = this.startsWith(s)
infix fun String.`(╯°□°）╯︵ ┻━┻`(s: String) = "** $this (이)가 $s 에게 밥상뒤집기를 시전중!"

object All {
    infix fun your(base: Pair<Base, Us>) {}
}

object Base {
    infix fun are(belong: Belong) = this
}

object Belong
object Us

enum class WolfActions {
    SLEEP, WALK, BITE
}

enum class WolfRelationships {
    FRIEND, SIBLING, ENEMY, PARTNER
}

class Wolf(val name: String) {
    operator fun plus(wolf: Wolf) = Pack(mapOf(name to this, wolf.name to wolf))
    operator fun invoke(action: WolfActions) = when (action) {
        WolfActions.SLEEP -> "$name(은)는 자는 중임 zzz"
        WolfActions.WALK -> "$name(은)는 걷는 중임. ㅌㅌㅌ"
        WolfActions.BITE -> "$name(은)는 깨무는 중임. 냠냠냠.."
    }
}

operator fun Wolf.set(relationship: WolfRelationships, wolf: Wolf) {
    println("${wolf.name} (은)는 내 새로운 $relationship (이)다")
}

operator fun Wolf.not() = "$name (이)가 뽷쳤다!"

class Pack(val members: Map<String, Wolf>)

operator fun Pack.plus(wolf: Wolf) = Pack(
    this.members + (wolf.name to wolf)
)

operator fun Pack.get(name: String) = members[name]!!

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

enum class Material {
    CARBON, STEEL, TITANIUM, ALUMINIUM
}

enum class BarType {
    DROP, FLAT, TT, BULLHORN
}

enum class Brake {
    RIM, DISK
}

@DslMarker
annotation class ElementMarker

@ElementMarker
abstract class Part(private val name: String) : Element {
    private val children = arrayListOf<Element>()
    protected val attributes = hashMapOf<String, String>()
    protected fun <T : Element> initElement(element: T, init: T.() -> Unit): T {
        element.init()
        children.add(element)
        return element
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent<$name${renderAttributes()}>\n")
        children.forEach { c -> c.render(builder, indent + "\t") }
        builder.append("$indent</$name>\n")
    }

    private fun renderAttributes(): String = buildString {
        attributes.forEach { attr, value -> append(" $attr=\"$value\"") }
    }

    override fun toString(): String = buildString {
        render(this, "")
    }
}

class Bicycle : Part("bicycle") {
    fun description(description: String) {
        attributes["description"] = description
    }

    fun bar(init: Bar.() -> Unit) = initElement(Bar(), init)
    fun frame(init: Frame.() -> Unit) = initElement(Frame(), init)
    fun fork(init: Fork.() -> Unit) = initElement(Fork(), init)
}

abstract class PartWithmaterial(name: String) : Part(name) {
    var material: Material
        get() = Material.valueOf(attributes["material"]!!)
        set(value) {
            attributes["material"] = value.name
        }
}

class Bar : PartWithmaterial("bar") {
    var barType: BarType
        get() = BarType.valueOf(attributes["type"]!!)
        set(value) {
            attributes["type"] = value.name
        }
}

class Frame : PartWithmaterial("frame") {
    fun backWheel(init: Wheel.() -> Unit) = initElement(Wheel(), init)
}

class Fork : PartWithmaterial("fork") {
    fun frontWheel(init: Wheel.() -> Unit) = initElement(Wheel(), init)
}

class Wheel : PartWithmaterial("wheel") {
    var brake: Brake
        get() = Brake.valueOf(attributes["brake"]!!)
        set(value) {
            attributes["brake"] = value.name
        }
}

fun bicycle(init: Bicycle.() -> Unit): Bicycle {
    val cycle = Bicycle()
    cycle.init()
    return cycle
}

fun <T> time(body: () -> T): Pair<T, Long> {
    val startTime = System.nanoTime()
    val v = body()
    val endTime = System.nanoTime()
    return v to endTime - startTime
}

inline fun <T> inNanoTime(body: () -> T): Pair<T, Long> {
    val startTime = System.nanoTime()
    val v = body()
    val endTime = System.nanoTime()
    return v to endTime - startTime
}

data class User(val name: String)
class UserService {
    val listeners = mutableListOf<(User) -> Unit>()
    val users = mutableListOf<User>()
    inline fun addListener(noinline listener: (User) -> Unit) {
        listeners += listener
    }

    inline fun transformName(crossinline transform: (name: String) -> String): List<User> {
        val buildUser = { name: String ->
            User(transform(name))
        }
        return users.map { user -> buildUser(user.name) }
    }
}

fun <T, S> unfold(s: S, f: (S) -> Pair<T, S>?): Sequence<T> {
    val result = f(s)
    return if (result != null) {
        sequenceOf(result.first) + unfold(result.second, f)
    } else {
        sequenceOf()
    }
}

fun <T> elements(element: T, numOfValues: Int): Sequence<T> {
    return unfold(1) { i ->
        if (numOfValues > i)
            element to i + 1
        else
            null
    }
}

fun myFactorial(size: Int): Sequence<Long> {
    var l = 1L
    return unfold(1) { i ->
        if (size + 1 > i) {
            l *= i
            l to i + 1
        } else {
            null
        }
    }
}

fun factorial(size: Int): Sequence<Long> {
    return sequenceOf(1L) + unfold(1L to 1) { accn ->
        if (size > accn.second) {
            val x = accn.first * accn.second
            x to (x to accn.second + 1)
        } else
            null
    }
}

fun myFib(size: Int): Sequence<Long> {
    return unfold( Triple(1L, 1L, 1) ) { i ->
        if (size + 1 > i.third) {
            i.first to Triple(i.second, i.first + i.second, i.third + 1)
        } else
            null
    }
}

fun fib(size: Int): Sequence<Long> {
    return sequenceOf(1L) + unfold(Triple(0L, 1L, 1)) { (cur, next, n) ->
        if (size > n) {
            val x = cur + next
            (x) to Triple(next, x, n + 1)
        } else
            null
    }
}