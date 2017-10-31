import app.user.UserController
import app.user.UserRepository
import config.api.DataParser
import db.Repository
import spark.Route
import spark.Spark

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        val userRepository by lazy { UserRepository(Repository()) }
        val userController by lazy { UserController(userRepository) }

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
