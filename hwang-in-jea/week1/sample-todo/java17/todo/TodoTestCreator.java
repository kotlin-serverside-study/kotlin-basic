package todo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TodoTestCreator {
    public static List<Todo> createTestTodoList() {
        // List.of() 팩토리 메서드로 생성한 리스트는 unmodifiableList 이다.
        // 이렇게 생성한 list는 add() 가 허용되지 않음.
        // https://openjdk.java.net/jeps/269

        return Stream.of(
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
                )
        ).collect(Collectors.toList());
    }
}
