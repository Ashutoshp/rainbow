package org.sa.rainbow.brass.adaptation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.sa.rainbow.brass.PropertiesConnector;

/**
 * @author jcamara
 *
 */

public class PrismConnector {

    private static final boolean m_print_output = true;

    private String m_prismBin;
    private String m_prismModel;
    private String m_prismProperties;
    private String m_prismParameters;
    private String m_prismAdvExport;

    private String m_result; // Result stored after prism invocation;

    public static String convertToAbsolute (String filename) {
        if (filename.startsWith ("\"") && filename.endsWith ("\"") && filename.length () > 2) {
            filename = filename.substring (1, filename.length () - 1);
        }
        if (filename.startsWith ("~" + File.separator)) {
            filename = System.getProperty ("user.home") + filename.substring (1);
        }
        return new File (filename).getAbsolutePath ();
    }

    public PrismConnector (Properties props) {
        if (props == null) {
            props = PropertiesConnector.DEFAULT;
        }
        m_prismBin = props.getProperty (PropertiesConnector.PRISM_BIN_PROPKEY);
        m_prismModel = props.getProperty (PropertiesConnector.PRISM_MODEL_PROPKEY);
        m_prismProperties = props.getProperty (PropertiesConnector.PRISM_PROPERTIES_PROPKEY);
        m_prismParameters = props.getProperty (PropertiesConnector.PRISM_PARAMETERS_PROPKEY);
        m_prismAdvExport = props.getProperty (PropertiesConnector.PRISM_ADV_EXPORT_PROPKEY);

        // Convert to full paths
        m_prismBin = convertToAbsolute (m_prismBin);
        m_prismModel = convertToAbsolute (m_prismModel);
        m_prismProperties = convertToAbsolute (m_prismProperties);
        m_prismAdvExport = convertToAbsolute (m_prismAdvExport);
    }

    public String getPrismModelLocation () {
        return m_prismModel;
    }

    public String getPrismPolicyLocation () {
        return m_prismAdvExport;
    }


    public String invokeGenPolicy (String filename, int currentLocationId, int toLocationId) {
        return invokeGenPolicy (filename, currentLocationId, toLocationId, m_prismParameters );
    }

    public String invokeGenPolicy (String filename, int currentLocationId, int toLocationId, String auxParameters) {
        String line;
        String result="";
        String locationParameterString = ",INITIAL_LOCATION=" + String.valueOf (currentLocationId) + ",TARGET_LOCATION="
                + String.valueOf (toLocationId) + ",INITIAL_HEADING=1";

        try {
            Process p = Runtime.getRuntime ()
                    .exec (m_prismBin + " " + filename + " " + m_prismProperties + " -prop 1 -ex -const "
                            + auxParameters + locationParameterString + " -exportstrat " + m_prismAdvExport);

            BufferedReader input = new BufferedReader (new InputStreamReader (p.getInputStream ()));
            while ((line = input.readLine ()) != null) {
                if (m_print_output) {
                    System.out.println (line);
                }
                String[] e = line.split(" ");
                if (e[0].equals("Result:")){
                    m_result = e[1];
                    result = e[1];
                }
            }


            input.close ();

            try{
                p.waitFor();
            } catch (InterruptedException e1){
                e1.printStackTrace();
            }

        }
        catch (IOException e) {
            e.printStackTrace ();
        }
        return result;
    }


    public void invoke (int currentLocationId, int toLocationId) {
        String line;
        String locationParameterString = ",INITIAL_LOCATION=" + String.valueOf (currentLocationId) + ",TARGET_LOCATION="
                + String.valueOf (toLocationId) + ",INITIAL_HEADING=1";

        try {
            Process p = Runtime.getRuntime ()
                    .exec (m_prismBin + " " + m_prismModel + " " + m_prismProperties + " -prop 1 -ex -const "
                            + m_prismParameters + locationParameterString + " -exportadv " + m_prismAdvExport);

            BufferedReader input = new BufferedReader (new InputStreamReader (p.getInputStream ()));
            while ((line = input.readLine ()) != null) {
                if (m_print_output) {
                    System.out.println (line);
                }
                String[] e = line.split(" ");
                if (e[0].equals("Result:")){
                    m_result = e[1];
                }
            }


            input.close ();

            try{
                p.waitFor();
            } catch (InterruptedException e1){
                e1.printStackTrace();
            }

        }
        catch (IOException e) {
            e.printStackTrace ();
        } 
    }

    public String getResult(){
        return m_result;
    }

    public static void main (String[] args) throws Exception {
        PrismConnector conn = new PrismConnector (PropertiesConnector.DEFAULT);
        conn.invoke (4, 0); // Go from "ls" to "l1" in simplemap
    }
}
