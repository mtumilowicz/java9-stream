# java9-stream
Overview of Java 9 Stream API news.

# preface
New methods:

* default Stream<T> takeWhile(Predicate<? super T> predicate)
* default Stream<T> dropWhile(Predicate<? super T> predicate) 
* public static<T> Stream<T> ofNullable(T t)
* public static<T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)

