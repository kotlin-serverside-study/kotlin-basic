# 6장 시퀀스

* 컬렉션에서 처리는 즉시(eager) 발생한다.
	* 즉 컬렉션의 map이나 filter가 호출될 때 컬렉션의 모든 원소는 즉시 처리된다.
* 반면에 시퀀스는 지연(lazy) 처리 된다.
	* 데이터를 처리하기 위해 시퀀스를 사용하면 각각의 원소는 자신의 다음 원소가 처리되기 전에 전체 파이프라인을 완료한다.

* 연산횟수 
	* 100(map) x 100(filter)
```kotlin
(100 until 200).map { it * 2}
	.filter { i % 3 == 0 }
	.first()
```
* 컬렉션 연산을 조금 더 개선
	* filter에서 반복 실행되는 연산이 줄어듬.
	* 연산횟수 : 100
```kotlin
(100 until 200).map { it * 2}
	.first { i % 3 == 0 }
```

* 시퀀스로 
	* 시퀀스의 각 원소는 다음 원소로 진행하기 전에 전체 파이프라인에서 처리된다.
	* 연산횟수 6회
```kotlin
(100 until 2000000).asSequence()
	.map { 
		println("doubling $it")
		it * 2
	}
	.filter { 
		println("filtering $it")
		it % 3 == 0
	}
	.first()
```

* 시퀀스 API는 컬렉션에 들어있는 함수와 똑같이 가지고 있지만 시퀀스에 대한 연산은 중간연산과 최종연산 이라늠 범주로 나뉜다.
	* 중간 연산은 새로운 시퀀스를 리턴한다.
	* 최종 연산은 시퀀스가 아닌 다른 것을 리턴한다.
	* 최종 연산 없이는 시퀀스는 데이터를 처리하지 않는다.
* 자바 스트림과 달리 코틀린의 일부 시퀀스는 여러 번 순회 할 수 있고 그렇지 못한 시퀀스는 여러 번 순회가 불가능 하다

#### 시퀀스 생성 방법
* 값이 있는 경우 ```sequenceOf```를 사용
```kotlin
val numSequence = sequenceOf(3,1,4,1,5,9)
```
* 컬렉션을 스트림으로 변환하려면 ```asSequence()``` 또는 ```generateSequence```
	* generateSequence의 경우 첫번째 인자는 초기값, 두번째는 시퀀스를 생성하는 함수(람다)


## Recipe 6.4 시퀀스에서 yield 하기

yield 함수는 이터레이터에 값을 제공하고 다음 값을 요청할 때 까지 값 생성을 중단한다.

yeild가 중단 함수라는 사실은 yield가 코루틴과도 잘 동작한다는 의미이다.
> 다시 말해 코틀린 런타임은 코루틴에 값을 제공한 후에 다음 값을 요청할 때 까지 해당 코루틴을 중단 시킬 수 있다.

```kotlin

fun fibonacciSequence() = sequence {
	var terms = Pair(0,1)
	
	while (true) {
		yield(terms.first)
		terms = terms.second to terms.first + terms.second
		}
	}

@Test
fun `first 10 Fibonacci numbers from sequende`() {
	val fibs = fibonacciSequence()
		.take(10)
		.toList()
	assertEquals(listOf(0,1,1,2,3,5,8,13,21,34), fibs)
}
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTMzMTEwNzU0MSwxNjYyMzY5OTkzLDE0MD
Y0MzE0NTJdfQ==
-->