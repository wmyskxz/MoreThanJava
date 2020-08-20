package module.client;

import java.util.Optional;
import java.util.stream.Stream;

public class HelloModuleClient {

    public static void main(String[] args) {
        long count = Stream.of(
            Optional.of(1),
            Optional.empty(),
            Optional.of(2)
        )
            .flatMap(Optional::stream)
            .count();
        System.out.println(count);

    }
}
