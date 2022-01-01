# 13. 코루틴과 구조적 동시성

코틀린에서 가장 인기 있는 기능 중 하나가 개발자가 동시성 코드를 마치 동기성(synchronous) 코드 처럼 작성할 수 있게 해주는 코루틴(coroutine)을 지원한다는 것이다.
* 코루틴의 지원으로 콜백 메소드 또는 리액티브 스트림과 같은 다른 방법들 보다 훨씬 더 쉽게 동시성 코드를 작성 할 수 있다.


### 13.1 코루틴 빌더 선택

새 코루틴을 생성하려면 빌더 함수 ```runBlocking```, ```launch```, ```async``` 중 하나를 사용할 수 있다.
* ```runBlocking``` 빌더 함수는 최상위 함수인 반면 ```launch```와 ```async```는 ```CoroutineScope```의 확장 함수 이다.
* 주의 할 것은 GlobalScope의 launch와 async를 사용해서는 안된다.
	* GlobalScope의 launch와 async는 시작 하는 코루틴이 특정 코루틴 잡에 할당되지 않고 영구적으로 취소되지 않으면 어플리케이션 전체 수명주기에 걸쳐 실행이 된다.
* 코루틴이 하나라도 실패하면 나머지 코루틴을 취소한다.

### runBlocking
* 현재 스레드를 블록하고 모든 내부 코류틴이 종료될 때까지 블록한다.
* 
```kotlin

fun main() {
	println("Before creating coroutine")
	runBlocking {
		print("hello, ")
		delay(200L)	// hello와 world 사이에 200밀리초 지연
		println("world!")
	}
	println("After coroutine is finished")
}
```
### launch
독립된 프로세스를 실행하는 코루틴을 시작하고, 해당 코루틴에서 리턴값을 받을 필요가 없다면 launch 코루틴 빌더를 사용한다.
* CoroutineScope이 사용 가능한 경우에만 사용 가능
* 코루틴 취소가 필요하면 사용할 수 잇는 Job 인스턴스를 리턴한다.
* CoroutineContext는 다른 코루틴과 상태를 공유하기 위해 사용한다.
* CoroutineStart 파라미터는 오직 DEFAULT, LAZY, ATOMIC 또는  UNDISPATCHED 값만이 될 수 있는 열거형 클래스이다.

```kotlin
fun main(){
	println("Before creating coroutine")
	runBlocking {
		println("Before launch")
		launch {
			print("hello, ")
			delay(200L)	// hello와 world 사이에 200밀리초 지연
			println("world!")
		}
		println("After launch")
	}
	println("After coroutine is finished")
}
```

### async
값을 리턴해야 하는 경우에는 일반적으로 async 빌더를 사용한다.
* async 빌더도 CoroutineScope의 확장함수 이며 launch빌더 처럼 async의 CoroutineContext와 CoroutineStart 파라미터도 적절한 기본값이 있다.
* 값을 리턴하면 async 함수가 지연된 **Deferred** 인스턴스로 해당 값을 감싼다. 

```kotlin
import kotlinx.coroutines.async  
import kotlinx.coroutines.coroutineScope  
import kotlinx.coroutines.delay  
import kotlin.random.Random  
  
suspend fun add(x: Int, y: Int): Int {  
    delay(Random.nextLong(1000L))  
    return x + y  
}  
  
suspend fun main() = coroutineScope {  
  val firstSum = async {  
  println(Thread.currentThread().name)  
        add(2, 2)  
    }  
  
  val secondSum = async {  
  println(Thread.currentThread().name)  
        add(3, 4)  
    }  
  println("Awaiting concurrent sums....")  
    val total = firstSum.await() + secondSum.await() // 코루틴이 종료될 때 까지 블록하기 위해 await 호출  
  println("Total is $total")  
}
```

### coroutineScope
* coroutineScope 함수는 종료 전에 포함된 모든 코루틴이 완료될 때까지 기다리는 일시 중단 함수이다.
* coroutineScope 함수는 runBlocking과는 다르게 메인 스레드를 블록하지 않는 장점이 있지만 반드시 일시 중단 함수의 일부로 호출되어야 한다.
* coroutineScope는 자동으로 모든 자식 코루틴이 완료될 때까지 기다린다.


## 13.2 async/await을 withContext로 변경하기

### 문제
async로 코루틴을 시작하고 바로 다음에 코루틴이 완료될 동안 기다리는 await 코드를 간소화 하고 싶다.

### 해법
async / await 조합을 withContext로 변경한다.

* CoroutineScope 클래스에는 withContext라는 확장 함수도 정의되어 있다.

```kotlin
suspend fun <T> withContext(
	context: CoroutineContext,
	block: suspend CoroutineScope.() -> T
): T
```
* 코틀린 공식 문서에서 withContext는 "주어진 코루틴 컨텍스트와 함께 명시한 일시정지 블록을 호출하고, 완료 될 때까지 일시 정지한 후 그 결과를 리턴한다" 라고 나와 있다.
	* https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/with-context.html

```kotlin
import kotlinx.coroutines.*  
  
// ktlint-disable no-wildcard-imports  
  
suspend fun retrieve1(url: String) = coroutineScope {  
  async(Dispatchers.IO) { // 코루틴을 어떤 스레드 풀에서 사용할 건지  
  println("Retrieving data on ${Thread.currentThread().name}")  
        delay(100L)  
        "asyncResults"  
  }.await()  
}  
  
suspend fun retrieve2(url: String) =  
    withContext(Dispatchers.IO) {  
  println("Retrieving data on ${Thread.currentThread().name}")  
        delay(100L)  
        "withContextResults"  
  }  
  
fun main() = runBlocking {  
  val result1 = retrieve1("www.mysite.com")  
    val result2 = retrieve2("www.mysite.com")  
    println("Retrieving data on ${Thread.currentThread().name} $result1")  
    println("Retrieving data on ${Thread.currentThread().name} $result2")  
}
```

* retrieve1과 retrieve2는 100밀리초 동안 지연한 다음 문자열을 리턴하는 같은 일을 한다.
* retrieve1과 retrieve2 함수 둘다 Dispatchers.IO 를 사용하기 때문에 두 함수 간에 차이점은 하나의 함수는 async/await을 사용하고 다른 하나는 async/await을 withContext로 대체한다는 것이다.

## 13.3 디스패처 사용하기

* 코루틴은 CoroutineContext 타입의 컨택스트 내에서 실행된다.
* 코루틴 컨텍스트에는 CoroutineDispatcher 클래스의 인스턴스에 해당되는 코루틴 디스패처가 포함되어 있다.
* 이 디스패처는 코루틴이 어떤 스레드 또는 스레드 풀에서 코루틴을 실행할 지 결정한다.
* 코틀린 표준 라이브러리에 다음과 같은 내장 디스패처가 있다.
	* Dispatcher.Default
		* 평범한 공유 백그라운드 스레드 풀을 사용
		* 코루틴이 대규모의 계산 리소스를 소모하는 경우 적합
	* Dispatchers.IO
		* 파일 I/O 또는 블록킹 네트워크 I/O 같은 I/O 집약적인 블로킹 작업을 제거하기 위해 디자인된 스레드 공유 풀을 사용한다.
	* Dispatchers.Unconfined
		* 이 디스패처는 일반적으로 애플리케이션 코드에서 사용하면 안된다.


## 13.4 자바 스레드 풀에서 코루틴 실행하기

* 코틀린 라이브러리는 java.util.concurrent.ExecutorService에 ```asCoroutineDispatcher```라는 확장 메서드를 추가하였다.

```kotlin
import kotlinx.coroutines.asCoroutineDispatcher  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.runBlocking  
import kotlinx.coroutines.withContext  
import java.util.concurrent.Executors  
  
fun main() = runBlocking {  
  val dispatcher = Executors.newFixedThreadPool(10)  
        .asCoroutineDispatcher()  
  
    withContext(dispatcher) {  
  delay(100L)  
        println(Thread.currentThread().name)  
    }  
  
  dispatcher.close() // 스레드풀 종료  
}
```
* ExecutorService는 close 함수를 호출하지 않으면 계속 실행되므로, main함수가 종료되지 않는한 close 함수 호출이 필요하다.


* user는 자바 closeable 인터페이스의 확장 함수로 정의되어 있다.
* 해당 함수를 사용하면 ExecutorService를 좀 더 우아하게 종료 시킬 수가 있다.

```kotlin
suspend fun useFunctionSample() = Executors.newFixedThreadPool(10).asCoroutineDispatcher().use {  
  withContext(it) {  
  delay(100L)  
        println(Thread.currentThread().name)  
    }  
}
```

## 13.5 코루틴 취소하기

###  해법
launch 빌더 또는 withTimeout이나 withTimeoutOrNull 같은 함수가 리턴하는 Job 레퍼런스를 사용한다.

```kotlin
fun main() = runBlocking {  
  val job = launch {  
  repeat(100) {  
  println("job : I'm waiting $it")  
            delay(100L)  
        }  
 }  delay(500L)  
    println("main : That's enough waiting")  
    job.cancel()  
    job.join()  
    println("main: Done")  
}
```
* 잡을 취소하려는 이유가 시간이 너무 오래걸려서라면 ```withTimeout``` 함수를 사용할 수도 있다.
	* 원한다면 TimeoutCancellationException을 캐치하거나 타임 아웃시에 예외를 던지는 대신 null을 리턴하는 withTimeoutOrNull을 사용할 수 있다.
```kotlin
fun main() = runBlocking {  
  withTimeout(1000L) {  
  repeat(50) {  
  println("job : I'm waiting $it")  
            delay(100L)  
        }  
 }}
```

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMjEwMDMyMzI4OSw4MDI1MTg4MTEsOTY4MT
YxMDk3LDE4NDM1MjQyNzUsNDM3NTE0OTc2LDE4MjkzMTYyMzAs
LTk4OTQ0NTU2MCwtMTUzMTYzMjg1NCwtOTg3NTQyNzA3LDEwNT
Q0MTA5MTQsLTExMDQzNTUzNjUsNzY4MTkwMjk0XX0=
-->