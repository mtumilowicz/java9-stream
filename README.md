[![Build Status](https://travis-ci.com/mtumilowicz/java9-stream.svg?branch=master)](https://travis-ci.com/mtumilowicz/java9-stream)

# java9-stream
Overview of Java 9 Stream API news.

# preface
New methods:

* `default Stream<T> takeWhile(Predicate<? super T> predicate)`
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


# project description
Test for above mentioned methods.