package week2.board

import reactor.core.publisher.Flux
import week2.sampledb.Board
import week2.sampledb.BoardRepo
import week2.sampledb.Comment
import week2.sampledb.CommentRepo
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.streams.toList

fun createSampleBoard() {

    println("===> createSampleBoard Start")
    var index = AtomicInteger()

    Stream.generate {
        BoardRepo.add(
            Board(
                "title : " + System.nanoTime(),
                "sample-${(index.incrementAndGet() + Random.nextInt(0,10))}",
                LocalDateTime.now(),
                null
            )
        )
    }.limit(100_001)
        .toList()
//        .takeWhile { index.get() > 1_000_000 }
    println("===> createSampleBoard End")
}

fun createSampleComment() {

    println("===> createSampleComment Start")

    Flux
        .range(0, 1_000_000)
        .parallel(10)
        .map {
            Comment(
                it % Random.nextInt(1, 10_000),
                "content : ",
                "sample-${(it + Random.nextInt(0,10))}",
                LocalDateTime.now()
            )
        }.doOnNext {
            BoardRepo.addComment(
                it.boardId,
                it
            )
        }.doOnComplete {
            println("Comment creation Finished - ${Thread.currentThread().id}")
        }
        .subscribe()

    println("===> createSampleComment END")
}

fun main() {
    val boardRepo = BoardRepo
    val commentRepo = CommentRepo

    println("===> 1")
    createSampleBoard()
    println("board List size : ${BoardRepo.boardList.size}")

    BoardRepo.addComment(0, Comment(1, "comment1", "writer1", LocalDateTime.now()))

    println("===> 2")
    BoardRepo
        .boardList
        .asSequence()
        .take(10)
        .forEach {
            println("board : $it")
        }

    println("===> 3")
    createSampleComment()

    println("===> 4")

    // TODO : 문제 1. 모든 게시글 별로 Comment 달린 개수를 찾기
    val commentCountList = BoardRepo.boardList.map {
        it.commentList?.run {
            this.size
        }
    }.toList()

    println("===> 5")
    // TODO : 문제 2. 모든 게시글에 달린 Comment 중에서 Comment 작성자와 본글 작성자가 같은 Board를 찾기.
    val tempBoardList = BoardRepo.boardList.map { board ->
        board.commentList?.run {
            this.count { it.writer == board.author }
        } ?: 0
    }

    println("===> 6")
    val total = tempBoardList.reduce { acc, i -> acc.plus(i)  }
    println("total : $total")
}
