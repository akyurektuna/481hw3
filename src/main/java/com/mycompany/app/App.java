package com.mycompany.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;



public class App {

	public static ArrayList<Criminal> clist = new ArrayList<Criminal>();

	 public static ArrayList<Criminal> search(ArrayList<String> inputList,ArrayList<String> inputList2) {
         	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            MyHandler handler = new MyHandler();
            saxParser.parse(new File("/home/tuna/myDemoApp/EEAS.xml"), handler);
            
            List<Criminal> ch = handler.getCriminalList();
            //print employee information
            for(Criminal c : ch){
            	if(c.lastName!="" || c.name!=""){
                	clist.add(c);
            	}
                
            }
            
            return check(clist,inputList,inputList2);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
  
        }
    }
	public static ArrayList<Criminal> check(ArrayList<Criminal> clist,ArrayList<String> inputList,ArrayList<String> inputList2){

    	ArrayList<Criminal> liste = new ArrayList<Criminal> ();
    	if(inputList.size()>0 && inputList2.size()>0){
	    		if(inputList.get(0)!=null && inputList2.get(0)!=null){
	    			for(int i = 0 ; i < clist.size()-1 ; i++){
		    			if(clist.get(i).id!=clist.get(i+1).id){
		    				if(clist.get(i).name.equalsIgnoreCase(inputList.get(0))){
		    					if(clist.get(i).lastName.equalsIgnoreCase(inputList2.get(0))){
		    						System.out.println(clist.get(i));
		    						liste.add(clist.get(i));
		    					}
		    				}
		    			}
	    			}
	    	}
	    	return liste;
    	}

    	if(inputList2.size()>0){
	    		if(inputList==null && inputList2.get(0)!=null){
	    			for(int i = 0 ; i < clist.size()-1 ; i++){
		    			if(clist.get(i).id!=clist.get(i+1).id){
		    					if(clist.get(i).lastName.equalsIgnoreCase(inputList2.get(0))){
		    						System.out.println(clist.get(i));
		    						liste.add(clist.get(i));
		    					}
		    				
		    			}
	    			}
	    	}
	    	return liste;
    	}

    	if(inputList.size()>0){
	    		if(inputList.get(0)!=null && inputList2==null){
	    			for(int i = 0 ; i < clist.size()-1 ; i++){
		    			if(clist.get(i).id!=clist.get(i+1).id){
		    				if(clist.get(i).name.equalsIgnoreCase(inputList.get(0))){
		    						System.out.println(clist.get(i));
		    						liste.add(clist.get(i));
		    					
		    				}
		    			}
	    			}
	    	}

	    	return liste;
    	}
    	

    return liste;
    }
    
	public static boolean compute(ArrayList<Integer> array, int e) {
		System.out.println("inside search");
		if (array == null)
			return false;

		for (int elt : array) {
			if (elt == e)
				return true;
		}
		return false;
	}

	public static class Criminal{
		public String id;
		public String name;
		public String lastName;

		public Criminal(){
			id = "";
			name = "";
			lastName = "";
		}
		
		public Criminal(String i,String fn,String ln) {
			id = i;
			name = fn;
			lastName = ln;
		}
		
		public void setId(String i) {
			id = i;
		}
		public void setFName(String fn) {
			name = fn;
		}
		public void setLName(String ln) {
			lastName = ln;
		}
	}//criminal inner classi bitti
	
	
	public static class MyHandler extends DefaultHandler {

		private List<Criminal> cList = null;
		private Criminal c = null;
		private StringBuilder data = null;

		public List<Criminal> getCriminalList() {
			return cList;
		}

		
		boolean bFN = false;
		boolean bLN = false;

		
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			if (qName.equalsIgnoreCase("ENTITY")) {
				String id = attributes.getValue("Id");
				c = new Criminal();
				c.setId(id);
				if (cList == null)
					cList = new ArrayList<>();
			} else if (qName.equalsIgnoreCase("FIRSTNAME")) {
				bFN = true;
			} else if (qName.equalsIgnoreCase("LASTNAME")) {
				bLN = true;
			}
			data = new StringBuilder();
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (bFN) {
				c.setFName(data.toString());
				bFN = false;
			} else if (bLN) {
				c.setLName(data.toString());
				bLN = false;
			}
		}

		@Override
		public void characters(char ch[], int start, int length) throws SAXException {
			data.append(new String(ch, start, length));
		}
	}//myhandler inner classi bitti
	





    public static void main(String[] args) {


     port(getHerokuAssignedPort());

        get("/", (req, res) -> {
            Map map = new HashMap();
            return new ModelAndView(map, "real.mustache");
        },new MustacheTemplateEngine());



        post("/search", (req, res) -> {
            // System.out.println(req.queryParams("input1"));
            // System.out.println(req.queryParams("input2"));

            String input1 = req.queryParams("input1");
            java.util.Scanner sc1 = new java.util.Scanner(input1);
            sc1.useDelimiter("[;\r\n]+");
            java.util.ArrayList<String> inputList = new java.util.ArrayList<>();
            while (sc1.hasNext()) {
                String value = sc1.next().replaceAll("\\s", "");
                inputList.add(value);
            }
            System.out.println(inputList);

            String input2 = req.queryParams("input2");
            java.util.Scanner sc2 = new java.util.Scanner(input2);
            sc2.useDelimiter("[;\r\n]+");
            java.util.ArrayList<String> inputList2 = new java.util.ArrayList<>();
            while (sc2.hasNext()) {
                String value = sc2.next().replaceAll("\\s", "");
                inputList2.add(value);
            }
           

            ArrayList<Criminal> result = App.search(inputList,inputList2);

            Map map = new HashMap();
            map.put("result", result);
            return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());

        get("/search", (rq, rs) -> {
            Map map = new HashMap();
            map.put("result", "not computed yet!");
            return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());

        get("/search", (rq, rs) -> {
            Map map = new HashMap();
            map.put("result", "not computed yet!");
            return new ModelAndView(map, "search.mustache");
        }, new MustacheTemplateEngine());

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
    }
}

