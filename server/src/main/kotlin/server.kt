import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.FileInputStream

fun Application.main() {
    val db = SheetsDB()

    routing {

        static("/") {
            resources("/")
            default("client/main.html")
//            default("build/resources/main/main.html")           //for Intellij Idea
        }

        get("/content") {
            call.respond(TextContent(db.getPosts(), Json))
        }

        get("/download") {
            val id = call.parameters["id"]!!
            val file = db.getFile(id)

            if (file == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.response.header("Content-Disposition", "attachment; filename=\"${file.name}\"")
                call.respondFile(file)
                file.delete()
            }
        }

        post("/upload") {
            val name = call.parameters["name"]!!
            val instrument = call.parameters["instrument"]!!
            val difficulty = call.parameters["difficulty"]!!
            val comment = call.parameters["comment"]!!
            val file = PdfParser().parseFile(call.receiveMultipart())

            if (file == null) {
                call.respond(HttpStatusCode.NotAcceptable)
            } else {
                val fileStream = FileInputStream(file)
                db.post(name, instrument, difficulty, comment, fileStream)
                fileStream.close()
                file.delete()

                call.respondRedirect("/", false)
            }
        }
    }
}