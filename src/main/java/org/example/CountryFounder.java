package org.example;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class CountryFounder {
    public static String countryCode() throws IOException, GeoIp2Exception {

        URL ip = new URL("https://checkip.amazonaws.com");
        BufferedReader br = new BufferedReader(new InputStreamReader(ip.openStream()));
        String pip = br.readLine();
        System.out.println("Public/External IP Address = " +pip);

        File database = new File("./src/main/resources/GeoLite2-City.mmdb");

        // This reader object should be reused across lookups as creation of it is
        // expensive.
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        // If you want to use caching at the cost of a small (~2MB) memory overhead:
        // new DatabaseReader.Builder(file).withCache(new CHMCache()).build();

        InetAddress ipAddress = InetAddress.getByName(pip);

        CityResponse response = reader.city(ipAddress);

        Country country = response.getCountry();
        System.out.println(country.getIsoCode().toLowerCase());
        return country.getIsoCode().toLowerCase();
    }
}
