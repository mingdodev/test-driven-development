### 3. 테스트 코드 작성 순서

- 예외적인 경우에서 정상적인 경우로 진행
- 쉬운 경우에서 어려운 경우로 진행

#### 3.1. 테스트 코드 작성 연습 방법

- 완급 조절

```
1. 정해진 값을 리턴
2. 값 비교를 이용해서 정해진 값을 리턴
3. 다양한 테스트를 추가하면서 구현을 일반화
```
#### 3.2. 지속적인 리팩토링

- 테스트 코드를 통과한 뒤 리팩토링

#### 3.3 테스트 코드 작성 연습 예제

> 납부일을 기준으로 서비스 만료일을 계산하는 기능

- 매달 1만원 선불로 납부한다. 납부일 기준으로 한 달 뒤가 서비스 만료일이 된다.
- 2개월 이상 요금을 납부할 수 있다.
- 10만원을 납부하면 서비스를 1년 제공한다.