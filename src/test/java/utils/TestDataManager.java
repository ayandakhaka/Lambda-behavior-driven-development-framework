package utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * TestDataManager handles generating, saving, and loading user data.
 * Saves JSON only after successful registration.
 */
public class TestDataManager {

    private static final String USER_DATA_FILE = "src/test/resources/testdata/userdata.json";
    private static TestDataManager instance;
    private final Faker faker;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    private TestDataManager() {
        faker = new Faker();
    }

    public static TestDataManager getInstance() {
        if (instance == null) {
            instance = new TestDataManager();
        }
        return instance;
    }

    /** Generate new user data in memory (for registration) */
    public void generateNewUserData() {
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = firstName.toLowerCase() + System.currentTimeMillis() + "@mail.com";
        password = "Password123";
        phone = faker.phoneNumber().cellPhone();
        System.out.println("Generated user data: " + email);
    }

    /** Save the current user data to JSON (only after successful registration) */
    public void saveUserData() {
        try {
            File file = new File(USER_DATA_FILE);
            file.getParentFile().mkdirs(); // create folder if it doesn't exist

            try (FileWriter writer = new FileWriter(file)) {
                new Gson().toJson(toJsonObject(), writer);
            }

            System.out.println("✅ User data saved successfully: " + email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Load last successfully registered user data from JSON */
    public void loadUserData() {
        try {
            File file = new File(USER_DATA_FILE);
            if (!file.exists()) {
                System.out.println("❌ userdata.json does not exist yet!");
                return;
            }

            try (FileReader reader = new FileReader(file)) {
                JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                firstName = json.get("firstName").getAsString();
                lastName = json.get("lastName").getAsString();
                email = json.get("email").getAsString();
                password = json.get("password").getAsString();
                phone = json.get("phone").getAsString();
            }

            System.out.println("✅ User data loaded: " + email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject toJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("firstName", firstName);
        json.addProperty("lastName", lastName);
        json.addProperty("email", email);
        json.addProperty("password", password);
        json.addProperty("phone", phone);
        return json;
    }

    // -------------------- Getters --------------------
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
}
