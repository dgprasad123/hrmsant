/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.ifms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 *
 * @author Manas
 */
@WebServiceClient(name = "HrmsScrollDownloadService", targetNamespace = "http://hrms.ifms.cmcltd.com/", wsdlLocation = "https://www.odishatreasury.gov.in/webservices/HrmsScrollDownloadPort?wsdl")
public class HrmsScrollDownloadService extends Service {

    private final static URL HRMSSCROLLDOWNLOADSERVICE_WSDL_LOCATION;

    private final static Logger logger = Logger.getLogger(hrms.ws.client.ifms.HrmsScrollDownloadService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = hrms.ws.client.ifms.HrmsScrollDownloadService.class.getResource(".");
            url = new URL(baseUrl, "https://www.odishatreasury.gov.in/webservices/HrmsScrollDownloadPort?wsdl");
        } catch (final MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'https://www.odishatreasury.gov.in/webservices/HrmsScrollDownloadPort?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        HRMSSCROLLDOWNLOADSERVICE_WSDL_LOCATION = url;
    }

    public HrmsScrollDownloadService(final URL wsdlLocation, final QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public HrmsScrollDownloadService() {
        super(HRMSSCROLLDOWNLOADSERVICE_WSDL_LOCATION, new QName("http://hrms.ifms.cmcltd.com/", "HrmsScrollDownloadService"));
    }

    /**
     *
     * @return returns HrmsScrollDownload
     */
    @WebEndpoint(name = "HrmsScrollDownloadPort")
    public HrmsScrollDownload getHrmsScrollDownloadPort() {
        return super.getPort(new QName("http://hrms.ifms.cmcltd.com/", "HrmsScrollDownloadPort"), HrmsScrollDownload.class);
    }

    /**
     *
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
     * configure on the proxy. Supported features not in the
     * <code>features</code> parameter will have their default values.
     * @return returns HrmsScrollDownload
     */
    @WebEndpoint(name = "HrmsScrollDownloadPort")
    public HrmsScrollDownload getHrmsScrollDownloadPort(final WebServiceFeature features) {
        return super.getPort(new QName("http://hrms.ifms.cmcltd.com/", "HrmsScrollDownloadPort"), HrmsScrollDownload.class, features);
    }
}
