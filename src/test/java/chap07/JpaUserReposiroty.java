package chap07;

public interface JpaUserReposiroty extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}