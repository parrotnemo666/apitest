package filter;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Demo {

    static public void main(String args[]) {
        // Log4j 2 会自动查找 classpath 下的 log4j2.xml 或 log4j2.properties 配置文件
        // 因此，这里不需要手动调用 configure 方法
        Logger logger = LogManager.getLogger(Log4j2Demo.class);

        // 使用不同的日志级别来记录信息
        logger.trace("This is a TRACE message.");
        logger.debug("This is a DEBUG message.");
        logger.info("This is an INFO message.");
        logger.warn("This is a WARN message.");
        logger.error("This is an ERROR message.");
        logger.fatal("This is a FATAL message.");
        
        try {
            // 引发异常以测试异常日志记录
            throw new Exception("Test Exception");
        } catch (Exception e) {
            logger.error("Exception caught", e);
        }
    }
}



