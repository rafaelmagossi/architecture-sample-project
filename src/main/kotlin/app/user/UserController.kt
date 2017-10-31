package app.user

import config.api.Model
import config.bodyAsObject
import config.render
import spark.Request
import spark.Response
import java.util.Date

class UserController(val userRepository: UserRepository) {

    fun post(request: Request, response: Response): Model {
        // Transformando o JSON recebido na requisição em um objeto User.
        val user = request.bodyAsObject(User::class)

        // Adicionando mias informações ao usuário.
        user.logged = true
        user.lastSignInAt = Date()
        user.createdAt = Date()

        // Salvando usuário no banco de dados.
        userRepository.save(user)

        // Respondendo ao computador/pessoa que realizou a requisição ao servidor.
        return response.render(user, 201)
    }

    fun put(request: Request, response: Response): Model {
        // Transformando o JSON recebido na requisição em um objeto User.
        val user = request.bodyAsObject(User::class)

        if (user.id.isBlank()) {
            // Respondendo ao computador/pessoa que realizou a requisição ao servidor.
            return response.render("Usuário invalido", 422)
        } else {
            // Informando a data de atualização do usuário.
            user.updatedAt = Date()

            //Atualizando o usuário no banco de dados.
            userRepository.update(user)

            // Respondendo ao computador/pessoa que realizou a requisição ao servidor.
            return response.render("Usuário atualizado", 200)
        }
    }
}