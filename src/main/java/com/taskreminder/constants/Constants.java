package com.taskreminder.constants;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Constants {
    public static final DateTimeFormatter FORMATTER  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    public static final List<String> EQUAL_AND_NEGATION_OPERATORS = Arrays.asList("=","!");
    public static final List<String> EXISTS_OPERATORS = Collections.singletonList("exist");
    public static final List<String> AFFIX_OPERATORS = Collections.singletonList("prefix");
    public static final List<String> NOT_EXISTS_OPERATORS = Collections.singletonList("not_exist");
    public static final List<String> ONLY_STRING_FIELDS = Arrays.asList("id");
    public static final List<String> DATE_FIELDS = Arrays.asList("created_time", "modified_time");
    public static final List<String> LIST_QUERIES = Arrays.asList("");
    public static final List<String> TASK_STATUSES = Arrays.asList("Open", "InProgress", "Completed", "Closed", "Expired");

}
