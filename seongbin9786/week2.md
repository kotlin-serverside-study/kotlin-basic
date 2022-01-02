## Kotlin Study Week 2 (for me it's 1)



#### Why Is It Popular, Adoptable?

1. Google announced it as preferred language in 2019

2. JVM-compatible: enabling Java developers to gradually switch to Kotlin

   - **<u>Killer features</u>**

     > 출처: [[[YouTube]Kotlin in 100 seconds](https://www.youtube.com/watch?v=xT8oP0wy-A0&ab_channel=Fireship)]

     1. Co-routines: Async made easy

        > Android 개발 시 이전에 비해 훨씬 편해졌다고 한다

     2. Type Inference (like TypeScript): much convenient than without it.

     3. Null Safety

     4. Functions as first class citizen

     5. Data Class and Object destructuring



#### Multiplatform

[[kotlin docs](https://kotlinlang.org/docs/multiplatform.html)]

> Multiplatform projects are in [Alpha](https://kotlinlang.org/docs/components-stability.html). Language features and tooling may change in future Kotlin versions.





![Kotlin Multiplatform](https://kotlinlang.org/docs/images/kotlin-multiplatform.png)

![img](https://ucarecdn.com/28192c0c-2d02-4503-8948-034efaddb8a0/)



#### Kotlin for JavaScript

[[kotlin docs](https://kotlinlang.org/docs/js-overview.html#sample-projects-for-kotlin-js)]



#### Ktor

Jetbrains에서 직접 만드는 비동기 웹 프레임워크.



#### Coroutine

> JavaScript 코드와 비교하며 배우는 Kotlin Coroutine 사용법 배우기.

출처: [[blog](https://www.letmecompile.com/kotlin-coroutine-vs-javascript-async-comparison/)]

> JavaScript의 유명한 문제였던 비동기 코드 작성시 발생하는 콜백 지옥(callback hell)은 언어 문법 차원의 `async`/`await` 키워드 도입과 함께 대부분 해결되었다. Java에서 비동기 코드를 작성하려면 언어 문법차원의 지원이 없다보니 `Future` 등의 클래스를 통해서 가독성 떨어지는 복잡한 비동기 코드를 작성해야했지만, 이런 문제는 Kotlin coroutine을 도입하면 대부분 해결되리라 생각한다.





비동기 함수 정의 시 `suspend` 키워드를 prefix로 붙인다.

```kotlin
class ApiClient {
    suspend fun get(path: String): Response // suspend fun == async function
    // Promise<T> 같은 타입은 없는듯?
}

suspend fun getDataFromRemoteApi(): MyData { // suspend fun == async function
  val response = apiClient.get('resource') // 호출 시에 따로 await 키워드에 대응되는 키워드가 없다.
  // suspend fun 내에서 다른 suspend fun을 실행하는 경우 알아서 실행한다.
  return response.data
}
```



##### 1. 동시 실행 ( <-> 순차 실행 )

[before]

아래의 response 1, 2를 동시에 실행하고 싶다. 아래의 코드는 `await`과 동일하므로 순차 실행된다.

```kotlin
suspend fun getDataFromMultipleRemoteApi(): MyData {
  val response1 = apiClient.get('resource1')
  val response2 = apiClient.get('resource2')
  return response1.data + response2.data
}
```

[after]

JavaScript에서의 `Promise.all([...promises]);`와 같은 형태로 수행한다.

- 아래에서 나타나는 **couroutineScope**이 뭔지는 모르겠다.

  - 코틀린의 경우 객체로 반환값을 감싸는 듯하다.

    > 결국 JavaScript의 `Promise`는 Kotlin coroutine의 `Deferred` class 와 개념적으로 동일한 것이고 (Java 에서는 `Future` class 사용) , 해당 개념이 지원하는 근본적인 operation 들은 동일함을 알 수 있다.

    - 이 내용을 보니, 객체가 맞고, 이것의 정체는 `Deferred class`의 literal이다. (아마 async 키워드 뒤라서 타입 추론되는듯?)

```kotlin
suspend fun getDataFromMultipleRemoteApi(): MyData {
  // async {} 를 사용하려면 새로운 coroutineScope를 생성해서 감싸줘야한다.
  val deferedList = coroutineScope  {
    // async 로 감싸면 Deferred 객체가 바로 리턴되고 코드 실행을 계속 진행한다.
    val deferred1 = async { apiClient.get('resource1') }
    val deferred2 = async { apiClient.get('resource2') }
    listOf(deferred1, deferred2)
  }
  // JavaScript의 Promise.all(...) 과 동일한 역할 수행
  val deferred = deferedList.awaitAll()
  val response1 = deferred[0]
  val response2 = deferred[1]
  return response1.data + response2.data
}
```



##### 2. Callback 함수 구현

1. `suspend fun` 이 아니면 `launch {}` 블록 혹은 `runBlocking {}` 블록을 선언한 후 해당 블록 내에서 비동기 코드를 호출해야 한다.
   - 이 블록들은 코루틴을 생성한다고 한다.
2. `launch`는 non-blocking async를, `runBlocking`은 blocking async인 듯하다. 

```kotlin
// launch를 사용하는 방법
fun getDataFromRemoteApi(callback: (MyData) -> Unit) {
  // suspend 키워드가 붙지않은 함수내에서는 suspend fun인 apiClient.get을 호출할 수 없다.
  // 이 경우 suspend func을 실행할 수 있도록 코루틴을 생성하고 해당 스코프 안에서 suspend fun을 수행한다.
  launch {
      val response = apiClient.get('resource')
      callback(response.data)
  }
  // apiClient.get 수행이 완료되기 전에 이미 이 코드가 실행됨
  logger.info("When will this code executed?")
}

// runBlocking을 사용하는 방법
fun getDataFromRemoteApi(callback: (MyData) -> Unit) {
  runBlocking {
      val response = apiClient.get('resource')
      callback(response.data)
  }
  // apiClient.get 수행이 완료되고 runBlocking내부의 나머지 코드가 실행이 완전히 끝난 후에 이 코드가 실행됨
  logger.info("When will this code executed?")
}
```



##### 3. Callback 함수 대기

Javascript는 아래와 같이 `Promise`로 감싸 `async-await` 로 대기할 수 있다.

```js
// apiCallbackClient#get을 대기한다.
async function getDataFromRemoteApi() {
  const promise = new Promise((resolve, reject) => {
    apiCallbackClient.get('resource', (response) => {
      resolve(response)
    })
  })
  const response = await promise 
  return response.data
}
```

Kotlin에서는 아래와 같이 비동기 함수를 작성할 수 있다. (Javascript와 비슷하다.)

```kotlin
suspend fun getDataFromRemoteApi(): MyData {
    // 코루틴 스코프를 잠시 멈추고 결과를 기다리는 의미의 suspendCoroutine 함수를 사용
   val response = suspendCoroutine { cont: Continuation<Response>
       // 
       logger.info("Call order: 0")
        apiCallbackClient.get('resource') { response ->
             logger.info("Call order: 2")
             // 성공적으로 결과를 받아온 경우 결과값과 함께 코루틴의 진행을 재개
             cont.resumeWith(response)
        }
        logger.info("Call order: 1")
    }
    logger.info("Call order: 3")
    return response.data
}

//  위  코드에서 사용된 apiCallbackClient는 아래와 같이 구성되어있다고 가정
class ApiCallbackClient {
    fun get(path: String, (Response) -> Unit)
}
```



#### Coroutine [Android Guide ver]

> 코루틴을 사용하는 전문 개발자 중 50% 이상이 생산성이 향상되었다고 보고했습니다.

> > 아래에서 사용할 `suspend fun`을 '정지 함수'라고 번역한다.
>
> `suspend` 함수는 다른 `suspend` 함수에서 호출하거나 코루틴 빌더(예: `launch`)를 사용하여 새 코루틴을 시작하는 방법으로만 호출할 수 있습니다.
>
> - `suspend`는 현재 코루틴 실행을 일시중지하고 모든 로컬 변수를 저장합니다.
> - `resume`은 정지된 위치부터 정지된 코루틴을 계속 실행합니다.

[[android guide](https://developer.android.com/kotlin/coroutines?hl=ko)]

```kotlin
// fetchDocs는 
suspend fun fetchDocs() {                             // Dispatchers.Main
    val result = get("https://developer.android.com") // Dispatchers.IO for `get`
    show(result)                                      // Dispatchers.Main
}

// withContext = ?
suspend fun get(url: String) = withContext(Dispatchers.IO) { /* ... */ }
```

이 예에서 `get()`은 여전히 기본 스레드에서 실행되지만 네트워크 요청을 시작하기 전에 코루틴을 정지합니다. 

네트워크 요청이 완료되면 `get`은 콜백을 사용하여 기본 스레드에 알리는 대신 정지된 코루틴을 재개합니다.

**Kotlin은 *스택 프레임*을 사용하여 로컬 변수와 함께 실행 중인 함수를 관리합니다. 코루틴을 정지하면 현재 스택 프레임이 복사되고 저장됩니다.** 

**재개되면 스택 프레임이 저장된 위치에서 다시 복사되고 함수가 다시 실행됩니다.** 

> Q. 코루틴은 하나의 스레드를 재활용하기 위해 작업을 큐(내부적으로 이벤트 루프 같은 게 있을듯)에 넣고 Context Switch를 언어 수준에서 수행하는가?

코드가 일반적인 순차 차단 요청처럼 보일 수도 있지만 코루틴은 네트워크 요청이 기본 스레드를 차단하지 않도록 합니다.



##### 기본 안전을 위해 코루틴 사용

Kotlin 코루틴은 *디스패처*를 사용하여 코루틴 실행에 사용되는 스레드를 확인합니다. 코드를 기본 스레드 외부에서 실행하려면 *기본* 또는 *IO* 디스패처에서 작업을 실행하도록 Kotlin 코루틴에 지시하면 됩니다. Kotlin에서 모든 코루틴은 기본 스레드에서 실행 중인 경우에도 디스패처에서 실행되어야 합니다. 코루틴은 자체적으로 정지될 수 있으며 디스패처는 코루틴 재개를 담당합니다.

Kotlin은 코루틴을 실행할 위치를 지정하는 데 사용할 수 있는 **<u>세 가지 디스패처</u>**를 제공합니다.

- **Dispatchers.Main** - 이 디스패처를 사용하여 **<u>기본 Android 스레드에서</u>** 코루틴을 실행합니다. 이 디스패처는 UI와 상호작용하고 빠른 작업을 실행하기 위해서만 사용해야 합니다. 예를 들어 `suspend` 함수를 호출하고 Android UI 프레임워크 작업을 실행하며 [`LiveData`](https://developer.android.com/topic/libraries/architecture/livedata?hl=ko) 객체를 업데이트합니다.
- **Dispatchers.IO** - 이 디스패처는 **<u>기본 스레드 외부에서</u>** 디스크 또는 네트워크 I/O를 실행하도록 최적화되어 있습니다. 예를 들어 [회의실 구성요소](https://developer.android.com/topic/libraries/architecture/room?hl=ko)를 사용하고 파일에서 읽거나 파일에 쓰며 네트워크 작업을 실행합니다.
- **Dispatchers.Default** - 이 디스패처는 **<u>CPU를 많이 사용하는 작업을 기본 스레드 외부에서</u>** 실행하도록 최적화되어 있습니다. 예를 들어 목록을 정렬하고 JSON을 파싱합니다.

> Q. '기본 스레드 외부'라는 것은 디스패처와 스레드가 1:1이 아니라 1:N일 수도 있다는 것일까
>
> > 스레드 풀을 사용하는 디스패처(예: `Dispatchers.IO` 또는 `Dispatchers.Default`) <--- 가이드에 있음.
> >
> > - 스레드 로컬 변수가 전체 `withContext()` 블록에 동일한 값을 가리키지 않을 수 있습니다.

계속해서 이전 예에서 디스패처를 사용하여 `get` 함수를 다시 정의할 수 있습니다. `get`의 본문 내에서 `withContext(Dispatchers.IO)`를 호출하여 IO 스레드 풀에서 실행되는 블록을 만듭니다. 블록 안에 넣은 코드는 항상 `IO` 디스패처를 통해 실행됩니다. `withContext`는 그 자체로 정지 함수이므로 `get` 함수도 정지 함수입니다.

