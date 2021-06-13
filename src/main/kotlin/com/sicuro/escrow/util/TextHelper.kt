package com.sicuro.escrow.util

import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.Title
import java.util.*

object TextHelper {


    fun getName(customer: Customer): String {
        return StringBuilder().append(getTitle(customer.title, customer.language))
            .append(" ")
            .append(customer.firstname)
            .append(" ")
            .append(customer.lastname)
            .toString()
    }

    fun getTitle(title: Title, locale:String) = messageUtil(locale, "com.sicuro.i18n.application").getMessage(title.elKey) ?: title.value

    private fun messageUtil (locale:String, resource:String) =  MessageUtil.getInstance(MessageBundleKey(Locale(locale), resource))
}
