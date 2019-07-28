package com.bhbac.kotlinstudysample

import arrow.optics.Lens
import arrow.optics.optics

fun calculatePrice(quote: Quote) = Bill(quote.value * quote.quantity, quote.client) to PickingOrder(quote.item, quote.quantity)
fun filterBills(billAndOrder: Pair<Bill, PickingOrder>): Pair<Bill, PickingOrder>? {
    val (bill, _) = billAndOrder
    return if (bill.value >= 100) {
        billAndOrder
    } else {
        null
    }
}

fun warehouse(order: PickingOrder) {
    println("오더 처리중 = $order")
}

fun accounting(bill: Bill) {
    println("처리중 = $bill")
}

fun splitter(billAndOrder: Pair<Bill, PickingOrder>?) {
    if (billAndOrder != null) {
        warehouse(billAndOrder.second)
        accounting(billAndOrder.first)
    }
}

fun partialSplitter(billAndOrder: Pair<Bill, PickingOrder>?,
                    warehouse: (PickingOrder) -> Unit,
                    accounting: (Bill) -> Unit) {
    if (billAndOrder != null) {
        warehouse(billAndOrder.second)
        accounting(billAndOrder.first)
    }
}

typealias GB = Int

data class Memory(val size: GB)
data class MotherBoard(val brand: String, val memory: Memory)
data class Laptop(val price: Double, val motherBoard: MotherBoard)

val laptopPrice: Lens<Laptop, Double> = Lens(
        get = { laptop -> laptop.price },
        set = { laptop, price -> laptop.copy(price = price) }
)
val laptopMotherBoard: Lens<Laptop, MotherBoard> = Lens(
        get = { laptop -> laptop.motherBoard },
        set = { laptop, motherBoard -> laptop.copy(motherBoard = motherBoard) }
)
val motherBoardMemory: Lens<MotherBoard, Memory> = Lens(
        get = { motherBoard -> motherBoard.memory },
        set = { motherBoard, memory -> motherBoard.copy(memory = memory) }
)
val memorySize: Lens<Memory, GB> = Lens(
        get = { memory -> memory.size },
        set = { memory, size -> memory.copy(size = size) }
)

typealias GB2 = Int

@optics
data class Memory2(val size: GB2) {
    companion object
}

@optics
data class MotherBoard2(val brand: String, val memory: Memory2) {
    companion object
}

@optics
data class Laptop2(val price: Double, val motherBoard: MotherBoard2) {
    companion object
}