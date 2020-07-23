package com.qa.apiCode;

import io.restassured.response.Response;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ApiCalls {

    public String validateString(String strMain) {
        String strInput = strMain;
        boolean check;

        // validate String should be not null/ blank/ no special character, long string
        if ((strInput != null) && (!strInput.isBlank())) {
            check = strInput.contains(" ");
            if (strInput.length() == 1) {
                System.out.println("Please enter minimum 2 character for country code..");
                return "invalid_Input" + strMain;
            } else {
                // remove leading / trailing spaces/ extra spaces between string
                strInput = strInput.replaceAll("[^\\w\\s+]", " ");
                strInput = strInput.replaceAll("[^a-zA-Z]", " ");
                strInput = strInput.trim().replaceAll("\\s+", " ");

                // largest country name is of 56 characters. check for length
                if (strInput.length() <= 57 && strInput.length() !=0 ) {
                    if(!check){
                        strInput = strInput.replaceAll("\\s+","");
                    }
                    return strInput;
                } else { /// extra large country name...

                    System.out.println("Please enter the valid country name or country code.");
                    System.out.println(" Your input name/ code is not valid :- " + strMain);
                    return "invalid_Input :- "+strMain;
                }
            }

        } else {
            System.out.println("Please enter a valid country name or country code..");
            return "invalid_Input :- "+strMain;
        }
    }


    /**
     * *   Below coed is for getting calls for the and validate the response
     */
    public Response getCountryUsingCode(String baseUrl, String codeUrl, String nameUrl, String code) {
        // call for the api to get the values
        if (code.length() < 4) {
            // call for code
            return given().when().get(baseUrl + codeUrl + code);

        } else { // call for name
            return given().when().get(baseUrl + nameUrl + code);
        }

    }

    /**
     * Below code is to check response
     */

    public List<String> validateResponse(Response resp, String code) {
        String strValue = null, strInp;
        List<String> codeVal = null;
        List<String> strActual = new ArrayList<String>();
        int count = 0;


        if (code.length() < 4) {
            strValue = resp.jsonPath().get("capital");
        } else {
            codeVal = resp.jsonPath().get("capital");
        }

        // check for the return type

        if (strValue != null) {
            if( !strValue.contains("Washington")) {
                String[] str = strValue.split(",");
                for (String c : str) {
                    strInp = "Country name :-" + resp.jsonPath().get("name").toString() + "- Capital =" + c;
                    strActual.add(strInp);
                }
            }else{
                strInp = "Country name :-" + resp.jsonPath().get("name").toString() + "- Capital =" + strValue;
                strActual.add(strInp);
            }
        } else if (codeVal != null ) {

            for (String c : codeVal) {
                strInp = "Country name :-" + resp.jsonPath().get("name[" + count + "]").toString() + "- Capital =" + c;
                strActual.add(strInp);
                count++;
            }
        }
        return strActual;
    }

}