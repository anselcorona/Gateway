package com.practicafinal.gateway.controladores;

import com.practicafinal.gateway.entidades.Articulo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController()
public class ArticuloController {

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/api/articulos/todos", produces = { "application/json" })
    public ResponseEntity<List<Articulo>> listar() {

        ResponseEntity<List<Articulo>> listResponseEntity = restTemplate.exchange("http://drand.me:8081/articulos",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Articulo>>() {
                });
        System.out.println("cod:" + listResponseEntity.getStatusCode());
        return new ResponseEntity<>(listResponseEntity.getBody(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/articulos/nombre/{nombre}")
    public ResponseEntity<List<Articulo>> articulosPorNombre(@PathVariable String nombre) {

        ResponseEntity<List<Articulo>> listResponseEntity = restTemplate.exchange(
                "http://drand.me:8081/articulos/nombre/" + nombre, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Articulo>>() {
                });

        return new ResponseEntity<>(listResponseEntity.getBody(), HttpStatus.OK);
    }

    @PostMapping(value = "/api/articulos", consumes = "application/json")
    public ResponseEntity<Articulo> crear(@RequestBody Articulo articulo) {

        HttpEntity<Articulo> request = new HttpEntity<>(articulo);

        ResponseEntity<Articulo> exchange = restTemplate.exchange("http://drand.me:8081/articulos/", HttpMethod.POST,
                request, Articulo.class);

        return new ResponseEntity<>(exchange.getStatusCode());
    }

    @RequestMapping(value = "/api/articulos", method = RequestMethod.PUT)
    public ResponseEntity<Articulo> actualizar(@RequestBody Articulo articulo) {

        HttpEntity<Articulo> request = new HttpEntity<>(articulo);

        ResponseEntity<Articulo> exchange = restTemplate.exchange("http://drand.me:8081/articulos/", HttpMethod.PUT,
                request, Articulo.class);

        return new ResponseEntity<>(exchange.getStatusCode());
    }

    @RequestMapping(value = "/api/articulos/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Articulo> borrar(@PathVariable Long id) {

        HttpEntity<Long> request = new HttpEntity<>(id);

        ResponseEntity<Long> exchange = restTemplate.exchange("http://drand.me:8081/articulos/?id="+id, HttpMethod.DELETE,
                request, Long.class);

        return new ResponseEntity<>(exchange.getStatusCode());
    }

    @RequestMapping(value = "/api/articulos/comprar", method = RequestMethod.PUT, params = { "id", "cantidad" })
    public ResponseEntity<Articulo> comprarArticulos(@RequestParam("id") Long id,
            @RequestParam("cantidad") int cantidad) {

        String url = "http://drand.me:8081/articulos/comprar";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("id", id)
                .queryParam("cantidad", cantidad);

        restTemplate.put(builder.toUriString(), null);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/articulos/paginacion", method = RequestMethod.GET, params = { "limit",
            "offset" }, produces = { "application/json" })
    public ResponseEntity<List<Articulo>> articulosPaginacion(@RequestParam("limit") int limit,
            @RequestParam("offset") int offset) {

        String url = "http://drand.me:8081/articulos/paginacion";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("limit", limit)
                .queryParam("offset", offset);

        ResponseEntity<List<Articulo>> lista = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Articulo>>() {
                });

        return new ResponseEntity<>(lista.getBody(), lista.getStatusCode());
    }

    @GetMapping("/api/articulos/{id}")
    public ResponseEntity<Articulo> buscarPorId(@PathVariable Long id) {

        HttpEntity<Long> request = new HttpEntity<>(id);

        ResponseEntity<Articulo> exchange = restTemplate.exchange("http://drand.me:8081/articulos/" + id,
                HttpMethod.GET, request, Articulo.class);

        return new ResponseEntity<>(exchange.getBody(), HttpStatus.OK);
    }

    @GetMapping("/api/articulos/cantidad")
    public ResponseEntity<Long> contarArticulos() {

        ResponseEntity<Long> exchange = restTemplate.exchange("http://drand.me:8081/articulos/cantidad",
                HttpMethod.GET, null, Long.class);

        return new ResponseEntity<>(exchange.getBody(), HttpStatus.OK);
    }

}
