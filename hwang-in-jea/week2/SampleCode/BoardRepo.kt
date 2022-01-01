package week2.sampledb

import java.time.LocalDateTime

object BoardRepo {
    var boardList = mutableListOf<Board>()

    fun add(board: Board) {
        boardList.add(board)
    }

    fun addComment(boardId: Int, comment: Comment) {
        // TODO : Refactoring

        CommentRepo.add(comment)

        if (boardList[boardId].commentList == null) {
            boardList[boardId].commentList = listOf(comment)
            return
        }

        var newCommentList = boardList[boardId].commentList?.toMutableList()
        newCommentList?.add(comment)
        boardList[boardId].commentList = newCommentList
    }

    fun update(boardId: Int, author: String, updateTitle: String) {
        // TODO : not yet implemented
    }

    fun delete(boardId: Int) {
        boardList.removeAt(boardId)
    }
}

data class Board(
    val title: String,
    val author: String,
    val createdAt: LocalDateTime,
    var commentList: List<Comment>?
)
