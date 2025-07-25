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

#### 3.4. 테스트할 목록 정리하기

- TDD를 시작할 때 테스트할 목록을 미리 정하자.
- 정하고 새로운 사례 발견 할 때마다 추가하자. 특히 이 사례를 '실패하는 테스트'로 등록하자.
- '지라'나 '트렐로'와의 연동 고려
- 어떤 것이 구현이 가장 쉬울까?
- 하나씩. 다루는 범위는 작게, 개발 주기 짧게.
- 범위가 큰 리팩토링은 조급하지 않게, 단 꼭 진행하자.

- 시작이 안 될 때는 검증하는 코드부터 작성하자!
  - 이후 이 값을 어떻게 표현할지 타입을 선택한다.
- 구현이 막히면 순서를 바꾸자. 과감히 코드를 지우고 다시 시작하라.