# 8. 테스트 가능한 설계

## 8.1 테스트가 어려운 코드

### 하드 코딩된 경로

IP, 포트, URL 등

### 의존 객체를 직접 생성

예를 들어, PayInfoDao와 같은 Dao를 직접 생성하고 테스트 시 값을 주입한다고 가정하자. 테스트 전 모든 DB 접근 환경이 구성되어야 하며, 테스트를 실행하면 데이터가 DB에 추가되므로 재실행 시 삭제가 필요하다.

### 정적 메서드 사용

해당 클래스가 배포 환경에 의존하고 있다면, 정적 메서드 사용에 어려움이 있다.
테스트에 맞는 자체적인 환경을 구성하기 어렵다.

### 실행 시점에 따라 달라지는 결과

`LocalDate.now(), Random`

### 역할이 섞여 있는 코드

DAO 구성과 기능 로직이 섞여 있다면 DAO 때문에 단순한 기능을 테스트하기 어려울 수 있다.

### 그 외

- 메서드 중간에 소켓 통신 코드가 있다.

  - 이 경우 대역 서버 띄우기

- 테스트 대상이 사용하는 의존 클래스나 메서드가 final이다.

  - 이 경우 대역으로 대체가 어렵다. final class는 상속 자체가 불가능하고, final method는 오버라이드가 안 된다.
  대역은 대체로 상속해서 구현하는데 이것 자체가 불가능!

## 8.2 테스트 가능한 설계

왜 테스트가 어려웠나? **의존하는 코드를 교체할 수 있는 수단이 없기 때문**이다.

### 하드 코딩된 상수는 생성자나 메서드 파라미터로 받기

- 생성자, 세터(`setFilePath()`)나 메서드 파라미터로 받기

- 추가로, 테스트에 사용하는 파일은 소스 코드 리포지토리 `src/test/resources`에 함께 등록해야 한다.
  
### 의존 대상은 주입 받기

- 역시 생성자나 세터를 주입 수단으로 사용

- 특히 **레거시 코드** 같은 경우 생성자 없는 버전을 사용하고 있다면, 세터를 추가해 의존 대상을 교체할 수 있도록 만들 수 있다!

### 테스트하기 편하려면 코드를 분리하자

- 기능의 일부만 테스트하고 싶다면 별도 기능으로 분리한다.

예를 들어 별도 클래스로 분리하면

```java
public int calculatePoint(User u) {
    Subscription s = subscriptionDao.selectByUser(u.getId()); // 구독 정보 받아오기
    Product p = ... // 상품 정보 받아오기
    LocalDate now = LocalDate.now();
    int point = 0;
    ... // 포인트 계산 로직
    return point;
}
```
이런 여러 가지 객체에 의존해야 하는 코드에서,

```java
public calss PointRule{
    public int calculate(Subscription s, Product p, LocalDate now) {
        int point = 0;
        ... // 포인트 계산 로직
        return point;
    }
        }
```
특정 로직만 분리하여 특정 기능만 테스트할 수 있다!
원래 코드는 User, Subscription, Product 등 여러 가지 요인을 하나의 메서드에서 의존하므로 직접 테스트하려면 DB에 테스트 데이터를 넣어 놓거나
하는 등 대역을 손쉽게 사용할 수 없는 복잡한 상태였음

### 시간이나 임의 값 생성 기능 분리하기

실행 시간에 따라 테스트 결과가 달라지면 안 된다. 테스트가 사용하는 시간이나 임의 값을 제공하는 기능은 별도로 분리하자.

```java
public class Times {
    public LocalDate today() {
        return LocalDate.now();
    }
}
```
실제 코드에서 이렇게 시간을 제공하는 클래스를 사용하도록 만든다.
테스트에서는 해당 클래스의 대역을 사용하게 하는 것이다.
```java
public class timeTest {
    private Times mockTimes = Mockito.mock(Times.class);
}
```

### 외부 라이브러리는 직접 사용하지 말고 감싸서 사용하기

외부 라이브러리가 정적 메서드를 제공한다면? 대체할 수 없다.
이 경우 외부 라이브러리와 연동하기 위한 타입을 따로 만든다. 테스트 대상은 새로 분리한 타입을 사용한다.

만약 AuthUtil이 authorize()와 authenticate()라는 정적 메서드를 제공하는 외부 라이브러리라고 해보자.
이걸 어떻게 감싸서 대역을 사용가능하게 만드는가?

```java
public class AuthService {
    private String authKey = "somekey";
    
    public int authenticate(String id, String pw) {
        boolean authorized = AuthUtil.authorize(authKey);
        if (authorized) {
            return AuthUtil.authenticate(id, pw);
        } else {
            return -1;
        }
  }
}
```
원래는 이 로직을 곧바로 login 메서드에서 사용하여 대역을 사용하기 어려웠지만

```java
public class LoginService {
    private AuthService authService = new AuthService();
  ... // 의존성 주입하는 부분까지
  
  public LoginResult login(String id, String pw) {
      int resp = authService.authenticate(id, pw);
      if (resp == -1) ...
  }
}
```
이제 LoginService를 테스트할 때, AuthService의 대역을 만들어 사용할 수 있다!