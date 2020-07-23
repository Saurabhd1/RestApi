package com.qa.apiCode;

import com.qa.base.BaseTest;
import io.restassured.response.Response;

import java.util.List;
import java.util.Scanner;

public class CountryCapital extends BaseTest {
    public static void main(String[] args) {
        String codeUrl, baseUrl, nameUrl, inputVal ="us";
        BaseTest base; // base class
        ApiCalls apiCall = new ApiCalls();
        Response status;
        List<String> strCapCountry = null;

        // initiate the values
        base = new BaseTest();
        baseUrl = prop.getProperty("url");
        codeUrl = prop.getProperty("codeURL");
        nameUrl = prop.getProperty("nameURL");

        Scanner scanner = new Scanner(System.in);
        // code to continue checking the capital of country
        printCall();
        inputVal = scanner.next();

        while(!inputVal.equalsIgnoreCase("exit")){

            if ((inputVal != null) && !(inputVal.equalsIgnoreCase("exit"))){

                inputVal = apiCall.validateString(inputVal); // check the input string

                if (inputVal != null && (!inputVal.contains("invalid_Input"))) {
                    // call for the api to get the values
                       status= apiCall.getCountryUsingCode(baseUrl , codeUrl,nameUrl, inputVal);

                    if(status.getStatusCode() == 200 ){
                        /// call to get the response body
                        strCapCountry = apiCall.validateResponse(status,inputVal);
                        if (strCapCountry!= null){
                            for(String s:strCapCountry){
                                System.out.println(s);
                            }
                        }
                    }else{
                        // response code has errors display the meassage and retry
                        System.out.println("Please enter proper value to search, details :- "
                                + status.jsonPath().get("message").toString() + "  for input value :-" + inputVal);
                        System.out.println("Please retry...\n");
                    }
                } else {
                    System.out.println("..please retry..\n");
                }
            }
            // call for input values
            printCall();
            inputVal = scanner.next();
        }
        scanner.close(); /// closing the scanner
    }

    public static void printCall(){
        System.out.println("Please enter Name of Country of 2 or 3 letter Country code.");
        System.out.println("e.g. Name :- United States of America, 2/ 3 letter Code ''US'' or ''USA'' ");
        System.out.println("or type ''EXIT'' to finish the process");
    }
}
