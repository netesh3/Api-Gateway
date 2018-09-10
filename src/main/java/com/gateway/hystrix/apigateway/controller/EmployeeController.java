package com.gateway.hystrix.apigateway.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.Arrays;

@RestController
public class EmployeeController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/employeeDetails/{id}", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    public String getStudents(@PathVariable int id)
    {
        System.out.println("Getting Employee details for " + id);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> result  =
                restTemplate.exchange("http://localhost:8011/emp/{id}",
                        HttpMethod.GET, entity, String.class);

        System.out.println("Response Body " +result );

        return "Employee Id -  " + id + " [ Employee Details " + result+" ]";
    }

    public String  fallbackMethod(int employeeid){

        return "Fallback response:: No employee details available temporarily";
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}