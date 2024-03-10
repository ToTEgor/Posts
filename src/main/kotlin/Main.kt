data class Post(
    val id: Int,
    val ownerId: Int,
    val fromId: Int,
    val date: Int,
    val text: String,
    val postType: String,
    val canPin: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean,
    val likes: Int
)


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
    val dda = Post(20, 1, 2, 3, "sa", "wq", true, true, true, 3)
    var b = WallService
    b.add(dda)
    b.add(Post(23, 4, 5, 2, "cvcg", "[poi", true, false, false, 45))

    println(b)

    b.update(Post(4, 2, 3, 5, "234", "wer", true, true, true, 12))

    println(b)
}
