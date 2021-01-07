import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import org.w3c.xhr.XMLHttpRequest

enum class Instrument {
    Piano,
    Guitar,
    Woodwinds,
    Drums
}

enum class Difficulty {
    Easy,
    Medium,
    Hard
}

val posts = listOf(
    SheetPost(
        name = "Chopin wanltz in c# minor",
        instrument = Instrument.Piano,
        difficulty = Difficulty.Easy,
        comment = "OWOWOW kdjflsjdklf lsdkfjlksd  lkjdfscaption"
    ),
    SheetPost(
        name = "Chopin wanltz in c# minor",
        instrument = Instrument.Piano,
        difficulty = Difficulty.Easy,
        comment = "OWOWOW kdjflsjdklf lsdkfjlksd  lkjdfscaption"
    ),
    SheetPost(
        name = "Chopin wanltz in c# minor",
        instrument = Instrument.Piano,
        difficulty = Difficulty.Easy,
        comment = "OWOWOW kdjflsjdklf lsdkfjlksd  lkjdfscaption"
    ),
    SheetPost(
        name = "Chopin wanltz in c# minor",
        instrument = Instrument.Piano,
        difficulty = Difficulty.Easy,
        comment = "OWOWOW kdjflsjdklf lsdkfjlksd  lkjdfscaption"
    )
)

fun main() {
    window.onload = {
        document.head!!.append.apply {
            title {
                +"Music Sheets"
            }
            link {
                href = "https://fonts.googleapis.com/icon?family=Material+Icons"
                rel = "stylesheet"
            }
            link {
                href = "./styles_test.css"
                rel = "stylesheet"
            }
            link {
                href = "https://fonts.googleapis.com/css2?family=Lobster&display=swap"
                rel = "stylesheet"
            }
            link {
                href = "https://fonts.googleapis.com/css2?family=Bebas+Neue&display=swap"
                rel = "stylesheet"
            }
            link {
                href = "https://fonts.googleapis.com/css2?family=Pirata+One&display=swap"
                rel = "stylesheet"
            }
            link {
                href = "https://fonts.googleapis.com/css2?family=Source+Code+Pro&display=swap"
                rel = "stylesheet"
            }
        }

        document.getElementById("main")!!.append {
            posts.forEach { it.run { render() } }
        }

        (document.getElementById("button-upload") as HTMLButtonElement).addEventListener(
            "click", {

                val input_name = document.getElementById("name") as HTMLInputElement;
                val input_comment = document.getElementById("comment") as HTMLInputElement;

                val http = XMLHttpRequest()
                http.open("GET", "/upload?name=${input_name.value}")

                http.onload = {
                    if (http.status in 200..399) {
                        val value = http.responseText
                        window.alert("Uploaded file: $value")
                        document.getElementById("main")!!.prepend {
                            SheetPost(
                                name = value,
                                instrument = Instrument.Piano,
                                difficulty = Difficulty.Easy,
                                comment = "OWOWOW kdjflsjdklf lsdkfjlksd  lkjdfscaption"
                            ).run { render() }
                        }
                    }
                }
                http.send()
            }
        )
    }
}
