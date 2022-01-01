package todo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TodoMain {

    private static final String INTRO_BANNER = """
            |=====================================
               초간단 To-Do List
            |=====================================
            """;

    private static final String MENU_DISPLAY = """
            |=====================================
            |******* 메뉴를 입력하세요 ********    
            |=====================================
            |[1] Todo List 조회
            |[2] Todo 등록
            |[3] Todo 수정
            |[4] Todo 삭제
            |[5] 종료
            |====================================
            """;

    private static List<Todo> todoList = new ArrayList<>();

    public static void main(String[] args) {
        todoList = TodoTestCreator.createTestTodoList();
        var sc = new Scanner(System.in);

        System.out.println(INTRO_BANNER);
        while (true) {
            System.out.println(MENU_DISPLAY);
            var input = sc.nextLine();

            switch (Integer.parseInt(input)) {
                case 1 -> readTodoList();
                case 2 -> insertNewTodoProcess(sc);
                case 3 -> updateTodoProcess(sc);
                case 4 -> deleteTodoProcess(sc);
                case 5 -> {
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                }
                default -> {
                    System.out.println("입력값 오류로 프로그램을 종료합니다.");
                    System.exit(-1);
                }
            }
        }
    }

    public static void readTodoList() {
        if (todoList.size() == 0) {
            System.out.println("Todo 리스트가 비었습니다.");
            return;
        }

        todoList.forEach(System.out::println);
    }

    public static void insertNewTodoProcess(Scanner sc) {
        System.out.print("할일 제목을 입력하세요 : ");
        var inputTitle = sc.nextLine();
        System.out.print("할일 내용을 입력하세요 : ");
        var inputContent = sc.nextLine();
        System.out.print("종료 날짜를 입력해주세요(오늘 날짜 기준 숫자)");
        var inputDuration = Long.parseLong(sc.nextLine());

        todoList.add(
                Todo.newTodo(
                        (long) todoList.size(), // long 타입으로 캐스팅
                        inputTitle,
                        inputContent,
                        LocalDateTime.now().plusDays(inputDuration)
                )
        );
    }

    public static void updateTodoProcess(Scanner sc) {
        System.out.print("수정할 Todo id를 입력하세요 : ");
        var updateId = Integer.parseInt(sc.nextLine());
        var beforeUpdateTodo = todoList.get(updateId); // indexoutofbound Exception 예상됨.

        System.out.println(beforeUpdateTodo);

        System.out.println(">---------수정 작업 시작---------------<");
        System.out.printf("수정할 할일 제목을 입력하세요 : 현재 값 [%s]%n", beforeUpdateTodo.title());
        var inputTitle = sc.nextLine();
        System.out.printf("수정할 할일 내용을 입력하세요 : 현재 값 [%s]%n", beforeUpdateTodo.content());
        var inputContent = sc.nextLine();
        System.out.println("수정할 종료 날짜를 입력해주세요(오늘 날짜 기준 숫자) ");
        var inputDuration = Long.valueOf(sc.nextLine());

        var updatedTodo = Todo.newTodo((long) updateId, inputTitle, inputContent, LocalDateTime.now().plusDays(inputDuration));

        todoList.set(updateId, updatedTodo);
        System.out.println(">---------수정 작업 종료---------------<");
    }

    public static void deleteTodoProcess(Scanner sc) {
        System.out.print("삭제할 Todo id를 입력하세요 : ");
        var deleteId = Long.parseLong(sc.nextLine());

        todoList = todoList
                .stream()
                .filter(it -> it.id() != deleteId)
                .map(filtered -> {
                    if (filtered.id() > deleteId) {
                        return new Todo(
                                filtered.id() - 1,
                                filtered.title(),
                                filtered.content(),
                                filtered.createdAt(),
                                filtered.dueDate()
                        );
                    }
                    return filtered;
                }).collect(Collectors.toList());
    }

}
