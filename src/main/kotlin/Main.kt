data class Post(
    val id: Int?,
    val ownerId: Int?,
    val fromId: Int,
    val date: Int,
    val text: String?,
    val postType: String,
    val canPin: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean,
    val likes: Int,
    val attachment: Array<Attachment> = arrayOf()
)

data class Comment(
    val id: Int,
    val text: String
)

data class Notes(
    var title: String,
    var text: String,
    val note_id: Int,
    var message: String,
    var comment_id: Int,
    val comments: MutableList<Comment> = mutableListOf()
)

class PostNotFoundException(s: String) : Exception()

abstract class Attachment(val type: String)

data class Audio(val id: Int, val title: String, val description: String, val date: Int)

data class AudioAttachment(val audio: Audio) : Attachment("audio")

data class Video(val id: Int, val title: String, val description: String, val date: Int)

data class VideoAttachment(val video: Video) : Attachment("video")

data class Photo(val id: Int, val title: String, val description: String, val date: Int)

data class PhotoAttachment(val photo: Photo) : Attachment("photo")

data class File(val id: Int, val title: String, val description: String, val date: Int)

data class FileAttachment(val file: File) : Attachment("file")

data class Order(val id: Int, val title: String, val description: String, val date: Int)

data class OrderAttachment(val order: Order) : Attachment("order")

object WallService {
    var posts = arrayOf<Post>()
    var comments = arrayOf<Comment>()
    var countId = 0

    fun clear() {
        posts = arrayOf()
        comments = arrayOf()
        countId = 0
    }

    fun add(post: Post): Post {
        countId++
        posts += post.copy(id = countId)
        return posts.last()
    }

    fun createComment(postId: Int, comment: Comment): Comment {
        for (post in posts) {
            if (post.id == postId) {
                comments += comment
                return comments.last()
            }
        }
        throw PostNotFoundException("Пост с номером ID $postId не найден")
    }

    fun update(newPost: Post): Boolean {
        for ((index, value) in posts.withIndex()) {
            if (value.id == newPost.id) {
                posts[index] = newPost
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return posts.contentToString()
    }
}

object NoteService {
    var notes = mutableListOf<Notes>()
    var comments = mutableListOf<Comment>()

    fun add(note: Notes): Int {
        notes += note
        return note.note_id
    }

    fun createComment(note_id: Int, comment: Comment): Int {
        for (note in notes) {
            if (note.note_id == note_id) {
                note.comments += comment
                comments += comment
                return comment.id
            }
        }
        throw PostNotFoundException("Заметка с номером ID $note_id не найден")
    }

    fun deleteNote(note_id: Int): Boolean {
        val noteIterator = notes.iterator()
        while (noteIterator.hasNext()) {
            val note = noteIterator.next()
            if (note.note_id == note_id) {
                noteIterator.remove()
                return true
            }
        }
        return false
    }

    fun deleteComment(comment_id: Int, comment: Comment): Boolean {
        for (note in notes) {
            if (note.comment_id == comment_id) {
                val index = comments.indexOf(comment)
                if (index != -1) {
                    comments.removeAt(index)
                    return true
                }
            }
        }
        return false
    }

    fun edit(note_id: Int, title: String, text: String): Boolean {
        for (note in notes) {
            if (note.note_id == note_id) {
                note.title = title
                note.text = text
                return true
            }
        }
        return false
    }

    fun editComment(comment_id: Int, message: String): Boolean {
        for (note in notes) {
            if (note.comment_id == comment_id) {
                note.message = message
                return true
            }
        }
        return false
    }

    fun get(note_id: Int): List<Notes> {
        val result = mutableListOf<Notes>()
        for (note in notes) {
            if (note.note_id == note_id) {
                result.add(note)
            }
        }
        return result
    }
}

data class Message(val text: String, var read: Boolean = false)
data class Chat(val messages: MutableList<Message> = mutableListOf())
class NoChatException : Throwable()
object ChatService {
    private val chats = mutableMapOf<Int, Chat>()

    fun addMessage(user_id: Int, message: Message) {
        chats.getOrPut(user_id) { Chat() }.messages += message
    }

    fun getUnreadChatsCount() =
        chats.values.count { chat: Chat -> chat.messages.any { message: Message -> !message.read } }

    fun getLastMessage() = chats.values.map { it.messages.lastOrNull()?.text ?: "no messages" }

    fun getMessage(user_id: Int, count: Int): List<Message> {
        val chat = chats[user_id] ?: throw NoChatException()
        return chat.messages.takeLast(count).onEach { it.read = true }
    }

    fun deleteChat(user_id: Int)  {chats.remove(user_id)}

    fun listChat() = println(chats)

    fun deleteMessage (user_id : Int)
        { chats[user_id]?.messages?.clear()}

}
    fun main() {
    val x = WallService
    x.add(
        Post(
            1,
            3,
            4,
            2,
            "ertw",
            "sdfs",
            true,
            true,
            true,
            11,
            attachment = arrayOf(
                AudioAttachment(Audio(1, "sdf", "doom", 22)),
                VideoAttachment(Video(2, "zoo", "boom", 13)),
                PhotoAttachment(Photo(22, "sdfw", "zxczx", 4)),
                FileAttachment(File(9, "sbvb", " cvbc", 3)),
                OrderAttachment(Order(5, "bnm", "zcvxn", 23))
            )
        )
    )
    println(x)
    x.update(Post(1, 3, 4, 5, "sdfs", "sdffw", true, false, false, 13))


}

