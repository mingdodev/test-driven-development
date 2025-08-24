# 9. 테스트 범위와 종류

## 9.1 테스트 범위

- 단위 테스트: 서비스와 모델

    - 한 클래스, 한 메서드. 의존 대상은 스텁이나 모의 객체 이용.

- 통합 테스트: 웹애플리케이션 서버와 DB

- 기능 테스트/E2E 테스트: 클라이언트(브라우저 등)까지 포함한 전체

  - 사용자와 동일한 방식으로 기능을 검증해야 한다. 예를 들면 데이터 생성 결과를 DB가 아닌 사용자 인터페이스로 확인해야 한다는 뜻.

문맥이나 사용자에 따라 의미가 달라지기도 한다.

- 중복 기능에 대해서는 테스트하지 않아도 된다. 여러 범위의 테스트를 하다 보면 중복이 생기기 마련이다.

  - 단위 테스트에서 다양한 상황을 다루고, 통합/기능 테스트는 주요 상황에 초점을 맞춘다. 테스트 실행 시간을 줄여야 하기 때문이다.

## 9.2 외부 연동이 필요한 테스트 예

가장 찾아보기 쉬운 통합 테스트의 예시를 살펴보자.

### 스프링 부트와 DB 통합 테스트

회원 가입 예시를 활용한다. 통합 테스트인 경우 다음과 같이 환경을 미리 구성해두는 조치가 필요하다.

- 동일 ID가 이미 존재하는 상황을 만들기 위해 insert할 때, on duplicate key를 사용해 오류를 방지한다.
- 동일 ID가 존재하지 않는 상황을 만들기 위해 미리 delete문을 실행한다.

단위 테스트라면? 대역을 사용하여 위와 같은 신경 쓸 부분이 없다. 또한, 테스트를 위해 스프링 컨테이너를 생성하는 시간을 아낄 수 있다.

### WireMock을 이용한 REST 클라이언트 테스트

WireMock을 사용하면 서버 API를 스텁으로 대체할 수 있다.

```java
public calss CardNumberValidatorTest {
    private WireMockServer wireMockServer;
    
    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
    }
    
    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
    
    @Test
    void valid() {
        wireMockServer.stubFor(post(urlEqualTo("/card")))
                .withRequestBody(equalTo("123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("ok"));
                // .withFixedDelay(5000)과 같이 의도적인 지연을 만들어,
                // 응답 타임아웃에 대한 테스트도 진행할 수 있다.
        
        CardNumberValidator validator =
                new CardNumberValidator("http://localhost:8089");
        CardValidity validity = validator.validate("123");
        assertEquals(CardValidity.VALID, validity);
    }
}
```

### 스프링 부트의 내장 서버를 이용한 API 기능 테스트

내장 톰캣을 사용하는 예시를 살펴보자.

```java

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserApiE2ETest {
    @AutoWired
    private TestRestTemplate restTemplate;

    @Test
    void weakPwResponse() {
        String reqBody = ...;
        RequestEntity<String> request = RequestEntity.post(URI.create("/users"))
                .contentType(...)
                .body(reqBody);
        
        ResponseEntity<String> response = restTemplate.exchange(
                request,
                String.class
        );
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("WeakPasswordException"));
    }
}
```

TestRestTemplate은 스프링 부트가 테스트 목적으로 제공하는 것으로, 내장 서버에 연결하는 RestTemplate이다.