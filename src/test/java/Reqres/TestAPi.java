package Reqres;


import Reqres.colors.ColorsData;
import Reqres.registration.RegSuccess;
import Reqres.registration.RegUnSuccess;
import Reqres.registration.Register;
import Reqres.spec.Specifications;
import Reqres.users.UserReqres;
import Reqres.users.UserTime;
import Reqres.users.UserTimeResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestAPi {
    private final static String URL = "https://reqres.in/api/";

    @Test
    public void reqresTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response200());
        List<UserReqres> user = given()
                .when()
                .get("users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserReqres.class);
        user.stream().forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(user.stream().allMatch((x -> x.getEmail().endsWith(("@reqres.in")))));
        List<String> avatar = user.stream().map(UserReqres::getAvatar).collect(Collectors.toList());
        List<String> ids = user.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
        for (int i = 0; i < avatar.size(); i++) {
            Assert.assertTrue(avatar.get(i).contains(ids.get(i)));
        }
    }

    @Test
    public void registerSuccessTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        RegSuccess successReg = given()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .extract().as(RegSuccess.class);
        Assert.assertNotNull(successReg.getId());
        Assert.assertNotNull(successReg.getToken());
        Assert.assertEquals(id, successReg.getId());
        Assert.assertEquals(token, successReg.getToken());
    }

    @Test
    public void registerUnSuccessTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response400());
        Register user = new Register("eve.holt@reqres.in", "");
        RegUnSuccess regUnSuccess = given()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .extract().as(RegUnSuccess.class);
        Assert.assertEquals("Missing password", regUnSuccess.getError());
    }

    @Test
    public void sortYearsTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response200());
        List<ColorsData> colors = given()
                .when()
                .get("unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(sortYears, years);
        System.out.println(years);
        System.out.println(years);
    }

    @Test
    public void deleteTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.responseSpecUnique(204));
        given()
                .when()
                .delete("users/2")
                .then().log().all();
    }

    @Test
    public void timeTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response200());
        UserTime user = new UserTime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .body(user)
                .when()
                .put("users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);
        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{11})$","");
        System.out.println(currentTime);
        System.out.println(response.getUpdatedAt().replaceAll("(.{5})$",""));
        Assert.assertEquals(currentTime,response.getUpdatedAt().replaceAll("(.{5})$",""));

    }

    /***********************                NoPojo                            ***************************/

    @Test
    public void checkAvatarNoPojoTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response200());
        Response response = given()
                .when()
                .get("users?page=2")
                .then().log().all()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> emails = jsonPath.get("data.email");
        List<Integer> ids = jsonPath.get("data.id");
        List<String> avatars = jsonPath.get("data.avatar");
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }
        Assert.assertTrue(emails.stream().allMatch((x->x.endsWith("reqres.in"))));
    }

    @Test
    public void registerSuccessNoPojoTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response200());
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        Response response = given().body(user)
                .when()
                .post("register")
                .then().log().all()
                .body("id", equalTo(4))
                .body("token", equalTo("QpwL5tke4Pnpja7X4"))
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        int id = jsonPath.get("id");
        String token = jsonPath.get("token");
        Assert.assertEquals(4,id);
        Assert.assertEquals("QpwL5tke4Pnpja7X4",token);
    }

    @Test
    public void registerUnSuccessNoPojoTest() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response400());
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        given()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .body("error",equalTo("Missing password"));
    }
    @Test
    public void registerUnSuccessNoPojo() {
        Specifications.installSpec(Specifications.request(URL), Specifications.response400());
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        Response response = given()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.get("error");
        Assert.assertEquals("Missing password",error);
    }
}