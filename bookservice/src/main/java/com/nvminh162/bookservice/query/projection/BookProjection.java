package com.nvminh162.bookservice.query.projection;

import com.nvminh162.bookservice.command.data.Book;
import com.nvminh162.bookservice.command.data.BookRepository;
import com.nvminh162.bookservice.query.model.BookResponseModel;
import com.nvminh162.bookservice.query.queries.GetAllBookQuery;
import com.nvminh162.bookservice.query.queries.GetBookDetailQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Projection
 * Là 1 cách để xây dựng view model hay cấu trúc dữ liệu đọc để tối ưu hóa từ các sự kiện Event
 * còn trong CQRS: khi 1 sự kiện xảy ra:
 * VD: Command 1 đơn hàng được tạo, 1 truy vấn Projection sẽ lắng nghe sự kiện và cập nhật View Model để phản ánh trạng thái hệ thống
 * VD: Query lắng nghe query truy vấn vào database trả về dữ liệu
 * => Projection thường được sử dụng các mô hình chỉ đọc và tối ưu hóa truy vấn phức tạp
 * */

@Component // => giúp spring scan tới Class này, class này có những method đang lắng nghe các query v.v...
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookProjection {

    BookRepository bookRepository;

    /*
     * QueryGetaway dispatch GetAllBookQuery thì func này có @QueryHandler lắng nghe GetAllBookQuery nó sẽ chạy vào hàm trong => xử lý*/
    @QueryHandler
    public List<BookResponseModel> handle(GetAllBookQuery query) {
        List<Book> list = bookRepository.findAll();

        /*List<BookResponseModel> listBookResponseModel = new ArrayList<>();
        // forEach -> Consumer không return
        list.forEach(book -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(book, model);
            listBookResponseModel.add(model);
        });*/

        return list.stream().map(book -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(book, model);
            return model;
        }).toList();
    }

    @QueryHandler
    public BookResponseModel handle(GetBookDetailQuery query) throws Exception {
        BookResponseModel model = new BookResponseModel();
        Book book = bookRepository.findById(query.getId()).orElseThrow(() -> new Exception("Not found Book with BookId: " + query.getId()));
        BeanUtils.copyProperties(book, model);
        return model;
    }
}
