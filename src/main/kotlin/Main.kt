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
abstract class Attachment (val type : String)

data class Audio (val id : Int, val title : String, val description : String)

data class AudioAttachment(val audio: Audio) : Attachment("audio")

class Video (val id : Int,val title : String, val description : String)

data class VideoAttachment(val video: Video) : Attachment("video")

object WallService {
    var posts = arrayOf<Post>()
    var countId = 0

    fun add(post: Post): Post {
        countId++
        posts += post.copy(id = countId)
        return posts.last()
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
x.add(Post(1,3,4,2,"ertw","sdfs",true,true,true,11, attachment = arrayOf(AudioAttachment(Audio(1,"Moon","doom")),VideoAttachment(Video(2,"zoo","boom")))))
println(x)
x.update(Post(1,3,4,5,"sdfs","sdffw",true,false,false,13))
}
