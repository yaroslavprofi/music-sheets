import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.main() {

    val db = SheetsDB()

    routing {

        static("/") {
            resources("/")
            default("build/resources/main/main.html")
        }

        get("/content") {
            call.respond(TextContent(db.getPosts(), io.ktor.http.ContentType.Application.Json))
        }

        post("/upload") {
            val name = call.parameters["name"]!!
            val instrument = call.parameters["instrument"]!!
            val difficulty = call.parameters["difficulty"]!!
            val comment = call.parameters["comment"]!!

            db.post(name, instrument, difficulty, comment)
            call.respondRedirect("/", false)
        }
    }
}
