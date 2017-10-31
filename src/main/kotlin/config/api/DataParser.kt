package config.api

import com.fasterxml.jackson.databind.ObjectMapper
import spark.ResponseTransformer
import java.text.SimpleDateFormat
import kotlin.reflect.KClass

class DataParser : ResponseTransformer {

    val mapper by lazy { ObjectMapper() }

    override fun render(model: Any): String {
        return if (model is Model) toJson(model.body) else "{}"
    }

    fun toJson(model: Any): String {
        try {
            mapper.dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            return mapper.writeValueAsString(model)
        } catch (e: Exception) {
            return "{}"
        }
    }

    fun <T : Any> toObject(json: String, kClass: KClass<out T>): T {
        try {
            mapper.dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            return mapper.readValue(json, kClass.java)
        } catch (e: Exception) {
            return kClass.java.newInstance()
        }
    }
}
