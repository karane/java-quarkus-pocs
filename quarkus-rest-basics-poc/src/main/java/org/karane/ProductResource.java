package org.karane;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.karane.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private static final Map<Long, Product> store = new ConcurrentHashMap<>();
    private static final AtomicLong idSeq = new AtomicLong(1);

    static {
        store.put(1L, new Product(1L, "Laptop", 1299.99, "electronics"));
        store.put(2L, new Product(2L, "Desk Chair", 349.00, "furniture"));
        store.put(3L, new Product(3L, "Mechanical Keyboard", 129.99, "electronics"));
        idSeq.set(4L);
    }

    /** GET /products?category=electronics */
    @GET
    public List<Product> list(@QueryParam("category") String category) {
        List<Product> all = new ArrayList<>(store.values());
        if (category != null && !category.isBlank()) {
            return all.stream()
                    .filter(p -> category.equalsIgnoreCase(p.category))
                    .collect(Collectors.toList());
        }
        return all;
    }

    /** GET /products/{id} */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Product product = store.get(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Product " + id + " not found"))
                    .build();
        }
        return Response.ok(product).build();
    }

    /** POST /products */
    @POST
    public Response create(Product product) {
        product.id = idSeq.getAndIncrement();
        store.put(product.id, product);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    /** PUT /products/{id} */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Product updated) {
        if (!store.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Product " + id + " not found"))
                    .build();
        }
        updated.id = id;
        store.put(id, updated);
        return Response.ok(updated).build();
    }

    /** DELETE /products/{id} */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (store.remove(id) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Product " + id + " not found"))
                    .build();
        }
        return Response.noContent().build();
    }
}
