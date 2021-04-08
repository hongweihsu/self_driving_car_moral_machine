/*
 *  File: EthicalEngine.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The output format for file handler to generate log files.
 * @author HongWei-HSU
 */
public class LogFormatter extends Formatter
{
    public LogFormatter() { super(); }

    @Override
    public String format(final LogRecord record)
    {
        return record.getMessage();
    }
}