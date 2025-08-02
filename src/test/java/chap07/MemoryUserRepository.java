package chap07;

@Repository
@Profile("test")
public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(User user) {
        store.put(user.getId(), user);
    }
}