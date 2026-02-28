
# Liquibase là gì?

**Liquibase** là thư viện **quản lý thay đổi schema cơ sở dữ liệu (database migration)**. Thay vì tự tạo bảng/cột bằng tay hoặc để JPA/Hibernate tự tạo, bạn mô tả từng bước thay đổi trong file (XML, YAML, JSON, SQL…) và Liquibase sẽ **chạy đúng một lần**, **theo thứ tự**, và **ghi nhớ** đã chạy changeSet nào rồi để không chạy lại.

---

## Vấn đề Liquibase giải quyết

- Nhiều môi trường (dev, test, prod), nhiều người dev → cần **cùng một lịch sử thay đổi DB**.
- Thêm/sửa bảng, cột, index… → cần **script rõ ràng**, **có version**, **không chạy trùng**.
- Rollback (nếu cần) hoặc deploy mới → **áp dụng đúng các thay đổi đã định nghĩa**.

Liquibase giúp: **mọi thay đổi DB đều nằm trong file changelog, có id/author, chạy tự động khi ứng dụng khởi động (hoặc qua CLI)**.

---

## Các khái niệm chính

### 1. **Changelog (file cấu hình)**

- File mô tả **toàn bộ thay đổi** của DB.
- Trong project của bạn:  
  `changelog-master.xml` là **master** – chỉ include các file con:

```7:10:book-service/src/main/resources/db/changelog/changelog-master.xml
    <include file="book-changelog-create.xml" relativeToChangelogFile="true" />

</databaseChangeLog>
```

- Có thể có nhiều file (ví dụ: `book-changelog-create.xml`, `book-changelog-v2.xml`…) và include dần theo thời gian.

### 2. **ChangeSet**

- Mỗi **thay đổi nhỏ** (một bảng, một cột, một index…) = **một changeSet**.
- ChangeSet có **id** + **author** → Liquibase dùng để biết “đã chạy rồi” hay “chưa chạy”.
- Mỗi changeSet **chỉ được áp dụng một lần**; lần sau Liquibase bỏ qua.

Ví dụ trong project của bạn:

- **ChangeSet 1** – tạo bảng `books`:

```8:18:book-service/src/main/resources/db/changelog/book-changelog-create.xml
    <changeSet id="create-table-book" author="nvminh162">
        <preConditions onFail="CONTINUE" onFailMessage="table books already exists">
            ...
        </preConditions>
        <createTable tableName="books">
            <column name="id" type="VARCHAR(255)">
                <constraints primaryKey="true" nullable="false" />
            </column>
        </createTable>
    </changeSet>
```

- **ChangeSet 2, 3, 4** – thêm các cột `author`, `name`, `is_ready` vào `books`.

### 3. **Bảng DATABASECHANGELOG**

- Liquibase tạo bảng **DATABASECHANGELOG** trong DB.
- Mỗi lần chạy xong một changeSet, nó ghi vào bảng này: **id, author, filename, dateexecuted**…
- Lần chạy sau: đọc bảng này → chỉ chạy những changeSet **chưa có trong DATABASECHANGELOG**.

Nhờ đó:
- Dev A thêm 1 changeSet → commit.
- Dev B pull → khi chạy app, Liquibase chỉ chạy **đúng changeSet mới** của A, không chạy lại của B.

---

## Luồng hoạt động (trong project của bạn)

1. **Spring Boot khởi động** → tạo `DataSource`.
2. **LiquibaseConfig** (bean `liquibase`) chạy sau khi có `DataSource`, đọc file:
   - `spring.liquibase.change-log` (mặc định `classpath:db/changelog/changelog-master.xml`).
3. Liquibase:
   - Đọc `changelog-master.xml` → include `book-changelog-create.xml`.
   - So sánh các changeSet trong file với **DATABASECHANGELOG**.
   - Chỉ **chạy những changeSet chưa được ghi nhận** (tạo bảng, thêm cột…).
   - Ghi từng changeSet đã chạy vào **DATABASECHANGELOG**.
4. Sau đó JPA/Hibernate mới dùng DB (bảng đã tồn tại, schema đã đúng).

---

## PreConditions trong project của bạn

```9:12:book-service/src/main/resources/db/changelog/book-changelog-create.xml
        <preConditions onFail="CONTINUE" onFailMessage="table books already exists">
            <not>
                <tableExists tableName="books" />
            </not>
        </preConditions>
```

- **PreCondition**: “Chỉ chạy changeSet này khi điều kiện thỏa”.
- Ở đây: “Chỉ khi **chưa** có bảng `books` thì mới `createTable`.”
- `onFail="CONTINUE"`: nếu bảng đã tồn tại (điều kiện không thỏa) → **bỏ qua changeSet**, không crash. Hữu ích khi DB đã được tạo tay hoặc bởi bản cũ.

Tương tự với các changeSet thêm cột: chỉ `addColumn` khi cột đó **chưa tồn tại**.

---

## So sánh nhanh

| Cách làm | Ưu | Nhược |
|----------|----|--------|
| **JPA/Hibernate `ddl-auto`** | Không cần viết script | Khó kiểm soát, dễ mất dữ liệu, không phù hợp prod |
| **SQL tay** | Linh hoạt | Khó đồng bộ team, không biết đã chạy script nào trên từng DB |
| **Liquibase** | Có lịch sử, chạy đúng một lần, version rõ, nhiều DB hỗ trợ | Cần viết changelog/changeSet |

---

## Tóm lại

- **Liquibase** = công cụ **migration DB**: mô tả thay đổi schema trong **changelog** (file), mỗi thay đổi là **changeSet** (id + author).
- Nó **tự ghi nhớ** đã chạy changeSet nào (bảng **DATABASECHANGELOG**) nên **không chạy trùng**, đảm bảo dev/test/prod dùng **cùng một lịch sử thay đổi**.
- Trong project của bạn: **LiquibaseConfig** đảm bảo Liquibase chạy với `changelog-master.xml` → `book-changelog-create.xml` để tạo bảng `books` và các cột **trước** khi JPA sử dụng DB.

Nếu bạn muốn, có thể hỏi tiếp ví dụ: “thêm một bảng mới” hoặc “đổi tên cột” bằng Liquibase sẽ viết changeSet như thế nào.