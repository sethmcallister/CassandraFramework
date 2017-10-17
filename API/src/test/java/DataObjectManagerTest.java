import org.junit.Before;
import org.junit.Test;
import xyz.sethy.profiles.api.data.DataObjectManager;
import xyz.sethy.profiles.api.profile.Profile;

import java.util.UUID;

public class DataObjectManagerTest {

    private DataObjectManager<Profile> profileDataObjectManager;

    @Before
    public void before() {
        this.profileDataObjectManager = new DataObjectManager<>("test");
    }

    @Test
    public void test() {
        Profile profile = new Profile(UUID.randomUUID());
        profileDataObjectManager.insert(profile);
        System.out.println("insert profile done");

        System.out.println("getting profile");
        Profile profile1 = profileDataObjectManager.get(profile.get_id());
        System.out.println("got profile");
        System.out.println(profile1.get_id());
    }
}
