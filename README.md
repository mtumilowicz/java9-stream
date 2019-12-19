[![Build Status](https://travis-ci.com/mtumilowicz/java9-stream.svg?branch=master)](https://travis-ci.com/mtumilowicz/java9-stream)

# java9-stream
* [Java 9 and Beyond by Venkat Subramaniam](https://www.youtube.com/watch?v=oRcOiGWK9Ts)
* https://www.codingame.com/playgrounds/46649/java-9-improvements/collectors-improvements
* https://docs.oracle.com/javase/9/docs/api/java/util/stream/Collectors.html

# preface
New methods:

* `default Stream<T> takeWhile(Predicate<? super T> predicate)`
   ```
   IntStream.iterate(0, i -> i + 3) // for(int i = 0;; i = i + 3)
   .takeWhile(i -> i <= 20)         //   if (i > 20) break
   .forEach(System.out::println)    //   System.out.println(i)
   ```
* `default Stream<T> dropWhile(Predicate<? super T> predicate)`
* `public static<T> Stream<T> ofNullable(T t)`
    * suppose we have function:
        ```
        List<String> getX() {...}
        ```
        that possibly may return null, so:
        ```
        getX().stream // could throw NullPointerException
        ```
        and
        ```
        Stream.of(getX())...
        ```
        could throw NullPointerException also, for example:
        ```
        List<String> strings = null;

        Stream.of(strings).flatMap(Collection::stream)
                .forEach(System.out::println);        
        ```
        but with `ofNullable` it is a lot of easier & null safe
        ```
         List<String> strings = null;
 
         String joined = Stream.ofNullable(strings)
                 .flatMap(Collection::stream)
                 .collect(Collectors.joining());
         
         assertThat(joined, is(""));       
        ```
* `public static<T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)`
    * equivalent of `for(seed; Predicate; Function)`, example: `for(int i = 0; i < 10; i = i + 3)`
    * old iterate method does not have stop function:
        ```
        public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f)
        ```
        so in java 8 we have to go through limit functions:
        ```
        Stream.iterate(0, x -> ++x).limit(10)...
        ```
    * in java 9 it is a lot easier, extremely elastic and readable
        ```
        Stream.iterate(0, x -> x < 10, x -> ++x)...
        ```
* `Collectors.filtering(Predicate<? super T> predicate, Collector<? super T,A,R> downstream)`
    * applies the predicate to each input element and only accumulating if the predicate returns true
    ```
     given:
     List<Expense> expenses = ...
  
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
             ));

     then: 'filter then map produces only non empty entries'
     and: 'map then filter produces all entries, and some are empty'
    ```
* `Collectors.flatMapping​(Function<? super T,? extends Stream<? extends U>> mapper, Collector<? super U,A,R> downstream)`
    ```
    given:
    List<Expense> expenses = ...
  
    when:
    Map<Integer, Set<Tag>> tagsByYear = expenses
            .stream()
            .collect(Collectors.groupingBy({ it.getYear() },
                    Collectors.flatMapping({ it.getTags().stream() }, Collectors.toSet())
            ));
  
    then: 'tags year by year in a map'
  ```

# project description
Test for above mentioned methods.
