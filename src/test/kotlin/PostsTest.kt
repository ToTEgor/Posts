import jdk.internal.vm.vector.VectorSupport.test
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class WallServiceTest {

    @Test
    fun testAddPost() {
        val testId = 0
        val testPost = Post(
            id = 0,
            ownerId = 123,
            fromId = 456,
            date = 123456,
            text = "test",
            postType = "test",
            canPin = false,
            canDelete = true,
            canEdit = true,
            likes = 0

        )

        val addedPost = WallService.add(testPost)
        val newId = addedPost.id

        Assert.assertNotEquals(testId, newId)
    }
}

class WallServiceTestUpdate {
    @Before
    fun beforeEach() {
        WallService.clear()
    }

    @Test
    fun UpdateReturnTrue() {

        val testPost = Post(
            id = 1,
            ownerId = 1,
            fromId = 1,
            date = 123456,
            text = "Initial post",
            postType = "regular",
            canPin = false,
            canDelete = true,
            canEdit = true,
            likes = 0
        )
        WallService.add(testPost)

        val updatedPost = Post(
            id = 1,
            ownerId = 1,
            fromId = 1,
            date = 123456,
            text = "test post",
            postType = "test",
            canPin = false,
            canDelete = true,
            canEdit = true,
            likes = 0
        )

        val result = WallService.update(updatedPost)

        Assert.assertTrue(result)
    }

    @Test
    fun UpdateReturnFalse() {

        val nonExistingPost = Post(
            id = 1000,
            ownerId = 1,
            fromId = 1,
            date = 123456,
            text = "test post",
            postType = "test",
            canPin = false,
            canDelete = true,
            canEdit = true,
            likes = 0
        )

        val result = WallService.update(nonExistingPost)

        Assert.assertFalse(result)
    }
    @Test
    fun testAddCommentSuccessful() {
        val post = Post(1, 1, 1, 123456, "Post 1", "post", false, false, true, 0)
        WallService.add(post)
        val comment = Comment(1, "Test comment")
        WallService.createComment(1, comment)
    }

    @Test (expected = PostNotFoundException::class)
    fun shouldThrowCom() {
        val post = Post(1, 1, 1, 123456, "Post 1", "post", false, false, true, 0)
        WallService.add(post)
        val comment = Comment(1, "Test comment")
        WallService.createComment(3,comment)
    }
}