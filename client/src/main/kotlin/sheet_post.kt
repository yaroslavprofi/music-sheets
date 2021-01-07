import kotlinx.html.*
import org.w3c.dom.HTMLElement

class SheetPost(
    private val name: String,
    private val instrument: Instrument,
    private val difficulty: Difficulty,
    private val comment: String
) {

    fun TagConsumer<HTMLElement>.render() {
        article(classes = "sheet-example") {
            img { src = "test_img.jpg" }
            div(classes = "sheet-info") {
                span(classes = "name") { +name }
                span(classes = "instrument") { +instrument.toString() }
                span(classes = "difficulty") { +difficulty.toString() }
                span(classes = "comment") { +comment }
            }
        }
    }
}
