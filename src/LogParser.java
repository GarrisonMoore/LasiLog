public abstract interface LogParser {

    // Validate if the line can be parsed by our parsers
    boolean canParse(String rawline);

    // Turn this line into a LogObject
    LogObject parse(String rawline);
}
