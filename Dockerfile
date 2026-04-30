# FROM eclipse-temurin:17-jdk-jammy

# WORKDIR /app

# COPY target/*.jar app.jar

# EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "app.jar"]



# Build stage
# Mục tiêu của Build stage là tạo ra một file JAR từ mã nguồn của ứng dụng bằng cách sử dụng Maven. Chúng ta sẽ sử dụng một image Maven có sẵn để thực hiện quá trình build, sau đó copy mã nguồn vào container, tải dependencies và chạy lệnh build để tạo file JAR. Bằng cách tách quá trình build thành một stage riêng biệt, chúng ta có thể giữ cho image cuối cùng nhẹ hơn và chỉ chứa file JAR đã được build, mà không cần phải bao gồm toàn bộ môi trường phát triển của Maven.
FROM maven:3.9.6-eclipse-temurin-21 AS build
# thiết lập thư mục làm việc trong container là /app, nơi mà tất cả các lệnh tiếp theo sẽ được thực thi. Việc sử dụng WORKDIR giúp tổ chức mã nguồn và các file liên quan trong một thư mục cụ thể, làm cho quá trình build và chạy ứng dụng trở nên dễ dàng hơn. Nếu thư mục /app chưa tồn tại, Docker sẽ tự động tạo nó trước khi thiết lập làm thư mục làm việc. Sau khi thiết lập WORKDIR, tất cả các lệnh COPY, RUN, và các lệnh khác sẽ được thực thi trong ngữ cảnh của thư mục này, giúp giữ cho cấu trúc dự án của bạn gọn gàng và dễ quản lý.
WORKDIR /app
# copy file pom.xml vào container trước, giúp Docker cache các layer liên quan đến việc tải dependencies. Khi chúng ta chạy lệnh build, Docker sẽ kiểm tra xem file pom.xml có thay đổi hay không. Nếu không có thay đổi, Docker sẽ sử dụng cache và bỏ qua bước tải dependencies, giúp tăng tốc quá trình build. Nếu chúng ta copy toàn bộ mã nguồn trước, bất kỳ thay đổi nào trong mã nguồn cũng sẽ làm mất cache và buộc Docker phải tải lại tất cả các dependencies, điều này sẽ làm chậm quá trình build đáng kể. Do đó, việc copy file pom.xml trước là một cách tối ưu để tận dụng cache của Docker và giảm thời gian build.
COPY pom.xml .
# tải tất cả các dependencies được liệt kê trong pom.xml vào local repository của Maven, giúp tăng tốc quá trình build sau này khi chúng ta copy mã nguồn và chạy lệnh build. Nếu không có dòng này, Maven sẽ phải tải lại tất cả các dependencies mỗi khi chúng ta chạy lệnh build, điều này sẽ làm chậm quá trình build đáng kể, đặc biệt là khi có nhiều dependencies hoặc khi mạng chậm.
RUN mvn dependency:go-offline
# copy toàn bộ mã nguồn của ứng dụng vào container và sau đó chạy lệnh build để tạo file JAR. Bằng cách tách việc tải dependencies và việc build thành hai bước riêng biệt, chúng ta có thể tận dụng cache của Docker để tránh phải tải lại dependencies mỗi khi mã nguồn thay đổi, giúp tăng tốc quá trình build đáng kể.
COPY src ./src
# chạy lệnh build để tạo file JAR, nhưng với tùy chọn -DskipTests để bỏ qua việc chạy các bài kiểm tra (tests) trong quá trình build. Điều này giúp giảm thời gian build, đặc biệt là khi có nhiều bài kiểm tra hoặc khi chúng ta chỉ muốn nhanh chóng tạo file JAR mà không cần chạy các bài kiểm tra. Tuy nhiên, nếu bạn muốn đảm bảo rằng mã nguồn của mình đã được kiểm tra đầy đủ trước khi tạo file JAR, bạn có thể bỏ tùy chọn này và cho phép Maven chạy các bài kiểm tra như bình thường.
RUN mvn clean package -DskipTests

# Run stage
# mục tiêu của run stage là tạo ra một image nhẹ hơn chỉ chứa file JAR đã được build từ Build stage, và thiết lập môi trường để chạy ứng dụng. Chúng ta sẽ sử dụng một image JDK nhẹ hơn (như Eclipse Temurin) để chạy ứng dụng, sau đó copy file JAR từ Build stage vào Run stage. Bằng cách tách quá trình build và run thành hai stage riêng biệt, chúng ta có thể giữ cho image cuối cùng nhẹ hơn và chỉ chứa những gì cần thiết để chạy ứng dụng, mà không cần phải bao gồm toàn bộ môi trường phát triển của Maven.
FROM eclipse-temurin:21-jdk-alpine
# thiết lập thư mục làm việc trong container là /app, nơi mà tất cả các lệnh tiếp theo sẽ được thực thi. Việc sử dụng WORKDIR giúp tổ chức mã nguồn và các file liên quan trong một thư mục cụ thể, làm cho quá trình build và chạy ứng dụng trở nên dễ dàng hơn. Nếu thư mục /app chưa tồn tại, Docker sẽ tự động tạo nó trước khi thiết lập làm thư mục làm việc. Sau khi thiết lập WORKDIR, tất cả các lệnh COPY, RUN, và các lệnh khác sẽ được thực thi trong ngữ cảnh của thư mục này, giúp giữ cho cấu trúc dự án của bạn gọn gàng và dễ quản lý.
WORKDIR /app

# copy file JAR đã được build từ Build stage vào Run stage. Bằng cách sử dụng chỉ định --from=build, chúng ta có thể truy cập vào file JAR được tạo ra trong Build stage và copy nó vào thư mục làm việc của Run stage. Điều này giúp giữ cho image cuối cùng nhẹ hơn, vì chúng ta chỉ copy file JAR đã được build mà không cần phải bao gồm toàn bộ môi trường phát triển của Maven hoặc mã nguồn.
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
# thiết lập lệnh entrypoint cho container, chỉ định rằng khi container được khởi động, nó sẽ chạy lệnh java -jar app.jar để khởi chạy ứng dụng. Bằng cách sử dụng ENTRYPOINT, chúng ta đảm bảo rằng lệnh này sẽ luôn được thực thi khi container bắt đầu, giúp đơn giản hóa việc chạy ứng dụng và đảm bảo rằng nó sẽ hoạt động đúng cách khi được triển khai.
ENTRYPOINT ["java", "-jar", "app.jar"] 