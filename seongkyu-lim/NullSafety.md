

# Null Safety

---

## Nullable types (?) and non-null types ()

코틀린의 타입 시스템은 위험한 널 참조를 제거하는 것을 지향합니다.

자바의 경우 NPE(NullPointerException)를 알기 위해서는 프로그램을 실행하여야만 합니다. 또한 NPE를 예방하기위해 예외처리를 try-catch와 같은 구문으로 일일이 해주어야합니다.

→ 코틀린에서는 물음표 기호로 nullable인지 nonnull인지를 미리 정해줄 수 있습니다.

타입에 물음표(?) 기호가 있는 경우 : nullable

타입에 물음표(?) 기호가 없는 경우 : nonnull

```kotlin
var a: String = "abc" // Regular initialization means non-null by default
a = null // compilation error

var b: String? = "abc" // can be set to null
b = null // ok
```

이렇게 함으로써 null이 가능한지 아닌지를 미리 정해주어 물음표(?)가 없는 경우 null값이 불가능하므로 null값에는 적용이 불가능한 메소드를 사용하더라도 NPE로부터 안전함을 보장할 수 있게 됩니다.

만약 nullable한 property에 대해 메소드를 사용하려는 경우 error를 냅니다.

```kotlin
val l = a.length

val l = b.length // error: variable 'b' can be null
```

nullable한 property에 대해서도 접근이 필요한 경우에는 몇가지 방법을 통해 실행을 시킬 수 있습니다.

첫번째 방법으로 condition구문을 통해 null check를 함으로써 실행이 가능합니다. 다만 nullable property가 mutable한 경우 check후에 null이 될 수도 있으므로 반드시 immutable해야 합니다.

```kotlin
val l = if (b != null) b.length else -1
```

두번째방법으로 safe call operator인 **?.**을 사용하는 것입니다.

?.을 사용하여 null일 경우 null을 반환하고 nonnull인 경우 정상적으로 실행시킵니다. 

```kotlin
val a = "Kotlin"
val b: String? = null
println(b?.length)
println(a?.length) // Unnecessary safe call
```

nonnull일 경우 특정 statment를 실행시키고 싶을 때는 ?.let{}을 사용할 수 있습니다.

```kotlin
val listWithNulls: List<String?> = listOf("Kotlin", null)
for (item in listWithNulls) {
item?.let { println(it) } // prints Kotlin and ignores null
}
```

두 가지 방법 모두 null check를 미리 해줌으로써 null safety를 보장하게됩니다.

---

## Elvis Operator (?:)

nullable한 property에 대해서 접근이 필요할 경우 조건문이나 safe call operator를 이용하여 null을 체크하여 실행을 시킬 수 있었습니다. 다만 safe call operator를 이용할 경우 null일 경우에는 null을 반환하였지만, Elvis Operator를 이용할 경우 조건문을 이용하는 것과 같이 null일 경우에는 다른 nonnull값을 반환할 수 있도록 할 수 있습니다. 

```kotlin
val l = b?.length ?: -1
```

---

## not-null assertion operator (!!)

for NPE-lovers.

```kotlin
val l = b!!.length
```

b가 Null일 경우에도 실행되어 NPE 반환.

---

## **Safe casts**

일반적으로 casts 시에 object가 target type이 아닌 경우 ClassCastException를 발생 시킵니다. 이를 방지하기 위해 아래와 같이 적용을 하면 ClassCastException을 일으키는 대신 null을 반환합니다.

```kotlin
val aInt: Int? = a as? Int
```

---

## **Collections of a nullable type**

collection에서 null값을 배제하고 싶은 경우, .filterNotNull()을 사용하면 됩니다.

```kotlin
val nullableList: List<Int?> = listOf(1, 2,null, 4)
val intList: List<Int> = nullableList.filterNotNull()
```

---

참고 문헌 : [https://kotlinlang.org/docs/null-safety.html#safe-casts](https://kotlinlang.org/docs/null-safety.html#safe-casts)