/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudysample

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class Chapter03 {
    class MyData {
        var someData: Int = 0
    }


    fun main() = runBlocking {
        val myData = MyData()

        async {
            var someDataCopy = myData.someData
            for(i in 11..20) {
                someDataCopy += i
                println("1번째 async로부터의 someDataCopy $someDataCopy")
                delay(500)
            }
        }

        async {
            var someDataCopy = myData.someData
            for(i in 1..10) {
                someDataCopy += i
                println("=== 2번째 async로부터의 someDataCopy $someDataCopy")
                delay(300)
            }
        }

        async {
            delay(500)
            for(i in 11..20) {
                myData.someData += i
                println("====== 3번째 async로부터의 someData ${myData.someData}")
                delay(500)
            }
        }

        async {
            delay(500)
            for(i in 1..10) {
                myData.someData += i
                println("========= 4번째 async로부터의 someData ${myData.someData}")
                delay(300)
            }
        }

        runBlocking {
            delay(10000)
        }
    }
}