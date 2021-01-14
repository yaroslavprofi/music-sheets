import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.sql.*

private val properties = java.util.Properties()

class SheetsDB {

    private val tableName: String
    private val password: String
    private val user: String
    private val url: String

    init {
        properties.load(FileInputStream("db.properties"))
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: SQLException) {
            println(e.message)
            e.stackTrace
        }
        tableName = properties.getProperty("tableName")
        user = properties.getProperty("user")
        password = properties.getProperty("password")
        url = "jdbc:mysql://" +
                properties.getProperty("dbPath") +
                ":${properties.getProperty("port")}" +
                "/${properties.getProperty("dbName")}"
    }

    private fun createConnection(): Connection {
        return DriverManager.getConnection(
            url,
            user,
            password
        )
    }

    fun post(
        name: String,
        instrument: String,
        difficulty: String,
        comment: String,
        fileStream: FileInputStream
    ) {
        val connection = createConnection()

        val statement = connection.prepareStatement(
            """
            INSERT INTO $tableName (name, instrument, difficulty, comment, file) Values(?, ?, ?, ?, ?)
            """.trimIndent()
        )
        statement.setString(1, name)
        statement.setString(2, instrument)
        statement.setString(3, difficulty)
        statement.setString(4, comment)
        statement.setBlob(5, fileStream)
        statement.executeUpdate()

        connection.close()
    }

    fun getPosts(): String {
        val connection = createConnection()

        val result = connection.createStatement().executeQuery(
            """
SELECT JSON_ARRAYAGG(
               JSON_MERGE(
                       json_object('id', id),
                       json_object('name', name),
                       json_object('instrument', instrument),
                       json_object('difficulty', difficulty),
                       json_object('comment', comment)
                   )
           )
FROM $tableName;
            """.trimIndent()
        )
        result.next()
        val value = result.getString(1) ?: ""

        connection.close()

        return value
    }

    fun getFile(id: String): File? {
        val connection = createConnection()

        val result = connection.createStatement().executeQuery(
            """
                SELECT name,file FROM $tableName WHERE id=$id;
            """.trimIndent()
        )
        if (result.next()) {
            val filename = "${result.getString(1)}.pdf"
            val blobStream = result.getBlob(2).binaryStream

            val fos =
                FileOutputStream(filename)
            var temp = blobStream.read()
            while (temp != -1) {
                fos.write(temp)
                temp = blobStream.read()
            }

            connection.close()

            blobStream.close()
            fos.close()

            return File(filename)
        }
        return null
    }
}