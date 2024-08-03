/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.ifms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ClientAppConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("hrms.ws.client.ifms.model");
        return marshaller;
    }

    @Bean
    public VoucherClient getScroll(Jaxb2Marshaller marshaller) {
        VoucherClient client = new VoucherClient();                
        client.setDefaultUri("https://www.odishatreasury.gov.in:443/webservices/HrmsScrollDownloadPort?xsd=1");        
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
