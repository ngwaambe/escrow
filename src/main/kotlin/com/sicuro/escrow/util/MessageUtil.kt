package com.sicuro.escrow.util

import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.util.*
import kotlin.collections.HashMap

data class MessageBundleKey(
    val locale: Locale,
    val resource: String
)

open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

class MessageUtil private constructor(messageBundleKey: MessageBundleKey) {

    private var bundle: ResourceBundle? = null

    private val bundleStore: MutableMap<MessageBundleKey, ResourceBundle> = HashMap<MessageBundleKey, ResourceBundle>()

    private var locale = Locale.ENGLISH

    private val logger = LoggerFactory.getLogger(MessageUtil::class.java)

    init {
        bundle = bundleStore.get(messageBundleKey)
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(messageBundleKey.resource, messageBundleKey.locale)
            bundleStore.put(messageBundleKey, bundle!!)
        }
    }

    companion object : SingletonHolder<MessageUtil, MessageBundleKey>(::MessageUtil)

    fun getMessage(key: String): String? {
        return try {
            bundle!!.getString(key)
        } catch (ex: MissingResourceException) {
            logger.error(" message : {} not available for language: {}", key, locale)
            throw ex
        }
    }

    /**
     * Creates a temporary bundle for given locale. Please use this method only when necessary. Its quite expensive.
     *
     * @param key
     * @param locale
     * @return
     */
    fun getMessage(key: String?, locale: Locale?): String? {
        val tempBundle = ResourceBundle.getBundle("com.sicuro.i18n.messages", locale)
        return tempBundle.getString(key)
    }

    fun getMessage(key: String, arg1: Any): String? {
        return getMessage(key, arrayOf(arg1))
    }

    fun getMessage(key: String, arg1: Any, arg2: Any): String? {
        return getMessage(key, arrayOf(arg1, arg2))
    }

    private fun getMessage(key: String, args: Array<Any>): String? {
        return if (args.isEmpty()) {
            getMessage(key)
        } else {
            val fmt = MessageFormat(getMessage(key))
            fmt.locale = locale
            fmt.format(args)
        }
    }
}
