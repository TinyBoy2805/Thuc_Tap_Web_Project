# 🚫 Lưu ý cho tất cả thành viên

**⚠️ KHÔNG ĐƯỢC PUSH TRỰC TIẾP VÀO NHÁNH `main` ⚠️**

Hãy tạo **nhánh riêng** để làm việc, chú ý:

1. Trước khi code hãy pull main để đồng bộ dữ liệu tránh xung đột (git pull origin main)
2. Code trên nhánh cá nhân
3. Sau khi code xong hãy pull 1 lần nữa để đảm bảo code luôn cập nhật mới nhất

sau đó:
1. Commit và push code lên nhánh của bạn.  
2. Tạo **Pull Request (PR)** để merge vào `main`.  
3. Chờ **review & approve** trước khi hợp nhất.

Cách commit: commit đúng chuẩn sẽ bao gồm type: description
Type bao gồm:
1. feat: thêm 1 tính năng mới
2. fix: sửa lỗi
3. docs: cập nhật tài liệu
4. style: tái cấu trúc giao diện không ảnh hưởng tới logic project
5. refactor: tái cấu trúc code
6. perf: tối ưu hiệu năng
7. chore: cập nhật nhỏ


🧩 Cấu trúc làm việc:
- `main` → Nhánh ổn định, chỉ merge code đã qua review.   
- `<feature>/<tên_chức_năng>` → Nhánh tính năng cá nhân.
- Sau khi code xong phải xoá nhánh chức năng đi

> Hãy tuân thủ quy trình này để tránh xung đột code và đảm bảo chất lượng project.
