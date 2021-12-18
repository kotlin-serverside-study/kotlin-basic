package todo

/**
 * TodoAppMain.kt
 * */


import java.time.LocalDateTime
import kotlin.system.exitProcess

// private 키워드로 클래스 밖에 선언된 프로퍼티는
// TodoMain.kt 파일 안에서만 유효합니다.
private val introBanner = """
|=====================================
   초간단 To-Do List
|=====================================
"""

// 앞에 키워드가 없다면 아래 menuDisplay 변수는 public 합니다.
val menuDisplay = """
|=====================================
|******* 메뉴를 입력하세요 ********     
|=====================================
|[1] Todo List 조회
|[2] Todo 등록
|[3] Todo 수정
|[4] Todo 삭제
|[5] 종료
|====================================
"""

fun main(args: Array<String>) {

    var mutableTodoList = TestDataCreator.createTestTodoList()

    println(introBanner)
    while (true) {
        println(menuDisplay)
        var input: Int = readLine()?.run {
            this.toInt()
        } ?: -1

        when (input) {
            1 -> {
                // 조회
                mutableTodoList.let { todoList ->
                    if (todoList.size == 0)
                        println("Todo 리스트가 비었습니다.")
                    else
                        todoList.forEach {
                            println(it.toStringForTodo())
                        }
                }
            }
            2 -> {
                // 등록
                insertNewTodoProcess(mutableTodoList)
            }
            3 -> {
                // 수정
                updateTodoProcess(mutableTodoList)
            }
            4 -> {
                // 삭제
                deleteTodoProcess(mutableTodoList)
            }
            5 -> {
                println("프로그램을 종료합니다.")
                break
            }
            else -> {
                println("입력값 오류로 프로그램을 종료합니다.")
                exitProcess(-1)
            }
        }
    }
}

// 등록 처리
fun insertNewTodoProcess(todoList: MutableList<Todo>) {
    print("할일 제목을 입력하세요 : ")
    val inputTitle = readLine() ?: ""
    print("할일 내용을 입력하세요 : ")
    val inputContent = readLine() ?: ""
    print("종료 날짜를 입력해주세요(오늘 날짜 기준 숫자)")
    val inputDuration = readLine()?.run { this.toInt() } ?: 0

    todoList.add(
        Todo.newTodo(
            todoList.lastIndex.toLong().inc(),
            inputTitle,
            inputContent,
            LocalDateTime.now().plusDays(inputDuration.toLong())
        )
    )
}

fun updateTodoProcess(todoList: MutableList<Todo>) {
    print("수정할 Todo id를 입력하세요 : ")
    val updateId = readLine()?.run {
        this.toInt()
    } ?: throw IllegalArgumentException("입력된 값이 적절하지 않습니다.")
    val beforeUpdateTodo = todoList[updateId]

    println(beforeUpdateTodo.toStringForTodo())

    println(">---------수정 작업 시작---------------<")
    println("수정할 할일 제목을 입력하세요 : 현재 값 [${beforeUpdateTodo.title}]")
    val inputTitle = readLine() ?: ""
    println("수정할 할일 내용을 입력하세요 : 현재 값 [${beforeUpdateTodo.content}]")
    val inputContent = readLine() ?: ""
    println("수정할 종료 날짜를 입력해주세요(오늘 날짜 기준 숫자) ")
    val inputDuration = readLine()?.run { this.toInt() } ?: 0

    val updatedTodo = Todo.newTodo(updateId.toLong(), inputTitle, inputContent, LocalDateTime.now().plusDays(inputDuration.toLong()))

//    todoList.set(updateId, updatedTodo)
    todoList[updateId] = updatedTodo
    println(">---------수정 작업 종료---------------<")
}

fun deleteTodoProcess(todoList: MutableList<Todo>) {
    print("삭제할  Todo id를 입력하세요 : ")
    val deleteId = readLine()?.run {
        this.toInt()
    } ?: throw IllegalArgumentException("입력된 값이 적절하지 않습니다.")

    todoList.removeTodoItem(deleteId)
}

// 삭제에 대한 확장 함수
fun MutableList<Todo>.removeTodoItem(deleteId: Int) = run {
    this.removeAt(deleteId)
    // 중간에 Element가 빠지면 List 뒤에 있는 요소들의 id 번호를 앞으로 땡겨와야 합니다.
    this.forEachIndexed { index, todo ->
        if (todo.id != index.toLong()) {
            this[index] = todo.copy(id = index.toLong())
        }
    }
}

/**
 * TodoDataCreator.kt
 * */

internal class TestDataCreator {
    companion object{
        fun createTestTodoList(): MutableList<Todo> = mutableListOf(
            Todo.newTodo(
                0L,
                "Todo Item 1",
                "Study Java",
                LocalDateTime.now().plusDays(2)
            ),
            Todo.newTodo(
                1L,
                "Todo Item 2",
                "Study OS",
                LocalDateTime.now().plusDays(1)
            ),
            Todo.newTodo(
                2L,
                "Todo Item 3",
                "Study Kotlin",
                LocalDateTime.now().plusDays(2)
            ),
            Todo.newTodo(
                3L,
                "Todo Item 4",
                "Study Spring",
                LocalDateTime.now().plusDays(2)
            ),
        )
    }
}

/**
 * Todo.kt
 * */


data class Todo(
    val id: Long, // 기호에 따라서 kotlin list 컬렉션에서 제공하는 index 관련 함수를 사용할 수 도 있습니다.
    val title: String,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val dueDate: LocalDateTime
) {
    companion object {
        fun newTodo(
            id: Long,
            title: String,
            content: String,
            dueDate: LocalDateTime?
        ): Todo = Todo(
            id = id,
            title = title,
            content = content,
            dueDate = dueDate ?: LocalDateTime.now().plusDays(1L)
        )
    }
}

fun Todo.toStringForTodo(): String = """
    ------------------------------------
    * id : ${this.id}
    * title : ${this.title}
    * content : ${this.content}
    * createdAt : ${this.createdAt}
    * dueDate : ${this.dueDate}
    ------------------------------------
""".trimIndent()

