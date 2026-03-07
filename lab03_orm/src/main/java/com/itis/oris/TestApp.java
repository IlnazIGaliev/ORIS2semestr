package com.itis.oris;

import com.itis.oris.model.City;
import com.itis.oris.model.Country;
import com.itis.oris.orm.EntityManager;
import com.itis.oris.orm.EntityManagerFactory;

import java.io.IOException;
import java.util.List;

public class TestApp {

    public static void main(String[] args) throws IOException {

        EntityManagerFactory factory = new EntityManagerFactory();

        try (EntityManager em = factory.getEntityManager()) {

            Country country = new Country();
            country.setName("France");

            em.save(country);

            System.out.println("Country id = " + country.getId());

            City city = new City();
            city.setName("Paris");
            city.setCountry(country);

            em.save(city);

            System.out.println("City id = " + city.getId());


            City dbCity = em.find(City.class, city.getId());

            System.out.println("Loaded city = " + dbCity.getName());


            List<City> cities = em.findAll(City.class);

            System.out.println("All cities:");

            for (City c : cities) {
                System.out.println(c.getId() + " " + c.getName());
            }


            em.remove(city);

            System.out.println("City removed");

        }

        factory.close();
    }
}
