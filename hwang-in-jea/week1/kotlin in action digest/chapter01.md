# Kotlin in Action 요약

## 1. Chapter01

* [코틀린 PlayGround](https://play.kotlinlang.org/) 에서 간략한 데모 실행이 가능.

### 1.1 코틀린의 주요 특성

#### 1.1.1 멀티 플랫폼
* 안드로이드, 서버 등등 다양한 플랫폼 지원,
* JVM 베이스, JavaScript 등등 다른 환경에도 호환이 가능.
* 자바 코드에 대한 상호 운용성이 뛰어남.

#### 1.1.2  정적 타입 지정 언어

* **정적 타입 지정(statically typed)** 언어 : 프로그램의 구성 요소의 타입을 컴파일 시점에 알 수 있고, 컴파일러가 필드와 메서드에 대한 타입 검증을 해주는 언어

* **동적 타입 지정(dynamic typed)** 언어 : 메서드나 필드의 검증이 실행 시점에 이루어 진다.
    * JVM 기반 언어 중 대표적인 동적 타입 지정 언어로 ```groovy```와 ```JRuby```가 있음.
    * 코드가 짧아지고 표현이 유연해지는 장점이 있으나, 개발자 실수로 잘못된 타입이 입력되었을 때 런타임 때 오류가 발생한다는 문제가 있음.

* 코틀린에서는 문맥에 따라 타입을 추론하는 ```타입 추론(type inference)``` 이 지원이 된다.
    * 로컬 변수, 멤버변수, 전역변수 모두 타입 추론 지정이 가능.

    * Java 10 이전에는 로컬변수 선언 및 할당 시
    ```java
    public static void main(String[] args) {

        String strVal = "String Type입니다.";
        System.out.println(strVal);

    }
    ```

    * java 10 이후 부터는 ```var``` 라는 키워드를 통해로컬 변수에 대해 타입 추론 선언이 가능하다.(멤버 변수로는 활용 불가능)
    * [JEP 286: Local-Variable Type Inference](http://openjdk.java.net/jeps/286)

    ```java
    public class TypeInferenceDemo{

        // 아래와 같은 멤버 변수 선언은 불가능.
        // var notAllowdVar;

        public static void main(String[] args) {

        var strVal = "String Type입니다.";
        System.out.println(strVal);

        }
    }
    ```

    ```kotlin
    var strVal = "String Type입니다.";

    fun main() {
        println(strVal)
    }
    ```

#### 1.1.3 함수형 프로그래밍과 객체 지향 프로그래밍

* 코틀린은 함수형 스타일로 프로그램을 작성할 수 있게 지원하지만, 함수형 스타일을 강제하지 않음.
* 하이브리드(객체지향 + 함수형)

* cf. 함수형 프로그래밍
  * 영상 참고 : [[토크ON세미나] 함수형 프로그래밍이란 무엇이고? 어디에? 어떻게 쓸까? 1강 - 함수형 프로그래밍 개념 | T아카데미](https://youtu.be/V1u3aqV-qXg)


### 1.2 코틀린 철학
#### 1.2.1 실용성
* IDE를 만든 회사에서 만든 언어...
 
#### 1.2.2 간결성
* Java보다 상대적으로 간단한 표현

#### 1.2.3 안정성
* **Nullable** 제약
* 패턴 매칭 -> 타입 검증과 캐스팅이 한번에 표현이 가능

```kotlin
if (value is String)
    println(value.toUpperCase()) // String 객체 안에 있는 메서드 사용
```

#### 1.2.4 상호운용성
* JVM 베이스에서 Java와의 100% 호환성
* 다중 언어를 지원하면서 자바의 라이브러리를 활용을 할 수 있는 이점이 있음

### 1.2 코틀린 빌드 과정
![](https://media.vlpt.us/images/dbsdlswp/post/e78bc926-63b5-4d35-b842-3b542ae3e493/image.png)