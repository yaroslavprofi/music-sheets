import io.ktor.http.content.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "BlockingMethodInNonBlockingContext")
class PdfParser {
    suspend fun parseFile(data: MultiPartData): File? {
        var file: File? = null

        data.forEachPart { part ->
            if (part is PartData.FileItem) {
                val ext = File(part.originalFileName).extension
                val tempFile = File("upload-${System.currentTimeMillis()}.$ext")

                part.streamProvider().use { its -> tempFile.outputStream().buffered().use { its.copyToSuspend(it) } }
                file = tempFile
            }

            part.dispose()
        }
        return file
    }

    private suspend fun InputStream.copyToSuspend(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        yieldSize: Int = 4 * 1024 * 1024,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Long {
        return withContext(dispatcher) {
            val buffer = ByteArray(bufferSize)
            var bytesCopied = 0L
            var bytesAfterYield = 0L
            while (true) {
                val bytes = read(buffer).takeIf { it >= 0 } ?: break
                out.write(buffer, 0, bytes)
                if (bytesAfterYield >= yieldSize) {
                    yield()
                    bytesAfterYield %= yieldSize
                }
                bytesCopied += bytes
                bytesAfterYield += bytes
            }
            return@withContext bytesCopied
        }
    }
}