package org.karane;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.karane.model.Book;
import org.karane.service.BookService;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject BookService bookService;

    @GET
    public List<Book> list(@QueryParam("genre") String genre) {
        if (genre != null && !genre.isBlank())
            return bookService.findByGenre(genre);
        return bookService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return bookService.findById(id)
                .map(b -> Response.ok(b).build())
                .orElse(Response.status(404).entity(Map.of("error", "Book " + id + " not found")).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Book book, @Context UriInfo uriInfo) {
        if (book == null || book.getTitle() == null || book.getTitle().isBlank())
            return Response.status(400).entity(Map.of("error", "title is required")).build();
        Book created = bookService.create(book);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Book updated) {
        return bookService.update(id, updated)
                .map(b -> Response.ok(b).build())
                .orElse(Response.status(404).entity(Map.of("error", "Book " + id + " not found")).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (!bookService.delete(id))
            return Response.status(404).entity(Map.of("error", "Book " + id + " not found")).build();
        return Response.noContent().build();
    }
}
