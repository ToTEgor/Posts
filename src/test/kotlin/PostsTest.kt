import ChatService.deleteChat
import ChatService.deleteMessage
import ChatService.getLastMessage
import ChatService.getMessage
import ChatService.getUnreadChatsCount
import ChatService.listChat
import jdk.internal.vm.vector.VectorSupport.test
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.testng.Reporter.getOutput


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

    @Test(expected = PostNotFoundException::class)
    fun shouldThrowCom() {
        val post = Post(1, 1, 1, 123456, "Post 1", "post", false, false, true, 0)
        WallService.add(post)
        val comment = Comment(1, "Test comment")
        WallService.createComment(3, comment)
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
        val note2 = Notes("tit", "text2", 2, "mess", 2, comments)

        noteManager.add(note)
        noteManager.add(note2)

        val notesWithId1 = noteManager.get(1)
        assertEquals(2, notesWithId1.size)
        assertEquals(note, notesWithId1[0])
        assertEquals(note2, notesWithId1[1])
    }
}

class ChatServiceTest {
    lateinit var chatService: ChatService
    val chats = mutableMapOf<Int, Chat>()
    val message = mutableListOf<Message>()
    val chat = Chat(message)
    val message1 = Message("Hi")

    @Test
    fun testAddMessage() {
        chatService.addMessage(1, message1)
        assertEquals(1, chat.messages.size)
    }

    @Test
    fun testGetUnreadChatsCount() {
        chats[1] = Chat(mutableListOf(Message("Hello", true), Message("Unread message", false)))
        chats[2] = Chat(mutableListOf(Message("Hi", false), Message("Another message", false)))
        chats[3] = Chat(mutableListOf(Message("Read message", true)))

        assertEquals(2, getUnreadChatsCount())
    }

    @Test
    fun testGetLastMessage() {
        chats[1] = Chat(mutableListOf(Message("Hello")))
        chats[2] = Chat(mutableListOf(Message("Hi"), Message("Last message")))

        val expected = listOf("Hello", "Last message")
        assertEquals(expected, getLastMessage())
    }

    @Test
    fun testGetMessage() {
        val userId = 2
        val messages = mutableListOf(
            Message("Message 1"),
            Message("Message 2"),
            Message("Message 3")
        )
        chats[userId] = Chat(messages)

        val result = getMessage(userId, 3)

        assertEquals(3, result.size)
        assertEquals("Message 1", result[0].text)
        assertEquals("Message 2", result[1].text)
        assertEquals("Message 3", result[2].text)
        assertEquals(true, result[0].read)
        assertEquals(true, result[1].read)
        assertEquals(true, result[2].read)
    }

    @Test
    fun testDeleteChat() {
        val userId1 = 1
        val userId2 = 2
        chats[userId1] = Chat(mutableListOf(Message("Hello")))
        chats[userId2] = Chat(mutableListOf(Message("Hi")))

        deleteChat(userId1)

        assertEquals(false, chats.containsKey(userId1))
        assertEquals(true, chats.containsKey(userId2))
    }

    @Test
    fun testDeleteMessage() {
        val user_id = 1
        deleteMessage(user_id)
        val MessagesCount = 0
        val actualMessagesCount = chats[user_id]?.messages?.size ?: 0
        assertEquals(MessagesCount, actualMessagesCount)
    }

}