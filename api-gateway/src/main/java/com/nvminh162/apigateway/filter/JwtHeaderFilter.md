Collecting workspace information## Giải thích `JwtHeaderFilter`

Filter này có nhiệm vụ **trích xuất thông tin từ JWT token và forward vào header request** trước khi chuyển tiếp tới downstream service.

### Luồng hoạt động:

```
Client Request (có JWT Bearer token)
        ↓
   API Gateway
        ↓
  JwtHeaderFilter  ← đang xét
        ↓
  Downstream Service (book-service, user-service...)
```

### Chi tiết từng bước:

1. **`exchange.getPrincipal()`** - Lấy thông tin người dùng đã được xác thực từ Spring Security context (đã được xử lý bởi [`SecurityConfig`](api-gateway/src/main/java/com/nvminh162/apigateway/configuration/SecurityConfig.java) với `oauth2ResourceServer`)

2. **`principal instanceof JwtAuthenticationToken jwtAuth`** - Kiểm tra xem principal có phải là JWT token hợp lệ không (đã được Keycloak cấp phát)

3. **Trích xuất claims từ JWT:**
   ```java
   var userId = claims.get("sub").toString();           // Subject = Keycloak User ID
   var userName = claims.get("preferred_username").toString(); // Username
   ```

4. **Gắn vào request header mới:**
   ```java
   .header("X-User-Id", userId)
   .header("X-Username", userName)
   ```

5. **Forward request đã được mutate** tới downstream service

### Kết quả downstream service nhận được:

| Header | Giá trị |
|--------|---------|
| `X-User-Id` | Keycloak user UUID (claim `sub`) |
| `X-Username` | Username (claim `preferred_username`) |

> Downstream service **không cần tự decode JWT** — chỉ cần đọc `X-User-Id` và `X-Username` từ header là biết ai đang gọi. Filter này được apply cho route user-service trong application.yaml.