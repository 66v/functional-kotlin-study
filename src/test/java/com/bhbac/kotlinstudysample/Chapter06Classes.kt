/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudy

import java.util.*
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

var notNullStr: String by Delegates.notNull()
lateinit var notNullStr2: String

val myLazyVal: String by lazy {
    println("myLazyVal 초기화")
    "초기 값 val"
}

var myStr: String by Delegates.observable("<init Value>") {
    property, oldValue, newValue ->
    println("속성 `${property.name}` (을)를 \"$oldValue\" 에서 \"$newValue\" (으)로 변경한다.")
}

var myIntEven: Int by Delegates.vetoable(0) {
    property, oldValue, newValue ->
    val judge = newValue % 2 == 0
    if (judge) {
        println("변환 가능. 속성 `${property.name}` (을)를 \"$oldValue\" 에서 \"$newValue\" (으)로 변경한다.")
    } else {
        println("변환 불가. 속성 `${property.name}` (을)를 \"$oldValue\" 에서 \"$newValue\" (으)로 변경할 수 없다.")
    }
    judge
}

var myCounter: Int by Delegates.vetoable(0) {
    property, oldValue, newValue ->
    val judge = newValue > oldValue
    if (judge) {
        println("(O) ${property.name} $oldValue -> $newValue")
    } else {
        println("(X) ${property.name} $oldValue -> $newValue")
    }
    judge
}

data class Book(val delegate: Map<String, Any?>) {
    val name: String by delegate
    val authors: String by delegate
    val pageCount: Int by delegate
    val publicationDate: Date by delegate
    val publisher: String by delegate
}

abstract class MakeEven(initVal: Int): ReadWriteProperty<Any?, Int> {
    private var value: Int = initVal
    override fun getValue(thisRef: Any?, property: KProperty<*>) = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, newValue: Int) {
        val oldValue = this.value
        val wasEven = newValue % 2 == 0
        if (wasEven) {
            this.value = newValue
        } else {
            this.value = newValue + 1
        }
        afterAssignmentCall(property, oldValue, newValue, wasEven)
    }
    abstract fun afterAssignmentCall(property: KProperty<*>, oldValue: Int,
                                     newValue: Int, wasEven: Boolean)
}

inline fun makeEven(initVal: Int,
                    crossinline onAssignment:(property: KProperty<*>, oldValue: Int, newValue: Int, wasEven: Boolean) -> Unit)
: ReadWriteProperty<Any?, Int> = object: MakeEven(initVal) {
    override fun afterAssignmentCall(property: KProperty<*>, oldValue: Int, newValue: Int, wasEven: Boolean) {
        onAssignment(property, oldValue, newValue, wasEven)
    }
}

var myEven: Int by makeEven(0) {
    property, oldValue, newValue, wasEven ->
    val judge = newValue % 2 == 0
    if (judge) {
        println("(O) ${property.name} $oldValue -> $newValue, Even: $wasEven")
    } else {
        println("(X) ${property.name} $oldValue -> $newValue + 1, Even: $wasEven")
    }
    judge
}

interface Person {
    fun printName()
    fun printAge()
}

class PersonImpl(val name: String, val age: Int): Person {
    override fun printName() {
        println(name)
    }

    override fun printAge() {
        println(age)
    }
}

class User2(val person: Person): Person by person {
    override fun printName() {
        println("이름 출력")
        person.printName()
    }
}