package chap07;

// 동작하지 않는 예시 코드: 대역 테스트를 사용하는 UserRepository 설계
public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    void save(User user);
}