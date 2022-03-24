import controller.InMemoryTasksManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTasksManager> {
    public InMemoryTasksManagerTest() {
        super(new InMemoryTasksManager());
    }
}
