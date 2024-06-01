import jdk.internal.vm.vector.VectorSupport.test
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

class NoteServiceTest {
    lateinit var noteManager: NoteService
    private val notes = mutableListOf<Notes>()
    private val comments = mutableListOf<Comment>()
    val note = Notes("err", "Test Note", 1, "rty", 1)
    val comment = Comment(1, "text test")

    @Test
    fun testAddNote() {
        val returnedId = noteManager.add(note)
        assertEquals(1, returnedId)
        assertEquals(note, notes[0])
    }

    @Test
    fun testCreateComment() {
        notes.add(note)
        val create = noteManager.createComment(2, comment)
        assertEquals(1, create)
        assertEquals(1, note.comments.size)
    }

    @Test
    fun testDeleteNote() {
        notes.add(note)
        val isDeleted = noteManager.deleteNote(1)
        assertTrue(isDeleted)
        assertEquals(0, notes.size)
    }

    @Test
    fun testDeleteComment() {
        notes.add(note)
        noteManager.createComment(1, comment)
        val isDeleterComment = noteManager.deleteComment(1, comment)
        assertTrue(isDeleterComment)
        assertEquals(0, notes.size)
    }

    @Test
    fun testEditNote() {
        noteManager.add(note)
        val isEdited = noteManager.edit(1, "Edited Title", "This is the edited content")
        assertTrue(isEdited)
        assertEquals(1, notes.size)
        assertEquals("Edited Title", notes[0].title)
        assertEquals("This is the edited content", notes[0].text)
    }
    @Test
    fun testEditComment() {
        noteManager.createComment(1, comment)
        val isEdited = noteManager.editComment(
            comment_id = 1,
            message = "This is the edited comment content"
        )
        assertTrue(isEdited)
        assertEquals(1, comments.size)
    }
    @Test
    fun testGetNotesById() {
        val note2 = Notes("tit","text2",2,"mess",2,comments)

        noteManager.add(note)
        noteManager.add(note2)

        val notesWithId1 = noteManager.get(1)
        assertEquals(2, notesWithId1.size)
        assertEquals(note, notesWithId1[0])
        assertEquals(note2, notesWithId1[1])
    }
}