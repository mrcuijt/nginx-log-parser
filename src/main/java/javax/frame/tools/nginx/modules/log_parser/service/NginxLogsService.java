package javax.frame.tools.nginx.modules.log_parser.service;

public interface NginxLogsService {

    void processLogs(String filePath, String dbLocation);

    void processBatchLogs(String filePath, String dbLocation);
}
