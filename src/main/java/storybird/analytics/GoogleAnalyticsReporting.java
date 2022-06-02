package storybird.analytics;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;

import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

public class GoogleAnalyticsReporting {
    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "static/setting/GA-1d702b818f51.json";
    private static final String VIEW_ID = "262066326";

    public static void main(String[] args) {
        try {
            AnalyticsReporting service = initializeAnalyticsReporting();

            GetReportsResponse response = getKeywordReport(service,"2021-01-01","2022-01-31");
            System.out.println(initKeywordResponseJSON(response).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes an Analytics Reporting API V4 service object.
     *
     * @return An authorized Analytics Reporting API V4 service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

        ClassPathResource cpr = new ClassPathResource(KEY_FILE_LOCATION);
        System.out.println(cpr.getFilename());

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential
                .fromStream(cpr.getInputStream())
                .createScoped(AnalyticsReportingScopes.all());

        // Construct the Analytics Reporting service object.
        return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Queries the Analytics Reporting API V4.
     *
     * @param service An authorized Analytics Reporting API V4 service object.
     * @return GetReportResponse The Analytics Reporting API V4 response.
     * @throws IOException
     */
    private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
        // Create the DateRange object.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        // Create the Metrics object.
        Metric sessions = new Metric()
                .setExpression("ga:sessions") //방문 세션
                .setAlias("sessions");

        Metric users = new Metric()
                .setExpression("ga:users")
                .setAlias("users");

        Metric pageViews = new Metric() //페이지뷰
                .setExpression("ga:pageviews")
                .setAlias("pageviews");

        Metric ssd = new Metric()
                .setExpression("ga:sessionDuration")
                .setAlias("session_duration");

        Dimension browser = new Dimension().setName("ga:browser"); //접속 브라우저
        Dimension frf = new Dimension().setName("ga:fullReferrer"); // 접속경로
        Dimension userType = new Dimension().setName("ga:userType");
        Dimension device = new Dimension().setName("ga:deviceCategory");

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions,users,pageViews,ssd))
                .setDimensions(Arrays.asList(browser, frf, userType, device));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response An Analytics Reporting API V4 response.
     */
    private static void printResponse(GetReportsResponse response) {

        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for " + VIEW_ID);
                return;
            }

            for (ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    System.out.println("Date Range (" + j + ") >> ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
            }

        }
    }
    public static JSONObject initVisitorResponseJSON(GetReportsResponse response) {

        JSONObject resultData = new JSONObject();
        JSONObject desktopData = new JSONObject();
        JSONObject mobileData = new JSONObject();


        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                // System.out.println("No data found for " + VIEW_ID);
                resultData.put("desktop",desktopData);
                resultData.put("mobile",mobileData);
                return resultData;
            }
            for (ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                String deviceType = "";
                String timeValue = "";
                JSONObject timeData = new JSONObject();
                JSONObject data = new JSONObject();
                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    if(dimensionHeaders.get(i).equals("ga:deviceCategory")){
                        deviceType = dimensions.get(i);
                    }else if(dimensionHeaders.get(i).equals("ga:hour") ||
                            dimensionHeaders.get(i).equals("ga:day")||
                            dimensionHeaders.get(i).equals("ga:month")){
                        timeValue = Integer.valueOf(dimensions.get(i)).toString();
                    }
                    // System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    // System.out.println("Date Range (" + j + ") >> ");
                    DateRangeValues values = metrics.get(j);
                    data = new JSONObject();
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        //sessions, pageviews 정보 입력
                        data.put(metricHeaders.get(k).getName(),values.getValues().get(k));

                       //  System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                    timeData.put(timeValue, data);
                }

                switch(deviceType){
                    case "desktop":
                        desktopData.put(timeValue, data);
                        break;
                    case "mobile" :
                        mobileData.put(timeValue, data);
                        break;
                }
                // System.out.println();
            }

        }
        resultData.put("desktop",desktopData);
        resultData.put("mobile",mobileData);
        return resultData;
    }

    public static GetReportsResponse getSessionReport(AnalyticsReporting service, String start, String end, String type) throws IOException {
        // Create the DateRange object.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(start);
        dateRange.setEndDate(end);

        // Create the Metrics object
        Metric sessions = new Metric()
                .setExpression("ga:sessions") //방문 세션
                .setAlias("sessions");

        Metric pageViews = new Metric() //페이지뷰
                .setExpression("ga:pageviews")
                .setAlias("pageviews");

        Dimension device = new Dimension().setName("ga:deviceCategory");
        Dimension dateType = new Dimension().setName("ga:"+type);

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions,pageViews))
                .setDimensions(Arrays.asList(device, dateType));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

    public static JSONObject initInflowPlatformResponseJSON(GetReportsResponse response) {

        JSONObject resultData = new JSONObject();
        JSONArray desktopData = new JSONArray();
        JSONArray mobileData = new JSONArray();


        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                // System.out.println("No data found for " + VIEW_ID);
                resultData.put("desktop",desktopData);
                resultData.put("mobile",mobileData);
                return resultData;
            }
            for (ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                String deviceType = "";
                JSONObject data = new JSONObject();
                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    if(dimensionHeaders.get(i).equals("ga:deviceCategory")){
                        deviceType = dimensions.get(i);
                    }else{
                        data.put(dimensionHeaders.get(i), (dimensions.get(i)).toString());
                    }
                    // System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    // System.out.println("Date Range (" + j + ") >> ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        //sessions, pageviews 정보 입력
                        data.put(metricHeaders.get(k).getName(),values.getValues().get(k));

                        // System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }

                switch(deviceType){
                    case "desktop":
                        desktopData.put(data);
                        break;
                    case "mobile" :
                        mobileData.put(data);
                        break;
                }
                // System.out.println();
            }

        }
        resultData.put("desktop",desktopData);
        resultData.put("mobile",mobileData);
        return resultData;
    }

    public static GetReportsResponse getInflowPlatformReport(AnalyticsReporting service, String start, String end) throws IOException {
        // Create the DateRange object.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(start);
        dateRange.setEndDate(end);

        // Create the Metrics object
        Metric sessions = new Metric()
                .setExpression("ga:sessions") //방문 세션
                .setAlias("sessions");

        Metric pageViews = new Metric() //페이지뷰
                .setExpression("ga:pageviews")
                .setAlias("pageviews");

        Dimension device = new Dimension().setName("ga:deviceCategory");
        // Dimension device2 = new Dimension().setName("ga:keyword");
        Dimension device3 = new Dimension().setName("ga:source");
        //Dimension dateType = new Dimension().setName("ga:"+type);

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions,pageViews))
                .setDimensions(Arrays.asList(device,device3));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

    public static JSONObject initKeywordResponseJSON(GetReportsResponse response) {

        JSONObject resultData = new JSONObject();
        JSONArray dataArray = new JSONArray();


        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                // System.out.println("No data found for " + VIEW_ID);
                return resultData;
            }
            for (ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                String deviceType = "";
                JSONObject data = new JSONObject();
                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    data.put(dimensionHeaders.get(i), (dimensions.get(i)).toString());
                    // System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    // System.out.println("Date Range (" + j + ") >> ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        //sessions, pageviews 정보 입력
                        data.put(metricHeaders.get(k).getName(),values.getValues().get(k));

                        // System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
                dataArray.put(data);
                // System.out.println();
            }
            resultData.put("list", dataArray);
        }
        return resultData;
    }

    public static GetReportsResponse getKeywordReport(AnalyticsReporting service, String start, String end) throws IOException {
        // Create the DateRange object.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(start);
        dateRange.setEndDate(end);

        // Create the Metrics object
        Metric sessions = new Metric()
                .setExpression("ga:sessions") //방문 세션
                .setAlias("sessions");

        Metric pageViews = new Metric() //페이지뷰
                .setExpression("ga:pageviews")
                .setAlias("pageviews");

        Dimension device = new Dimension().setName("ga:keyword");

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions,pageViews))
                .setDimensions(Arrays.asList(device));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }
}

