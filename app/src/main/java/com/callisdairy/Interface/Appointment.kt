package com.callisdairy.Interface

interface AppointmentListener {
    fun appointmentListenerSet(position: Int, subposition: Int)
}


interface GetTime{
    fun getTime(time: String, appointment_date: String)
}


interface UnBlockUser {
    fun unBlockUser(position: Int, id: String)
}

interface PaymentDoneListener {
    fun paymentDone()
    fun failedPayment()

    fun cancelPayment()
}


interface RenewSubscription {
    fun renewNow()
}


interface BuyPlan {
    fun buy(id: String)
}


interface AsPerGoPay {
    fun payNow()
}