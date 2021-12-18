package todo;

import java.time.LocalDateTime;

public record Todo(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime dueDate
) {
    public static Todo newTodo(
            Long id,
            String title,
            String content,
            LocalDateTime dueDate
    ) {
        return new Todo(
                id,
                title,
                content,
                LocalDateTime.now(),
                dueDate
        );
    }

    @Override
    public String toString() {
        return """
                ------------------------------------
                * id : %s
                * title : %s
                * content : %s
                * createdAt : %s
                * dueDate : %s
                ------------------------------------
                """.formatted(this.id, this.title, this.content, this.createdAt, this.dueDate);
    }
}
