package com.borsam.web.controller.admin.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.classic.html.UrlCssBuilder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.borsam.pojo.log.ExtCyclicBufferAppender;
import com.borsam.pojo.log.Info;
import com.borsam.pojo.log.Query;
import com.hiteam.common.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-22 17:14
 * </pre>
 */
@RestController("logController")
@RequestMapping("/admin/log")
public class LogController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(LogController.class);
    private static final String APPEND_NAME_CYCLIC = "CYCLIC";
    private static String PATTERN = "%d%thread%level%logger{25}%mdc{loginUserName}%msg";

    private ExtCyclicBufferAppender<ILoggingEvent> cyclicBufferAppender;
    private HTMLLayout layout;

    @PostConstruct
    public void init() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        initialize(lc);
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView openPage(){
        return new ModelAndView("/admin/log/infos");
    }

    @RequestMapping(value = "/index2",method = RequestMethod.GET)
    public ModelAndView openPage2(){
        return new ModelAndView("/admin/log/infos2");
    }

    @RequestMapping("/infos")
    public void getLog(Query query, HttpServletRequest req, HttpServletResponse resp) {
        reacquireCBA();

        try {
            resp.setContentType("text/html");
            PrintWriter output = resp.getWriter();

            output.append(layout.getFileHeader());
            String localRef = req.getContextPath();
            output.append("<h2>Last logging events</h2>");
            output.append("<table class=\"nav\">");
            output.append("<tr><td class=\"sexy\"><a href=\"" + localRef
                    + "/index.jsp\" class=\"sexy\">Main Page</a></td></tr>");
            output
                    .append("<tr><td class=\"sexy\"><a href=\"#bottom\" class=\"sexy\">Jump to bottom</a></td></tr>");
            output.append("</table>");

            output.append("<div class=\"content_full\">");
            output.append(layout.getPresentationHeader());

            printLogs(output);

            output.append(layout.getPresentationFooter());

            output.append("<a name=\"bottom\" />");

            output.append("</div>");

            output.append(layout.getFileFooter());

            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printLogs(PrintWriter output) {
        int count = -1;
        if (cyclicBufferAppender != null) {
            count = cyclicBufferAppender.getLength();
        }

        System.out.println("sout count = " + count);
        if (count == -1) {
            output.append("<tr><td>Failed to locate CyclicBuffer</td></tr>\r\n");
        } else if (count == 0) {
            output.append("<tr><td>No logging events to display</td></tr>\r\n");
        } else {
            LoggingEvent le;
            for (int i = 0; i < count; i++) {
                le = (LoggingEvent) cyclicBufferAppender.get(i);
                output.append(layout.doLayout(le) + "\r\n");
            }
        }
    }

    private void addInfo(List<Info> infos, LoggingEvent event) {
        Info info = new Info();
        info.setDate(new Date(event.getTimeStamp()));
        info.setLevel(event.getLevel());
        info.setLogger(event.getLoggerName());
        info.setThread(event.getThreadName());
        info.setMessage(event.getFormattedMessage());

        infos.add(info);
    }

    private void reacquireCBA() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        cyclicBufferAppender = (ExtCyclicBufferAppender<ILoggingEvent>) context.getLogger(
                Logger.ROOT_LOGGER_NAME).getAppender(APPEND_NAME_CYCLIC);
    }

    private void initialize(LoggerContext context) {
        logger.debug("Initializing ViewLastLog Servlet");
        cyclicBufferAppender = (ExtCyclicBufferAppender<ILoggingEvent>) context.getLogger(
                Logger.ROOT_LOGGER_NAME).getAppender(APPEND_NAME_CYCLIC);

        layout = new HTMLLayout();
        layout.setContext(context);
        UrlCssBuilder cssBuilder = new UrlCssBuilder();
        cssBuilder.setUrl("../../resources/css/pk.css");
        layout.setCssBuilder(cssBuilder);
        layout.setPattern(PATTERN);
        layout.setTitle("Last Logging Events");
        layout.start();
    }
}
