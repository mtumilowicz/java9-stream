import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.Stream

class StreamTest extends Specification {

    def 'takeWhile'() {
        given:
        List<Integer> ints = Stream.iterate(0) { ++it }
                .takeWhile { it < 10 }
                .collect(Collectors.toList())

        expect:
        ints == [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
    }

    def 'dropWhile'() {
        given:
        List<Integer> ints = Stream.iterate(0) { ++it }
                .dropWhile { it < 10 }
                .takeWhile { it < 15 }
                .collect(Collectors.toList())

        expect:
        ints == [10, 11, 12, 13, 14]
    }

    def 'ofNullable with null list'() {
        given:
        List<String> strings = null

        when:
        String joined = Stream.ofNullable(strings)
                .flatMap { it.stream() }
                .collect(Collectors.joining())

        then:
        joined == ''
    }

    def 'ofNullable with not null list'() {
        given:
        List<String> strings = ['a', 'b', 'c']

        when:
        String joined = Stream.ofNullable(strings)
                .flatMap { it.stream() }
                .collect(Collectors.joining())

        then:
        joined == 'abc'
    }

    def 'of with null list'() {
        given:
        List<String> strings = null

        when:
        Stream.of(strings)
                .flatMap { it.stream() }
                .forEach { println it }

        then:
        thrown(NullPointerException)
    }

    def 'filtering'() {
        given:
        List<Expense> expenses = [
                new Expense(500, 2016, [Tag.FOOD, Tag.ENTERTAINMENT]),
                new Expense(1_500, 2016, [Tag.UTILITY]),
                new Expense(700, 2015, [Tag.TRAVEL, Tag.ENTERTAINMENT])
        ]

        when: 'filter then map'
        Map<Integer, List<Expense>> usingFilter = expenses
                .stream()
                .filter { it.getAmount() > 1_000 }
                .collect(Collectors.groupingBy({ it.getYear() }))

        and: 'map then filter'
        Map<Integer, List<Expense>> usingFiltering = expenses
                .stream()
                .collect(Collectors.groupingBy({ it.getYear() },
                        Collectors.filtering({ it.getAmount() > 1_000 }, Collectors.toList())
                ))

        then: 'filter then map produces only non empty entries'
        usingFilter.size() == 1
        usingFilter[2016] == [new Expense(1_500, 2016, [Tag.UTILITY])]

        and: 'map then filter produces all entries, and some are empty'
        usingFiltering.size() == 2
        usingFiltering[2016] == [new Expense(1_500, 2016, [Tag.UTILITY])]
        usingFiltering[2015] == []
    }

    def 'flatMapping'() {
        given:
        List<Expense> expenses = [
                new Expense(500, 2016, [Tag.FOOD, Tag.ENTERTAINMENT]),
                new Expense(1_500, 2016, [Tag.UTILITY]),
                new Expense(700, 2015, [Tag.TRAVEL, Tag.FOOD])
        ]

        when:
        Map<Integer, Set<Tag>> tagsByYear = expenses
                .stream()
                .collect(Collectors.groupingBy({ it.getYear() },
                        Collectors.flatMapping({ it.getTags().stream() }, Collectors.toSet())
                ))

        then: 'tags year by year in a map'
        tagsByYear.size() == 2
        tagsByYear[2016] == [Tag.FOOD, Tag.UTILITY, Tag.ENTERTAINMENT] as Set
        tagsByYear[2015] == [Tag.TRAVEL, Tag.FOOD] as Set
    }
}
