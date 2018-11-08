import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mtumilowicz on 2018-11-08.
 */
public class StreamTest {

    @Test
    public void takeWhile() {
        List<Integer> ints = Stream.iterate(0, x -> ++x)
                .takeWhile(x -> x < 10)
                .collect(toList());

        assertThat(ints, is(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void dropWhile() {
        List<Integer> ints = Stream.iterate(0, x -> ++x)
                .dropWhile(x -> x < 10)
                .takeWhile(x -> x < 15)
                .collect(toList());

        assertThat(ints, is(List.of(10, 11, 12, 13, 14)));
    }

    @Test
    public void ofNullable_null() {
        List<String> strings = null;

        String joined = Stream.ofNullable(strings)
                .flatMap(Collection::stream)
                .collect(Collectors.joining());
        
        assertThat(joined, is(""));
    }

    @Test
    public void ofNullable_notNull() {
        List<String> strings = List.of("a", "b", "c");

        String joined = Stream.ofNullable(strings)
                .flatMap(Collection::stream)
                .collect(Collectors.joining());

        assertThat(joined, is("abc"));
    }

    @Test(expected = NullPointerException.class)
    public void of_null() {
        List<String> strings = null;

        Stream.of(strings).flatMap(Collection::stream)
                .forEach(System.out::println);
    }
}
