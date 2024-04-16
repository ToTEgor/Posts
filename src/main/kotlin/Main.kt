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
