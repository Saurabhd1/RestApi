package com.qa.test;

import com.qa.apiCode.ApiCalls;
import com.qa.base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.NoInjection;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class TestCases extends BaseTest {
    String codeUrl, baseUrl, nameUrl, inputVal ="us";
    BaseTest base; // base class
    ApiCalls apiCall = new ApiCalls();
    Response status;
    List<String> strCapCountry = null;

@BeforeTest
    public void beforeTest(){
    base = new BaseTest();
    baseUrl = prop.getProperty("url");
    codeUrl = prop.getProperty("codeURL");
    nameUrl = prop.getProperty("nameURL");
}

@Test(dataProvider = "validValue")
public void validValues(String inputStr, String capital){
    // get the call based on
    status=apiCall.getCountryUsingCode(baseUrl,codeUrl,nameUrl,inputStr);
    assertTrue(status.jsonPath().get("capital").toString().contains(capital),"The Capital is :- "+ capital);

}

@Test(dataProvider = "invalidValue")
    public void invalidValues(String inputStr){
        // get the call based on
        String strBuffer = inputStr;
        inputStr = apiCall.validateString(inputStr);
        assertTrue(inputStr.contains("invalid_Input"), "The Input String contain error as ''"+ inputStr + " '' ");
        // since the string is handled before sending into request no handling is required
    }

@Test(dataProvider = "invalidCalls")
    public void invalidCalls(String inputStr,int rCode){
        // get the call based on
        String strBuffer ;
        strBuffer = apiCall.validateString(inputStr);
        status=apiCall.getCountryUsingCode(baseUrl,codeUrl,nameUrl,strBuffer);
        assertTrue(status.getStatusCode() == rCode,
                "The Response as expected :- "+ rCode + ", Actual is = "+ status.getStatusCode());

    //assertTrue(inputStr.contains("invalid_Input"), "The Input String contain error as ''"+ inputStr + " '' ");
        // since the string is handled before sending into request no handling is required
    }


@DataProvider(name = "validValue")
    public static Object[][] positiveValues( ) {
        return new Object[][] {{"usa", "Washington, D.C."}, {"india", "New Delhi"}, {"russia", "Moscow"}, {"tz", "Dodoma"}};
    }


@DataProvider(name = "invalidValue")
    public static Object[][] negativeValues() {
        return new Object[][] {{"   "}, {"1234"}, {"@!$%#^&*"},{null},{"a"}};
    }


@DataProvider(name = "invalidCalls")
    public static Object[][] negativeCalls() {
        return new Object[][] {{"  ca ",200},{"t12345z",200},{"12a34  z",404}, {"i@!$%n#^&*",200},{"akuhlhgkjblkg",404}};
    }


}
