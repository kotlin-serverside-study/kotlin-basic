# Kotlin in Action 정리

> Chapter02의 일부 내용만 발췌 해서 정리하였습니다.


### Kotlin Refactoring

#### when
* ```when``` 키워드를 사용하여 자바의 swtich ~ case 문을 대체할 수 있습니다.

```kotlin
fun getMnemonic(color: Color) = 
    when (color) {
        Color.RED -> "빨강"
        Color.GREEN -> "초록"
        Color.BLUE -> "파랑"
    }

println(getMnemonic(Color.BLUE)); // 파랑
```

* 중첩된 if 문도 간결하게 표현이 가능합니다.

```java

public int eval(Expr e) {
    if(e instanceof Num) {
        return e.value;
    } else if (e instanceof Sum) {
        return eval(e.right) + eval(e.left);
    } else {
        throw IllegalArgumentException("Unknown expression");
    }
}
```

```kotlin
fun eval(e: Expr) : Int = 
    when (e) {
        is Num ->
            e.value
        is Sum ->
            eval(e.right) + eval(e.left)
        else ->
            throw IllegalArgumentException("Unknown expression")
    }
```

#### 이터레이션

* 수열에 대한 반복문 표현을 아래와 같이 for 문에서 표현이 가능합니다.

```kotlin
for (i in 1...100) {
    println("number : $i")
}
```

* Map 타입에 대한 이터레이션 표현도 가능합니다.

```
val binaryReps = TreeMap<Char, String>()

for (c in  'A'...'F') {
    val binary = Integer.toBinaryString(c.toInt()) // ASCII 코드를 2진 표현으로 바꾼다.
    binaryReps[c] = binary
}

for ((letter, binary) in binaryReps) {
    println("$letter = $binary)
}
```


* cf. 자바에서도 ```Iterable<T>``` 인터페이스를 상속한 컬렉션에 대해서 ```forEach()``` 를 이용한 순회가 가능합니다.


#### 예외처리

* Java와 마찬가지로 try~catch~finally 구문 사용이 가능합니다.
    * 다만 차이점은, 자바에서는 체크 예외(Checked Exception)을 catch문에서 처리하지 않으면 throws 절에 명시를 해주어야 하지만,
    * 코틀린에서는 언체크드 예외를 잡지 않아도 됩니다.

* 1.3 버전 이상에서는 ```Result<T>``` 타입과 ```runCatching``` 블록으로 예외를 처리할 수도 있습니다.
    * runCatching 블록 안에서 실행된 결과는 Result<T> 타입으로 리턴이 됩니다.
    * 그외 ```map``` 이나 ```recover``` 블록을 중첩해서 예외 처리도 가능합니다.
    * 참고 : https://medium.com/harrythegreat/kotlin-runcatching%EA%B3%BC-result-%ED%83%80%EC%9E%85-ab261f47efa8
    * Kotlin in Action 도서에서는 설명이 빠져있는 스펙인거 같네요.