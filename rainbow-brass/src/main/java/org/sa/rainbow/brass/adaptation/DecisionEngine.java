package org.sa.rainbow.brass.adaptation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.sa.rainbow.brass.PropertiesConnector;
import org.sa.rainbow.brass.model.map.EnvMap;
import org.sa.rainbow.brass.model.map.MapTranslator;
import org.sa.rainbow.brass.adaptation.PrismPolicy;
import org.sa.rainbow.brass.adaptation.PolicyToIG;






/**
 * @author jcamara
 *
 */
public class DecisionEngine {

    public static String m_export_path;
    public static MapTranslator m_mt;
    public static PrismConnectorAPI m_pc;
    public static String m_origin;
    public static String m_destination;
    public static Map<List, String> m_candidates;
    public static Map<List, Double > m_scoreboard;
    public static double m_selected_candidate_time;
    public static PrismPolicy m_plan;


    /**
     * Initializes decision engine
     * @param props
     */
    public static void init (Properties props) {
        if (props == null) {
            props = PropertiesConnector.DEFAULT;
        }
        m_export_path = props.getProperty (PropertiesConnector.PRISM_OUTPUT_DIR);
        m_mt = new MapTranslator ();
        m_pc = new PrismConnectorAPI (); // PRISM invoked via API
        m_origin="";
        m_destination="";
        m_selected_candidate_time=0.0;
        m_scoreboard= new HashMap<List, Double>();
    }

    /**
     * Sets the map to extract data 
     * @param map
     */
    public static void setMap(EnvMap map){
        m_mt.setMap(map);
    }

    /**
     * Generates all PRISM specifications corresponding to the different non-cyclic paths between
     * origin and destination locations
     * @param origin String label of origin map location
     * @param destination String label of destination map location
     */
    public static void generateCandidates(String origin, String destination){
    	m_origin = origin;
    	m_destination = destination;
        m_candidates = m_mt.exportConstrainedTranslationsBetween(m_export_path, origin, destination);	
    }

    /**
     * Assings a score to each one of the candidate policies synthesized based on the specifications
     * generated by generateCandidates
     * @param map
     * @param batteryLevel String amount of remaining battery 
     * @param robotHeading String robot Heading (needs to be converted to an String encoding an int from MissionState.Heading)
     */
    public static void scoreCandidates(EnvMap map, String batteryLevel, String robotHeading){
        m_scoreboard.clear();
        synchronized (map){
        String m_consts = MapTranslator.INITIAL_ROBOT_LOCATION_CONST+"="+String.valueOf(map.getNodeId(m_origin)) +","+ MapTranslator.TARGET_ROBOT_LOCATION_CONST 
        		+ "="+String.valueOf(map.getNodeId(m_destination))+ "," + MapTranslator.INITIAL_ROBOT_BATTERY_CONST+"="+batteryLevel+","+MapTranslator.INITIAL_ROBOT_HEADING_CONST+"="+robotHeading;

        System.out.println(m_consts);
        String result;
            for (List candidate_key : m_candidates.keySet() ){                           	
            result = m_pc.modelCheckFromFileS(m_candidates.get(candidate_key), m_export_path+"mapbot.props", m_candidates.get(candidate_key), 0, m_consts);
            m_scoreboard.put(candidate_key, Double.valueOf(result));
            }
        }
    }

    /**
     * Selects the policy with the best score
     * @return String filename of the selected policy
     */
    public static String selectPolicy(){
        Map.Entry<List, Double> maxEntry = null;
        for (Map.Entry<List, Double> entry : m_scoreboard.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) < 0)
            {
                maxEntry = entry;
            }
        }
        m_selected_candidate_time = maxEntry.getValue();
        return m_candidates.get(maxEntry.getKey())+".adv";
    }

    public static double getSelectedPolicyTime(){
    	return m_selected_candidate_time;
    }

    /**
     * Class test
     * @param args
     */
    public static void main(String[] args){
        init (null);
        
        List<Point2D> coordinates = new ArrayList<Point2D>();
        
        EnvMap dummyMap = new EnvMap (null, null);
        setMap(dummyMap);
        for (int i=1000; i< 32000; i+=500){
	        generateCandidates("l5", "l1");
	        scoreCandidates(dummyMap, String.valueOf(i), "1");
	        System.out.println(String.valueOf(m_scoreboard));	        
	        PrismPolicy pp = new PrismPolicy(selectPolicy());
	  	  	pp.readPolicy();  
	  	  	String plan = pp.getPlan().toString();
	  	    System.out.println(plan);
	  	    PolicyToIG translator = new PolicyToIG(pp, dummyMap);
	        System.out.println(translator.translate());
	        coordinates.add(new Point2D.Double(i, m_selected_candidate_time));
        }
        
        for (int j=0; j< coordinates.size(); j++){
        	System.out.println(" ("+String.valueOf(coordinates.get(j).getX())+", "+String.valueOf(coordinates.get(j).getY())+") ");
        }
        
    }

}
