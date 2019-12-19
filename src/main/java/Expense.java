import lombok.Value;

import java.util.List;

@Value
public class Expense {
    long amount;
    int year;
    List<Tag> tags;
}

