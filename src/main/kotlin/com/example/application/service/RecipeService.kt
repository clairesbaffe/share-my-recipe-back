import com.example.application.port.input.BookUseCasePort
import com.example.application.port.output.BookLoaderPort
import com.example.domain.model.Book
import org.koin.core.annotation.Single

@Single
class RecipeService(
    private val bookLoaderPort: BookLoaderPort
) : BookUseCasePort {
    override suspend fun findBookById(bookId: Long): Book? {
        return bookLoaderPort.loadBook(bookId)
    }

    override suspend fun postBook(title: String, author: String): Book {
        val book = Book(
            id = 0,
            title = title,
            author = author,
        )
        return bookLoaderPort.saveBook(book)
    }

    override suspend fun findAllBooks(): List<Book> {
        return bookLoaderPort.findAllBooks()
    }
}