<!--
1주차 공부 내용
Kotlin in action 4.1 - 4.2 장 정리
-->

# 클래스, 객체, 인터페이스

## 계층 구조

### 인터페이스

- 인터페이스 안에는 추상 메소드, 디폴트 구현이 있는 메소드 정의 가능.
- `override` 변경자로 상위 클래스나 인터페이스에 있는 프로퍼티나 메소드를 오버라이드 한다.

```kotlin
// 인터페이스 선언
interface Clickable {
	fun click()
}

// 인터페이스 구현
class Button : Clickable {
  override fun click() = println("I was Clicked")
}
```

- 동일한 디폴트 메소드가 있는 두 인터페이스를 상속받은 클래스에서는 명시적으로 해당 메소드에 대한 오버라이드를 수행해야한다. `super<Interface Name>` 을 이용하면 해당 인터페이스 디폴트 메소드를 호출할 수 있다.

### open, final, abstract 변경자 : 기본적으로 final

- **취약한 기반 클래스**(상속받은 하위 클래스에서 원래 의도와 다르게 오버라이드로 인해 동작이 예기치 않게 바뀔 수 있는 경우 해당 기반 클래스를 ‘취약'하다고 한다.) 문제를 예방 하기 위해, 코틀린의 클래스와 메서드는 기본적으로 **final** 이다.
- 상속을 허용하려면 `open`을 붙여야 한다.
- 상위 클래스에서 상속받아 구현한(`override` 가 앞에 붙어있음) 메소드의 경우 상속이 가능하다. 이를 막기 위해서는 명시적으로 `final` 을 붙여 주어야 한다.
- 코틀린에서는 디폴트가 final이기 때문에 다양한 경우에 스마트 캐스트가 가능하지는 장점이 있다.

```kotlin
// open 을 붙였기 때문에 상속 가능
open class RichButton: Clickable {
  // 하위 클래스에서 오버라이드 불가능
	fun disable() {}

  // 오버라이드 가능
	open fun animate() {}

  // 하위 클래스에서 오버라이드 가능
  override fun click() {}
}
```

- 추상 클래스(오버라이드 해서 사용해야하는 클래스. `abstract`로 선언.)에서는 디폴트가 open 이다.

### 가시성 변경자. public, protected, private

- 코틀린에서 가시성 변경자의 디폴트는 public이다.
- package-private 은 존재하지 않는다. 코틀린에서 패키지는 단순히 네임스페이스의 용도로만 사용한다.
- 대신 `internal` 을 지원한다. 이는 모듈 내부에서만 볼수 있음을 의미하는데, 모듈은 한꺼번에 한번의 컴파일 되는 코틀린 파일을 의미한다. 모듈 단위를 제공함으로써 진정한 캡슐화를 제공한다는 장점이 있다.

| 변경자 | 클래스 맴버 | 최상위 선언 |
| --- | --- | --- |
| public(디폴트) | 모든 곳에서 볼 수 있음 | 모든 곳에서 볼 수 있음 |
| internal | 같은 모듈 | 같은 모듈 |
| protected | 하위 클래스 | (최상위 선언에 적용할 수 없음) |
| private | 동일 클래스 | 동일 파일 |

### Nested class

- 클래스 안에 다른 클래스를 선언할 수 있다.
- 명시적으로 요청하지 않는 한 중첩 클래스에서는 바깥쪽 클래스 인스턴스에 접근 권한이 없다.
- `inner`을 명시함으로써 바깥 클래스 참조가 가능해진다.

```kotlin
class Outer {
	inner class Inner {
		fun getOuterReference(): Outer = this@Outer
	
```

### 봉인된 클래스 : 클래스 계층 정의 시 계층 확장 제한

- `sealed` 를 이용하여 선언하는 경우 모든 하위 클래스를 중첩클래스로 제한한다.
- 하위 클래스를 중첩 클래스로 제한하면서 미처 처리 못한 하위 클래스로 인한 오류를 예방할 수 있다.

```kotlin
sealed classs Expr {
	class Num(val value: Int) : Expr()
	class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr): Int = 
	when (e) {
		is Expr.Num -> e.value
		is Expr.Sum -> eval(e.right) + eval(e.left)
	}
```

- `sealed` 로 표현된 클래스는 자동으로 open으로 설정된다.
- 내부적으로 Expr 클래스는 private 생성자를 가진다.
- 자바와의 호환성 문제로 sealed 인터페이스는 없다.

## 클래스 선언

### 주 생성자와 초기화 블록

- 주 생성자란 클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 표현.
- `constructor`는 주생성자 혹은 부생성자 정의를 시작할 때 사용한다.
- `init` 은 초기화 블록을 시작한다. 이는 클래스 인스턴스가 생성될 때, 실행된다. 클래스 안에 여러 초기화 블록을 선언할 수 있다.
- `val` 키워드를 클래스 파라미터에 추가하면 프로퍼티가 정의 + 초기화가 된다.

```kotlin
// 모두 같은 결과이다.
class User constructor(_nickname: String) {
	val nickname: String
	
	init {
		// this.nickname = nickname 으로 사용 가능
		nickname = _nickname
	}
}

class User(_nickname: String) {
	val nickname = _nickname
}

class User(val nickname: String)
```

- 함수와 마찬가지로 파라미터에 프로퍼티의 디폴트 값을 추가할 수 있다.
- 인스턴스 생성을 하려면 new 캐워드 없이 생성자를 직접 호출하면 된다.
- 기반 클래스가 있다면, 주 생성자에서 기반 클래스의 생성자를 호출해야한다. 기반 클래스 이름 뒤에 괄호를 치고 생성자 인자를 넘긴다.

```kotlin
open class User(val nickname: String) { ... }
class TwitterUser(nickname: String) : User(nickname) { ... }
```

- 별도의 생성자를 정의하지 않으면 컴파일러는 자동으로 아무 일도 하지 않는 인자가 없는 디폴트 생성자를 만들어준다.
- 하위 클래스에서는 꼭 기반 클래스의 생성자를 호출해주어야 한다. 이를 이용해서 클래스와 인스턴스 구분이 가능한데, 클래스는 괄호가 있고 인스턴스는 그렇지 않다.

## 부 생성자

- 생성자가 여러개 필요한 경우 부생성자를 사용할 수 있다.
- `constructor` 키워드로 생성 가능하며 여러개를 선언할 수 있다.
- `this()`를 통해 자신의 다른 생성자를 호출 가능하다.

### 인터페이스에 선언된 프로퍼티 구현

- 인터페이스 추상 프로퍼티 선언이 있을 수 있다.

```kotlin
interfae User {
	val nickname: String
}
```

- 이를 구현하는 여러 방법이 있다.

```kotlin
class PrivateUser(override val nickname: String) : User

class SubscribingUser(val email: String) : User {
	overridee val nickname: String
		get() = email.substringBefore('@')
}

class FacebookUser(val accountId: Int) : User {
	override val nickname = getFacebookName(accountId)
}
```

- SubscribingUser는 커스텀 게터를 사용해서 프로퍼티 설정을 한다. 이렇게 하면 매번 이메일 주소에서 별명을 계산해서 반환한다.
- FacebookUser에는 초기화 식을 사용한다. 이미 정의되 있는 식을 호출하여 초기화한다. 이는 객체 초기화할때 정의된 값을 계속 가져오는 방식으로 매번 계산하는 커스텀 방식과 다르다는 점을 유의해아한다.
- 인터페이스에서는 게터, 세터가 있는 프로퍼티를 선언이 가능하다. 하지만 이를 뒷받침하는 필드를 참조할 수는 없다. 이는 상태를 저장해야 가능한데, 인터페이스는 상태를 저장할 수 없다.

```kotlin
interface User {
	val email: String
	val nickname: String
		get() = emain.substringBefore('@')
}
```

### 게터와 세터에서 뒷받침하는 필드에 접근

- 코틀린에서 프로퍼티 값을 바꿀 때는 `user.address = "new value"` 로 필드 설정 구문을 사용한다. 이 경우 내부적으로는 address의 세터를 호출한다.
- 게터와 세터에서는 `field` 라는 특별한 식별자를 통해 뒷받침하는 필드 값에 접근 가능하다. (위에서는 ‘new value’) 이를 통해 추가로직 구현이 가능하다.

```kotlin
class User(val name: STring) {
	var address: Stirng = "unspecified"
		set (value: String) {
			println("""
				Address was changed for $name:
				"$field" -> "$value".""".trimIndent())
			field = value
		}
}
```

### 접근자 가시성 변경

- 기본적으로는 프로퍼티의 가시성과 접근자의 가시성은 같다. 단, 원한다면 get, set의 가시성을 명시적으로 선언 가능하다.

```kotlin
class LengthCounter {
	var counter: Int = 0
		private set
	fun addWord(word: String) {
		counter += word.length
	}
}
```