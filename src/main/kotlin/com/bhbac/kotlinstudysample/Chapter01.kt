/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudysample



typealias Oven2 = com.bhbac.kotlinstudysample.Chapter01.Machine<Chapter01.Bakeable>

class Chapter01 {

    fun main() {
        val basic = Basic("let's start function")
        println(basic.print())

        val iceCupcake: BakeryGood = Cupcake("아이스")
        println(iceCupcake.eat())
        val blueberryCupcake = Cupcake("블루베리")
        println(blueberryCupcake.eat())
        val aceBiscuit = Biscuit("에이스")
        println(aceBiscuit.eat())
        println(aceBiscuit.eatBiscuit())
        val roll: BakeryGood = Roll("소프트")
        println(roll.eat())
        val cinnamonRoll: BakeryGood = CinnamonRoll()
        println(cinnamonRoll.eat())
        val donut = Donut("유황", "소금")
        println(donut.eat())

        val mario = Customer("뫄리오")
        mario.eats(donut)
        val luigi = Customer("루이지")
        luigi.eats(object : BakeryGood("test1") {
            override fun name(): String {
                return "test2"
            }
        })
        val food: BakeryGood = object : BakeryGood("test3") {
            override fun name(): String {
                return "test4"
            }
        }
        luigi.eats(food)

        val somethingFried = object : Fried {
            override fun fry(): String {
                return "test5"
            }
        }

        val expression = object {
            val property = "프로퍼티"
            fun method(): Int {
                println("오브젝트 시작")
                return 66
            }
        }
        val i = "${expression.method()}${expression.property}"
        println(i)

        ElectricOven.process(blueberryCupcake)

        val almondCupcake = Cupcake.almond()
        val cheeseCupcake = Cupcake.cheese()
        val cupcakeFactory = Cupcake.Factory
        val al = cupcakeFactory.almond()
        val cupFac2: Cupcake.Factory = Cupcake
        val ch = cupFac2.cheese()

        val nullableCupcake: Cupcake? = Cupcake.almond()
        val length: Int = nullableCupcake?.bake()?.length ?: 0

        val anyMachine = object : Machine<Any> {
            override fun process(product: Any) {
                println(product.toString())
            }
        }
        anyMachine.process(3)
        anyMachine.process("anyany~")
        anyMachine.process(almondCupcake)

        val x: Nothing? = null
        val nullsList: List<Nothing?> = listOf(null)
        println("end")

        val item = Item(al, 0.66, 3)
        val item2 = item.copy(product = donut, unitPrice = 1.2)
        val (compo1, _, compo3) = item2
        println("item : ${item2.toString()}")
        println("prod : $compo1")
        println("qty : $compo3")

        // 코틀린 온라인에서 실행시 exception 발생
        val annotations: List<Annotation> = ElectricOven3::class.annotations
        for (annotation in annotations) {
            when (annotation) {
                is Tasty -> println("맛남? ${annotation.tasty}")
                else -> println(annotation)
            }
        }
    }

    class Basic(val str: String) {
        init {
        }

        constructor(str: String, b: Int) : this(str) {
        }

        fun print(): String {
            return str
        }
    }

    abstract class BakeryGood(val flavour: String) {
        init {
            println("빵집 문 열었소~")
        }

        fun eat(): String {
            return "아이구 맛나, ${name()}"
        }

        abstract fun name(): String
    }

    interface Bakeable {
        fun bake(): String {
            return "핫!뜨거뜨거 핫!뜨거뜨거 핫핫~"
        }
    }

    @Tasty
    interface Fried {
        fun fry(): String
    }

    class Cupcake(flavour: String) : BakeryGood(flavour), Bakeable {
        fun eatCake(): String {
            return "냠냠냠 맛있는 $flavour 케이크~"
        }

        override fun name(): String {
            return eatCake()
        }

        companion object Factory {
            fun almond(): Cupcake {
                return Cupcake("아몬드")
            }

            fun cheese(): Cupcake {
                return Cupcake("치즈")
            }
        }
    }

    class Biscuit(flavour: String) : BakeryGood(flavour) {
        fun eatBiscuit(): String {
            return "바삭바삭 맛있는 ${flavour + "(바삭!)"} 비스킷!"
        }

        override fun name(): String {
            return eatBiscuit()
        }
    }

    open class Roll(flavour: String) : BakeryGood(flavour) {
        override fun name(): String {
            return "$flavour 롤"
        }
    }

    @Tasty
    class CinnamonRoll : Roll("시나몬")

    class Donut(flavour: String, val topping: String) : BakeryGood(flavour), Fried {
        override fun name(): String {
            return "$topping 으로 토핑된 $flavour 맛 도너츠!"
        }

        override fun fry(): String {
            return "지글지글 앗뜨거!"
        }
    }

    class Customer(val name: String) {
        fun eats(food: BakeryGood) {
            println("$name 아자씨가 ${food.eat()} 를 먹네요호~")
        }
    }

    interface Machine<T> {
        fun process(product: T)
    }

    interface Oven : com.bhbac.kotlinstudysample.Chapter01.Machine<Bakeable>
    object ElectricOven : Oven {
        override fun process(product: Bakeable) {
            println(product.bake())
        }
    }

    object ElectricOven2 : Oven2 {
        override fun process(product: Bakeable) {
            println(product.bake())
        }
    }

    @Tasty(false)
    object ElectricOven3 : Oven2 {
        override fun process(product: Bakeable) {
            println(product.bake())
        }
    }

    data class Item(
        val product: BakeryGood,
        val unitPrice: Double,
        val quantity: Int
    )

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Tasty(val tasty: Boolean = true)

    interface Exotic {
        fun isExotic(): Boolean
    }

    enum class Flour : Exotic {
        WHEAT {
            override fun isGlutenFree(): Boolean {
                return false
            }

            override fun isExotic(): Boolean {
                return false
            }
        },
        CORN {
            override fun isGlutenFree(): Boolean {
                return true
            }

            override fun isExotic(): Boolean {
                return false
            }
        },
        CASSAVA {
            override fun isGlutenFree(): Boolean {
                return true
            }

            override fun isExotic(): Boolean {
                return true
            }
        };

        abstract fun isGlutenFree(): Boolean
    }

    fun flourDescription(flour: Flour): String {
        return when (flour) {
            Flour.CASSAVA -> "매우 이국적인 맛"
            else -> "지루함"
        }
    }
}