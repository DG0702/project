import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    public static List<Integer> strTolnList(List<String> strList){
        return strList.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
