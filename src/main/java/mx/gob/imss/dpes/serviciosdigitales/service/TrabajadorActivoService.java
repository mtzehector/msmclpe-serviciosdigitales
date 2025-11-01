/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.service;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.serviciosdigitales.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.serviciosdigitales.models.TrabajadorImss;
import mx.gob.imss.dpes.support.util.ServiceLogger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author juan.garfias
 */
@Provider
public class TrabajadorActivoService extends ServiceDefinition<TrabajadorImss, TrabajadorImss> {

    @Inject
    @ConfigProperty(name = "webServiceTrabajadorActivoUrl")
    private String url;

    @Inject
    @ConfigProperty(name = "webServiceTrabajadorActivoSoapAction")
    private String soapAction;
    
    @Inject
    CreateBitacoraService createBitacoraService;


    @Override
    public Message<TrabajadorImss> execute(Message<TrabajadorImss> request) throws PersonaNoRegistradaException, BusinessException {

        log.log(Level.INFO, "Datos de entrada para Validar Trabajador IMSS: {0}", request.toString());
        TrabajadorImss trabajadorImss = request.getPayload();
            
        try {
            request.getPayload().setEstatus(getVigencia(request.getPayload().getDelegacion(), request.getPayload().getMatricula()));
            //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(trabajadorImss,request.getPayload())));
            return request;
        } catch (IOException ex) {
            Logger.getLogger(TrabajadorActivoService.class.getName()).log(Level.SEVERE, null, ex);
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(trabajadorImss,ex.getMessage(),true)));
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

    public String getVigencia(String delegacion, String matricula) throws MalformedURLException,
            IOException,
            PersonaNoRegistradaException {

        //Code to make a webservice HTTP request
        String responseString = "";
        String outputString = "";
        String wsURL = this.url;
        URL urlWs = new URL(wsURL);
        URLConnection connection = urlWs.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        String xmlInput
                = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<tem:ConsultaVigencia>"
                + "<tem:Delegacion>" + delegacion + "</tem:Delegacion>"
                + "<tem:Matricula>" + matricula + "</tem:Matricula>"
                + "</tem:ConsultaVigencia>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

        byte[] buffer = new byte[xmlInput.length()];
        buffer = xmlInput.getBytes();
        bout.write(buffer);
        byte[] b = bout.toByteArray();
        String SOAPAction = this.soapAction;
        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        //Write the content of the request to the outputstream of the HTTP Connection.
        out.write(b);
        out.close();
        //Ready with sending the request.

        //Read the response.
        InputStreamReader isr
                = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        //Write the SOAP message response to a String.
        while ((responseString = in.readLine()) != null) {
            outputString = outputString + responseString;
        }
        log.log(Level.INFO, "XML Response Trabjador IMSS: " + outputString);
        //Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.

        Document document = parseXmlFile(outputString);

        try {
            if (document.getElementsByTagName("qry").getLength() == 1) {
                NodeList nodeLst = document.getElementsByTagName("qry");
                String resultXml = nodeLst.item(0).getTextContent();

                //Write the SOAP message formatted to the console.
                //String formattedSOAPResponse = formatXML(outputString);
                //System.out.println(formattedSOAPResponse);
                log.log(Level.INFO, "Trabajador Activo");
                if (resultXml.contains("No se encontro el registro.")) {
                    log.log(Level.INFO, "Trabajador Inactivo");
                    throw new PersonaNoRegistradaException("msg002");
                } else {
                    return "Activo";
                }

            } else {
                log.log(Level.INFO, "Trabajador Inactivo");
                throw new PersonaNoRegistradaException("msg002");
            }
        } catch (DOMException e) {
            log.log(Level.INFO, "Trabajador Inactivo");
            throw new PersonaNoRegistradaException("msg002");
        }
    }

    //format the XML in your String
    public String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
