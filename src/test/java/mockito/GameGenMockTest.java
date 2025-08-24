package mockito;

public class GameGenMockTest {
    @Test
    void mockTest() {
        GameNumGen genMock = mock(GameNumGen.class);
        given(genMock.generate(GameLevel.EASY)).willReturn("123");
        // 이렇게 특정 메서드가 반환할 값을 지정할 수 있다.
        // willThrow()로 리턴 타입이 void인 메서드에 대해 익셉션을 발생시킬 수도 있다.
    }
}
