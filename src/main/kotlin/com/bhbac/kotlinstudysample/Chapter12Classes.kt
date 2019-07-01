package com.bhbac.kotlinstudysample

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