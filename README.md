# Socket-Server
- Spring Boot Server와 Android Client가 소켓 통신하기 위한 예시 코드
- Thread 사용
- 첫 접속 시 사용자의 id를 먼저 보내서 HashpMap에 저장
- portNumber: 포트 번호는 사용 중이 아닌 임의의 포트로 설정

### 📌 사용 방법
1. Client는 SocketServer의 ip주소와 포트 번호로 접속함. (서버의 ip 주소 및 포트 번호 확인 필요, 한 컴퓨터에서 실행 시 localhost로 사용 가능)
2. GachicarApplication 실행(run) => SocketServer 실행됨.
3. Server Thread가 정상적으로 실행되어 "Server is running on port" 메시지가 뜨면 SocketClient 파일 실행
