package chap07;

@Repository
@Profile("prod")
public class UserRepositoryImpl implements UserRepository {

    // JPA에 역할 위임
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        jpaUserRepository.save(user);
    }
}
