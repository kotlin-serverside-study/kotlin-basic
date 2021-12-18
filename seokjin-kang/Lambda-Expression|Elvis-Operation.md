# 람다표현식 (Lambda expression)
* 람다는 익명함수로, 익명함수는 함수의 이름이 없는 함수를 말한다.
* 보통 한번 사용되고 재사용되지 않는 함수를 만들때 익명함수로 만든다. 
* 코틀린 람다식은 항상 중괄호로 감싼다.
* 인자는 ()로 감싸지 않는다.
* 인자와 본문은 ->로 구분한다.
* (파라미터 -> 자료형)
* (Unit : 값이 없다.)

```
// 전체 표현
val test: (Int, Int) -> Int = {x:Int, y:Int -> x + y}

// 선언 자료형 생략
val test = {x:Int, y:Int -> x+y}

//람다 매개변수 자료형의 생략
val test: (Int, Int) -> Int = {x, y -> x+y}
```

```
// 람다를 활용하여 만든 고차함수
func sum(x:Int, y:Int, p: (Int, Int) -> Int) {
	println("$x, $y -> ${p(x, y)}")
}

sum(10, 5, {x:Int, y:Int -> x + y})

// 람다함수가 마지막 인자라면 밖으로 꺼낼 수 있다.
sum(10, 5) {x:Int, y:Int -> x + y}

// 고차함수에 타입이 정의되어 있는 경우 타입을 생략할 수 있다.
sum(10, 5) {x, y -> x + y}

// 람다함수가 아닌 일반함수를 인자로 넣을 수 있다.
// 이 경우에는 일반함수 앞에 ::을 넣어준다.
func sum(x:Int, y:Int) = x + y
sum(10, 5, ::sum)

// 함수형 변수를 인자타입에 넣어줄 수 있다.
val sum : (Int, Int) -> Int = {x:Int, y:Int -> x + y}
sum(10, 5, sum)

// 결과 : 10, 5 -> 15
```

* 반환값이 없는 경우에는 Unit을 사용한다.

```
var test = func() { println("Test Func") }
test("test")
// 결과 Test Func
```
* 위의 함수를 람다로 다시 작성을 한 경우
```
var test: () -> Unit = { println("Test Func") }
test()
// 결과 Test Func
```

+ run { print("test") } : run 함수는 인자로 받은 람다를 실행해주는 함수이다.




# 엘비스 연산자(Elvis Operation)

엘비스 연산자는 ?:로 표현하며, ?:의 왼쪽 객체가 non-null이면 그객체의 값이 리턴되고, null이라면 ?:의 오른쪽 값을 리턴합니다.

```
fun main(args: Array<String>) {
	val str: String ?= "1234"
	val nummStr: String ?= null

	var len: Int= if(str != null) str.length else -1
	println("str.length: $len")
	
	len = if(nullStr != null) nullStr.length else -1
	println("nullStr.length: $len")
}
```

* 메소드에서 null을 리턴하도록 만들 수 있다.
```
fun test(node: Node): String? {
	val parent = node.getParent() ?: return null
}
```

* Exception이 발생되도록 구현할 수 있다.
```
fun test(node: Node): String? {
	val name = node.getName() ?: throw Exception("Error")
}
```
