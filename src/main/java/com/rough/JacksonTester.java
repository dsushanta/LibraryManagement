package com.rough;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JacksonTester {

    public static void main(String[] args) {


        String fileName = "DatabaseConfigurations.json";
        File databaseConfigFile = new File(
                "/Users/admin/Documents/LibraryManagementProject/Workspace/LibraryManagement/src/main/resources/"+ fileName);
        String jsonString = "{ \"name\":\"Joey Tribbiani\", \"profession\":\"Actor\", \"age\":30 }";
        ObjectMapper mapper = new ObjectMapper();

        try {
            Person one_of_the_friends = mapper.readValue(databaseConfigFile, Person.class);
            System.out.println(one_of_the_friends);

            Person another_one_from_friends = new Person("Chandler Bing", "IT Procurement Manager");
            String jsonString1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(another_one_from_friends);
            System.out.println(jsonString1);

        } catch (JsonParseException e) {
            System.out.println("from exception A");
            e.printStackTrace();
        } catch (JsonMappingException e) {
            System.out.println("from exception B");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("from exception C");
            e.printStackTrace();
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Person {
    String name;
    String profession;

    public Person() {
    }

    public Person(String name, String profession) {
        this.name = name;
        this.profession = profession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }
}