package week2.sampledb

import java.time.LocalDateTime

object CommentRepo {
    var commentList = mutableListOf<Comment>()

    @Synchronized fun add(comment: Comment) {
        commentList.add(comment)
    }

    @Synchronized fun delete(idx: Int) {
        commentList.removeAt(idx)
    }
}

data class Comment(
    val boardId: Int,
    val content: String,
    val writer: String,
    val createdAt: LocalDateTime
)
