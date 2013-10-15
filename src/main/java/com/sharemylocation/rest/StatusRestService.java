package com.sharemylocation.rest;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.sharemylocation.converters.Converter;
import com.sharemylocation.converters.Converters;
import com.sharemylocation.domain.Status;
import com.sharemylocation.service.ApplicationDao;

@Path("/statuses")
public class StatusRestService {

    @Inject
    private ApplicationDao dao;

    @Inject
    private Converters converters;

    @SuppressWarnings("unchecked")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response postStatus(@Valid Status status) {
        Converter<Status> converter = (Converter<Status>) converters.converter("status-converter");
        dao.save(status, converter);
        URI uri = UriBuilder.fromResource(StatusRestService.class).build();
        return Response.created(uri).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<Status> allStatuses() {
        Converter<Status> converter = (Converter<Status>) converters.converter("status-converter");
        return dao.findAll(converter);
    }
}
