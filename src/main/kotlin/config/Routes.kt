package config

import app.controllers.UserController
import app.repositories.UserRepository
import config.api.DataParser
import db.Repository
import spark.Route
import spark.Spark
import spark.TemplateViewRoute
import spark.template.mustache.MustacheTemplateEngine

object Routes {

    @JvmStatic
    fun main(args: Array<String>) {

        val userRepository by lazy { UserRepository(Repository()) }
        val userController by lazy { UserController(userRepository) }
        val templateEngine by lazy { MustacheTemplateEngine("views") }

        // Função que renderiza a página HTML que lista os usuários.
        Spark.get("/users", TemplateViewRoute { request, response ->
            userController.get(request, response)
        }, templateEngine)

        // Função que registrar um novo usuário.
        Spark.post("/users", Route { request, response ->
            userController.post(request, response)
        }, DataParser())

        // Função de atualiza um usuário já existente.
        Spark.put("/users", Route { request, response ->
            userController.put(request, response)
        }, DataParser())
    }
}
