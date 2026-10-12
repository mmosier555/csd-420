// Adapted from the Gson User Guide, "Collections Examples"
// Google. (n.d.). Gson user guide. GitHub. https://github.com/google/gson/blob/main/UserGuide.md

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import java.util.Collection;

public class GsonCollectionDemo {

    public static void main(String[] args) {
        Gson gson = new Gson();
        Collection<Integer> ints = Arrays.asList(1, 2, 3, 4, 5);

        // Serialization
        String json = gson.toJson(ints);
        System.out.println("Serialized: " + json);   // [1,2,3,4,5]

        // Deserialization: TypeToken keeps the generic type that Java erases at runtime
        TypeToken<Collection<Integer>> collectionType = new TypeToken<Collection<Integer>>() {};
        Collection<Integer> ints2 = gson.fromJson(json, collectionType);
        System.out.println("Deserialized: " + ints2); // [1, 2, 3, 4, 5]
    }
}