package com.bravo.johny.controller;

import com.bravo.johny.controller.filterbeans.CopyFilterBean;
import com.bravo.johny.dto.Copy;
import com.bravo.johny.service.CopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/books/{bookId}/copies")
public class CopyController {

    @Autowired
    private CopyService copyService;

    @GetMapping
    public List<Copy> getCopies(CopyFilterBean copyBean, @PathVariable("bookId") int bookId) {

        copyBean.setBookId(bookId);
        List<Copy> copies = copyService.getCopies(copyBean);

        for(Copy copy : copies) {
            copy.add(linkTo(methodOn(CopyController.class).getCopies(copyBean, bookId)).slash(Integer.toString(copy.getCopyId())).withSelfRel());
        }

        return copies;
    }

    @GetMapping("/{copyId}")
    public Copy getCopyDetails(@PathVariable("copyId") int copyId,
                               @PathVariable("bookId") int bookId) {

        Copy copy = copyService.getCopyDetails(copyId);
        if(bookId != copy.getBookId())
            throwBadRequestRuntimeException("BookEntity Id belonging to the copy id is different from the book id present in the URL");
        copy.add(linkTo(methodOn(CopyController.class).getCopyDetails(copyId, bookId)).slash(Integer.toString(copy.getCopyId())).withSelfRel());

        return copy;
    }

    @DeleteMapping("/{copyId}")
    public void deleteCopy(@PathVariable("copyId") int copyId,
                           @PathVariable("bookId") int bookId) {

        Copy copy = copyService.getCopyDetails(copyId);
        if(bookId != copy.getBookId())
            throwBadRequestRuntimeException("BookEntity Id belonging to the copy id is different from the book id present in the URL");
        copyService.deleteCopyFromDatabase(copyId);
    }

}
