
## Recipe 3.6 나중 초기화를 위해 lateinit 사용하기

널 비허용으로 선언된 클래스 속성은 생성자에서 초기화되어야 한다. 
모든 객체가 생성 될 때까지 의존성 주입이 일어나지 않는 의존성 주입 프레임워크에서 주로 생성 시점에 초기화가 발생하지 않응 수 있는데 이를 대비해 ```lateinit``` 변경자를 사용한다.

* ```lateinit``` 변경자는 클래스 몸체에만 서언되고 사용자 정의 획득자와 설정자가 없는 var 속성에만 사용 가능하다.
* 코틀린 1.2 부터 최상위 속성과 지역변수에서 latinit 사용이 가능
* **lateinit을 사용할 수 있는 속성 타입은 Null 할당이 불가능한 타입이어야 하며 기본 타입에는 latinit을 사용할 수 없다**

> ### latinit과 lazy의 차이
> lateinit 변경자는 var 속성에 사용되는 제약이 있음. lazy 대리자는 속성에 처음 접근할 때 평가되는 람다를 받는다.
> 초기화 비용이 높은데 lazy를 사용하면 초기화는 반드시 실패한다.
> lazy는 val 속성에 사용할 수 있는 반면 latinit은 var속성에만 적용 가능하다.
> lateinit은 속성에 접근할 수 있는 모든 곳에서 초기화가 가능하며 객체 밖에서도 가능하다.


## Recipe 3.9 Nothing

코틀린의 ```Nothing``` 클래스는 private 생성자만 구현되어 있는 클래스이다.

코틀린 공식 문서에서는 "결코 존재할 수 없는 값을 나타내기 위해 Nothint을 사용할 수 있다" 고 명시하고 있다.

* Nothing 클래스는 모든 클래스의 하위 타입이다.

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE2NTEwMzA2MzldfQ==
-->