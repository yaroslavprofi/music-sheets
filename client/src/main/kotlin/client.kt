import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.xhr.XMLHttpRequest
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.w3c.dom.*
import org.w3c.files.get
import org.w3c.xhr.FormData

@Serializable
data class ParsePost(
    val id: Int,
    val name: String,
    val instrument: String,
    val difficulty: String,
    val comment: String
)

fun loadContent() {

    val http = XMLHttpRequest()

    http.open("GET", "/content")

    http.onload = {
        if (http.status in 200..399) {
            val parsedJson = Json.decodeFromString<List<ParsePost>>(http.responseText)

            for (post in parsedJson) {
                document.getElementById("main")!!.prepend {
                    SheetPost(
                        id = post.id.toString(),
                        name = post.name,
                        instrument = post.instrument,
                        difficulty = post.difficulty,
                        comment = post.comment
                    ).run { render() }
                }
            }
        }
    }

    http.send()
}

fun buttonUploadAddEventListener(id: String) {
    (document.getElementById(id) as HTMLButtonElement).addEventListener("click", {
        val inputName = (document.getElementById("input-name") as HTMLInputElement).value
        val selectInstrument = (document.getElementById("select-instrument") as HTMLSelectElement).value
        val selectDifficulty = (document.getElementById("select-difficulty") as HTMLSelectElement).value
        val inputComment = (document.getElementById("input-comment") as HTMLInputElement).value
        val file = (document.getElementById("input-file") as HTMLInputElement).files?.get(0)

        if (inputName.isEmpty()
            || selectInstrument.isEmpty()
            || selectDifficulty.isEmpty()
            || file == null
        ) {
            window.alert("Incorrect input 'Sheet name' or 'Instrument'/'Difficulty' not selected or File not chosen")
            return@addEventListener
        }

        val data = FormData()
        data.append(file.name, file)

        val http = XMLHttpRequest()
        http.open(
            "POST", "/upload" +
                    "?name=${inputName}" +
                    "&instrument=${selectInstrument}" +
                    "&difficulty=${selectDifficulty}" +
                    "&comment=${inputComment}",
            true
        )
        http.onload = {
            if (http.status in 200..399) {
                window.location.reload()
            }
        }
        http.send(data)
    }
    )
}

fun main() {
    window.onload = {
        loadContent()
        buttonUploadAddEventListener("button-upload")
    }
}
