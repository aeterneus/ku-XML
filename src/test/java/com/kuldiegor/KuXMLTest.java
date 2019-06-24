package com.kuldiegor;

import com.google.gson.annotations.SerializedName;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KuXMLTest {

    @Test
    public void fromXML() {
        String xml = "<Payload name=\"payload name\" number=\"123456789\"><Users><User id=\"id1\">content1</User><User id=\"id2\">content2</User><User id=\"id3\">content3</User><User id=\"id4\">content4</User></Users></Payload>";
        KuXML kuXML = KuXML.getInstance();
        XmlTest xmlTest= kuXML.fromXML(xml,XmlTest.class);

        assertEquals("payload name",xmlTest.mPayload.name);
        assertEquals("123456789",xmlTest.mPayload.number);
        {
            XmlTest.Payload.Users.User user = xmlTest.mPayload.mUsers.mUserList.get(0);
            assertEquals("id1",user.mId);
            assertEquals("content1",user.mContent);
        }
        {
            XmlTest.Payload.Users.User user = xmlTest.mPayload.mUsers.mUserList.get(1);
            assertEquals("id2",user.mId);
            assertEquals("content2",user.mContent);
        }
        {
            XmlTest.Payload.Users.User user = xmlTest.mPayload.mUsers.mUserList.get(2);
            assertEquals("id3",user.mId);
            assertEquals("content3",user.mContent);
        }
        {
            XmlTest.Payload.Users.User user = xmlTest.mPayload.mUsers.mUserList.get(3);
            assertEquals("id4",user.mId);
            assertEquals("content4",user.mContent);
        }
        System.out.println(xmlTest);
    }

    @Test
    public void fromXML2(){
        String xml = "<Payload>\n" +
                "        <Cities>\n" +
                "            <City id=\"spb\">Санкт-Петербург</City>\n" +
                "            <City id=\"mow\">Москва</City>\n" +
                "        </Cities>\n" +
                "        <Users>\n" +
                "            <User city=\"mow\">\n" +
                "                <email>gmail@gmail.com</email>\n" +
                "                <fullName>Gmail User</fullName>\n" +
                "            </User>\n" +
                "            <User city=\"spb\">\n" +
                "                <email>admin@javaops.ru</email>\n" +
                "                <fullName>Admin</fullName>\n" +
                "            </User>\n" +
                "        </Users>\n" +
                "    </Payload>";
        KuXML kuXML = KuXML.getInstance();
        XMLModel2 xmlModel2 = kuXML.fromXML(xml,XMLModel2.class);
        {
            XMLModel2.Payload.Cities.City city = xmlModel2.mPayload.mCities.mCityList.get(0);
            assertEquals("spb",city.mId);
            assertEquals("Санкт-Петербург",city.mContent);
        }
        {
            XMLModel2.Payload.Cities.City city = xmlModel2.mPayload.mCities.mCityList.get(1);
            assertEquals("mow",city.mId);
            assertEquals("Москва",city.mContent);
        }
        {
            XMLModel2.Payload.Users.User user = xmlModel2.mPayload.mUsers.mUserList.get(0);
            assertEquals("mow",user.mCity);
            assertEquals("gmail@gmail.com",user.mEmail);
            assertEquals("Gmail User",user.mFullName);
        }
        {
            XMLModel2.Payload.Users.User user = xmlModel2.mPayload.mUsers.mUserList.get(1);
            assertEquals("spb",user.mCity);
            assertEquals("admin@javaops.ru",user.mEmail);
            assertEquals("Admin",user.mFullName);
        }

    }

    @Test
    public void toXML() {
        XmlTest xmlTest = new XmlTest();
        xmlTest.mPayload = new XmlTest.Payload();
        xmlTest.mPayload.name = "payload name";
        xmlTest.mPayload.number = "123456789";
        xmlTest.mPayload.mUsers = new XmlTest.Payload.Users();
        xmlTest.mPayload.mUsers.mUserList = Arrays.asList(
                new XmlTest.Payload.Users.User("id1","content1"),
                new XmlTest.Payload.Users.User("id2","content2"),
                new XmlTest.Payload.Users.User("id3","content3"),
                new XmlTest.Payload.Users.User("id4","content4")
        );
        KuXML kuXML = KuXML.getInstance();
        String xml = kuXML.toXML(xmlTest);
        assertEquals("<Payload name=\"payload name\" number=\"123456789\"><Users><User id=\"id1\">content1</User><User id=\"id2\">content2</User><User id=\"id3\">content3</User><User id=\"id4\">content4</User></Users></Payload>",xml);
    }
    public static class XmlTest {
        @SerializedName("Payload")
        public Payload mPayload;
        public static class Payload {
            String name;
            String number;
            @SerializedName("Users")
            public Users mUsers;

            public static class Users {

                @SerializedName("User")
                public List<User> mUserList;

                public static class User {
                    @SerializedName("id")
                    public String mId;
                    @SerializedName("content")
                    public String mContent;

                    public User(String id, String content) {
                        mId = id;
                        mContent = content;
                    }

                    @Override
                    public String toString() {
                        return "User{" +
                                "mId='" + mId + '\'' +
                                ", mContent='" + mContent + '\'' +
                                '}';
                    }
                }

                @Override
                public String toString() {
                    return "Users{" +
                            "mUserList=" + mUserList +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "Payload{" +
                        "name='" + name + '\'' +
                        ", number='" + number + '\'' +
                        ", mUsers=" + mUsers +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "XmlTest{" +
                    "mPayload=" + mPayload +
                    '}';
        }
    }
}