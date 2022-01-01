# Week2

코틀린 표준 라이브러리에는 객체 컨텍스트 안에서 코드 블록을 실행할 목적으로 만든 다수의 함수가 포함되어 있다. 

### 들어가기 전에..

> ### 람다 표현식(lambda expression)
> 코틀린에서는 함수리터럴(function literals) 또는 익명의 함수로 정리가 가능합니다.
>>Lambda expressions and anonymous functions are function literals. Function literals are functions that are not declared but are passed immediately as an expression. Consider the following example:
> https://kotlinlang.org/docs/lambdas.html
> 
> 자바(Java)에서의 람다 표현식의 정의와 그 뉘앙스가 코틀린과는 살짝 다릅니다.
> 이는 표현식의 모양은 비슷하지만 그 기반에 깔린 언어의 구조적인 차이가 있기에 한번 쯤 참고해보면 좋을 듯 합니다.
>
> https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html


> ### 람다 리시버(lambda with receiver)
> 확장함수 + 람다 
>>Function types can optionally have an additional receiver type, which is specified before the dot in the notation: the type A.(B) -> C represents functions that can be called on a receiver object A with a parameter B and return a value C. Function literals with receiver are often used along with these types.
### apply

* ```apply``` 함수는 this를 인자로 전달하고 this를 리턴하는 확장 함수이다.

```Kotlin
inline fun <T> T.apply(block: T.() -> Unit) : T
```

* 모든 제너릭 타입(T)에 존재하는 확장함수.
* 명시된 블록을 수신자인 this와 함께 호출하고 해당 블록이 완료되면 this를 리턴한다.

### also

* ```also```는 모든 제너릭 타입 T에 추가되고 block 인자를 실행 시킨 후에 자신을 리턴한다.
* 일반적으로 객체에 함수 호출을 연쇄 시키기 위해 사용한다.

```Kotlin
inline fun <T> T.also(
    block: (T) -> Unit
): T
```

* 블록 안에서 객체를 it 이라고 언급한다.
* ```also```는 컨텍스트 객체를 리턴하기 때문에 추가 호출을 함께 연쇄시키기에 용이하다.


### let

```Kotlin
inline fun <T, R> T.let(
    block(T) -> R
) : R
```

* 많은 자바 API 결과가 없는 경우 null을 돌려 주는데 안전한 호출연산자(?.)와 let 함수, 엘비스 연산자(?:) 조합은 이를 쉽게 처리하게 한다.

> 널을 허용하는 객체의 일반적인 예외 처리 예
```Kotlin
data class user(val firstName: String, val lastName: String)

fun printUserName(user: User?) {
    if(user != null) {
        println(user.firstName)
    }
}
```

> let으로 null 처리를 간결하게
```Kotlin
data class user(val firstName: String, val lastName: String)

fun printUserName(user: User?) {
    user?.let{println(user.firstName)}
}
```

> 기출 변형
```java
package week2;

public class PersonEntity {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "PersonEntity{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

```
```Kotlin
object KotlinStandardLibrary {
    @JvmStatic
    fun main(arg: Array<String>) {
        var person: PersonEntity = PersonEntity()
        person.let {
            it.age = 10
            it.name = "jay"
        }

        println(person)
    }
}
```

* Kotlin class 파일을 Java로 decompile
```java
package week2;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 5, 1},
   k = 1,
   d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0007¢\u0006\u0002\u0010\b¨\u0006\t"},
   d2 = {"Lweek2/KotlinStandardLibrary;", "", "()V", "main", "", "arg", "", "", "([Ljava/lang/String;)V", "kotlin-demo-app"}
)
public final class KotlinStandardLibrary {
   @NotNull
   public static final KotlinStandardLibrary INSTANCE;

   @JvmStatic
   public static final void main(@NotNull String[] arg) {
      Intrinsics.checkNotNullParameter(arg, "arg");
      PersonEntity person = new PersonEntity();
      boolean var3 = false;
      boolean var4 = false;
      int var6 = false;
      person.setAge(10);
      person.setName("jay");
      boolean var2 = false;
      System.out.println(person);
   }

   private KotlinStandardLibrary() {
   }

   static {
      KotlinStandardLibrary var0 = new KotlinStandardLibrary();
      INSTANCE = var0;
   }
}

```

* Nullable한 참조 타입에 ```?.``` 연산자와 let을 조합해서 사용한다면
```kotlin
object KotlinStandardLibrary {
    @JvmStatic
    fun main(arg: Array<String>) {
        var person: PersonEntity? = createPerson()
        person?.let {
            it.age = 10
            it.name = "jay"
        }

        println(person)
    }
}

fun createPerson() : PersonEntity? {
    return PersonEntity()
}
```

```java
// KotlinStandardLibraryKt.java
package week2;

import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 5, 1},
   k = 2,
   d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\b\u0010\u0000\u001a\u0004\u0018\u00010\u0001¨\u0006\u0002"},
   d2 = {"createPerson", "Lweek2/PersonEntity;", "kotlin-demo-app"}
)
public final class KotlinStandardLibraryKt {
   @Nullable
   public static final PersonEntity createPerson() {
      return new PersonEntity();
   }
}
// KotlinStandardLibrary.java
package week2;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 5, 1},
   k = 1,
   d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0007¢\u0006\u0002\u0010\b¨\u0006\t"},
   d2 = {"Lweek2/KotlinStandardLibrary;", "", "()V", "main", "", "arg", "", "", "([Ljava/lang/String;)V", "kotlin-demo-app"}
)
public final class KotlinStandardLibrary {
   @NotNull
   public static final KotlinStandardLibrary INSTANCE;

   @JvmStatic
   public static final void main(@NotNull String[] arg) {
      Intrinsics.checkNotNullParameter(arg, "arg");
      PersonEntity person = KotlinStandardLibraryKt.createPerson();
      if (person != null) {
         boolean var3 = false;
         boolean var4 = false;
         int var6 = false;
         person.setAge(10);
         person.setName("jay");
      }

      boolean var2 = false;
      System.out.println(person);
   }

   private KotlinStandardLibrary() {
   }

   static {
      KotlinStandardLibrary var0 = new KotlinStandardLibrary();
      INSTANCE = var0;
   }
}

```
### with

```Kotlin
fun <T, R> with(receiver: T, block: T.() -> R) : R
```

* 수신 객체(receiver)를 직접 입력 받고, 객체를 사용하기 위한 block 함수를 두번 째 매개변수로 받는다.
* 여기서 ```T.()``` 를 람다 리시버라고 하는데, 람다 리시버는 첫 번째 매개변수로 받은 receiver의 타입 T를 block함수의 입력인 T.()로 전달한다.
* 이렇게 전달 받으면 block 함수에서 receiver로 받은 객체에 this를 사용하지 않고 접근이 가능
* with 함수는 block 함수의 반환값 그대고 사용한다.
* 어떤 객체의 상태를 변경시키는데 활용이 가능

```Kotlin
data class Person(
  var name: String,
  var age: Int
)


val person = Person("FP", 30)
println("before convert : $person")
val result = with(person) {
  name = "Kotlin"
  age = 10
  this
}
println(result)
println("after convert : $person")
```
* 위와 같은 코드는 오용하게 되면 부수효과(Side Effect)가 우려가 됩니다.
  * [불변 객체(immutable object)](https://ko.wikipedia.org/wiki/%EB%B6%88%EB%B3%80%EA%B0%9D%EC%B2%B4)


### run

* 첫번째 선언
```Kotlin
fun <T, R> T.run(block: T.() -> R): R
```

* run은 block 함수의 반환값 그대로를 반환한다.
* ```run``` 함수는 확장 함수이기 때문에 객체로 부터 연속적으로 호출 될 수 있고, 람다 리시버로 받았기 때문에 it이나 this 없이 객체의 프로퍼티에 접근 가능

> run을 사용해서 객체를 변경한 예
```Kotlin
val person = Person("FP", 30)
val result = person.run {
    name = "Kotlin"
    age = 10
    this
}
println(result)
```

* run 함수의 두번 째 선언
```Kotlin
fun <R> run(block: ()-> R): R
```

* 두 번째 run 함수는 확장 함수가 아니고, block 함수에 입력값이 없다.
    * 따라서, let, with 함수나 첫 번째 run 함수와 같이 어떤 객체로부터 연결되는 블록을 수행하기 위한 함수가 아님
* 어떤 객체를 생성하기 위한 명령문을 하나의 블록으로 묶는 용도로 활용
    * 이를 통해 코드의 가독성을 높일 수 있음.

```Kotlin
// 코틀린은 변수에 함수와 같이 실행 블록을 담을 수 있음.

val person = run{
    val name = "Kotlin"
    val age = 10
    Person(name, age)
}

println(person)
```

### 정리
|....|let|run|with|apply|also|
|------|---|---|-----|-----|-----|
|코드블록|람다식|람다 리시버|람다 리시버|람다 리시버|람다식|
|접근|it|this|this|this|it|
|반환값|람다식 반환값|람다식 반환값|람다식 반환값|자기자신|자기자신|

### 참고

![](http://image.kyobobook.co.kr/images/book/xlarge/557/x9788966262557.jpg)

![](http://image.kyobobook.co.kr/images/book/xlarge/147/x9791189909147.jpg)
