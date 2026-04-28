import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.dto.BookRequest;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class BookRequestTest {

    @Test
    void grab() throws JsonProcessingException {
        Set<Long> authorIds = Set.of(1L);
        BookRequest bookRequest = new BookRequest(
                "hello",
                authorIds,
                null,
                null,
                null);

        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(bookRequest));
    }
}
