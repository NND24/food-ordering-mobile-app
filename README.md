---

# **Project Structure Documentation**

## **1. Adapters**
- **Mục đích**: Chứa tất cả các lớp Adapter được sử dụng để kết nối dữ liệu với giao diện (UI) trong ứng dụng. 
- **Vai trò chính**:
  - Quản lý hiển thị danh sách dữ liệu thông qua các thành phần như `RecyclerView`, `Spinner`, hoặc các view tương tự.
- **Ví dụ**:
  - `FoodRecyclerAdapter.java`: Adapter cho danh sách món ăn trong `RecyclerView`.
  - `OrderSpinnerAdapter.java`: Adapter cho dropdown lựa chọn đơn hàng.

---

## **2. Constants**
- **Mục đích**: Tập trung quản lý các giá trị cố định, giúp giảm trùng lặp và dễ dàng thay đổi tại một nơi.
- **Vai trò chính**:
  - Định nghĩa URL API, key của `SharedPreferences`, và các giá trị cố định khác.
- **Ví dụ**:
  - `ApiConstants.java`: Chứa các endpoint của API.
  - `PrefConstants.java`: Chứa key được sử dụng trong SharedPreferences.

---

## **3. Helpers**
- **Mục đích**: Chứa các lớp hoặc phương thức hỗ trợ, công cụ tiện ích dùng chung trong ứng dụng.
- **Vai trò chính**:
  - Xử lý logic phụ trợ như format dữ liệu, kiểm tra đầu vào, hoặc các thao tác lặp lại.
- **Ví dụ**:
  - `DateHelper.java`: Xử lý định dạng và chuyển đổi ngày tháng.
  - `ValidationHelper.java`: Xác minh dữ liệu đầu vào từ người dùng.

---

## **4. Model**
- **Mục đích**: Chứa các lớp đại diện cho cấu trúc dữ liệu trong ứng dụng.
- **Vai trò chính**:
  - Xác định các thuộc tính và hành vi của đối tượng (e.g., `Food`, `User`, `Order`).
  - Tương thích với các tầng khác như ViewModel, Repository và API.
- **Ví dụ**:
  - `Food.java`: Định nghĩa cấu trúc dữ liệu món ăn.
  - `Order.java`: Định nghĩa cấu trúc dữ liệu đơn hàng.

---

## **5. Network**
- **Mục đích**: Chứa toàn bộ logic liên quan đến gọi API và giao tiếp mạng.
- **Vai trò chính**:
  - Cấu hình và định nghĩa các API endpoint.
  - Xử lý phản hồi từ API và lỗi mạng.
- **Ví dụ**:
  - `ApiClient.java`: Cấu hình Retrofit hoặc OkHttp.
  - `ApiService.java`: Định nghĩa các endpoint API.
  - `ApiResponseModel.java`: Định nghĩa model dữ liệu trả về từ API.

---

## **6. Repository**
- **Mục đích**: Quản lý nguồn dữ liệu và cung cấp dữ liệu cho ViewModel.
- **Vai trò chính**:
  - Tích hợp giữa các nguồn dữ liệu (API, database cục bộ, hoặc cache).
  - Tách biệt logic truy xuất dữ liệu khỏi ViewModel.
- **Ví dụ**:
  - `FoodRepository.java`: Quản lý dữ liệu món ăn từ API hoặc database.
  - `UserRepository.java`: Quản lý thông tin người dùng.

---

## **7. UI**
- **Mục đích**: Chứa các lớp giao diện người dùng (UI), bao gồm `Activity`, `Fragment`, và các adapter liên quan.
- **Tổ chức thư mục**: 
  - Theo từng module hoặc tính năng để dễ quản lý.
  - Mỗi module chứa các thành phần liên quan đến tính năng cụ thể.
- **Ví dụ**:
  ```
  ui/
  ├── home/
  │   ├── HomeActivity.java
  │   ├── HomeFragment.java
  │   ├── HomeAdapter.java
  ├── profile/
  │   ├── ProfileActivity.java
  │   ├── ProfileFragment.java
  │   ├── ProfileAdapter.java
  ```

---

## **8. ViewModel**
- **Mục đích**: Chứa các lớp ViewModel, cầu nối giữa UI và các nguồn dữ liệu (Repository).
- **Vai trò chính**:
  - Quản lý logic nghiệp vụ và dữ liệu cần thiết cho giao diện.
  - Tách biệt logic xử lý dữ liệu khỏi các thành phần giao diện.
- **Ví dụ**:
  - `HomeViewModel.java`: Quản lý dữ liệu cho giao diện Home.
  - `ProfileViewModel.java`: Quản lý dữ liệu cho giao diện Profile.

---
