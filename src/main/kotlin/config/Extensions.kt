package config

import config.api.DataParser
import config.api.Model
import spark.ModelAndView
import spark.Request
import spark.Response
import kotlin.reflect.KClass

fun Response.render(body: Any, status: Int): Model {
    type("application/json")
    status(status)
    return Model(body, status)
}

fun Response.render(tag: String, model: Any, viewName: String) = ModelAndView(hashMapOf(Pair(tag, model)), viewName)

fun <T : Any> Request.bodyAsObject(kClass: KClass<T>) = body().toObject(kClass)

fun <T : Any> String.toObject(kClass: KClass<out T>) = DataParser().toObject(this, kClass)
