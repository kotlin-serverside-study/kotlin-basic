# Kotlin in Action 요약

## 4. Chapter04

### 4.1 개요
* 코틀린의 클래스와 인터페이스는 자바와 약간 다르다.
    * 코틀린 선언은 기본적으로(val) final이며 public이다.
    * 중첩 클래스는 외부 클래스에 대한 참조가 없다.


### 4.2. 인터페이스
* https://kotlinlang.org/docs/interfaces.html#properties-in-interfaces
* 코틀린 인터페이스 안에 추상 메소드 뿐만 아니라, 구현된 메소드도 정의가 가능하다.
* 인터페이스에 상태(필드)가 들어갈 수 없다.
    * 추상 프로퍼티를 인터페이스에 선언을 할 수 있고 하위 클래스에서 오버라이딩이 가능(Java의 인터페이스와 차이)
    * https://kotlinlang.org/docs/interfaces.html#properties-in-interfaces
    ```kotlin
    interface User {
        val nickname : String // 인터페이스에 추상 프로퍼티 선언
    }

    class SubscribingUser(val email: String) : User {
        override val nickname: String // 하위 클래스에서는 반드시 오버라이딩을 해야 한다.
            get() = email.subscribingBefore('@')
    }

    class FaceBookUser(val accountId: Int) : User {
        override val nickname = getFaceBookName(accountId) // 이 함수는 다른 곳에 정의돼 있다고 가정한다.
    }
    ```
* ```override```라는 변경자를 앞에 꼭 붙여주어야 한다.


### 4.3. 변경자(open, final, abstract)

#### 어떤 클래스의 멤버의 상속을 허용하려면 ```open```을 사용해야 한다.

```kotlin
open class RichButrton: Clickable { // open 을 사용하면 다른 클래스가 상속을 할 수 있음.
    fun disable() {} // 이 함수는 final . 하위 클래스가 오버라이드 할 수 없음.
    open fun animate() {} // 하위 클래스에서 오버라이딩이 가능하다.
    override fun click() {} // 상위 클래스에서 선언된 메서드를 오버라이딩 하고 있음. 오버라이드한 메소드는 기본적으로 열려 있음.
    final override fun doubleClick() {} // final이 붙은 override 메소드나 프로퍼티는 하위 클래스에서 오버라이딩이 금지되어 있다.
}
```

#### 자바와 마찬가지로 추상 클래스 정의에 ```abstract```를 사용한다.

```kotlin
abstract class Animated {
    abstract fun animate() // 추상 함수이다. 구현이 없기에 하위 클래스에서 반드시 오버라이딩 해야 한다.
}
```

### 4.4 가시성 변경자(visibility modifier)
|변경자|클래스 멤버|최상위 선언|
|------|---|---|
|```public```|모든 곳에서 볼 수 있다.|모든 곳에서 볼 수 있다.|
|```internal```|같은 모듈 안에서만 볼 수 있다.|같은 모듈 안에서만 볼 수 있다.|
|```protected```|하위 클래스에서만 볼 수 있다.|(최상위 선언에 적용할 수 없음)|
|```private```|같은 클래스 안에서만 볼 수 잇다. | 같은 파일 안에서만 볼 수 있다|

> 자바와의 호환성
> 자바에서는 클래스를 ```private```로 만들 수 없으므로 내부적으로 코틀린은 private 클래스를 패키지-전용 클래스로 컴파일 한다.
> ```internal``` 키워드는 바이트코드상 public이 된다.

### 4.5 코틀린은 기본적으로 중첩 클래스
* https://kotlinlang.org/docs/nested-classes.html


### 4.6 봉인된 클래스(sealed classes)
* 상위 클래스 ```Expr``` 에 숫자를 표현하는 ```Num```과 덧셈 연산을 표현하는 ```Sum``` 이라는 두 하위 클래스가 있을 때,...

> 인터페이스 구현을 통해 식 표현하기
```kotlin
interface Expr
class Num(val value: Int) : Expr
class Sum(val left Expr, val right; Expr) : Expr

fun eval (e:Expr) : Int = 
    when (e) {
        is Num -> e.value
        is Sum --> eval(e.right) + eval(e.left)
        else -> thfow IllegalArgumentException("Unknown expression")
    }
```

> sealed 클래스로 식 표현하기
```kotlin
sealed class Expr {
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right:Expr) : Expr()
}

fun eval (e: Expr) : Int = 
    when (e) {
        is Expr.Num -> e.value
        is Expr.Sum -> eval (e.right) + eval(e.left)
    }
```
* ```sealed``` 키워드를 사용하면 상위 클래스를 삳속한 하위 클래스 정의를 제한 할 수 있음.
* ```sealed```로 표시된 클래스는 자동적으로 open(별도로 open 키워드를 붙일 필요가 없음)

### 4.7. Data Class
* https://kotlinlang.org/docs/data-classes.html
* 어떤 클래스가 데이터를 저장하는 역할을 수행한다면 toString, equals, hashCode를 오버라이딩 해야 한다.
* 자바의 경우 ```lombok```이나  IDE의 Generate Code 기능을 이용하여 자동 생성이 가능하지만, 보일러 플레이트성 코드가 남발되는 문제가 있음.
* 코틀린에서 ```data class``` 라는 키워드를 통해 필요한 메서드를 **컴파일러가 자동으로 만들어준다**

```kotlin
data class Client(val name: String, val postalCode: Int)
```

* 코틀린 컴파일러는 ```data``` 클래스에게 toString, equals, hashCode 세 가지 메서드 뿐만 아니라 유용한 메서드를 추가로 제공한다.

#### copy()
* 기본적으로 data class는 불변 클래스(immutable class)로 만드는 것을 구너장
* 코틀린 컴파일러는 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 편의 메서드인 ```copy()```를 제공함.

#### by
* https://kotlinlang.org/docs/delegation.html
* [**위임 패턴(Delegation Pattern)**](https://medium.com/remember/delegation-pattern-336d16b20b05) 혹은 Decorator Pattern 과 관련이 있음
    * 상속의 부작용을 개선할 수 있는 디자인 패턴이지만, 이를 위해 준비해야될 코드가 많아지는 문제가 필요하다.

* 코틀린은 인터페이스를 구현할 때 ```by``` 키워드를 통해 인터페이스에 대한 구현을 다른 객체에 위임한다는 것을 명시 할 수 있음.

```kotlin
class DelegatingCollection<T> (
    innerList: Collection<T> = ArrayList<T> ()
) : Collection<T> by innerList ()
```

### 4.8. object
* 코틀린에서 ```Object``` 키워드를 통해 싱글턴 객체를 쉽게 선언할 수 있습니다.

> 두 파일 경로를 대소문자 관계 없이 비교해주는 **Comparator**를 Object 키워드로 구현한 예제
```kotlin
object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(file1: File, file2: File) : Int {
        return file1.path.compareTo(file2.path, ignoreCase = true)
    }
}
```

### 4.9 companion object
* 코틀린 클래스 안에서 정적인 멤버가 없음(자바의 ```static``` 키워드를 지원하지 않음.)
* 이러한 정적 멤버가 필요한 경우 코틀린에서는 동반 객체(companion object)로 사용하는 것을 권장함.
* 클래스 안에 정의된 객체 중 하나에 ```companion```이라는 특별한 표시를 붙이면 그 클래스의 동반 객체로 만들 수 있다.

> 생성자를 팩토리 메서드로 대신하기

```kotlin
class User private constructor(val nickname: String) {
    companion object {
        fun newSubscribingUser(email : String) = 
            User(email.substringBefore('@'))

        fun newFacebookUser(accountId: Int) = 
            User(getFacebookNmae(accountId))
    }
}
```

> #### 코틀린 동반 객체와 정적 멤버
> 클래스의 동반 객체는 일반 객체와 비슷한 방식으로, 클래스에 정의된 인스턴스를 가리키는 정적 필드로 컴파일 된다.
> 때로, 자바에서 사용하기 위해 코틀린 클래스의 멤버를 정적인 멤버로 만들어야될 필요가 있다.
> 이런 경우 ```@JvmStatic``` 어노테이션을 코틀린 멤버에 붙이면 된다.
> 이 기능은 자바와의 상호 호환성을 위해 존재합니다.

> #### 자바 API를 어노테이션으로 제어하기 (p.437)
> 코틀린으로 선언한 내용을 자바 바이트코드로 컴파일 하는 방법과 코틀린 선언을 자바에 노출하는 방법을 제어하기 위한 어노테이션을 제공합니다.
> 아래 어노테이션을 사용하면 코틀린 선언을 자바에 노출하는 방법을 변경할 수 있습니다.
> * ```@JvmName``` : 코틀린 선언이 만들어내는 자바 필드나 메소드 이름을 변경
> * ```@JvmStatic``` : 메소드, 객체선언, 동반 객체에 적용하면, 그 요소가 자바 정적 메소드로 노출됩니다.
> * ```@JvmOverloads``` : 디폴트 파라미터 값이 있는 함수에 대해 컴파일러가 자동으로 오버로딩한 함수를 생성합니다.
> * ```@JvmField``` :  게터나 세터가 없는 공개된 자바필드로 프러퍼티를 노출시킵니다.(public)

### 4.10 객체 식(object expression)
* https://kotlinlang.org/docs/object-declarations.html#using-anonymous-objects-as-return-and-value-types
* 코틀린에서는 익명 객체(anoyumous object)를 정의할 때도 object 클래스를 선언합니다.
* 자바의 익명 클래스와 같이 객체 식 안의 코드는 그 식이 포함된 함수의 변수에 접근할 수 있습니다.
* 자바와 달리 final 이 아닌 변수도 객체 식에서 사용이 가능합니다.
```kotlin
val listener = object : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) : {}
    override fun mouseEntered(e: MouseEvent) : {}
}
```


### 4.11 Inline Classes - value class
* https://kotlinlang.org/docs/whatsnew15.html#inline-classes
* Kotlin 1.5 버전 이후 부터 정식 릴리즈로 소개된 새로운 형태의 클래스 feature 입니다. 
    * https://velog.io/@dhwlddjgmanf/Kotlin-1.5%EC%97%90-%EC%B6%94%EA%B0%80%EB%90%9C-value-class%EC%97%90-%EB%8C%80%ED%95%B4-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90
